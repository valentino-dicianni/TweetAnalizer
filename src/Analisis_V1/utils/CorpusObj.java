package Analisis_V1.utils;

import java.util.HashMap;
import java.util.Vector;

public class CorpusObj {
    public String path;
    public HashMap<String,Double> table;
    private String content;
    private Vector<Concept> concepts;
    private  int numWords;



    public CorpusObj(String path, String content,Vector<Concept> concepts){
        this.content = content;
        this.path = path;
        this.concepts = concepts;
        this.numWords = calculateNumWords(content);
    }

    public int getNumWords() {
        return numWords;
    }

    private int calculateNumWords(String content) {
        String[] words = content.replaceAll("[^a-zA-Z0-9]", " ").split("\\s+");
        return words.length;
    }

    public Vector<Concept> getConcepts() {
        return concepts;
    }

    public String getContent() {
        return content;
    }

    public String termsToString(){
        StringBuilder result = new StringBuilder();
        for (Concept concept: concepts) {
            result.append(concept.getString());
            result.append(" ");
        }
        return result.toString();
    }

    public void assignWeigths() {
        for (Concept concept : concepts) {
            String[] pieces = concept.getString().split(" ");
            for (String str : pieces) {
                if (table.get(str) != null)
                    concept.setWeigth(concept.getWeigth() + table.get(str));
                else System.out.println("---> ERROR getting weigths from table Concept: " + concept.getString());
            }
            //System.out.println( "TERM: " +  term.getString() + "\tWeigth: " + term.getWeigth() + "\tSYSID: " + term.getSysid());
        }
    }

    @Override
    public String toString() {
        return ("Copus Object Path: " + path);
    }
}



