package Analisis_V1.tests;

import Analisis_V1.*;
import Analisis_V1.utils.Concept;
import Analisis_V1.utils.Language;


import java.util.Vector;



public class BbTest {

    public static void main(String[] args)  {
        BabelNetIDsUtility bbnet = new BabelNetIDsUtility(Language.IT);


        Vector<Concept> v = bbnet.executePost("L’adozione delle politiche di austerità nel sistema sanitario italiano è significativamente associata a una diminuzione dei tassi di vaccinazione per morbillo, parotite e rosolia.\n");
        System.out.println(v);

        System.out.println("\n------\n");

    }

}
