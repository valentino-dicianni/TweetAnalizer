package Analisis_V1.utils;

import java.util.HashMap;

public class DocumentProperties{
    private HashMap<String,Double> termFreqMap ;
    private HashMap<String,Integer> DocWordCounts;

    public HashMap<String,Double> getTermFreqMap() {
        return termFreqMap;
    }

    public HashMap<String,Integer> getWordCountMap() {
        return DocWordCounts;
    }

    public void setTermFreqMap(HashMap<String, Double> inMap) {
        termFreqMap = new HashMap<>(inMap);
    }


    public void setWordCountMap(HashMap<String, Integer> inMap) {
        DocWordCounts = new HashMap<>(inMap);
    }

}