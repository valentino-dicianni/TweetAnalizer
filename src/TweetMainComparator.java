import java.util.ArrayList;

public class TweetMainComparator {

    public static void main(String[] args) {

        TweetReader t = new TweetReader();
        TweetTokenizer tk = new TweetTokenizer(t.readTweets("src/tweet"), "stoplists/ita.txt");
        ArrayList<TweetTokenizer.TokenList> tokenCorpus = tk.tokenizeCorpus();

        for(TweetTokenizer.TokenList tl: tokenCorpus){
            System.out.println(tl.toString());

        }
    }
}
