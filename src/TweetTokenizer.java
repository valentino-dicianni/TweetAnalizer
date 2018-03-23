import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

public class TweetTokenizer {
    private ArrayList<String> stopwords = new ArrayList<>();
    private HashSet<String> corpus;
    private ArrayList<TokenList> corpusTokenized = new ArrayList<>();
    private TweetDictionary dictionary;


    TweetTokenizer(HashSet<String> tweets, String stopPath, TweetDictionary dictionary){
        this.corpus = tweets;
        this.dictionary = dictionary;
        getStopwords(stopPath);

    }

    private void getStopwords(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while(line!=null) {
                stopwords.add(line);
                line = reader.readLine();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public ArrayList<TokenList> tokenizeCorpus(){
        for(String str : corpus){

            TokenList list = new TokenList();
            String[] tokens = str.split(" ");

            for(String token : tokens){
                if(!isLink(token) && !isStopword(token) && !isCitation(token) ){
                    String res = removePunctuation(token);
                    if(!isStopword(res)){
                        list.addToken(res);
                        dictionary.putWord(res);
                    }
                }

            }
            corpusTokenized.add(list);
        }
        return corpusTokenized;
    }

    private boolean isCitation(String str){
        return str.charAt(0) == '@';
    }

    private boolean isStopword(String str){
        return stopwords.contains(str.toLowerCase());
    }

    private boolean isLink(String str){
        return str.contains("http:") || str.contains("www.") || str.contains("https:");
    }

    private String removePunctuation(String str){
        String rep = str.replaceAll("[^a-z \\sA-Z \\s0-9]","");
        return rep.toLowerCase();
    }
    public void printTokenizedCorpus() {
        for(TokenList tl : corpusTokenized){
            System.out.println(tl.toString() );
        }
    }

    public class TokenList {
        ArrayList<String> tokens  = new ArrayList<>();

        void addToken(String token) {
            tokens.add(token);
        }

        @Override
        public String toString() {
            StringBuilder list = new StringBuilder("TOKEN LIST: ");
            for(String token : tokens){
                list.append(token).append(" | ");
            }
            return list.toString();
        }
    }

}
