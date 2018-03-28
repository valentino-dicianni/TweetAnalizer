import java.util.ArrayList;
import java.util.HashSet;

public class MainComparator {
    public static void main(String[] args) {

        TextReader reader;
        String stopPath = "stoplists/ita.txt";
        /*
        TweetReader t = new TweetReader();
        TweetDictionary td = new TweetDictionary();
        TweetTokenizer tk = new TweetTokenizer(t.parseFile("corpus/tweet"), "stoplists/ita.txt", td);
        ArrayList<TweetTokenizer.TokenList> tokenCorpus = tk.tokenizeCorpus();

        tk.printTokenizedCorpus();

        td.printDictionary();
        */

        reader = new PressReader();

        Tokenizer tk = new Tokenizer(reader.parseFile("corpus/press"), stopPath, new TweetDictionary());
        ArrayList<TokenList> tokenCorpus = tk.tokenizeCorpus();
        tk.printTokenizedCorpus();



    }
}
