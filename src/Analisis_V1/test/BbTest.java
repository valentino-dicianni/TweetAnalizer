package Analisis_V1.test;

import Analisis_V1.*;
import java.io.IOException;


public class BbTest {

    public static void main(String[] args) {
        BbNetIdGetter bbnet = new BbNetIdGetter(Language.IT);
        //boolean result = bbnet.executePost("La pianta in giardino è verde");
        //System.out.println("\n------\n");
        bbnet.executePost("Il Sole è una stella di dimensioni medio-piccole costituita principalmente da idrogeno (circa il 74 della sua massa, il 92,1 del suo volume) ed elio (circa il 24-25 della massa, il 7,8 del volume), cui si aggiungono altri elementi più pesanti presenti in tracce. È classificato come una nana gialla di tipo spettrale G2 V: G2 indica che la stella ha una temperatura superficiale di 5 777 K (5 504 °C), caratteristica che le conferisce un colore bianco estremamente intenso e cromaticamente freddo, che però spesso può apparire giallognolo, a causa della diffusione luminosa nell'atmosfera terrestre, in ragione dell'elevazione dell'astro sull'orizzonte e nondimeno della limpidezza atmosferica. La V (5 in numeri romani) indica che il Sole, come la maggior parte delle stelle, è nella sequenza principale, ovvero in una lunga fase di equilibrio stabile in cui l'astro fonde, nel proprio nucleo, l'idrogeno in elio.");
        //bbnet.executePost("Il € Sole è una stella di dimensioni medio-piccole costituita principalmente da idrogeno  (circa il 74 della sua massa, il 92,1 del suo volume) ed elio ");





      /*  try {
            TTCSInterface.launch("{[bn:00015267n,bn:00004222n],[bn:00048144n,bn:00006125n],[bn:00047481n,bn:00079663n]}");
            TTCSInterface.print();

        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

}
