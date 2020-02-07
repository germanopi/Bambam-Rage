package com.germano.world;

public class Vector2i {

	/************ Atributos ************/

	public int x, y;

	/************ Construtor ************/

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/************ L�gica ************/

	public boolean equals(Object object) {// Compara duas posi��es
		Vector2i vec = (Vector2i) object;
		if (vec.x == this.x && vec.y == this.y) {
			return true;
		}
		return false;
	}
	/************************************/

}
