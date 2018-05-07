package Analisis_V1;

import Analisis_V1.utils.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class CorpusCreator {
    private  String CORPUS_PATH;
    private  String TEMP_PATH;

    private  BFYidGetter bbfy;
    private  TFIDFCalculation tfidfCalculation;
    private  Vector<CorpusObj> corpus;

    public CorpusCreator(String corpusPath, String tempPath){
        this.CORPUS_PATH = corpusPath;
        this.TEMP_PATH = tempPath;
        this.bbfy = new BFYidGetter(Language.IT);
        this.tfidfCalculation = new TFIDFCalculation();
        this.corpus = new Vector<>();
    }

    // Create a corpus from a JSON file
    public CorpusCreator(String jsonPath){
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
                JSONArray ca = (JSONArray)jsonObject.get("concepts");

                for(int j=0 ; j< ca.length(); j++){
                    String term = (String) ca.getJSONObject(j).get("string");
                    String sysid = (String) ca.getJSONObject(j).get("sysid");
                    double weigth = (double) ca.getJSONObject(j).get("weigth");
                    Concept concept = new Concept(term,sysid,weigth);
                    vectorConcepts.add(concept);

                }

                CorpusObj co = new CorpusObj(jsonObject.getString("path"),jsonObject.getString("content"), vectorConcepts, (int)jsonObject.get("numWords"));
                corpus.add(co);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    public Vector<CorpusObj> getCorpus() {
        return corpus;
    }

    private  CorpusObj disambiguation(String str, String path){
        CorpusObj corpusObj = new CorpusObj(path,str, bbfy.executePost(str));
        corpus.add(corpusObj);
        return corpusObj;
    }

    private  void executeTFIDF(String path) {
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

                // Add TFIDF tfidfTable to every CorpusObj
                for (CorpusObj obj : corpus){
                    if(obj.path.equals(CORPUS_PATH+file.getName())){
                        obj.setTfidfTable(tfIDFTable);

                    }
                }
            }
        }
    }

    private  void createTempCorpus(String name, CorpusObj obj){
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

    public  void outputJSONcorpus(){
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
            FileWriter file = new FileWriter("corpus/JSONcorpus/jsonCorpus.json");
            jsonArray.write(file);
            file.flush();
            file.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
    }
    }

    public  Vector<CorpusObj> createCorpus() {
        File folder = new File(CORPUS_PATH);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        System.out.println("Inizio creazione Corpus...");

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

                    CorpusObj tmp = disambiguation(sb.toString(), file.getPath());
                    createTempCorpus(file.getName(), tmp);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Disambiguazione eseguita...");
            executeTFIDF(TEMP_PATH);
            System.out.println("TF-IDF calcolato...");
            for( CorpusObj co : corpus)
                co.assignWeigths();

        }
        outputJSONcorpus();
        return this.corpus;

    }

}
