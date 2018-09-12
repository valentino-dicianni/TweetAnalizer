package Analisis_V1;

import Analisis_V1.utils.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MainTweetComparator {

    private static long elapsedTimeMillis;
    private final static int NUM_CONCEPTS = 20;
    private final static int PRESS_ACCOURACY = 20;
    private final static String corpusPath = "corpus/press_80_20/";
    private final static String tempPath = "corpus/press_80_20/temp/";

    /**
     * Main method: compare the input tweet with che corpus of documents and returns a
     * vector vith the result scores.
     *
     * @param corpus the document corpus
     * @param tweet the tweet we are considering
     * @return the score vector with Concept Similarities between {@code tweet} and {@code corpus}
     */
    private static Vector<ResultObject> compare(Vector<CorpusObject> corpus, Vector<String> tweet) {
        StringBuilder couple = new StringBuilder("{");
        Vector<ResultObject> score = new Vector<>();

        for(CorpusObject obj : corpus) {
            for (String id : tweet) {
                for (Concept t : obj.getConcepts()) {
                    couple.append("[").append(id).append(",").append(t.getSysid()).append("]");
                    couple.append(",");
                }
            }
        }
        String res = couple.toString();
        res = res.substring(0, res.length() - 1);
        res += "}";

        CoverInterface.calculateConceptSimilarity(res);
        //CoverInterface.print();

        //Compose results
        for(CorpusObject obj : corpus) {
            double sc = 0;
            for (String id : tweet) {
                for (Concept t : obj.getConcepts()) {
                    sc += (CoverInterface.getSimilarityScore(id + "_" + t.getSysid()) * t.getWeigth());
                }
            }
            score.add(new ResultObject(obj, sc));
        }

        // Reset tfidfTable
        CoverInterface.resetSimTable();

        return score;
    }

    /***
     * Calculate the cosine distance between the Tweet's conceptNet vector, and every
     * ConceptNet vector in the corpus.
     *
     * @param tweetVector Tweet's conceptNet vector
     * @param corpus corpus vector
     * @return the sorted corpus by cosine distance score.
     */
    private static Vector<CorpusObject> cosineDistance(Vector<Double> tweetVector, Vector<CorpusObject> corpus){
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        HashMap<CorpusObject, Double> tempMap = new HashMap<>();

        for(CorpusObject co : corpus){
            for (int i = 0; i < tweetVector.size(); i++) {
                dotProduct += tweetVector.get(i) * co.getConceptNetVector().get(i);
                normA += Math.pow(tweetVector.get(i), 2);
                normB += Math.pow(co.getConceptNetVector().get(i), 2);
            }
            double score = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));

            tempMap.put(co, score);

        }
        if(corpus.size() < PRESS_ACCOURACY)
            return sortByValue(tempMap, corpus.size());
        else
            return sortByValue(tempMap,PRESS_ACCOURACY);

    }

    private static Vector<CorpusObject> sortByValue(Map<CorpusObject, Double> unsortMap, int accouracy) {
        List<Map.Entry<CorpusObject, Double>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort(Comparator.comparing(o -> (o.getValue())));
        Vector<CorpusObject> res = new Vector<>();

        int i = 0;
        for (Map.Entry<CorpusObject, Double> entry : list) {
            if(i < accouracy ) {
                res.add(entry.getKey());
                i++;
            }
            else break;
        }
        return res;
    }

    private static void writeResults(Vector<ResultObject> res, String path){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));

            StringBuilder r = new StringBuilder();
            for(int i = res.size() - 1; i >= 0; i--){
                r.append(res.get(i));
                System.out.println(res.get(i).getCorpusObject().getConcepts()+"\n");
            }

            bw.write(r.toString());
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Vector<CorpusObject> corpus;
        long start = System.currentTimeMillis();

        //TODO: implementare tweet reader
        TweetReader tweetReader = new TweetReader();
        Tweet tweet = tweetReader.readTweet("vaccini,i presidi alla Camera: 'Ritirare emendamento ke rinvia esclusione.Se passa abbiamo x qst'anno scolastico 1 rischio di insicurezza x la salute Cari provax siete proprio convinti ke siano i non vaccinati a mettere a repentaglio la sicurezza dei vs figli a scuola?￼");

        //CorpusManager corpusManager = new CorpusManager(corpusPath, tempPath, Language.IT);
        CorpusManager corpusManager = new CorpusManager("corpus/JSONcorpus/jsonCorpus_press_99_1.json");

        //corpusManager.printLostWords();

        corpus = corpusManager.setLimitConcepts(NUM_CONCEPTS);
        Vector<CorpusObject> c2 = cosineDistance(tweet.getConceptNetVector(), corpus);


        System.out.println("Waiting for results...");
        Vector<ResultObject> res = compare(c2, tweet.getConceptsID());

        elapsedTimeMillis += System.currentTimeMillis() - start;
        System.out.println("\n##### Best Similarity found in "+ elapsedTimeMillis / 1000+" second #####\n");

        res.sort(Comparator.comparingDouble(ResultObject::getScore));
        //System.out.println(res.get(res.size()-1).getContentString());

        writeResults(res, "/Users/mac/IdeaProjects/Tesi_twitAnalizer/src/Analisis_V1/results/99_1/"+NUM_CONCEPTS+"_"+PRESS_ACCOURACY+".txt");

    }
}
