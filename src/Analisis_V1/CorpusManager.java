package Analisis_V1;

import Analisis_V1.utils.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class CorpusManager {
    private  String CORPUS_PATH;
    private  String TEMP_PATH;
    private  BFYidGetter bbfy;
    private  TFIDFCalculation tfidfCalculation;
    private  Vector<CorpusObj> corpus;

    public CorpusManager(String corpusPath, String tempPath, Language lang){
        this.CORPUS_PATH = corpusPath;
        this.TEMP_PATH = tempPath;
        this.bbfy = new BFYidGetter(lang);
        this.tfidfCalculation = new TFIDFCalculation();
        this.corpus = new Vector<>();
    }

    // Create a corpus from a JSON file
    public CorpusManager(String jsonPath){
        this.corpus = new Vector<>();
        JSONArray jsonArray = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonPath));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            String str = sb.toString();
            jsonArray = new JSONArray(str);

            for(int i=0 ; i< jsonArray.length(); i++){
                Vector<Concept> vectorConcepts = new Vector<>();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray conceptsArray = (JSONArray)jsonObject.get("concepts");

                for(int j=0 ; j< conceptsArray.length(); j++){
                    String term = (String) conceptsArray.getJSONObject(j).get("string");
                    String sysid = (String) conceptsArray.getJSONObject(j).get("sysid");
                    double weigth = (double) conceptsArray.getJSONObject(j).get("weigth");

                    Concept concept = new Concept(term,sysid,weigth);
                    if(!vectorConcepts.contains(concept))
                        vectorConcepts.add(concept);
                }
                CorpusObj co = new CorpusObj(jsonObject.getString("path"),jsonObject.getString("content"), vectorConcepts, (int)jsonObject.get("numWords"));
                corpus.add(co);
            }
            System.out.println("Corpus creato con Succsso...\n");

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public Vector<CorpusObj> getCorpus() {
        return corpus;
    }

    public void createCorpus() {
        File folder = new File(CORPUS_PATH);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt")); //TODO: altri tipi di file testuali
        System.out.println("Begin Corpus creation...");

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

                    String text = sb.toString().replaceAll("[^a-zA-Z0-9òàèù.,']+"," ");
                    CorpusObj tmpObj = disambiguation(text, file.getPath());
                    createTempCorpus(file.getName(), tmpObj);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Disambiguation executed...");
            executeTFIDF(TEMP_PATH);
            System.out.println("TF-IDF executed...");
            for( CorpusObj co : corpus)
                co.assignWeigths();
        }
        outputJSONcorpus("");//TODO mettere a posto
    }

    private CorpusObj disambiguation(String str, String path){
        CorpusObj corpusObj = new CorpusObj(path,str, bbfy.executePost(str));
        corpus.add(corpusObj);
        return corpusObj;
    }

    private void createTempCorpus(String name, CorpusObj obj){
        PrintWriter writer;
        try {
            String text = obj.termsToString();
            writer = new PrintWriter(TEMP_PATH + name, "UTF-8");
            writer.println(text);
            writer.close();

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void executeTFIDF(String path) {
        int count = 0;
        File folder = new File(path);

        // for every file in the directory except for hidden ones
        File[] listOfFiles = folder.listFiles(file -> !file.isDirectory() && !file.isHidden());
        if (listOfFiles != null) {

            // Get wordcount from files and calculate TermFrequency
            int numOfDocs = listOfFiles.length;
            DocumentProperties[] docProperties = new DocumentProperties[numOfDocs];

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    docProperties[count] = new DocumentProperties();
                    HashMap<String,Integer> wordCount = tfidfCalculation.getTermsFromFile(file.getAbsolutePath(),count, folder);
                    docProperties[count].setWordCountMap(wordCount);
                    HashMap<String,Double> termFrequency = tfidfCalculation.calculateTermFrequency(docProperties[count].getWordCountMap());
                    docProperties[count].setTermFreqMap(termFrequency);
                    count++;
                }
            }

            //Calculatin IF
            HashMap<String,Double> inverseDocFreqMap = tfidfCalculation.calculateInverseDocFrequency(docProperties);

            //Calculating tf-IDF
            count = 0;
            for (File file : listOfFiles) {
                HashMap<String,Double> tfIDFTable = new HashMap<>();
                if (file.isFile()) {
                    double tfIdfValue;
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

                // Add TFIDF tfidfTable to every CorpusObj
                for (CorpusObj obj : corpus){
                    if(obj.path.equals(CORPUS_PATH+file.getName())){
                        obj.setTfidfTable(tfIDFTable);
                    }
                }
            }
        }
    }

    /**
     * This method is essential to reduce the amount of work on Cover server.
     * Depending on the accuracy of the output you want to get from the Concept Similarity
     * calculation, this method set a limit to the number of concept to analise
     * for every document inside the corpus, taking into account only
     * the N most significant concepts.
     *
     * @param n is the number of concepts taken into consideration.
     */
    public void setLimitConcepts(int n) {
        for (CorpusObj co : corpus) {
            Vector<Concept> concepts = co.getConcepts();
            concepts.sort(Comparator.comparingDouble(Concept::getWeigth));
            Collections.reverse(concepts);
            concepts.setSize(n);
        }
    }

    /**
     * Exports the created corpus of documents into a JSON file,
     * to avoid creating the entire corpus from scratch
     * every time the program is called with the same corpus.
     *
     * @param path is the name to specify if you want to give a different name path to the
     *             output file.
     */
    public void outputJSONcorpus(String path){
        JSONArray jsonArray = new JSONArray();

        for (CorpusObj co : corpus) {
            JSONObject json = new JSONObject();
            try {
                json.put("path", co.path);
                json.put("content", co.getContent());
                json.put("concepts", co.getConcepts());
                JSONObject tfidfTable = new JSONObject(co.getTfidfTable());
                json.put("table", tfidfTable);
                json.put("numWords", co.getNumWords());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(json);
        }
        try {
            FileWriter file = new FileWriter("corpus/JSONcorpus/jsonCorpus"+path+".json");
            jsonArray.write(file);
            file.flush();
            file.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
