package it.polito.tdp.food.model;

import java.time.LocalTime;

public class Event implements Comparable<Event> {

	private LocalTime time;
	private Food food;

	/**
	 * @param time
	 * @param type
	 */
	public Event(LocalTime time, Food food) {
		super();
		this.time = time;
		this.food = food;
	}

	public LocalTime getTime() {
		return time;
	}

	public Food getFood() {
		return food;
	}

	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}

}
