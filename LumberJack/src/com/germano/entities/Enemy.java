package com.germano.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.germano.main.Game;
import com.germano.main.Sound;
import com.germano.world.Camera;
import com.germano.world.WallTile;
import com.germano.world.World;

public class Enemy extends Entity {

	/************ Atributos ************/

	// Movimentação dos inimigos
	private boolean moved = false;
	private double speed = 1;

	// Direção dos inimigos
	public int right_dir = 0;
	public int left_dir = 1;
	public int dir = right_dir;

	// Posição e tamanho da mascara de colisão dos inimigos
	private int maskX = 0;
	private int maskY = 0;
	private int maskWidth = 16;
	private int maskHeight = 16;

	// Animação dos inimigos
	private int frames = 0;
	private int maxFrames = 5;
	private int index = 0;
	private int maxIndexes = 3;

	// Guarda Sprites dos inimigos
	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;

	// Atributo dos inimigos
	private int life = 10;
	private boolean isDamaged = false;
	private int damageFrames = 10;
	private int damageCurrent = 0;
	private static  int mortos = 0;

	/************************************/

	/************ Construtor ************/

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];

		// Guarda os sprites dos lados do inimigo
		for (int i = 0; i < 4; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 32, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			leftEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 48, 16, 16);
		}
	}

	/************************************/

	/************ Lógica ************/

	public void tick() {
		moved = false;
		// Se o inimigo não está colidindo com o jogador
		if (this.isCollidingPlayer() == false) {
			// Movimentação dos Inimigos, Seguir o jogador até colidir
			if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY(), this.getZ())
					&& !isCollidingEnemy((int) (x + speed), this.getY())) {
				moved = true;
				x += speed;
			} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY(), this.getZ())
					&& !isCollidingEnemy((int) (x - speed), this.getY())) {

				moved = true;
				x -= speed;
			} else if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed), this.getZ())
					&& !isCollidingEnemy(this.getX(), (int) (y + speed))) {
				moved = true;
				y += speed;
			} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed), this.getZ())
					&& !isCollidingEnemy(this.getX(), (int) (y - speed))) {
				moved = true;
				y -= speed;
			}
			// Se o inimigo está colidindo com o jogador
		} else {
			if (Game.dificult.equals("EASY")) {
				// O player tem 10% chance de perder vida ao ser atacado
				if (Game.rand.nextInt(100) < 10) {
				Game.player.life -= Game.rand.nextInt(3);
					Sound.playerHurt.play();
					Game.player.isDamaged = true;
				}
			} else if (Game.dificult.equals("MEDIUM")) {
				// O player tem 25% chance de perder vida ao ser atacado
				if (Game.rand.nextInt(100) < 25) {
					Game.player.life -= Game.rand.nextInt(3);
					Sound.playerHurt.play();
					Game.player.isDamaged = true;
				}
			} else if (Game.dificult.equals("HARD")) {
				// O player tem 50% chance de perder vida ao ser atacado
				if (Game.rand.nextInt(100) < 50) {
					Game.player.life -= Game.rand.nextInt(3);
					Sound.playerHurt.play();
					Game.player.isDamaged = true;
				}
			}
		}
		// Muda os frames dos inimigos enquanto andam
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndexes) {
					index = 0;
				}
			}
		}

		// Se a vida do inimigo acabar ele morre
		if (life <= 0) {
			destroyself();
			return;
		}

		// Contabiliza o tempo do sprite de dano dos inimigos
		if (isDamaged) {
			this.damageCurrent++;
			if (this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}

		collidingBullet();

	}

	// Entrega os pontos do inimigo e o remove do jogo
	public void destroyself() {
		
		if (Game.dificult.equals("EASY")) {
			Game.pontos += 10;
		} else if (Game.dificult.equals("MEDIUM")) {
			Game.pontos += 20;

		} else if (Game.dificult.equals("HARD")) {
			Game.pontos += 30;
		}
		Game.entities.remove(this);
		Game.enemies.remove(this);
	}

	// Verifica se os inimigos estão colidindo com o tiro
	public void collidingBullet() {
		for (int i = 0; i < Game.shoot.size(); i++) {
			Entity e = Game.shoot.get(i);
			if (Entity.isColliding(this, e)) {
				isDamaged = true;
				life--;
				Sound.woodHurt.play();
				Game.shoot.remove(i);
				return;
			}
		}
	}

	// Verifica se os inimigos estão colidindo com o jogador
	public boolean isCollidingPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskWidth, maskHeight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		return enemyCurrent.intersects(player);
	}

	// Verifica se os inimigos estão colidindo entre eles
	public boolean isCollidingEnemy(int xNext, int yNext) {
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

	/************************************/

	/************ Renderização ************/

	public void render(Graphics g) {
		// Monstra a imagem do inimigo sem levar dano
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			// Mostra a imagem do inimigo levando dano
		} else {
			g.drawImage(Entity.TREE_WHITE_EN, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}

		// Mostra as mascaras de colisão dos inimigos
		// g.setColor(Color.blue);
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,
		// maskWidth, maskHeight);
	}

	/************************************/

}
