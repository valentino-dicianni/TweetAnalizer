package Analisis_V1.test;

import Analisis_V1.*;
import Analisis_V1.utils.Language;


public class BbTest {

    public static void main(String[] args) {
        BbNetIdGetter bbnet = new BbNetIdGetter(Language.IT);
        //boolean result = bbnet.executePost("La pianta in giardino è verde");
        //System.out.println("\n------\n");
        bbnet.executePost("frase di prova");





      /*  try {
            TTCSInterface.launch("{[bn:00015267n,bn:00004222n],[bn:00048144n,bn:00006125n],[bn:00047481n,bn:00079663n]}");
            TTCSInterface.print();

        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

}
