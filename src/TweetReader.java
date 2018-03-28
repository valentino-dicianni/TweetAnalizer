import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

public class TweetReader  implements TextReader{
    private ArrayList<String> tweets = new ArrayList<>();
    private HashSet<String> noRepeatTweet = new HashSet<>(); //per eliminare doppioni


    @Override
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

}