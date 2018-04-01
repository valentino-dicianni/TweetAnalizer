import java.util.HashMap;

public class TweetDictionary implements Dictionary{

    private HashMap<String, Integer> dictionary = new HashMap<>();
    private int numWords = 0;

    public boolean putWord(String word){
        if(!dictionary.containsKey(word)){
            dictionary.put(word, numWords);
            numWords++;
            return true;
        }
        else{
            return false;
        }
    }

    public boolean containsWord(String word){
        return dictionary.containsKey(word);
    }

    public int getIndex(String word){
        return dictionary.get(word);
    }




    public void printDictionary(){
        for(String iterator : dictionary.keySet()){
            String value = dictionary.get(iterator).toString();
            System.out.println("- KEY: "+ iterator + " INDEX: " + value);
        }

    }

    public int getSize(){
        return dictionary.size();
    }



}
