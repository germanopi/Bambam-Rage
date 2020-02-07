package com.germano.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.germano.graficos.Spritesheet;
import com.germano.main.Game;
import com.germano.main.Sound;
import com.germano.world.Camera;
import com.germano.world.World;

public class Player extends Entity {

	/************ Atributos ************/

	// Movimentação
	public boolean right;
	public boolean up;
	public boolean left;
	public boolean down;
	private boolean moved = false;
	public double speed = 1.2;

	// Direção
	public int right_dir = 0;
	public int left_dir = 1;
	public int dir = right_dir;

	// Animação
	private int frames = 0;
	private int maxFrames = 5;
	private int index = 0;
	private int maxIndexes = 3;
	private int damageFrames = 0;

	// Sprites
	private BufferedImage[] rightPlayer = new BufferedImage[4];;
	private BufferedImage[] leftPlayer = new BufferedImage[4];;
	private BufferedImage playerDamage;

	// Atributos
	public static double life = 100;
	public static double maxLife = 100;
	public double ammo = 0;
	public int maxAmmo = 100;

	// Dano
	public boolean isDamaged = false;
	private boolean hasWeapon = false;
	public boolean shoot = false;
	public boolean mouseShoot = false;
	public int pos_mouse_x;
	public int pos_mouse_y;

	// fakeJump
	public boolean jump = false;
	public int z = 0;
	public int jumpFrames = 50;
	public int jumpCurrent = 0;
	public boolean isJumping = false;
	private int jumpSpeed = 2;
	public boolean jumpUp = false;
	public boolean jumpDown = false;

	/************ Construtor ************/

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		// Guarda os sprites de dano do jogador
		playerDamage = Game.spritesheet.getSprite(16, 64, 16, 16);

		for (int i = 0; i < 4; i++) {// Guarda os sprites do lado direito do jogador
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 64, 16, 16);
		}
		for (int i = 0; i < 4; i++) {// Guarda os sprites do lado esquerdo do jogador
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 80, 16, 16);
		}
	}

	/************ Lógica ************/

	public void tick() {

		if (jump) { // fakeJump
			if (isJumping == false) {
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
		}
		if (isJumping) {
			if (jumpUp) {
				jumpCurrent += jumpSpeed;
			} else if (jumpDown) {
				jumpCurrent -= jumpSpeed;
				if (jumpCurrent <= 0) {
					isJumping = false;
					jumpUp = false;
					jumpDown = false;
				}
			}
			z = jumpCurrent;
			if (jumpCurrent >= jumpFrames) {
				jumpUp = false;
				jumpDown = true;
			}

		}

		moved = false; // Movimentação
		if (right && World.isFree((int) (x + speed), this.getY(), jumpCurrent)) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int) (x - speed), this.getY(), jumpCurrent)) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		if (up && World.isFree(this.getX(), (int) (y - speed), jumpCurrent)) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int) (y + speed), jumpCurrent)) {
			moved = true;
			y += speed;
		}
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

		checkItems(); // Confere colisão com items

		if (isDamaged) {// Conta o tempo da animação de dano do jogador
			this.damageFrames++;
			if (this.damageFrames == 10) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}

		if (shoot) {// Atirar com o teclado
			shoot = false;
			if (hasWeapon && ammo > 0) {
				ammo--;
				int dir_x = 0;
				int dir_y = 0;
				int pos_x = 0;
				int pos_y = 0;
				if (dir == right_dir) {
					dir_x = 1;
					pos_x = 20;
					pos_y = 9;
				} else {
					dir_x = -1;
					pos_x = -6;
					pos_y = 9;
				}
				Shoot shoot = new Shoot(this.getX() + pos_x, this.getY() + pos_y, 16, 16, null, dir_x, dir_y);
				Game.shoot.add(shoot);
			}
		}

		if (mouseShoot) {// Atirar com o mouse
			mouseShoot = false;
			if (hasWeapon && ammo > 0) {
				ammo--;
				int pos_x = 0;
				int pos_y = 0;
				double angle = 0;
				if (dir == right_dir) {
					pos_x = 20;
					pos_y = 9;
					angle = Math.atan2(pos_mouse_y - (this.getY() + pos_y - Camera.y),
							pos_mouse_x - (this.getX() + pos_x - Camera.x));
				} else {
					pos_x = -6;
					pos_y = 9;
					angle = Math.atan2(pos_mouse_y - (this.getY() + pos_y - Camera.y),
							pos_mouse_x - (this.getX() + pos_x - Camera.x));
				}
				// Converte o angulo do mouse em direçoes
				double dir_x = Math.cos(angle);
				double dir_y = Math.sin(angle);

				Shoot shoot = new Shoot(this.getX() + pos_x, this.getY() + pos_y, 16, 16, null, dir_x, dir_y);
				Game.shoot.add(shoot);
			}

		}

		if (life <= 0) { // Jogador está morto
			life = 0;
			Game.gameState = "GAME_OVER";
		}
	}

	public void updateCamera() {// Centralizar camera no jogador
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void checkItems() {// Verifica colisões com items
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			// Colisão com Heart
			if (e instanceof Heart) {
				if (Entity.isColliding(this, e)) {
					life += 20;
					Game.entities.remove(e);
					if (life >= maxLife) {
						life = 100;
						return;
					}
				}
				// Colisão com Ammo
			} else if (e instanceof Ammo) {
				if (Entity.isColliding(this, e)) {
					ammo += 100;
					Game.entities.remove(e);
					if (ammo >= maxAmmo) {
						ammo = 100;
						return;
					}
				}
				// Colisão com Weapon
			} else if (e instanceof Weapon) {
				if (Entity.isColliding(this, e)) {
					hasWeapon = true;
					Game.entities.remove(e);
					return;
				}
			}
		}
	}

	/************ Renderização ************/

	public void render(Graphics g) {

		if (!isDamaged) {// Se não levou dano
			if (dir == right_dir) {// desenha jogador para direita
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasWeapon) {// desenha arma para direita
					g.drawImage(Entity.AXE_WEAPON_RIGHT_EN, this.getX() + 9 - Camera.x, this.getY() + 6 - Camera.y - z,
							null);
				}
			} else if (dir == left_dir) {// desenha jogador para esquerda
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasWeapon) {// desenha arma para esquerda
					g.drawImage(Entity.AXE_WEAPON_LEFT_EN, this.getX() - 9 - Camera.x, this.getY() + 6 - Camera.y - z,
							null);
				}
			}
		} else {// Se levou dano
			if (dir == right_dir) {// desenha jogador levando dano
				g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasWeapon) {// desenha arma para direita levando dano
					g.drawImage(Entity.AXE_WEAPON_RIGHT_WHITE_EN, this.getX() + 9 - Camera.x,
							this.getY() + 6 - Camera.y - z, null);
				}
			} else if (dir == left_dir) {// desenha jogador para esquerda levando dano
				g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasWeapon) {// desenha arma para esquerda levando dano
					g.drawImage(Entity.AXE_WEAPON_LEFT_WHITE_EN, this.getX() - 9 - Camera.x,
							this.getY() + 6 - Camera.y - z, null);
				}
			}
		}

		if (isJumping) {// Desenha a sombra do pulo
			g.setColor(Color.black);
			g.fillOval(this.getX() - Camera.x + 3, this.getY() - Camera.y + 16, 8, 8);
		}
	}

	/************************************/

}