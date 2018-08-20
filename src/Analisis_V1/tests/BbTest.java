package Analisis_V1.tests;

import Analisis_V1.*;
import Analisis_V1.utils.Concept;
import Analisis_V1.utils.Language;


import java.util.Vector;



public class BbTest {

    public static void main(String[] args)  {
        BFYidGetter bbnet = new BFYidGetter(Language.IT);


        Vector<Concept> v = bbnet.executePost("Premio Strega, vince Janeczek: \"Racconto la vita di Gerda, è il simbolo della donna libera\" 196 voti per \"La ragazza con la Leica\", è la biografia di Gerda Taro e racconta la storia della fotografa con Robert Capa. Dopo 15 anni una donna torna al successo. L'altra novità è nel duello dei maggiori gruppi editoriali, questa volta l'ha spuntata Guanda Dopo 15 anni una donna torna a vincere il Premio Strega. Helena Janeczek trionfa con 196 voti per il suo romanzo “La ragazza con la Leica” (pubblicato da Guanda). Un successo che cambia la storia del premio per due motivi: l’ultima scrittrice ad aver vinto era stata Melania Mazzucco con “Vita” nel 2003. Da allora solo uomini. “Sono contenta – ha detto lei alla fine – spero che dopo di me ce ne siano molte altre”. Ma non è l’unico grande cambiamento. Premio Strega 2018, al Ninfeo di Valle Giulia la 72° edizione del famoso premio letterario Da diverse stagioni il premio era monopolio dei maggiori gruppi editoriali: stavolta ha vinto Guanda, sigla del gruppo Gems. Il libro di Janeczek è la biografia di Gerda Taro e racconta la storia della fotografa con Robert Capa, nonché quella della generazione degli anni Trenta nella guerra civile spagnola. Janeczek nata in Germania nel 1964, vive in Italia dal 1983. Arte e Cultura di HELENA JANECZEK “Ho scelto di raccontare la vita di Gerda perché è il simbolo di una donna libera e indipendente, che ha creduto nelle sue convinzioni”. Premio Strega, vince Helena Janeczek: dopo 15 anni la vittoria è donna Al secondo posto Marco Balzano con 'Resto qui' (Einaudi) con 144 voti e al terzo Sandra Petrignani con 'La corsara. Ritratto di Natalia Ginzburg' (Neri Pozza) con 101 voti. Al quarto Carlo D'Amicis con 'Il gioco' (Mondadori) con 57 voti e al quinto Lia Levi con 'Questa sera è già domani' (Edizioni E/O) con 55 voti. A presiedere il seggio Paolo Cognetti, vincitore della scorsa edizione del Premio Strega. Un tripudio di applausi e felicità al tavolo del Gruppo Gems per il quale, da quando si è costituito, è la prima vittoria del Premio Strega. Radioso il presidente Guanda, Luigi Brioschi che sottolinea come \"lo Strega premia un libro originale, una voce inconfondibile della letteratura e un'autrice che ha un indiscutibile profilo letterario, e premia anche una casa editrice che ha fortemente creduto fin dall'inizio nel romanzo di Helena Janeczek e ne ha visto crescere il consenso\". Stefano Mauri, presidente e ad del Gruppo Gems commenta con una battuta: \"Sono contento per lo Strega, per questo riconoscimento\". Premio Strega, Bonisoli: \"Scateniamo la fame di cultura nei giovani\" Con 'La ragazza con la Leica', Janeczek, scrittrice tedesca naturalizzata italiana, di origini familiari ebreo-polacche, guida anche la cinquina del Premio Strega Campiello. Di origine ebreo-polacca, Helena Janeczek, 54 anni, risiede in Lombardia. Nella sua produzione il volume Lezioni di tenebra - ampiamente autobiografico - è uscito in prima edizione per Mondadori nel 1997 ed è stato ripubblicato nel 2011 da Guanda. Il libro ha vinto il Premio Bagutta Opera Prima. Del 2002 è il romanzo Cibo. Segue, per il Saggiatore, Bloody Cow, storia di Clare Tomkins, la prima vittima della malattia di Creutzfeldt-Jacob, comunemente nota come \"mucca pazza\". Per Guanda ha pubblicato Le rondini di Montecassino, per raccontare la presenza di polacchi, pachistani (e altre nazionalità dimenticate) a una delle battaglie più cruente della Seconda guerra mondiale.");
        System.out.println(v);

        System.out.println("\n------\n");

    }

}
