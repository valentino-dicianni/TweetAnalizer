package Analisis_V1.utils;

import java.util.Vector;

public class Tweet {
    private String content;
    private Vector<String> conceptsID;
    private Vector<Double> conceptNetVector;


    public Tweet(String str, Vector<String> ids, Vector<Double> netVector) {
        this.content = str;
        this.conceptsID = ids;
        this.conceptNetVector = netVector;

    }


    public Vector<Double> getConceptNetVector() {
        return conceptNetVector;
    }

    public void setConceptNetVector(Vector<Double> conceptNetVector) {
        this.conceptNetVector = conceptNetVector;
    }

    public Vector<String> getConceptsID() {
        return conceptsID;
    }

    public void setConceptsID(Vector<String> conceptsID) {
        this.conceptsID = conceptsID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
