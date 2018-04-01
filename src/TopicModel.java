import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class TopicModel {
    private ArrayList<TokenList> tokenList;
    private Dictionary dictionary;



    TopicModel(ArrayList<TokenList> list, Dictionary dictionary){
        this.tokenList = list;
        this.dictionary = dictionary;
    }

    public void setTokenList(ArrayList<TokenList> tokenList) {
        this.tokenList = tokenList;
    }



    //TODO: non ritorna un valore nullo
    /**
     * Dato un arraylist di tokenList (percedentemente passato attaverso un tokenizzatore)
     * restituisce per ogni lista le N parole più significative
     *
     */


    public void getTopics(int numTopics){
        if(tokenList == null){
            System.out.println("No TokenList set..");
            return;
        }

        for(TokenList tl : tokenList){

            TopicObject[] topics = new TopicObject[dictionary.getSize()];

            String[] tokens = tl.getTokens();
            for (String str : tokens) {
                if( topics[dictionary.getIndex(str)] == null) {
                    topics[dictionary.getIndex(str)] = new TopicObject(str);
                }
                topics[dictionary.getIndex(str)].incrCounter();
            }
            Arrays.sort(topics);
            System.out.println(Arrays.toString(topics));


        }

    }

    private class TopicObject implements Comparable<TopicObject> {
        private int counter = 0;
        private String word;

        TopicObject(String str){
            this.word = str;

        }
        private void incrCounter(){
            counter++;
        }

        private int getCounter() {
            return counter;
        }


        //TODO: serve un comparator, perchè spesso ci sono oggetti nulli

        @Override
        public int compareTo(TopicObject o) {
            if(o != null )
                return o.getCounter() - this.counter;
            else return 0;
        }

        @Override
        public String toString() {
            return "Word: "+word+"§Counter: "+counter;
        }




    }




}
