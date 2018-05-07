# Analisi dei nessi fra tweet e notizie provenienti da stampa quotidiana

Autore: Valentiono Di Cianni\
Relatore: Daniele Radicioni

##Introduzione

L’ idea è quella di creare uno strumento di analisi testuale che preso in input un documento, restituisce in output il grado di Similarity che ha questo documento con un corpus di documenti analizzati in precedenza.
Ad un livello più alto, il documento in input sarà rappresentato da un tweet, mentre il corpus di documenti da una raccolta di articoli di stampa.


##Funzione Caratteristica
La funzione caratteristica è il cuore del progetto, ciò che mi restituisce il risultato finale.
Nello specifico:

Input: <V_q ,m>



dove:
    
V_q : è il vettore query ( il documento in input ) 
m   : è la matrice ( il corpus )


Output:

V_r \in m\mid \max( CS(V_q, V_r))

dove:
 
CS(V_q, V_r) : è la funzione di concept similarity. 
  
V_q : il vettore query
  
V_r : il vettore risultato


##Concept Similarity
La Concept Similarity tra un tweet e un articolo di giornale viene calcolata come la somma del confronto tra ogni termine del tweet ed ogni termine del corpus moltiplicato per il peso di quest’ultimo all’interno del corpus. 

Per evitare che la similarity in un articolo molto lungo, ma poco significativo, sia maggiore rispetto ad esempio un articolo breve ma molto significativo, i risultati ottenuti vengono normalizzati a seconda del numero di parole in ogni documento

Un esempio di quello che ho cercato di descrivere sopra.

##Vettori di rappresentazione
Ogni file all’interno del corpus di documenti è un insieme di vettori di rappresentazione. Questi ultimi, in maniera astratta, possono essere visti come costrutti che rappresentano un concetto.
In particolare, ogni concetto ha questa forma:

<Concept, SysID,Weigth, Syntx>

dove:

Concept :  il concetto rappresentato
    
SysID :  il codice identificativo ottenuto dalla disambiguazione di Babelfy

Weigth :  il peso della parola calcolato tramite TF-IDF 
     
Syntx :  Il Governor della parola (Optional)

Il calcolo del SysID è effettuato tramite query al server di Babbelfy che esegue la disambiguazione e restituisce un ID identificativo.


Il calcolo del Weigth è effettuato tramite calcolo di TF-IDF sull’intero corpus di documenti, ed ad ogni parola specifica è assegnato un numero che rappresenta il peso della parola.


##Corpus di documenti
Il corpus di documenti, dopo la disambiguazione (Babelfy) e l’assegnazione dei pesi tramite TF-IDF ai vari concetti si presenta in questo modo:

I concetti composti da più parole come ad esempio “partito comunista” sono riconosciuti da Babelfy in fase di disambiguazione. 
Per scelta implementativa, a loro viene assegnato un peso che è la somma dei pesi che ottengono le singole parole tramite TF-IDF.
Intuitivamente, sembra un approccio corretto in quanto il numero assegnato da TF-IDF tiene conto delle occorrenze della parola singola in relazione ai documenti e della unicità dell’occorrenza di un termine in un determinato documento, quindi se abbiamo una parola composta, questa sarà sicuramente più unica di una parola singola, e dunque otterrà un peso maggiore nel contesto.


