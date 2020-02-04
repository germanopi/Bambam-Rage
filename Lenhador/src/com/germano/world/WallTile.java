package com.germano.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.germano.entities.Entity;
import com.germano.main.Game;

public class WallTile extends Tile {

	/************ Atributos ************/

	// Posições e tamanho das paredes
	protected int x = 0;
	protected int y = 0;
	
	// Posição e tamanho da mascara de colisão das paredes
	private int maskX = 0;
	private int maskY = 0;
	private int maskWidth = 16;
	private int maskHeight = 16;

	/************************************/

	/************ Construtor ************/

	public WallTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
		
		this.x = x;
		this.y = y;
		
		this.maskX = 0;
		this.maskY = 0;
		this.maskWidth = 16;
		this.maskHeight = 16;

	}

	/************************************/

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

	public int getMaskX() {
		return maskX;
	}

	public void setMaskX(int maskX) {
		this.maskX = maskX;
	}

	public int getMaskY() {
		return maskY;
	}

	public void setMaskY(int maskY) {
		this.maskY = maskY;
	}

	public int getMaskWidth() {
		return maskWidth;
	}

	public void setMaskWidth(int maskWidth) {
		this.maskWidth = maskWidth;
	}

	public int getMaskHeight() {
		return maskHeight;
	}

	public void setMaskHeight(int maskHeight) {
		this.maskHeight = maskHeight;
	}

	/************************************/

	/************ Lógica ************/

	public void tick() {
		collidingWall();
	}

	// Verifica se o inimigo estão colidindo com o tiro
	public void collidingWall() {
		for (int i = 0; i < Game.shoot.size(); i++) {
			Entity e = Game.shoot.get(i);
			if (Tile.isCollidingShoot(this, e)) {
				Game.shoot.remove(i);
				return;
			}
		}
	}

	/************************************/

	/************ Renderização ************/

	public void render(Graphics g) {

		g.drawImage(sprite, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);

		// Mostra as mascaras de colisão das paredes
		// g.setColor(Color.blue);
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,
		// maskWidth, maskHeight);
	}

	/************************************/

}
