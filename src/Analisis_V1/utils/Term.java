package Analisis_V1.utils;

public class Term {
    private String term;
    private String sysid;
    private double weigth;


    public String getTerm() {
        return term;
    }

    public Term(String term, String id){
       this.term = term;
       this.sysid = id;
   }
}
