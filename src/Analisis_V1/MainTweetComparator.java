package Analisis_V1;

import Analisis_V1.utils.Concept;
import Analisis_V1.utils.CorpusObj;
import Analisis_V1.utils.Language;

import java.io.IOException;
import java.util.Vector;

public class MainTweetComparator {

    private static long elapsedTimeMillis;
    private final static int NUM_CONCEPTS = 10;
    private final static String corpusPath = "corpus/press/";
    private final static String tempPath = "corpus/press/temp/";

    /**
     * Main method: compare the imput tweet with che corpus of documents and returns a
     * vector vith the result scores.
     *
     * @param corpus the document corpus
     * @param tweet the tweet we are considering
     * @return the score vector with Concept Similarities between {@code tweet} and {@code corpus}
     * @throws IOException from {@code TTCSInterface.launch()}
     */
    private static Vector<Double> compare(Vector<CorpusObj> corpus, Vector<String> tweet) throws IOException {
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

        TTCSInterface.launch("vdicianni","Tavol15pork1" ,res);
        //TTCSInterface.print();

        //Compose results
        for(CorpusObj obj : corpus) {
            double sc = 0;
            for (String id : tweet) {
                for (Concept t : obj.getConcepts()) {
                    sc += (TTCSInterface.getScore(id + "_" + t.getSysid()) * t.getWeigth());
                }
            }
            // Nomalize doc length
            //score.add(sc / obj.getNumWords());
            score.add(sc);
        }

        // Reset tfidfTable
        TTCSInterface.resetTable();

        return score;

    }

    public static void main(String[] args) {
        Vector<CorpusObj> corpus;
        Vector<String> tweetIDs;
        long start = System.currentTimeMillis();

        TweetReader tweetReader = new TweetReader();
        //todo: implementare tweet reader
        tweetIDs = tweetReader.getIDsFromTweet("la zanzara è un'insetto che provoca punture fastidiose" );

        CorpusManager corpusManager = new CorpusManager(corpusPath, tempPath, Language.IT);

        //CorpusManager corpusManager = new CorpusManager("corpus/JSONcorpus/jsonCorpus.json");

        //Optimization
        corpusManager.setLimitConcepts(NUM_CONCEPTS);

        corpus = corpusManager.getCorpus();
        System.out.println("\nRisultati ottenuti:\n");
        try {
            Vector<Double> res = compare(corpus, tweetIDs);
            elapsedTimeMillis += System.currentTimeMillis() - start;
            int best = 0;
            for (int i = 0; i < res.size();  i++){
                System.out.println(corpus.get(i) + "/ - Scored: " + res.elementAt(i));
                if ( res.elementAt(i) > res.elementAt(best)) best = i;
            }

            System.out.println("\n##### Best Similarity in "+ elapsedTimeMillis / 1000+" second #####\n");
            System.out.println(corpus.get(best).path);
            System.out.println(corpus.get(best).getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
