package Analisis_V1.tests;

import Analisis_V1.*;
import Analisis_V1.utils.Concept;
import Analisis_V1.utils.Language;


import java.util.Vector;



public class BbTest {

    public static void main(String[] args)  {
        BabelNetIDsUtility bbnet = new BabelNetIDsUtility(Language.IT);


        Vector<Concept> v = bbnet.executePost("Una giapponese di 36 anni, residente a Firenze, ha denunciato ai carabinieri di esser stata aggredita sabato mattina mentre faceva jogging nel parco di Varlungo, alla periferia sud del capoluogo toscano. L'aggressione, secondo il suo racconto, sarebbe avvenuta intorno alle 5.30 da parte di un uomo di origini asiatiche. La donna avrebbe anche perso conoscenza. Le indagini dei carabinieri della compagnia Oltrarno e del nucleo investigativo del Comando provinciale prendono in considerazione anche l'ipotesi di violenza sessuale. La giapponese è stata ricoverata presso l'ospedale Santissima Annunziata di Ponte a Niccheri con tumefazioni al viso. Nell'aggressione le è stato portato via il cellulare. ");
        System.out.println(v);

        System.out.println("\n------\n");

    }

}
