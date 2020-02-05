package com.germano.graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritesheet {
	
	/************ Atributos ************/

	private BufferedImage spritesheet;

	/************************************/

	/************ Construtor ************/

	public Spritesheet(String path) {
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/************************************/

	/************ Get/Set ************/

	public BufferedImage getSprite(int x, int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}

	/************************************/
}
