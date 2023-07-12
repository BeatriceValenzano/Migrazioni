package it.polito.tdp.borders.model;

public class Event implements Comparable<Event>{
	
//	tempo (si parla di passi --> intero che si incrementa), tipo e altri valori
	private int time; //livelli di visita in ampiezza
	//tipo di evento solo INGRESSO
	//L'ingresso di un gruppo di persone in una nazione ha come interesse la nazione e il n di persone entranti
	private Country destinazione;
	private int dimensione;
	
	public Event(int time, Country destinazione, int dimensione) {
		super();
		this.time = time;
		this.destinazione = destinazione;
		this.dimensione = dimensione;
	}

	public int getTime() {
		return time;
	}

	public Country getDestinazione() {
		return destinazione;
	}

	public int getDimensione() {
		return dimensione;
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", destinazione=" + destinazione + ", dimensione=" + dimensione + "]";
	}

	@Override
	public int compareTo(Event other) {
		return this.time-other.time;
	}
	
	

}
