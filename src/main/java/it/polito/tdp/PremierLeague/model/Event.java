package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		GOAL_CASA,
		GOAL_OSPITE,
		ESPULSIONE_CASA,
		ESPULSIONE_OSPITE,
		INFORTUNIO_CASA,
		INFORTUNIO_OSPITE
	}
	private EventType type;
	
	public Event(EventType type) {
		this.type = type;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	@Override
	public int compareTo(Event o) {
		return 1;
	}
}
