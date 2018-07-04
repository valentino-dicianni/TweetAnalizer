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

   public Concept (String term, String id, double weigth){
       this.term = term;
       this.sysid = id;
       this.weigth = weigth;
   }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Concept))return false;
        Concept obj = (Concept)o;
        return this.term.equals(obj.term) && this.term.equals(obj.term)
                && this.weigth == obj.weigth;
    }

    @Override
    public String toString() {
        return term + " @ "+ weigth;
    }

}
