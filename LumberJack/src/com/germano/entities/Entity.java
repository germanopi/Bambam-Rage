package com.germano.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import com.germano.main.Game;
import com.germano.world.Camera;
import com.germano.world.Node;
import com.germano.world.Vector2i;

public class Entity {

	/************ Atributos ************/

	// Sprites dos itens do chão
	public static BufferedImage LIFE_EN = Game.spritesheet.getSprite(0, 16, 16, 16);
	public static BufferedImage AXE_EN = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage AXE_AMMO_EN = Game.spritesheet.getSprite(0, 48, 16, 16);
	// Sprites dos inimigos
	public static BufferedImage TREE_EN = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage TREE_WHITE_EN = Game.spritesheet.getSprite(16, 32, 16, 16);
	// Sprites dos utilitarios
	public static BufferedImage AXE_SHOOT_RIGHT_EN = Game.spritesheet.getSprite(96, 0, 16, 16);
	public static BufferedImage AXE_SHOOT_LEFT_EN = Game.spritesheet.getSprite(112, 0, 16, 16);
	public static BufferedImage AXE_WEAPON_RIGHT_EN = Game.spritesheet.getSprite(96, 16, 16, 16);
	public static BufferedImage AXE_WEAPON_LEFT_EN = Game.spritesheet.getSprite(112, 16, 16, 16);
	public static BufferedImage AXE_WEAPON_RIGHT_WHITE_EN = Game.spritesheet.getSprite(96, 32, 16, 16);
	public static BufferedImage AXE_WEAPON_LEFT_WHITE_EN = Game.spritesheet.getSprite(112, 32, 16, 16);

	// Posições e tamanho
	protected double x;
	protected double y;
	protected int z;
	protected int width;
	protected int height;

	// Posição e tamanho da colisão
	public int maskX = 0;
	public int maskY = 0;
	public int maskWidth = 16;
	public int maskHeight = 16;

	private BufferedImage sprite;
	protected List<Node> path;

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

	/************ Lógica ************/

	public void tick() {

	}

	public double calculateDistanced(int x1, int x2, int y1, int y2) { // Distancia euclidiana
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	public boolean isCollidingEnemy(int xNext, int yNext) { // Colisão entre inimigos
		Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskWidth, maskHeight);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskWidth, maskHeight);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}

	public void followPath(List<Node> path) {// Segue o caminho calculado
		if (path != null) { // Encontrou um caminho
			if (path.size() > 0) { // Ainda tem caminho pra ir
				Vector2i target = path.get(path.size() - 1).tile;
				if (x < target.x * 16) {
					x++;
				} else if (x > target.x * 16) {
					x--;
				}
				if (y < target.y * 16) {
					y++;
				} else if (y > target.y * 16) {
					y--;
				}
				if (x == target.x * 16 && y == target.y * 16) { // Pode procurar outro caminho
					path.remove(path.size() - 1);
				}
			}
		}
	}

	public static boolean isColliding(Entity e1, Entity e2) {// Verifica colisão entre entidades
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
