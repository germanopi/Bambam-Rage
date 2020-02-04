package com.germano.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.germano.main.Game;
import com.germano.main.Sound;
import com.germano.world.Camera;
import com.germano.world.Tile;
import com.germano.world.WallTile;

public class Shoot extends Entity {

	/************ Atributos ************/

	// posições dos tiros
	private double dir_x;
	private double dir_y;

	private double speed = 4;

	// Tempo de vida dos tiros
	private int duration = 0;
	private int maxDuration = 40;

	// Posição e tamanho da mascara de colisão dos tiros
	private int maskX = -8;
	private int maskY = -8;
	private int maskWidth = 16;
	private int maskHeight = 16;

	/************************************/

	/************ Construtor ************/
	// Construtor dos objetos atirados pela arma
	public Shoot(int x, int y, int width, int height, BufferedImage sprite, double dir_x, double dir_y) {
		super(x, y, width, height, sprite);

		this.dir_x = dir_x;
		this.dir_y = dir_y;

		this.maskX = -8;
		this.maskY = -8;
		this.maskWidth = 16;
		this.maskHeight = 16;
	}

	/************************************/

	/************ Lógica ************/

	// Muda a posição das balas e remove depois de um tempo
	public void tick() {
		x += dir_x * speed;
		y += dir_y * speed;
		duration++;
		if (duration == maxDuration) {
			Game.shoot.remove(this);
			return;
		}
	}

	/************************************/

	/************ Renderização ************/

	public void render(Graphics g) {
		// Verifica se o player está virado para direita
		if (Game.player.dir == 0) {
			g.drawImage(Game.player.SHOOT_RIGHT_EN, this.getX() - 8 - Camera.x, this.getY() - 8 - Camera.y, null);
			// Verifica se o player está virado para esquerda
		} else if (Game.player.dir == 1) {
			g.drawImage(Game.player.SHOOT_LEFT_EN, this.getX() - 8 - Camera.x, this.getY() - 8 - Camera.y, null);
		}

		// Mostra a mascara de colisão dos tiros
		// g.setColor(Color.blue);
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,
		// maskWidth, maskHeight);
	}
}
/************************************/
