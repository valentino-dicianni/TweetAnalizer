package Analisis_V1;

import Analisis_V1.utils.Language;
import Analisis_V1.utils.Term;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

public class TweetReader {
    private ArrayList<String> tweets = new ArrayList<>();
    private HashSet<String> noRepeatTweet = new HashSet<>(); //per eliminare doppioni
    private BFYidGetter bbfy = new BFYidGetter(Language.IT);



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





    private boolean isCitation(String str){
        return str.charAt(0) == '@';
    }

    private boolean isLink(String str){
        return str.contains("http:") || str.contains("www.") || str.contains("https:");
    }

    private String removePunctuation(String str){
        String rep = str.replaceAll("[^a-z \\sA-Z \\s0-9]","");
        return rep.toLowerCase();
    }

    public Vector<String> getIDsFromTweet(String tweet){
        Vector<Term> terms = bbfy.executePost(tweet);
        Vector<String> ids = new Vector<>();
        for(Term t : terms){
            ids.add(t.getSysid());
        }
        return ids;
    }

}