package com.germano.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.germano.entities.NPC;
import com.germano.entities.Player;
import com.germano.main.Game;

public class UI {

	/************ Renderização ************/

	public static void dialogo(String[] frases, Graphics g) { // Cria as frases do NPC
		g.setColor(Color.black);
		g.fillRect(108, 239, 182, 27);
		g.setColor(Color.white);
		g.fillRect(109, 240, 180, 25);
		g.setColor(Color.black);
		g.setFont(Game.newfont);
		g.drawString(frases[NPC.fraseAtual].substring(0,NPC.letraAtual), 110, 250);
	
	}

	public static void rotacionaRetangulo(Graphics g) {// Rotaciona um retangulo pelo mouse
		Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2(200 + 25 - Game.mouse_y, 200 + 25 - Game.mouse_x);
		g2.rotate(angleMouse, 200 + 25, 200 + 25);
		g2.setColor(Color.red);
		g2.fillRect(200, 200, 50, 50);
	}

	public static void telaGameOver(Graphics g) {// Cria a tela de Game Over
		if (Game.gameState.equals("GAME OVER")) {
			// adiciona opacidade
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
			// adiciona mensagem de Game Over
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString("Game Over", ((Game.WIDTH * Game.SCALE) / 2) - 130, (Game.HEIGHT * Game.SCALE) / 2);
			// adiciona mensagem para Reiniciar
			g.setFont(new Font("arial", Font.BOLD, 28));
			if (Game.showMessageGameOver) {
				g.drawString("Pressione ENTER para reiniciar", ((Game.WIDTH * Game.SCALE) / 2) - 200,
						((Game.HEIGHT * Game.SCALE) / 2) + 36);
			}
			// adiciona a mensagem da quantidade de pontos
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, 28));
			g.drawString("Você fez: " + Game.pontos + " pontos!", ((Game.WIDTH * Game.SCALE) / 2) - 130,
					((Game.HEIGHT * Game.SCALE) / 2) + 70);
		}
	}

	public void render(Graphics g) {
		// Barra de vida
		g.setColor(Color.red);
		g.fillRect(8, 4, 50, 6);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int) ((Game.player.life / Player.maxLife) * 50), 6);
		// Contador Vida
		g.setColor(Color.white);
		g.setFont(Game.newfont);
		g.drawString((int) Game.player.life + "/" + (int) Player.maxLife, 58, 10);
		// Barra de munição
		g.setColor(Color.black);
		g.fillRect(8, 12, 50, 6);
		g.setColor(Color.yellow);
		g.fillRect(8, 12, (int) ((Game.player.ammo / Game.player.maxAmmo) * 50), 6);
		// Contador de munição
		g.setColor(Color.white);
		g.setFont(Game.newfont);
		g.drawString((int) Game.player.ammo + "/" + (int) Game.player.maxAmmo, 58, 20);
		// Contador de pontos
		g.setColor(Color.white);
		g.setFont(Game.newfont);
		g.drawString("Pontos: " + Game.pontos, 8, 30);
	}

	/************************************/

}
