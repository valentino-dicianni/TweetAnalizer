package Analisis_V1.utils;

import java.util.HashMap;
import java.util.Vector;

/***
 * This class represent a Corpus Document.
 * Every document in the corpus get this kind of representation. A {@code CorpusObj} is
 * a java object with:
 *
 * {@code path} : Document path
 * {@code content} : Document text content
 * {@code numWords} : Number of document's words
 * {@code concepts} : List of concepts disambiguated with Babelfy
 * {@code tfidfTable} : The TFIDF table for this document. Every word in the content gets a value
 * {@code conceptNetVector} : Media vector of word's vector calculated with ConceptNet
 */

public class CorpusObj {
    public  String path;
    private String content;
    private int numWords;
    private Vector<Concept> concepts;
    private HashMap<String,Double> tfidfTable;
    private Vector<Double> conceptNetVector;

    public CorpusObj(String path, String content,Vector<Concept> concepts){
        this.content = content;
        this.path = path;
        this.concepts = concepts;
        this.numWords = calculateNumWords(content);
    }

    public CorpusObj(String path, String content,Vector<Concept> concepts, int numWords, Vector<Double> conceptNetVector){
        this.path = path;
        this.content = content;
        this.concepts = concepts;
        this.numWords = numWords;
        this.conceptNetVector = conceptNetVector;
        this.tfidfTable = null;
    }

    public int getNumWords() {
        return numWords;
    }

    public void setTfidfTable(HashMap<String, Double> tfidfTable) {
        this.tfidfTable = tfidfTable;
    }

    public HashMap<String, Double> getTfidfTable() {
        return tfidfTable;
    }

    public Vector<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Vector<Concept> concepts) {
        this.concepts = concepts;
    }

    public String getContent() {
        return content;
    }

    public Vector<Double> getConceptNetVector() {
        return conceptNetVector;
    }

    public void setConceptNetVector(Vector<Double> conceptNetVector) {
        this.conceptNetVector = conceptNetVector;
    }

    private int calculateNumWords(String content) {
        String[] words = content.replaceAll("[^a-zA-Z0-9]", " ").split("\\s+");
        return words.length;
    }

    public String conceptToString(){
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
                if (tfidfTable.get(str) != null)
                    concept.setWeigth(concept.getWeigth() + tfidfTable.get(str));
                else System.out.println("ERROR getting weigths from tfidfTable Concept: " + concept.getString());
            }
        }
    }

    @Override
    public String toString() {
        return ("Copus Object Path: " + path);
    }
}



