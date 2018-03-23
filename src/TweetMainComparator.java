import java.util.ArrayList;

public class TweetMainComparator {

    public static void main(String[] args) {

        TweetReader t = new TweetReader();
        TweetDictionary td = new TweetDictionary();
        TweetTokenizer tk = new TweetTokenizer(t.readTweets("corpus/tweet"), "stoplists/ita.txt", td);
        ArrayList<TweetTokenizer.TokenList> tokenCorpus = tk.tokenizeCorpus();

        tk.printTokenizedCorpus();

        //td.printDictionary();


    }
}
