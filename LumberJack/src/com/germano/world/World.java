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
import com.germano.entities.Shoot;
import com.germano.entities.Weapon;
import com.germano.graficos.Spritesheet;
import com.germano.main.Game;

public class World {
	/************ Atributos ************/

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;

	/************ Construtor ************/

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));

			int[] pixels = new int[map.getWidth() * map.getHeight()];// Array de todos os pixels do mapa

			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();

			// Array de todos os pixels com estruturas fixas (tiles) no mapa
			tiles = new Tile[map.getWidth() * map.getHeight()];

			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());// Pega as cores do mapa

			for (int xx = 0; xx < map.getWidth(); xx++) {// Converte a cor do mapa nos objetos do mapa
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if (pixelAtual == 0xFF000000) {// Chão
						FloorTile floor = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
						tiles[xx + (yy * WIDTH)] = floor;
					} else if (pixelAtual == 0xFFFFFFFF) {// Parede
						WallTile wall = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
						tiles[xx + (yy * WIDTH)] = wall;
						Game.walls.add(wall);
					} else if (pixelAtual == 0xFF0026FF) {// Player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if (pixelAtual == 0xFFFF0000) {// Inimigo
						Enemy tree = new Enemy(xx * 16, yy * 16, 16, 16, Entity.TREE_EN);
						Game.entities.add(tree);
						Game.enemies.add(tree);
					} else if (pixelAtual == 0xFF00FFFF) {// Arma
						Weapon weapon = new Weapon(xx * 16, yy * 16, 16, 16, Entity.AXE_EN);
						Game.entities.add(weapon);
					} else if (pixelAtual == 0xFF4CFF00) { // Coração
						Heart heart = new Heart(xx * 16, yy * 16, 16, 16, Entity.LIFE_EN);
						Game.entities.add(heart);
					} else if (pixelAtual == 0xFFFFD800) {// Munição no chão
						Ammo axe = new Ammo(xx * 16, yy * 16, 16, 16, Entity.AXE_AMMO_EN);
						Game.entities.add(axe);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//	 	Gerar Mapa Randomicamente, Apaga tudo do construtor, comenta em Enemy quando enemies.size()==0 
//			Game.player.setX(0);
//			Game.player.setY(0);
//			WIDTH = 25;
//			HEIGHT = 25;
//			tiles = new Tile[25 + (WIDTH * HEIGHT)]; 

//			for (int xx = 0; xx < WIDTH; xx++) { // Enche o mundo com parede
//				for (int yy = 0; yy < HEIGHT; yy++) {
//					tiles[xx + yy * WIDTH] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
//				}
//			}

//			int dir = 0;
//			int xx = 0;
//			int yy = 0;

//			for (int i = 0; i < 650; i++) {
//				tiles[xx + yy * WIDTH] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR); // Preenche com chão
//				if (dir == 0) {// direita
//					if (xx < WIDTH) {
//						xx++;
//					}
//				} else if (dir == 1) {// esquerda
//					if (xx > 0) {
//						xx--;
//					}
//				} else if (dir == 2) {// baixo
//					if (yy < HEIGHT) {
//						yy++;
//					}
//				} else if (dir == 3) {// cima
//					if (yy > 0) {
//						yy--;
//					}
//				}

//				if (Game.rand.nextInt(100) < 30) { // Troca de direção
//					dir = Game.rand.nextInt(4);
//				}
//			}
//		}
	}

	/************ Lógica ************/

	public static boolean isFree(int xNext, int yNext, int zPlayer) {// Checa se o espaço está vazio
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

	public static void restartGame(String level) {// Recomeça o jogo
		Game.shoot = new ArrayList<Shoot>();
		Game.walls = new ArrayList<WallTile>();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);

		return;
	}

	/*********** Renderização ************/

	public void render(Graphics g) {

		// ">>" Divide por 16 mais rapido que "/"
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);

		// Só renderiza oque está na camera
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {// Evita erro com camera em posição negativa
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}

	public static void renderMiniMap() { // Renderiza o minimapa
		for (int i = 0; i < Game.minimapaPixels.length; i++) {
			Game.minimapaPixels[i] = 0;
		}
		for (int xx = 0; xx < World.WIDTH; xx++) {
			for (int yy = 0; yy < World.HEIGHT; yy++) {
				if (tiles[xx + (yy * WIDTH)] instanceof WallTile) {
					Game.minimapaPixels[xx + (yy * WIDTH)] = 0xff0000;
				}
			}
		}
		int xPlayer = Game.player.getX() / 16;
		int yPlayer = Game.player.getY() / 16;

		Game.minimapaPixels[xPlayer + (yPlayer * WIDTH)] = 0x0000ff;

		for (int i = 0; i < Game.enemies.size(); i++) {
			int xEnemy = Game.enemies.get(i).getX() / 16;
			int yEnemy = Game.enemies.get(i).getY() / 16;
			Game.minimapaPixels[xEnemy + (yEnemy * WIDTH)] = 0x00ff00;
		}
	}

	/************************************/

}
