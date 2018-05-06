package Analisis_V1.utils;

import java.util.HashMap;
import java.util.Vector;

public class CorpusObj {
    public String path;
    public HashMap<String,Double> table;
    private String content;
    private Vector<Term> terms;
    private  int numWords;



    public CorpusObj(String path, String content,Vector<Term> terms){
        this.content = content;
        this.path = path;
        this.terms = terms;
        this.numWords = calculateNumWords(content);
    }

    public int getNumWords() {
        return numWords;
    }

    private int calculateNumWords(String content) {
        String[] words = content.replaceAll("[^a-zA-Z ]", "").split("\\s+");
        return words.length;
    }

    public Vector<Term> getTerms() {
        return terms;
    }

    public String getContent() {
        return content;
    }

    public String termsToString(){
        StringBuilder result = new StringBuilder();
        for (Term term: terms) {
            result.append(term.getString());
            result.append(" ");
        }
        return result.toString();
    }

    public void assignWeigths() {
        for (Term term : terms) {
            String[] pieces = term.getString().split(" ");
            for (String str : pieces) {
                if (table.get(str) != null)
                    term.setWeigth(term.getWeigth() + table.get(str));
                else System.out.println("---> ERROR getting weigths from table Term: " + term.getString());
            }
            //System.out.println( "TERM: " +  term.getString() + "\tWeigth: " + term.getWeigth() + "\tSYSID: " + term.getSysid());
        }
    }

    @Override
    public String toString() {
        return ("Copus Object Path: " + path);
    }
}



