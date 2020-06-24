package it.polito.tdp.food.model;

import java.util.Comparator;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class ComparatoreEdges implements Comparator<DefaultWeightedEdge> {

	private Graph<Food, DefaultWeightedEdge> graph;

	public ComparatoreEdges(Graph<Food, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	@Override
	public int compare(DefaultWeightedEdge e1, DefaultWeightedEdge e2) {

		Double p2 = this.graph.getEdgeWeight(e2);

		return p2.compareTo(this.graph.getEdgeWeight(e1));
	}

}
