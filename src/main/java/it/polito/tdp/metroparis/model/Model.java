package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	private Graph<Fermata, DefaultEdge> graph;
	private List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIdMap;
	
	public Model() {
		graph=new SimpleDirectedGraph<>(DefaultEdge.class);
		
		MetroDAO dao=new MetroDAO();
		fermate=dao.getAllFermate();
		
		long start= System.nanoTime();
		Graphs.addAllVertices(graph, fermate);
		this.fermateIdMap=new HashMap<>();
		for(Fermata f: this.fermate) {
			fermateIdMap.put(f.getIdFermata(), f);
		}
		
		/*
		//METODO 1 PER AGGIUNGERE EDGES: Prendo tutte le coppie di vertici
		for(Fermata fp: fermate) {
			for(Fermata fa: fermate) {
				if(dao.fermateConnesse(fp, fa))
						this.graph.addEdge(fp, fa);
			}
		}
		
		//METODO 2: Chiedo ad ogni vertice a chi sia collegato
		for(Fermata fp: fermate) {
			List<Fermata> connesse=dao.fermateSuccessive(fp, fermateIdMap); //Tutte le fermate connesse ad essa
			for(Fermata fa: connesse)
				this.graph.addEdge(fp,  fa);
		}*/
		
		
		//METODO 3: Sfrutto le istanze della tabella "Connessione"
		List<CoppiaFermate> coppie=dao.coppieFermate(fermateIdMap);
		for(CoppiaFermate coppia: coppie) {
			graph.addEdge(coppia.getFp(), coppia.getFa());
		}
		
		long end=System.nanoTime();
		System.out.println((end-start)/1E6+" millisecondi");
	}

	public Graph<Fermata, DefaultEdge> getGraph(){
		return this.graph;
	}
}
