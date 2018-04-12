import Interfaces.Dictionary;
import Tokenizer.TokenList;

import java.util.*;


public class TopicModel {
    private ArrayList<TokenList> tokenList;
    private Interfaces.Dictionary dictionary;



    TopicModel(ArrayList<TokenList> list, Dictionary dictionary){
        this.tokenList = list;
        this.dictionary = dictionary;
    }

    public void setTokenList(ArrayList<TokenList> tokenList) {
        this.tokenList = tokenList;
    }



    /**
     * Dato un arraylist di tokenList (percedentemente passato attaverso un tokenizzatore)
     * restituisce per ogni lista le N parole più significative
     *
     */


    public void getTopics(int numTopics){
        if(tokenList == null){
            System.out.println("No Tokenizer.TokenList set..");
            return;
        }

        for(TokenList tl : tokenList){

            TopicObject[] topics = new TopicObject[dictionary.getSize()];
            ArrayList<TopicObject> sortedArray = new ArrayList<>();
            List queriedTopics;

            String[] tokens = tl.getTokens();
            for (String str : tokens) {
                if( topics[dictionary.getIndex(str)] == null) {
                    topics[dictionary.getIndex(str)] = new TopicObject(str);
                }
                topics[dictionary.getIndex(str)].incrCounter();
            }

            for(TopicObject to: topics){
                if(to != null){
                    sortedArray.add(to);
                }
            }

            sortedArray.sort(new TopicComparator());
            if(numTopics < sortedArray.size()) {
                queriedTopics = sortedArray.subList(0,numTopics);
                System.out.println(queriedTopics);

            } else System.out.println("too much topics");

            //TODO: ritornare i topics
        }

    }

    private class TopicObject  {
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


        @Override
        public String toString() {
                return "WORD: "+word+" | COUNTER: "+counter;
        }

    }


    private class TopicComparator implements Comparator<TopicObject>{

        @Override
        public int compare(TopicObject o1, TopicObject o2) {
            return o2.getCounter() - o1.getCounter();

        }
    }




}
