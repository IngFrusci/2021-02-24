package it.polito.tdp.PremierLeague.model;

import java.util.PriorityQueue;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulatore {
	//Modello
	int N_azioni;
	
	//Parametri della simulazione
	private int N_CASA = 11;
	private int N_OSPITE = 11;//n giocatori sq. casa e ospite
	int GOAL_CASA = 0;
	int GOAL_OSPITE = 0;
	int RED_CASA = 0;
	int RED_OSPITE = 0;
	int INFORTUNIO_CASA = 0;
	int INFORTUNIO_OSPITE = 0;
	
	//Coda degli eventi
	private PriorityQueue<Event> queue;
	
	//Creazione eventi
	
	public void init(int N, BestPlayer bp, Match m) {
		//coda degli eventi
		this.queue = new PriorityQueue<Event>();
		for(int i=0; i<=N; i++) {
			creaEvento(bp, m);
		}
	}

	public void run() {
		while(!queue.isEmpty()){
			Event e = queue.poll();
			processaEvento(e);
		}
	}
	public void creaEvento(BestPlayer bp, Match m) {
		if(Math.random()<=0.5) {
			if(N_CASA>N_OSPITE){
				Event e = new Event(EventType.GOAL_CASA);
				this.queue.add(e);
				return;
			}
			if(N_CASA<N_OSPITE) {
				Event e = new Event(EventType.GOAL_OSPITE);
				this.queue.add(e);
				return;
			}
			else if(N_CASA==N_OSPITE) {
				if(bp.getAzioniGiocatore().getTeamID()== m.getTeamHomeID()) {
					Event e = new Event(EventType.GOAL_CASA);
					this.queue.add(e);
					return;
				}else {
					Event e = new Event(EventType.GOAL_OSPITE);
					this.queue.add(e);
					return;
				}
			}
			
		}
		if(Math.random()<=0.3) {
			if(Math.random()<=0.6) {
				if(bp.getAzioniGiocatore().getTeamID()== m.getTeamHomeID()) {
					Event e = new Event(EventType.ESPULSIONE_CASA);
					this.queue.add(e);
					N_CASA--;
					return;
				}else {
					Event e = new Event(EventType.ESPULSIONE_OSPITE);
					this.queue.add(e);
					N_OSPITE--;
					return;
				}
			}else {
				if(bp.getAzioniGiocatore().getTeamID()== m.getTeamHomeID()) {
					Event e = new Event(EventType.ESPULSIONE_OSPITE);
					this.queue.add(e);
					N_OSPITE--;
					return;}
				else {
					Event e = new Event(EventType.ESPULSIONE_CASA);
					this.queue.add(e);
					N_CASA--;
					return;
					}
				}
		}
		if(Math.random()<=0.2) {
			if(Math.random()<=0.5) {
				INFORTUNIO_CASA++;
				Event e = new Event(EventType.INFORTUNIO_CASA);
				this.queue.add(e);
				return;
			}else {
				INFORTUNIO_OSPITE++;
				Event e = new Event(EventType.INFORTUNIO_OSPITE);
				this.queue.add(e);
				return;
			}
		}
		//quando si verifica un infortunio N azioni salienti cresce di 2 o 3 in egual modo
		
	}
	private void processaEvento(Event e) {
		switch(e.getType()) {
			case GOAL_CASA:
				GOAL_CASA++;
				break;
			case GOAL_OSPITE:
				GOAL_OSPITE++;
				break;
			case ESPULSIONE_CASA:
				RED_CASA++;
				break;
			case ESPULSIONE_OSPITE:
				RED_OSPITE++;
				break;
			case INFORTUNIO_CASA:
				if(Math.random()<=0.5) {
					N_azioni = N_azioni+2;
				}else {
					N_azioni = N_azioni+3;
				}
				break;
			case INFORTUNIO_OSPITE:
				if(Math.random()<=0.5) {
					N_azioni = N_azioni+2;
				}else {
					N_azioni = N_azioni+3;
				}
				break;
		}
	}
	public String result() {
		return "Il risultato finale è: \n"+GOAL_CASA+" : "+GOAL_OSPITE+"\n"+
				"Il numero di Giocatori espulsi è stato: \n"+
				"Squadra casa = "+ RED_CASA +"\n"+
				"Squadra ospite = "+RED_OSPITE;
	}

}
