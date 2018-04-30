package Analisis_V1.tests;

import Analisis_V1.*;
import Analisis_V1.utils.Language;

import java.io.IOException;


public class BbTest {

    public static void main(String[] args) {
        BFYidGetter bbnet = new BFYidGetter(Language.IT);
        //boolean result = bbnet.executePost("La pianta in giardino è verde");
        //System.out.println("\n------\n");
        bbnet.executePost("il m5s è molto forte in italia. Di Maio è il leader del M5S");
        bbnet.printResults();


        System.out.println("\n------\n");



        try {
            TTCSInterface.launch("{[bn:00015267n,bn:00004222n],[bn:00048144n,bn:00006125n],[bn:00047481n,bn:00079663n]}");
            TTCSInterface.print();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
