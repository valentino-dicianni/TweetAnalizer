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
    private BabelNetIDsUtility bbfy;
    private  TFIDFCalculation tfidfCalculation;
    private  Vector<CorpusObject> corpus;

    /**
     * CorpusManager constructor for creating a new corpus from scratch, using
     * text files.
     *
     * @param corpusPath corpus directory path
     * @param tempPath temporary corpus path
     * @param lang language af the documents
     */
    public CorpusManager(String corpusPath, String tempPath, Language lang){
        this.CORPUS_PATH = corpusPath;
        this.TEMP_PATH = tempPath;
        this.bbfy = new BabelNetIDsUtility(lang);
        this.tfidfCalculation = new TFIDFCalculation();
        this.corpus = new Vector<>();
        createCorpus(corpusPath);
    }

    /**
     * CorpusManager constructor using a JSON file
     *
     * @param jsonPath path to the json file
     */
    public CorpusManager(String jsonPath){
        this.corpus = new Vector<>();
        JSONArray jsonArray;

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
                Vector<Double> conceptNetVector = new Vector<>();
                Vector<Concept> vectorConcepts = new Vector<>();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray conceptsArray = (JSONArray)jsonObject.get("concepts");
                JSONArray conceptNetArray = (JSONArray)jsonObject.get("conceptNetVector");

                for(int j=0 ; j< conceptNetArray.length(); j++){
                    conceptNetVector.add(conceptNetArray.getDouble(j));
                }

                for(int j=0 ; j< conceptsArray.length(); j++){
                    String term = (String) conceptsArray.getJSONObject(j).get("string");
                    String sysid = (String) conceptsArray.getJSONObject(j).get("sysid");
                    double weigth = (double) conceptsArray.getJSONObject(j).get("weigth");

                    Concept concept = new Concept(term,sysid,weigth);
                    if(!vectorConcepts.contains(concept))
                        vectorConcepts.add(concept);
                }
                CorpusObject co = new CorpusObject(jsonObject.getString("path"),jsonObject.getString("content"), vectorConcepts, (int)jsonObject.get("numWords"),  conceptNetVector);
                corpus.add(co);
            }
            System.out.println("Corpus created successfully...\n");


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public Vector<CorpusObject> getCorpus() {
        return corpus;
    }

    /**
     * Create a complete corpus from document files.
     * It follows a few steps:
     *
     * 1) read files and replace all unsupported characters.
     * 2) execute the disambiguation
     * 3) execute the TF-IDF calculation
     * 4) assign weights to che CorpusObject
     * 5) create a JSON file for future execution on the same corpus
     */
    private void createCorpus(String path) {
        File folder = new File(CORPUS_PATH);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
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
                    CorpusObject tmpObj = disambiguation(text, file.getPath());
                    createTempCorpus(file.getName(), tmpObj);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Disambiguation executed...");
            executeTFIDF(TEMP_PATH);
            System.out.println("TF-IDF executed...");

            for(CorpusObject co : corpus){
                co.assignWeigths();
                co.setConceptNetVector(CoverInterface.getConceptNetVector(co.path, co.getContent()));
                co.setNumLostWords(CoverInterface.getLostWordScore(co.path));
            }
            System.out.println("Weights assign and ConceptNet executed...");

        }
        outputJSONcorpus("_"+path.replace("corpus/", "").replace("/",""));
    }

    /**
     * Execute the call to {@code bbfy.executePost()} passing a string.
     * Create a new {@code CorpusObject} add adds it to the corpus.
     *
     * @param str the string to disambiguate
     * @param path the file path
     * @return returns the new CopusObj created
     */
    private CorpusObject disambiguation(String str, String path){
        CorpusObject corpusObj = new CorpusObject(path, str, bbfy.executePost(str));
        corpus.add(corpusObj);
        return corpusObj;
    }

    /**
     * Create a temporary corpus with all terms obtained from
     * the disambiguation made by Babelfy, and store it into the temp directory.
     * This temp corpus is useful for calculating the TF-IDF.
     *
     * @param name is the temporary file name
     * @param obj is the the reference to a {@code CorpusObject}
     */
    private void createTempCorpus(String name, CorpusObject obj){
        PrintWriter writer;
        try {
            String text = obj.conceptToString();
            writer = new PrintWriter(TEMP_PATH + name, "UTF-8");
            writer.println(text);
            writer.close();

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch the list of text files found on the {@code path} and execute the
     * TF-IDF algorithm. The first part calculate the Term Frequency of every document,
     * then the Inverse Doc Frequency is stored into the {@code inverseDocFreqMap} hashMap.
     * After that, every {@code CorpusObject} is assign with a TF-IDF table.
     *
     * @param path the corpus path.
     */
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
                    HashMap<String,Integer> wordCount = tfidfCalculation.getTermsFromFile(file.getAbsolutePath());
                    docProperties[count].setWordCountMap(wordCount);
                    HashMap<String,Double> termFrequency = tfidfCalculation.calculateTermFrequency(docProperties[count].getWordCountMap());
                    docProperties[count].setTermFreqMap(termFrequency);
                    count++;
                }
            }

            //Calculatin InverseDocFrequency
            HashMap<String,Double> inverseDocFreqMap = tfidfCalculation.calculateInverseDocFrequency(docProperties);

            //Calculating TF-IDF
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

                // Add TFIDF tfidfTable to every CorpusObject
                for (CorpusObject obj : corpus){
                    if(obj.path.equals(CORPUS_PATH+file.getName())){
                        obj.setTfidfTable(tfIDFTable);
                    }
                }
            }
        }
    }

    /**
     * This method is essential to reduce the amount of work on Cover server.
     * Creates a copy of the corpus and,depending on the accuracy of the output you
     * want to get from the Concept Similarity calculation, this method set a limit
     * to the number of concept to analise for every document inside the corpus,
     * taking into account only the N most significant concepts.
     *
     * @param n is the number of concepts taken into consideration
     * @return the new limited corpus
     */
    //TODO: la copia non funziona..perchè il corpusObj ha le reference al corpus
    public Vector<CorpusObject>  setLimitConcepts(int n) {

        for (CorpusObject co : corpus) {
            Vector<Concept> concepts = co.getConcepts();
            Vector<Concept> newConcepts = new Vector<>();

            concepts.sort(Comparator.comparingDouble(Concept::getWeigth));
            Collections.reverse(concepts);

            int found = 0;
            int i = 0;
            while(found < n && i < concepts.size()){
                if(concepts.get(i).getSysid().endsWith("n")){
                    newConcepts.add(concepts.get(i));
                    found++;
                }
                i++;
            }
            co.setConcepts(newConcepts);
        }
        return corpus;

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

        for (CorpusObject co : corpus) {
            JSONObject json = new JSONObject();
            try {
                json.put("path", co.path);
                json.put("content", co.getContent());
                json.put("concepts", co.getConcepts());
                JSONObject tfidfTable = new JSONObject(co.getTfidfTable());
                json.put("table", tfidfTable);
                json.put("numWords", co.getNumWords());
                json.put("conceptNetVector", co.getConceptNetVector());

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

    public void printLostWords(){
        String eol = System.getProperty("line.separator");

        try (Writer writer = new FileWriter("log_lostWords.csv")) {
            for (CorpusObject co : corpus) {
                writer.append(co.path)
                        .append(',')
                        .append(String.valueOf(co.getNumLostWords()))
                        .append(" su ")
                        .append(String.valueOf(co.getNumWords()))
                        .append(eol);
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

    }
}
