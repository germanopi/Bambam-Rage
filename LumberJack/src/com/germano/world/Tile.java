package com.germano.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.germano.entities.Entity;
import com.germano.main.Game;

public class Tile {
	/************ Atributos ************/

	// Sprites do chão e parede
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);

	protected BufferedImage sprite;

	// Posições e tamanho
	private int x;
	private int y;
	protected int width;
	protected int height;

	// Posição e tamanho da mascara de colisão
	private int maskX = 0;
	private int maskY = 0;
	private int maskWidth = 16;
	private int maskHeight = 16;

	/************ Construtor ************/

	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskX = 0;
		this.maskY = 0;
		this.maskWidth = 16;
		this.maskHeight = 16;
	}

	/************ Get/Set ************/

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/************ Lógica ************/

	public void tick() {

	}

	public static boolean isCollidingShoot(WallTile e1, Entity e2) {// Verifica colisão entre entidade e tile
		Rectangle e1Mask = new Rectangle(e1.getX() + 6, e1.getY() + 6, e1.getMaskWidth(), e1.getMaskHeight());
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.getMaskX(), e2.getY() + e2.getMaskY(), e2.getMaskWidth(),
				e2.getMaskHeight());
		return (e1Mask.intersects(e2Mask));

	}

	/************ Renderização ************/

	public void render(Graphics g) {

		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);

		// Mostra as mascaras de colisão dos tiles
		// g.setColor(Color.blue);
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,
		// maskWidth, maskHeight);
	}

	/************************************/
}
