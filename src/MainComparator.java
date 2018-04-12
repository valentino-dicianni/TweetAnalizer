import Interfaces.TextReader;
import Tokenizer.*;

import java.util.ArrayList;

public class MainComparator {
    public static void main(String[] args) {

        TextReader reader;
        String stopPath = "stoplists/ita.txt";
        /*
        TweetReader t = new TweetReader();
        TweetDictionary td = new TweetDictionary();
        TweetTokenizer tk = new TweetTokenizer(t.parseFile("corpus/tweet"), "stoplists/ita.txt", td);
        ArrayList<TweetTokenizer.Tokenizer.TokenList> tokenCorpus = tk.tokenizeCorpus();

        tk.printTokenizedCorpus();

        td.printDictionary();
        */

        reader = new PressReader();
        TweetDictionary pd = new TweetDictionary();

        Tokenizer tk = new Tokenizer(reader.parseFile("corpus/press"), stopPath,pd);
        ArrayList<TokenList> tokenCorpus = tk.tokenizeCorpus();
        TopicModel topics = new TopicModel(tokenCorpus,pd);
        topics.getTopics(5);



    }
}
