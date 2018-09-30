package Analisis_V1;

import Analisis_V1.utils.Concept;
import Analisis_V1.utils.Language;
import Analisis_V1.utils.Tweet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Vector;

public class TweetReader {
    public int numTweets = 0;
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private BabelNetIDsUtility bbfy = new BabelNetIDsUtility(Language.IT);
    private int index = 0;




    public TweetReader(String path){
        parseFile(path);
        numTweets = tweets.size();
    }
    /**
     * Parse the input tweets file and returns a set of tweets, deleting all double tweets,
     * and useless file parts.
     *
     * @param path the file path in input
     */
    public void parseFile(String path) {
       try {
           BufferedReader reader = new BufferedReader(new FileReader(path));
           String line = reader.readLine();

           while(line!=null) {
               if(line.contains("text:")){
                   line = line.replaceAll("#", "");
                   String[] splitText =  line.split("text: ");
                   String content = splitText[1];
                   String author = splitText[0].split("USER: ")[1];
                   Tweet tweet = new Tweet(author, content);
                   tweets.add(tweet);
               }
               line = reader.readLine();

           }
       }catch (Exception ex){
           ex.printStackTrace();
       }

    }


    private Vector<String> getIDsFromTweet(String tweet){
        Vector<Concept> concepts = bbfy.executePost(tweet);
        Vector<String> ids = new Vector<>();
        for(Concept t : concepts){
            if(t.getSysid().endsWith("n"))
                ids.add(t.getSysid());
        }
        return ids;
    }


    public Tweet readTweet(){
        if(index < tweets.size()){
            Tweet tw =  tweets.get(index);
            tw.setConceptNetVector(GroundInterface.getConceptNetVector("", tw.getContent()));
            tw.setConceptsID(getIDsFromTweet(tw.getContent()));
            index++;
            return tw;
        }
        return null;
    }

}