package Analisis_V1.utils;

public class Concept {
    private String term;
    private String sysid;
    private double weigth = 0.0;


    public String getString() {
        return term;
    }

    public String getSysid() {
        return sysid;
    }

    public double getWeigth() {
        return weigth;
    }

    public void setWeigth(double weigth) {
        this.weigth = weigth;
    }

    public Concept(String term, String id){
       this.term = term;
       this.sysid = id;
   }
}