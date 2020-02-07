package com.germano.world;

public class Vector2i {

	/************ Atributos ************/

	public int x, y;

	/************************************/

	/************ Construtor ************/

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/************************************/

	/************ Lógica ************/

	// Compara duas posições
	public boolean equals(Object object) {
		Vector2i vec = (Vector2i) object;
		if (vec.x == this.x && vec.y == this.y) {
			return true;
		}
		return false;
	}
	/************************************/

}
