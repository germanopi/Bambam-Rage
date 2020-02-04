package com.germano.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.germano.entities.Ammo;
import com.germano.entities.Enemy;
import com.germano.entities.Entity;
import com.germano.entities.Heart;
import com.germano.entities.Player;
import com.germano.entities.Weapon;
import com.germano.graficos.Spritesheet;
import com.germano.main.Game;

public class World {
	/************ Atributos ************/

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;

	/************************************/

	/************ Construtor ************/

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));

			// Array de todos os pixels do mapa
			int[] pixels = new int[map.getWidth() * map.getHeight()];

			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();

			// Array de todos os pixels com estruturas fixas (tiles) no mapa
			tiles = new Tile[map.getWidth() * map.getHeight()];

			// Pega as cores do mapa
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

			// Converte a cor do mapa nos objetos do mapa
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if (pixelAtual == 0xFF000000) {
						// Chão
						FloorTile floor = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
						tiles[xx + (yy * WIDTH)] = floor;
					} else if (pixelAtual == 0xFFFFFFFF) {
						// Parede
						WallTile wall = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
						tiles[xx + (yy * WIDTH)] = wall;
						Game.walls.add(wall);
					} else if (pixelAtual == 0xFF0026FF) {
						// Player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if (pixelAtual == 0xFFFF0000) {
						// Inimigo
						Enemy enemy = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(enemy);
						Game.enemies.add(enemy);
					} else if (pixelAtual == 0xFF00FFFF) {
						// Arma
						Weapon weapon = new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN);
						Game.entities.add(weapon);
					} else if (pixelAtual == 0xFF4CFF00) {
						// Coração
						Heart heart = new Heart(xx * 16, yy * 16, 16, 16, Entity.LIFE_EN);
						Game.entities.add(heart);
					} else if (pixelAtual == 0xFFFFD800) {
						// Munição no chão
						Ammo axe = new Ammo(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN);
						Game.entities.add(axe);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/************ Lógica ************/

	// Checa se o espaço está vazio
	public static boolean isFree(int xNext, int yNext, int zPlayer) {
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;

		int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;

		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

		if (!((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile))) {
			return true;
		}
		if (zPlayer > 0) {
			return true;
		} else {
			return false;
		}
	}

	// Recomeça o jogo
	public static void restartGame(String level) {

		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);

		return;
	}

	/************************************/

	public void render(Graphics g) {

		// ">>" Divide por 16 mais rapido que "/"
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);

		// Só renderiza oque está na camera
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				// Evita erro com camera em posição negativa
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			
			}
			
		}
	}

	/************************************/

}
