package it.polito.tdp.borders.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulator {
	
	//non avremo una classe testSimulator, ma è il model che richiamerà il simulatore
	
//	STATO DEL SISTEMA
	private Map<Country, Integer> stanziali;//Per ciascuna nazione devo sapere il numero di stanziali 
	//quelli che transitano li metto negli eventi
	
//	PARAMETRI DELLA SIMULAZIONE
	private Graph<Country, DefaultEdge> graph;
	private int nPersone = 1000;  //è il numero di persone che dobbiamo simulare
	private Country partenza; //stato di partenza
	
//	OUTPUT
	private int nPassi;
	
//	CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	public Simulator(Graph<Country, DefaultEdge> graph, Country partenza) { //parametri che vengono dall'esterno
		this.graph = graph;
		this.partenza = partenza;
		
//		Inizializzo
		this.nPassi = 0;
		this.stanziali = new HashMap<Country, Integer>(); 
		for(Country c : this.graph.vertexSet()) {
			this.stanziali.put(c, 0); //la riempio perchè già conosco tutte le country e poi andrò a cambiare il numero di stanziali
		}
		this.queue = new PriorityQueue<Event>();
	}
	
	public void initialize() {
		this.queue.add(new Event(0, this.partenza, this.nPersone)); //primo evento che a cascata ne genererà molti e che passa al tempo 0 la nazione da cui si parte e il numero di persone
	}
	
	public void run() {
		
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);
			int time = e.getTime();
			Country destinazione = e.getDestinazione();
			int dimensione = e.getDimensione();
			
			this.nPassi = time;
			
//			HO UN SOLO TIPO DI EVENTO CHE E' L'INGRESSO IN UN NUOVO STATO, A SEGUITO DELL'INGRESSO NEL PRIMO
//			STATO DEVO VEDERE CHE INGRESSI NEGLI STATI CONFINANTI DEVONO AVVENIRE
			
//			Mi serve sapere QUANTI e QUALI sono gli stati confinanti rispetto allo stato in cui sono entrato ora 
//			che abbiamo chiamato stato destinazione, cioè destinazione di questo movimento di ingresso
			
//			Mi serve l'elenco dei vertici adiacenti al vertice destinazione
			List<Country> vicini = Graphs.neighborListOf(this.graph, destinazione); //questo metodo va bene per un grafo non orientato
//			in quanto i vertici sono adiancenti nelle due direzioni, altrimenti per i grafi pesati avrei 
//			i metodi predecessore/successore
			
//			Ora so quanti vicini ho e anche quali essi sono, quindi posso sapere quanti ne vanno in ciascun 
//			adiacente
			
			int migranti = dimensione/2/vicini.size();  //sarà 500/n stati vicini -> la div intera approx per difetto
//			Può essere uguale a zero, ciò vuol dire che i migranti che entrano nello stato si fermano tutti lì
			System.out.println(destinazione.getStateAbb()+" ha "+vicini.size()+" confinanti");
			
//			Dimensione / 2 diventano stanziali nello stato destinazione
			if(migranti>0) {
//				Allora devo depositare questo valore negli stati vicini
				for(Country c : vicini) {
					this.queue.add(new Event(time+1, c, migranti/*numero di persone entrate totali poi vediamo gli stanziali*/));
				}
			}
			
//			Ci possono essere gruppi che migrano in francia da due stati differenti es: da germania e italia
			
//			I rimanenti si dividono negli stati adiacenti (vado a vedere il grado dello stato destinazione), dunque generano eventi di tipo INGRESSO con il relativo numero di persone
			
			int nuovi_stanziali = dimensione - migranti * vicini.size(); //perchè se ho un resto>0 nella divisione tra gli stati quelli che rimangono diventano stanziali in destinazione
			this.stanziali.put(destinazione, this.stanziali.get(destinazione)+nuovi_stanziali);
			 
		}
		
		
	}

	public Map<Country, Integer> getStanziali() {
		return stanziali;
	}

	public int getnPassi() {
		// TODO Auto-generated method stub
		return this.nPassi;
	}

}
