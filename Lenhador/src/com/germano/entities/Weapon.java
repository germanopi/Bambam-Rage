package com.germano.entities;

import java.awt.image.BufferedImage;

public class Weapon extends Entity {
	
	/************ Construtor ************/
	// Construtor dos itens de arma no chão
	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	/************************************/
}
