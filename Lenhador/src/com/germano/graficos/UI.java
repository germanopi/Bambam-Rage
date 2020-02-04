package com.germano.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.germano.entities.Player;
import com.germano.main.Game;

public class UI {

	/************ Renderização ************/
	
	// Cria a tela de Game Over
	public static void telaGameOver(Graphics g) {
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
			g.drawString("Pressione ENTER para reiniciar", ((Game.WIDTH * Game.SCALE) / 2 )- 200,
					((Game.HEIGHT * Game.SCALE) / 2) + 36);
		}
		// adiciona a mensagem da quantidade de pontos
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 28));
		g.drawString("Você fez: " + Game.player.pontos+" pontos!",((Game.WIDTH * Game.SCALE) / 2) - 130, ((Game.HEIGHT * Game.SCALE) / 2)+70);

	}

	public void render(Graphics g) {
		// Barra de vida
		g.setColor(Color.red);
		g.fillRect(8, 4, 50, 6);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int) ((Game.player.life / Player.maxLife) * 50), 6);
		// Contador Vida
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 9));
		g.drawString((int) Game.player.life + "/" + (int) Player.maxLife, 58, 10);
		// Barra de munição
		g.setColor(Color.black);
		g.fillRect(8, 12, 50, 6);
		g.setColor(Color.yellow);
		g.fillRect(8, 12, (int) ((Game.player.ammo / Game.player.maxAmmo) * 50), 6);
		// Contador de munição
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 9));
		g.drawString((int) Game.player.ammo + "/" + (int) Game.player.maxAmmo, 58, 20);
		// Contador de pontos
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 9));
		g.drawString("Pontos: " + Game.player.pontos, 8, 30);
	}

	/************************************/

}
