package com.germano.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.germano.main.Game;
import com.germano.world.Camera;

public class Entity {

	/************ Atributos ************/

	// Pegando os sprites das entidades
	// itens do chão
	public static BufferedImage LIFE_EN = Game.spritesheet.getSprite(0, 16, 16, 16);
	public static BufferedImage AXE_EN = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage AXE_AMMO_EN = Game.spritesheet.getSprite(0, 48, 16, 16);
	// inimigos
	public static BufferedImage TREE_EN = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage TREE_WHITE_EN = Game.spritesheet.getSprite(16, 32, 16, 16);
	// utilizando no personagem
	public static BufferedImage AXE_SHOOT_RIGHT_EN = Game.spritesheet.getSprite(96, 0, 16, 16);
	public static BufferedImage AXE_SHOOT_LEFT_EN = Game.spritesheet.getSprite(112, 0, 16, 16);
	public static BufferedImage AXE_WEAPON_RIGHT_EN = Game.spritesheet.getSprite(96, 16, 16, 16);
	public static BufferedImage AXE_WEAPON_LEFT_EN = Game.spritesheet.getSprite(112, 16, 16, 16);
	public static BufferedImage AXE_WEAPON_RIGHT_WHITE_EN = Game.spritesheet.getSprite(96, 32, 16, 16);
	public static BufferedImage AXE_WEAPON_LEFT_WHITE_EN = Game.spritesheet.getSprite(112, 32, 16, 16);
	
	// Não usado
	// public static BufferedImage HALTER_EN = Game.spritesheet.getSprite(0, 64, 16,16);
	// public static BufferedImage HALTER_AMMO_EN = Game.spritesheet.getSprite(0,80, 16, 16);
	// public static BufferedImage HALTER_SHOOT_OR_HAND_EN =Game.spritesheet.getSprite(96, 64, 16, 16);
	// public static BufferedImage HALTER_SHOOT_OR_HAND_WHITE_EN = Game.spritesheet.getSprite(96, 80, 16, 16);

	// Posições e tamanho das entidades
	protected double x;
	protected double y;
	protected int z;
	protected int width;
	protected int height;

	// Posição e tamanho da mascara de colisão das entidades
	private int maskX = 0;
	private int maskY = 0;
	private int maskWidth = 16;
	private int maskHeight = 16;

	private BufferedImage sprite;

	/************************************/

	/************ Construtor ************/
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskX = 0;
		this.maskY = 0;
		this.maskWidth = width;
		this.maskHeight = height;

	}

	/************************************/

	/************ Get/Set ************/

	public void setX(int newX) {
		this.x = newX;

	}

	public void setY(int newY) {
		this.y = newY;

	}

	public void setZ(int newZ) {
		this.z = newZ;

	}

	public int getX() {
		return (int) this.x;
	}

	public int getY() {
		return (int) this.y;
	}

	public int getZ() {
		return (int) this.z;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
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

	}

	// Verifica colisão entre entidades
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.getMaskX(), e1.getY() + e1.getMaskY(), e1.getMaskWidth(),
				e1.getMaskHeight());
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.getMaskX(), e2.getY() + e2.getMaskY(), e2.getMaskWidth(),
				e2.getMaskHeight());

		if (e1Mask.intersects(e2Mask) && e1.z == e2.z) {
			return true;
		} else {
			return false;
		}
	}

	/************************************/

	/************ Renderização ************/

	public void render(Graphics g) {

		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);

		// Mostra as mascaras de colisão das entidades
		// g.setColor(Color.blue);
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,
		// maskWidth, maskHeight);
	}

	/************************************/
}
