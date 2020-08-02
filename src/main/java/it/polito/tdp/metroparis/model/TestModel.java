package it.polito.tdp.metroparis.model;

public class TestModel {

	public static void main(String[] args) {
		Model model=new Model();
		System.out.println(model.getGraph());
		int numVertici= model.getGraph().vertexSet().size();
		int numEdges=model.getGraph().edgeSet().size();
		System.out.println("Il grafo contiene "+numVertici+" vertici e "+numEdges+" archi.");

	}

}
