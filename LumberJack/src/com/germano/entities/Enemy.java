package com.germano.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.germano.main.Game;
import com.germano.main.Sound;
import com.germano.world.AStar;
import com.germano.world.Camera;
import com.germano.world.Vector2i;

public class Enemy extends Entity {

	/************ Atributos ************/

	// Direção
	public int right_dir = 0;
	public int left_dir = 1;
	public int dir = right_dir;

	// Posição e tamanho da colisão
	private int maskX = 0;
	private int maskY = 0;
	private int maskWidth = 16;
	private int maskHeight = 16;

	// Animação
	private int frames = 0;
	private int maxFrames = 5;
	private int index = 0;
	private int maxIndexes = 3;

	// Sprites
	private BufferedImage[] rightEnemy = new BufferedImage[4];;
	private BufferedImage[] leftEnemy = new BufferedImage[4];

	// Atributos
	private int life = 10;
	private boolean isDamaged = false;
	private int damageFrames = 10;
	private int damageCurrent = 0;

	/************ Construtor ************/

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		for (int i = 0; i < 4; i++) {// Guarda os sprites do lado direito dos inimigos
			rightEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 32, 16, 16);
		}
		for (int i = 0; i < 4; i++) {// Guarda os sprites do lado esquerdo dos inimigos
			leftEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 48, 16, 16);
		}
	}

	/************ Lógica ************/

	public void tick() {
		depth = 0;
		if (!isCollidingPlayer()) { // Se não estiver colidindo com o player
			if (path == null || path.size() == 0) {
				Vector2i start = new Vector2i((int) (x / 16), (int) (y / 16));
				Vector2i end = new Vector2i((int) (Game.player.x / 16), (int) (Game.player.y / 16));
				this.path = AStar.findPath(Game.world, start, end);
			}
		} else { // Se estiver colidindo com o player
			if (Game.dificult.equals("EASY")) {
				if (new Random().nextInt(100) < 10) {
					Sound.playerHurt.play();
					Game.player.life -= Game.rand.nextInt(3);
					Game.player.isDamaged = true;
				}
			} else if (Game.dificult.equals("MEDIUM")) {
				if (new Random().nextInt(100) < 25) {
					Sound.playerHurt.play();
					Game.player.life -= Game.rand.nextInt(3);
					Game.player.isDamaged = true;
				}
			} else if (Game.dificult.equals("HARD")) {
				if (new Random().nextInt(100) < 50) {
					Sound.playerHurt.play();
					Game.player.life -= Game.rand.nextInt(3);
					Game.player.isDamaged = true;
				}
			}
		}

		if (new Random().nextInt(100) < 100) { // Diminuir velocidade dos inimigos
			followPath(path);
		}

		if (new Random().nextInt(100) < 20) { // Atualiza o caminho Inimigo -> Player
			Vector2i start = new Vector2i((int) (x / 16), (int) (y / 16));
			Vector2i end = new Vector2i((int) (Game.player.x / 16), (int) (Game.player.y / 16));
			this.path = AStar.findPath(Game.world, start, end);
		}

		frames++;
		if (frames == maxFrames) { // Muda sprite da animação dos inimigos
			frames = 0;
			index++;
			if (index > maxIndexes) {
				index = 0;
			}
		}

		if (life <= 0) {// Remove inimigo morto
			destroyself();
			return;
		}

		if (isDamaged) {// Conta o tempo da animação de dano dos inimigos
			this.damageCurrent++;
			if (this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}

		collidingBullet();
	}

	public void destroyself() {// Entrega os pontos do inimigo e o remove do jogo
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

	public void collidingBullet() {// Verifica Colisão Inimigo com Tiro
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

	public boolean isCollidingPlayer() {// Verifica Colisão Inimigo com Player
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskWidth, maskHeight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		return enemyCurrent.intersects(player);
	}

	/************ Renderização ************/

	public void render(Graphics g) {
		if (!isDamaged) {// Monstra a imagem do inimigo sem levar dano
			if (dir == right_dir) {
				g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		} else {// Mostra a imagem do inimigo levando dano
			g.drawImage(Entity.TREE_WHITE_EN, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}

		// Mostra as mascaras de colisão dos inimigos
		// g.setColor(Color.blue);
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,
		// maskWidth, maskHeight);
	}

	/************************************/

}
