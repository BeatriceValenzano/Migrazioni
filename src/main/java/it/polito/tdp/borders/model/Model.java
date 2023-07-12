package it.polito.tdp.borders.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	SimpleGraph <Country, DefaultEdge> grafo;
	Map<Integer, Country> idMap;
	List<Country> vertici;
	BordersDAO dao;
	
	private int nPassiSim;
	
	public Model() {
		
		this.idMap = new HashMap<>();
		dao = new BordersDAO();
		this.dao.loadAllCountries(idMap);
		vertici = new LinkedList<>();
	}
	
	public void creaGrafo(int anno) {
		
		grafo = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
		
//		AGGIUNTA VERTICI
		Graphs.addAllVertices(grafo, dao.getVertici(anno, idMap));
		for(Country c : this.grafo.vertexSet()) {
			vertici.add(c);
		}
//		AGGIUNTA ARCHI
		List<Adiacenza> adiacenti = dao.getEdges(anno);
		for(Adiacenza a : adiacenti) {
			grafo.addEdge(idMap.get(a.getState1no()), idMap.get(a.getState2no()));
		}
		System.out.println("Vertici: " + grafo.vertexSet().size() + " e archi " + grafo.edgeSet().size());
		
	}
	
	public List<CountryAndNumber> getCountryAndNumbers() {
		List<CountryAndNumber> result = new LinkedList<>();

		for (Country c : this.grafo.vertexSet()) {
			result.add(new CountryAndNumber(c, this.grafo.degreeOf(c)));  //ritorna un grado di uno specifico vertice
		}

		Collections.sort(result);
		return result;
	}
	
	public List<Country> getVertici() {
		
		if(this.vertici != null) {
			Collections.sort(vertici);
			return vertici;
		}
		
		return null;
	}
	
	public Map<Country, Integer> simulaMigrazione(Country partenza) {
		Simulator sim = new Simulator(this.grafo, partenza);
		sim.initialize();
		sim.run();
		this.nPassiSim = sim.getnPassi();
		return sim.getStanziali();
	}

	public int getnPassiSim() {
		return nPassiSim;
	}
	
	
}
