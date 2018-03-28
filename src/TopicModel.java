import java.util.ArrayList;

public class TopicModel {
    private ArrayList<TokenList> tokenList = new ArrayList<>();
    private Dictionary dictionary;


    TopicModel(ArrayList<TokenList> list, Dictionary dictionary){
        this.tokenList = list;
        this.dictionary = dictionary;
    }
}
