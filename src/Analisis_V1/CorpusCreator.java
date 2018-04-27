package Analisis_V1;

import Analisis_V1.utils.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class CorpusCreator {
    private static String CORPUS_PATH ="/Users/mac/IdeaProjects/TwitterAnalizer/corpus/press/";
    private static String TEMP_PATH ="/Users/mac/IdeaProjects/TwitterAnalizer/corpus/press/temp/";

    private static BFYidGetter bbfy = new BFYidGetter(Language.IT);
    private static TFIDFCalculation TfidfObj = new TFIDFCalculation();
    private static Vector<CorpusObj> corpus = new Vector<>();


    private static CorpusObj disambiguation(String str, String path){
        CorpusObj corpusObj = new CorpusObj(path,str, bbfy.executePost(str));
        corpus.add(corpusObj);
        return corpusObj;
    }

    private static void executeTFIDF(String path) {
        int count = 0;
        File folder = new File(path);

        // per ogni file nell directory path, tranne altre directory e file nascosti.
        File[] listOfFiles = folder.listFiles(file -> !file.isDirectory() && !file.isHidden());
        if (listOfFiles != null) {

            // Get wordcount from files and calculate TermFrequency
            int noOfDocs = listOfFiles.length;
            DocumentProperties[] docProperties = new DocumentProperties[noOfDocs];

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    docProperties[count] = new DocumentProperties();
                    HashMap<String,Integer> wordCount = TfidfObj.getTermsFromFile(file.getAbsolutePath(),count, folder);
                    docProperties[count].setWordCountMap(wordCount);
                    HashMap<String,Double> termFrequency = TfidfObj.calculateTermFrequency(docProperties[count].getWordCountMap());
                    docProperties[count].setTermFreqMap(termFrequency);
                    count++;
                }
            }

            //Calculatin IF
            HashMap<String,Double> inverseDocFreqMap = TfidfObj.calculateInverseDocFrequency(docProperties);

            //Calculating tf-IDF
            count = 0;
            for (File file : listOfFiles) {
                HashMap<String,Double> tfIDFTable = new HashMap<>();
                if (file.isFile()) {
                    double tfIdfValue = 0.0;
                    double idfVal = 0.0;
                    HashMap<String,Double> tf = docProperties[count].getTermFreqMap();

                    for (Object o : tf.entrySet()) {
                        Map.Entry pair = (Map.Entry) o;
                        double tfVal = (Double) pair.getValue();
                        if (inverseDocFreqMap.containsKey((String) pair.getKey())) {
                            idfVal = inverseDocFreqMap.get((String) pair.getKey());
                        }
                        tfIdfValue = tfVal * idfVal;
                        tfIDFTable.put((pair.getKey().toString()), tfIdfValue);
                    }
                    count++;
                }

                // Add TFIDF table to every CorpusObj
                for (CorpusObj obj : corpus){
                    if(obj.path.equals(CORPUS_PATH+file.getName())){
                        obj.table = tfIDFTable;

                    }
                }
            }

        }
    }

    private static void createTempCorpus(String name, CorpusObj obj){
        PrintWriter writer = null;
        try {
            String text = obj.termsToString();
            writer = new PrintWriter(TEMP_PATH + name, "UTF-8");
            writer.println(text);
            writer.close();

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    //TODO: implementare il metodo per esportare il corpus, e crearne uno per caricarlo
    public static void outputJSONcorpus(){}

    public static void main(String[] args) {
        File folder = new File(CORPUS_PATH);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (listOfFiles != null) {
            for(File file : listOfFiles){

                try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }

                    CorpusObj tmp = disambiguation(sb.toString(), file.getAbsolutePath());
                    createTempCorpus(file.getName(), tmp);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            executeTFIDF(TEMP_PATH);
            for( CorpusObj co : corpus)
                co.assignWeigths();

        }


    }

}
