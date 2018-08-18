package Analisis_V1.tests;

import Analisis_V1.*;
import Analisis_V1.utils.Concept;
import Analisis_V1.utils.Language;


import java.util.Vector;



public class BbTest {

    public static void main(String[] args)  {
        BFYidGetter bbnet = new BFYidGetter(Language.IT);


        Vector<Concept> v = bbnet.executePost("Quando le emozioni diventano forti e insopprimibili nasce qualcosa di nuovo. Nasce un antico sentimento libero dai vincoli dello spazio e del tempo. Nasce sotto il nome di amore e dall’amore nasce questo libro, che dà sfogo all’irrefrenabile voglia di scrivere. Come una stella che splende e brama nell’ombra, come un ladro che ruba fra l’omertoso silenzio dei complici, l’amore ci lascia dell’amaro in bocca, un incessante senso di impotenza e incapacità di capire che in realtà non sappiamo nulla di lui. Non sai mai cosa sente il tuo cuore anche se sei consapevole di non poterlo frenare, di non potergli comandare di chi innamorarsi. E un giorno, senza preavviso, si spezza. “Chiusa una porta si apre un portone”. Non possiamo cancellare dalle nostre linee un’intera storia. Cautamente, vogliamo ritornare padroni di noi stessi, vogliamo sapere se c’è un mondo che ci accetta, che ci protegge, che ci ama. Un mondo che ci faccia dimenticare tutto, in fretta.");

        System.out.println(v);

        System.out.println("\n------\n");

    }

}
