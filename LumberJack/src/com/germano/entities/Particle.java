package com.germano.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.germano.main.Game;
import com.germano.world.Camera;

public class Particle extends Entity {

	/************ Atributos ************/

	public int lifeTime = 10;
	public int currentLife = 0;
	public int speed = 2;
	public double dir_x = 0;
	public double dir_y = 0;

	/************ Construtor ************/

	public Particle(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		dir_x = new Random().nextGaussian();
		dir_y = new Random().nextGaussian();

	}

	/************ Lógica ************/

	public void tick() {
		x += dir_x * speed;
		y += dir_y * speed;
		currentLife++;
		if (lifeTime == currentLife) {
			Game.entities.remove(this);
		}
	}

	/************ Renderização ************/

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
	}

}
