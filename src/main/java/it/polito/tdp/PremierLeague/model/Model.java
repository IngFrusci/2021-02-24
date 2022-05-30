package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Map <Integer, Player> idMap;
	Graph<Player, DefaultWeightedEdge> grafo;
	private Simulatore sim;	
	Map<Integer,Action> idMapAzioni;
	BestPlayer MVP;
		
	public Model() {
		dao = new PremierLeagueDAO();
		sim = new Simulatore();
	}
	
	public List<Match> getAllListaMatch(){
		List<Match>lista = this.dao.listAllMatches();
		Collections.sort(lista);
		return lista;
	}
	public void setIdMapPlayer() {
		List<Player> lista = this.dao.listAllPlayers();
		idMap = new HashMap<Integer, Player>();
		for(Player p: lista) {
			idMap.put(p.playerID, p);
		}
	}
	public List<Adiacenze> getAdiacenze(Match m, Map<Integer, Player> idMap){
		List<Adiacenze>listaAdiacenze = this.dao.getAdiacenze(m, idMap);
//		for(Adiacenze a: listaAdiacenze) {
//			System.out.println(a);
//		}
		return listaAdiacenze;
	}
	
	public String creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		idMapAzioni = new HashMap<Integer,Action>();
		for(Action a: dao.listAllMatchAction(m)) {//vertici: tutti i player che hanno preso parte al match indicato
			grafo.addVertex(idMap.get(a.getPlayerID()));
			idMapAzioni.put(a.getPlayerID(), a);
		}
		System.out.println(this.grafo.vertexSet().size());
		for(Adiacenze a: this.getAdiacenze(m,idMap)) {
			if(a.getPeso()>0) {
			Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), Math.abs(a.getPeso()));
			}else{
			Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), Math.abs(a.getPeso()));
			}
			System.out.println(a.getPeso()+"\n");
		}
		return "Grafo creato con #VERTICI : "+this.grafo.vertexSet().size() +" e con #ARCHI: "+this.grafo.edgeSet().size()+"\n";
	}
	public String miglioreGiocatore(Match m) {
		double max=0.000;
		Player migliore = null;
		
		for(Player p: this.grafo.vertexSet()) {
			double s = 0.0;
			double entranti = 0.00;
			double uscenti = 0.00;
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				 uscenti += this.grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge ee : this.grafo.incomingEdgesOf(p)) {
				entranti += this.grafo.getEdgeWeight(ee);
			}
			s = uscenti - entranti;
			if(s > max) {
				max = s;
				migliore = p;
			}
		}
		MVP = new BestPlayer(idMapAzioni.get(migliore.getPlayerID()),max);
		
		List<Action> listaAzioniMatch = dao.listAllMatchAction(m);
	    for(Action a: listaAzioniMatch) {//vertici: tutti i player che hanno preso parte al match indicato
			if(a.getPlayerID().equals(MVP.getAzioniGiocatore().getPlayerID())) {
				MVP.setAzioniGiocatore(a);
			}}
		
		return "Il miglior giocatore è stato : "+migliore.getName()+" con un'efficienza di "+max;
	}
//	public BestPlayer getGiocatoreMigliore(Match m) { 
//		  double max =0.0; 
//		  BestPlayer MVP = null; 
//		  for(Player p : this.grafo.vertexSet()) { 
//		   double uscenti =0.0; 
//		   double entranti = 0.0; 
//		   double eff=0.0; 
//		   for(DefaultWeightedEdge d : this.grafo.outgoingEdgesOf(p)) { 
//		    uscenti += grafo.getEdgeWeight(d); 
//		   } 
//		   for(DefaultWeightedEdge dd : this.grafo.incomingEdgesOf(p)) { 
//		    entranti += grafo.getEdgeWeight(dd); 
//		   } 
//		    
//		   eff = uscenti - entranti; 
//		    
//		   if(eff > max) { 
//		    max = eff; 
//		    MVP = new BestPlayer(p);
//		    List<Action> listaAzioniMatch = dao.listAllMatchAction(m);
//		    for(Action a: listaAzioniMatch) {//vertici: tutti i player che hanno preso parte al match indicato
//				if(a.getPlayerID()==MVP.getPlayer().getPlayerID()) {
//					MVP.setAzioniGiocatore(a);
//				}
//			}
//		   } 
//		  }
//		  return MVP;
//		 }
	public String simula(int N, Match m) {
		sim.init(N, this.MVP, m);
		sim.run();
		String s = sim.result();
		return s;
	}
}
