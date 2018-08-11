package Analisis_V1.tests;

import Analisis_V1.CorpusManager;
import Analisis_V1.utils.Concept;
import Analisis_V1.utils.CorpusObj;

import java.util.Vector;

public class JsonCorpusTest {
    private final static String corpusPath = "corpus/press/";
    private final static String tempPath = "corpus/press/temp/";


    public static void main(String[] args) {

        Vector<CorpusObj> c;
        Vector<CorpusObj> c1;

        CorpusManager manager = new CorpusManager("corpus/JSONcorpus/jsonCorpus.json");

        c = manager.setLimitConcepts(5);
        c1 = manager.getCorpus();

        for(CorpusObj co : c1){
            System.out.println(co);
            for(Concept con : co.getConcepts())
                System.out.println("# "+con);
            System.out.println("-----------------------------");

        }
    }
}
