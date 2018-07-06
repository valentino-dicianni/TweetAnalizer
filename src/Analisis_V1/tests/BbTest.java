package Analisis_V1.tests;

import Analisis_V1.*;
import Analisis_V1.utils.Concept;
import Analisis_V1.utils.Language;


import java.util.Vector;



public class BbTest {

    public static void main(String[] args)  {
        BFYidGetter bbnet = new BFYidGetter(Language.IT);


        Vector<Concept> v = bbnet.executePost("ciao");

        System.out.println(v);

        System.out.println("\n------\n");

    }

}
