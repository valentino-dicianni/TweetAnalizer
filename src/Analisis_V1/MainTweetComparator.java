package Analisis_V1;

import Analisis_V1.utils.Concept;
import Analisis_V1.utils.CorpusObj;
import Analisis_V1.utils.Tweet;

import java.util.*;

public class MainTweetComparator {

    private static long elapsedTimeMillis;
    private final static int NUM_CONCEPTS = 10;
    private final static int PRESS_ACCOURACY = 10;
    private final static String corpusPath = "corpus/press/";
    private final static String tempPath = "corpus/press/temp/";

    /**
     * Main method: compare the input tweet with che corpus of documents and returns a
     * vector vith the result scores.
     *
     * @param corpus the document corpus
     * @param tweet the tweet we are considering
     * @return the score vector with Concept Similarities between {@code tweet} and {@code corpus}
     */
    private static Vector<Double> compare(Vector<CorpusObj> corpus, Vector<String> tweet) {
        StringBuilder couple = new StringBuilder("{");
        Vector<Double> score = new Vector<>();

        for(CorpusObj obj : corpus) {
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
        for(CorpusObj obj : corpus) {
            double sc = 0;
            for (String id : tweet) {
                for (Concept t : obj.getConcepts()) {
                    sc += (CoverInterface.getScore(id + "_" + t.getSysid()) * t.getWeigth());
                }
            }
            // Nomalize doc length
            //score.add(sc / obj.getNumWords());
            score.add(sc);
        }

        // Reset tfidfTable
        CoverInterface.resetSimTable();

        return score;
    }


    /***
     *
     * @param tweetVector
     * @param corpus
     * @return
     */

    public static Vector<CorpusObj> cosineDistance (Vector<Double> tweetVector, Vector<CorpusObj> corpus, int accouracy ){
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        HashMap<CorpusObj, Double> tempMap = new HashMap<>();

        for(CorpusObj co : corpus){
            for (int i = 0; i < tweetVector.size(); i++) {
                dotProduct += tweetVector.get(i) * co.getConceptNetVector().get(i);
                normA += Math.pow(tweetVector.get(i), 2);
                normB += Math.pow(co.getConceptNetVector().get(i), 2);
            }
            double score = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));

            tempMap.put(co, score);

        }
        if(accouracy < corpus.size()){
            return sortByValue(tempMap, accouracy);
        }
        else return null;
    }

    public static Vector<CorpusObj> sortByValue(Map<CorpusObj, Double> unsortMap, int accouracy) {

        // 1. Convert Map to List of Map
        List<Map.Entry<CorpusObj, Double>> list = new LinkedList<>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        list.sort(Comparator.comparing(o -> (o.getValue())));

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Vector<CorpusObj> res = new Vector<>();


        for (Map.Entry<CorpusObj, Double> entry : list) {
            res.add(entry.getKey());
        }

        return (Vector<CorpusObj>) res.subList(0, accouracy);
    }


    public static void main(String[] args) {
        Vector<CorpusObj> corpus;
        long start = System.currentTimeMillis();


        //TODO: implementare tweet reader
        TweetReader tweetReader = new TweetReader();
        Tweet tweet = tweetReader.readTweet("prova tweet");

        //CorpusManager corpusManager = new CorpusManager(corpusPath, tempPath, Language.IT);
        CorpusManager corpusManager = new CorpusManager("corpus/JSONcorpus/jsonCorpus.json");

        corpus = corpusManager.setLimitConcepts(NUM_CONCEPTS);

        Vector<CorpusObj> c2 = cosineDistance(tweet.getConceptNetVector(), corpus, PRESS_ACCOURACY);


        System.out.println("\nRisultati ottenuti:\n");
        Vector<Double> res = compare(corpus, tweet.getConceptsID());
        elapsedTimeMillis += System.currentTimeMillis() - start;
        int best = 0;
        for (int i = 0; i < res.size();  i++){
            System.out.println(corpus.get(i) + "/ - Scored: " + res.elementAt(i));
            if ( res.elementAt(i) > res.elementAt(best)) best = i;
        }

        System.out.println("\n##### Best Similarity in "+ elapsedTimeMillis / 1000+" second #####\n");
        System.out.println(corpus.get(best).path);
        System.out.println(corpus.get(best).getContent());

    }
}
