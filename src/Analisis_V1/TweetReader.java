package Analisis_V1;

import Analisis_V1.utils.Concept;
import Analisis_V1.utils.Language;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

public class TweetReader {
    private ArrayList<String> tweets = new ArrayList<>();
    private HashSet<String> noRepeatTweet = new HashSet<>();
    private BFYidGetter bbfy = new BFYidGetter(Language.IT);


    /**
     * Parse the input tweets file and returns a set of tweets, deleting all double tweets,
     * and useless file parts.
     *
     * @param path the file path in input
     * @return an HashSet of parsed tweets
     */
    public HashSet<String> parseFile(String path) {
       try {
           String lastLine = "";
           BufferedReader reader = new BufferedReader(new FileReader(path));
           String line = reader.readLine();
           while(line!=null) {

               if(line.contains("text:")){
                   String[] splitStr =  line.split("text: ");
                   tweets.add(splitStr[1]);
                   lastLine = splitStr[1];
                   noRepeatTweet.add(lastLine);
               }
               else{
                   String oldStr = tweets.get(tweets.indexOf(lastLine));
                   String newString = oldStr + " " +  line;
                   tweets.set(tweets.indexOf(lastLine),newString );
                   lastLine = newString;

               }
               line = reader.readLine();

           }
           System.out.println("Done Processing tweets");
           return noRepeatTweet;

       }catch (Exception ex){
           ex.printStackTrace();
       }

       return null;
    }


    public Vector<String> getIDsFromTweet(String tweet){
        Vector<Concept> terms = bbfy.executePost(tweet);
        Vector<String> ids = new Vector<>();
        for(Concept t : terms){
            ids.add(t.getSysid());
        }
        return ids;
    }

}