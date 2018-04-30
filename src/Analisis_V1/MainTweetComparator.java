package Analisis_V1;

import Analisis_V1.utils.CorpusObj;
import Analisis_V1.utils.Term;

import java.io.IOException;
import java.util.Vector;

public class MainTweetComparator {

    private static double compare(CorpusObj obj, Vector<String> tweet) throws IOException {
        StringBuilder couple = new StringBuilder("{");
        double score = 0;

        for(String id : tweet){
            for(Term t : obj.getTerms()){
                couple.append("[").append(id).append(",").append(t.getSysid()).append("]");
                couple.append(",");

            }
        }
        String res = couple.toString();
        res = res.substring(0, res.length() - 1);
        res += "}";

        TTCSInterface.launch(res);
        //TTCSInterface.print();

        //Compose results
        for(String id : tweet){
            for(Term t : obj.getTerms()){
                score += (TTCSInterface.getScore(id+"_"+t.getSysid()) * t.getWeigth());
            }
        }

        // Reset table
        TTCSInterface.resetTable();

        //System.out.println(score);
        return score;
    }

    public static void main(String[] args) {

        final String corpusPath = "/Users/mac/IdeaProjects/TwitterAnalizer/corpus/press/";
        final String tempPath = "/Users/mac/IdeaProjects/TwitterAnalizer/corpus/press/temp/";
        Vector<CorpusObj> corpus;
        Vector<String> tweetIDs;

        CorpusCreator corpusCreator = new CorpusCreator(corpusPath, tempPath);
        TweetReader tweetReader = new TweetReader();

        corpus = corpusCreator.createCorpus();

        //todo: implementare tweet reader
        tweetIDs = tweetReader.getIDsFromTweet("Roberto Fico ha deluso le aspettative: con le sue forze è pronto a sbaragliare il centrodestra");

        System.out.println("Risultati ottenuti:\n");

        double[] results = new double[corpus.size()];

        try {
            int i = 0;
            for (CorpusObj co: corpus) {
                double res = compare(co, tweetIDs);
                System.out.println(co + " scored: "+ res);
                results[i] = res;
                i++;
            }

            int best = 0;
            for (int j = 0; j < results.length;  j++){
                if ( results[j] > results[best] ) best = j;
            }

            System.out.println(corpus.get(best).getContent());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
