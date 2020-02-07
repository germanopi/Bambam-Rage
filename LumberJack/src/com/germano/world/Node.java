package com.germano.world;

public class Node {
	
	/************ Atributos ************/

	public Vector2i tile;
	public Node parent;
	public double fCost, gCost, hCost;
	
	/************************************/

	/************ Construtor ************/

	public Node(Vector2i tile, Node parent, double gCost, double hCost) {
		this.tile = tile;
		this.parent = parent;
		this.gCost = gCost;
		this.hCost = hCost;
		this.fCost = gCost + hCost;
	}
	
	/************************************/

}
