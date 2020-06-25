package it.polito.tdp.food.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {

	// PARAMETRI DI SIMULAZIONE
	private Integer k = 1; // parametro modificabile
	private LocalTime startTime = LocalTime.of(0, 0);
	private Graph<Food, DefaultWeightedEdge> graph;

	// OUTPUT DA CALCOLARE
	private Duration tempoTotale;

	// STATO DEL SISTEMA
	private List<Food> preparati;
	private int cibiPreparati;

	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;

	public Simulator(Graph<Food, DefaultWeightedEdge> graph2) {
		this.graph = graph2;
	}

	// INIZIALIZZAZIONE
	public void init(Food start, Map<Food, Double> cibi) {
		// coda
		this.queue = new PriorityQueue<>();

		// stato del sistema
		this.preparati = new ArrayList<>();
		this.cibiPreparati = 0;

		// output
		this.tempoTotale = Duration.of(0, ChronoUnit.MINUTES);

		// generiamo eventi iniziali
		if (cibi == null) {
			return;
		}
		for (Food f : cibi.keySet()) {
			Long durata = cibi.get(f).longValue();
			queue.add(new Event(startTime.plus(Duration.ofMinutes(durata)), f));
			this.tempoTotale = this.tempoTotale.plusMinutes(durata);
			preparati.add(f);
			this.cibiPreparati++;
		}

	}

	// getter output

	public Duration getTempoTotale() {
		return tempoTotale;
	}

	public int getNumeroCibi() {
		return this.cibiPreparati;
	}

	// ESECUZIONE
	public void run() {
		// presumibilmente si può lasciare così
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {

		Food bestF = null;
		Double bestP = 0.0;

		for (Food f : Graphs.neighborListOf(this.graph, e.getFood())) {
			if (!preparati.contains(f)) {
				Double peso = this.graph.getEdgeWeight(this.graph.getEdge(f, e.getFood()));
				if (peso > bestP) {
					bestF = f;
					bestP = peso;
				}
			}
		}

		if (bestF != null) {
			Long durata = bestP.longValue();
			queue.add(new Event(e.getTime().plus(Duration.ofMinutes(durata)), bestF));
			this.tempoTotale = this.tempoTotale.plusMinutes(durata);
			preparati.add(bestF);
			this.cibiPreparati++;
		}

	}

	// parametri della simulazione modificabili

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

}
