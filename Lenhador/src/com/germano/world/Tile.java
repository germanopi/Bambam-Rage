package com.germano.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


import com.germano.main.Game;

public class Tile {
	/************ Atributos ************/

	// Guarda os sprites do chão e parede
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);

	private BufferedImage sprite;
	private int x;
	private int y;

	/************************************/

	/************ Construtor ************/

	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}

	/************************************/

	/************ Renderização ************/

	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}

	/************************************/
}
