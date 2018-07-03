package Analisis_V1;

import Analisis_V1.utils.Concept;
import Analisis_V1.utils.CorpusObj;

import java.io.IOException;
import java.util.Vector;

public class MainTweetComparator {

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
            // normalizza per la lunghezza dei documenti
            score.add(sc / obj.getNumWords());
        }

        // Reset tfidfTable
        TTCSInterface.resetTable();

        return score;
        //prova di ritorno

    }

    public static void main(String[] args) {

        final String corpusPath = "corpus/press/";
        final String tempPath = "corpus/press/temp/";
        Vector<CorpusObj> corpus;
        Vector<String> tweetIDs;

        TweetReader tweetReader = new TweetReader();

        //CorpusCreator corpusCreator = new CorpusCreator(corpusPath,tempPath);
        //corpus = corpusCreator.createCorpus();

        CorpusCreator corpusCreator = new CorpusCreator("corpus/JSONcorpus/jsonCorpus.json");
        corpus = corpusCreator.getCorpus();


        //todo: implementare tweet reader
        System.out.println("Analisi tweet in input: 'Il teorema di Godel è una vera innovazione: chi non ama la logica formale! #godel #nelcuore' ");
        tweetIDs = tweetReader.getIDsFromTweet("Il teorema di Godel è una vera innovazione: chi non ama la logica formale! #goedel #nelcuore");

        System.out.println("\nRisultati ottenuti:\n");

        try {
            Vector<Double> res = compare(corpus, tweetIDs);
            int best = 0;
            for (int i = 0; i < res.size();  i++){
                System.out.println(corpus.get(i) + "/ - Scored: " + res.elementAt(i));
                if ( res.elementAt(i) > res.elementAt(best)) best = i;
            }

            System.out.println("\n------ Best Similarity ------\n");
            System.out.println(corpus.get(best).getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
