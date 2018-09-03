package Analisis_V1;

import Analisis_V1.utils.Concept;
import Analisis_V1.utils.CorpusObject;
import Analisis_V1.utils.Language;
import Analisis_V1.utils.Tweet;

import java.util.*;

public class MainTweetComparator {

    private static long elapsedTimeMillis;
    private final static int NUM_CONCEPTS = 4;
    private final static int PRESS_ACCOURACY = 40;
    private final static String corpusPath = "corpus/vaccini_press/";
    private final static String tempPath = "corpus/vaccini_press/temp/";

    /**
     * Main method: compare the input tweet with che corpus of documents and returns a
     * vector vith the result scores.
     *
     * @param corpus the document corpus
     * @param tweet the tweet we are considering
     * @return the score vector with Concept Similarities between {@code tweet} and {@code corpus}
     */
    private static Vector<Double> compare(Vector<CorpusObject> corpus, Vector<String> tweet) {
        StringBuilder couple = new StringBuilder("{");
        Vector<Double> score = new Vector<>();

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
            // Nomalize doc length
            //score.add(sc / obj.getNumWords());
            score.add(sc);
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



    public static void main(String[] args) {
        Vector<CorpusObject> corpus;
        long start = System.currentTimeMillis();


        //TODO: implementare tweet reader
        TweetReader tweetReader = new TweetReader();
        //Tweet tweet = tweetReader.readTweet("confcommercio dice che i saldi in toscana porteranno grande fatturato ai negozi");
        Tweet tweet = tweetReader.readTweet("A questo punto, per coerenza, invece di cacciare o schedare neri e rom Salvini dovrebbe vaccinarli");

        CorpusManager corpusManager = new CorpusManager(corpusPath, tempPath, Language.IT);
        //CorpusManager corpusManager = new CorpusManager("corpus/JSONcorpus/jsonCorpus.json");

        corpusManager.printLostWords();

        corpus = corpusManager.setLimitConcepts(NUM_CONCEPTS);
        Vector<CorpusObject> c2 = cosineDistance(tweet.getConceptNetVector(), corpus);


        System.out.println("## Results: ##\n");
        Vector<Double> res = compare(c2, tweet.getConceptsID());

        elapsedTimeMillis += System.currentTimeMillis() - start;
        int best = 0;
        for (int i = 0; i < res.size();  i++){
            System.out.println("\n"+c2.get(i) + "/ - Scored: " + res.elementAt(i));
            System.out.println("  -> "+c2.get(i).getConcepts());
            System.out.println("---------------");
            if ( res.elementAt(i) > res.elementAt(best)) best = i;
        }

        System.out.println("\n##### Best Similarity in "+ elapsedTimeMillis / 1000+" second #####\n");
        System.out.println(c2.get(best).path);
        System.out.println(c2.get(best).getContent());
    }
}
