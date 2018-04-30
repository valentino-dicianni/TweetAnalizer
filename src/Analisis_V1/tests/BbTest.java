package Analisis_V1.tests;

import Analisis_V1.*;
import Analisis_V1.utils.Language;

import java.io.IOException;


public class BbTest {

    public static void main(String[] args) {
        BFYidGetter bbnet = new BFYidGetter(Language.IT);
        //boolean result = bbnet.executePost("La pianta in giardino è verde");
        //System.out.println("\n------\n");
        bbnet.executePost("I teoremi di Gödel sono teoremi di logica del primo ordine, e devono essere collocati in questo contesto.\n" +
                "Nella logica formale, tanto gli enunciati matematici quanto le dimostrazioni sono scritti in un linguaggio simbolico,\n" +
                "dove è possibile verificare meccanicamente la validità delle dimostrazioni, e non ci possono essere dubbi sul fatto che\n" +
                "un teorema sia conseguenza degli assiomi inizialmente elencati. In teoria, qualsiasi dimostrazione sviluppata entro un\n" +
                "sistema formale può essere verificata da un computer, e di fatto esistono programmi fatti per controllare la validità\n" +
                "delle dimostrazioni o per cercare nuove dimostrazioni (sono chiamati dimostratori automatici dei teoremi o General Theorem Prover (G.T.P.))\n" +
                "Per definire una teoria formale bisogna conoscere e definire alcuni assiomi di partenza. Ad esempio si può partire da\n" +
                "un insieme finito di assiomi, come nella geometria euclidea, oppure, con maggiore generalità, si può consentire che\n" +
                "esista un insieme infinito di assiomi. In questo caso, però, deve essere possibile controllare meccanicamente se un\n" +
                "qualsiasi enunciato è un assioma di quell'insieme oppure no (si parla in questo caso di schema di assiomi).\n");


        System.out.println("\n------\n");



        try {
            TTCSInterface.launch("{[bn:00015267n,bn:00004222n],[bn:00048144n,bn:00006125n],[bn:00047481n,bn:00079663n]}");
            TTCSInterface.print();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
