package com.germano.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class FloorTile extends Tile {
	/************ Atributos ************/

	// Posições e tamanho 
	protected int x = 0;
	protected int y = 0;

	// Posição e tamanho da mascara de colisão
	private int maskX = 0;
	private int maskY = 0;
	private int maskWidth = 16;
	private int maskHeight = 16;

	/************ Construtor ************/

	public FloorTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);

		this.x = x;
		this.y = y;

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

	/************ Renderização ************/
	
	public void render(Graphics g) {

		g.drawImage(sprite, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);

		// Mostra as mascaras de colisão do chão
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskWidth, maskHeight);
	}

	/************************************/
}
