package it.polito.tdp.PremierLeague.model;

import java.util.List;

public class BestPlayer {
	Action azioniGiocatore;
	double delta;

	public BestPlayer(Action azioniGiocatore, double delta) {
		this.azioniGiocatore = azioniGiocatore;
		this.delta = delta;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public Action getAzioniGiocatore() {
		return azioniGiocatore;
	}

	public void setAzioniGiocatore(Action azioniGiocatore) {
		this.azioniGiocatore = azioniGiocatore;
	}


}
