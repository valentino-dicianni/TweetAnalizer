package Analisis_V1.utils;

import java.util.Vector;

public class CorpusObj {
    private String path;
    private String content;

    private Vector<Term> terms;


    public CorpusObj(String path, String content,Vector<Term> terms){
        this.content = content;
        this.path = path;
        this.terms = terms;

    }

    public String termsToString(){

        StringBuilder result = new StringBuilder();
        for (Term term: terms) {
            result.append(term.getTerm());
            result.append(" ");
        }

        return result.toString();
    }

}



