package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private Map<Integer, Condiment> idMapCondiment;
	private Map<Integer, Food> idMapFood;
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> graph;

	public Model() {
		this.idMapCondiment = new HashMap<>();
		this.idMapFood = new HashMap<>();

		dao = new FoodDao();

		dao.listAllCondiments(idMapCondiment);
		dao.listAllFoods(idMapFood, idMapCondiment);
	}

	public void creaGrafo(Integer number) {

		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		List<Portion> portions = dao.listPortionsByNumber(number, idMapFood);

		for (Portion p : portions) {
			this.graph.addVertex(p.getFood());
		}

		for (Food f : this.graph.vertexSet()) {
			for (Food f2 : dao.listConnectionsOf(f, idMapFood)) {
				if (!this.graph.containsEdge(f, f2) && this.graph.containsVertex(f2)) {
					this.graph.addEdge(f, f2);
					this.graph.setEdgeWeight(f, f2, this.calcolaPeso(f, f2));
				}
			}
		}
	}

	private double calcolaPeso(Food f, Food f2) {
		List<Condiment> intersezione = new ArrayList<>(f.getCondiments());
		intersezione.retainAll(f2.getCondiments());
		double somma = 0;
		for (Condiment c : intersezione) {
			somma += c.getCondiment_calories();
		}

		return somma / intersezione.size();
	}

	public List<Food> getFood() {

		return new ArrayList<Food>(this.graph.vertexSet());
	}

	public Map<Food, Double> getNeighboursOf(Food f) {
		List<DefaultWeightedEdge> edges = new ArrayList<>(this.graph.edgesOf(f));
		Map<Food, Double> mappa = new HashMap<>();

		if (edges.size() == 0) {
			return null;
		} else if (edges.size() <= 5) {
			for (DefaultWeightedEdge e : edges) {
				Food altro;

				if (this.graph.getEdgeSource(e) == f) {
					altro = this.graph.getEdgeTarget(e);
				} else {
					altro = this.graph.getEdgeSource(e);
				}

				mappa.put(altro, this.graph.getEdgeWeight(e));

			}
		} else {
			Collections.sort(edges, new ComparatoreEdges(this.graph));
			int cont = 0;
			for (DefaultWeightedEdge e : edges) {
				Food altro;

				if (this.graph.getEdgeSource(e) == f) {
					altro = this.graph.getEdgeTarget(e);
				} else {
					altro = this.graph.getEdgeSource(e);
				}

				mappa.put(altro, this.graph.getEdgeWeight(e));

				cont++;
				if (cont == 5) {
					break;
				}
			}

		}

		return mappa;
	}

}
