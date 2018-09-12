package Analisis_V1.utils;

public class ResultObject {

    private CorpusObject corpusObject;
    private double score;


    public ResultObject(CorpusObject corpusObject, double res) {
        this.corpusObject = corpusObject;
        this.score = res;
    }

    public String getContentString() {
        return corpusObject.getContent();
    }

    public CorpusObject getCorpusObject() {
        return corpusObject;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return corpusObject.path.substring(18) + " & " + score+"\n";
    }


}
