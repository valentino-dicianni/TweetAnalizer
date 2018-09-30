package Analisis_V1;

import Analisis_V1.utils.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MainTweetComparator {

    private static long elapsedTimeMillis;
    private final static int NUM_CONCEPTS = 20;
    private final static int PRESS_ACCOURACY = 10;
    private final static String corpusPath = "corpus/press_99_1/";


    public static void main(String[] args) {
        Vector<CorpusObject> corpus;

        TweetReader tweetReader = new TweetReader("corpus/testTweets_99_1");

        for(int i = 0; i < tweetReader.numTweets; i++) {
            long start = System.currentTimeMillis();
            System.out.println("### Starting analysis n°"+(i+1)+" ###");

            Tweet tweet = tweetReader.readTweet();

            //CorpusManager corpusManager = new CorpusManager(corpusPath+tweet.getAuthor()+"/", corpusPath+tweet.getAuthor()+"/temp/", Language.IT);
            CorpusManager corpusManager = new CorpusManager("corpus/JSONcorpus/jsonCorpus_press_99_1"+tweet.getAuthor()+".json");

            corpus = corpusManager.setLimitConcepts(NUM_CONCEPTS);
            Vector<CorpusObject> c2 = calculateCosineDistance(tweet.getConceptNetVector(), corpus);


            System.out.println("Waiting for results...");
            Vector<ResultObject> res = compare(c2, tweet.getConceptsID());

            elapsedTimeMillis += System.currentTimeMillis() - start;
            System.out.println("\n##### Best Similarity found in " + elapsedTimeMillis / 1000 + " second #####\n");

            res.sort(Comparator.comparingDouble(ResultObject::getScore));

            writeResults(res, "/Users/mac/IdeaProjects/Tesi_twitAnalizer/src/Analisis_V1/results/99_1/" + NUM_CONCEPTS + "_" + tweet.getAuthor() + ".txt");

            System.out.println("=================================================");
        }

    }


    /**
     * Main method: compare the input tweet with che corpus of documents and returns a
     * vector with the result scores.
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

        GroundInterface.calculateConceptSimilarity(res);
        //GroundInterface.print();

        //Compose results
        for(CorpusObject obj : corpus) {
            double sc = 0;
            for (String id : tweet) {
                for (Concept t : obj.getConcepts()) {
                    sc += (GroundInterface.getSimilarityScore(id + "_" + t.getSysid()) * t.getWeigth());
                }
            }
            score.add(new ResultObject(obj, sc));
        }

        // Reset tfidfTable
        GroundInterface.resetSimTable();

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
    private static Vector<CorpusObject> calculateCosineDistance(Vector<Double> tweetVector, Vector<CorpusObject> corpus){
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
            if(i < accouracy-1 ) {
                res.add(entry.getKey());
                i++;
            }
            else if(entry.getKey().path.endsWith("target.txt")){ res.add(entry.getKey()); }
        }
        return res;
    }

    private static void writeResults(Vector<ResultObject> res, String path){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));

            StringBuilder r = new StringBuilder();
            for(int i = res.size() - 1; i >= 0; i--){
                r.append(res.get(i));
            }

            bw.write(r.toString());
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}