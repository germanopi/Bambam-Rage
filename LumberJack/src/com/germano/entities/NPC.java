package com.germano.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.germano.graficos.UI;
import com.germano.main.Game;

public class NPC extends Entity {

	/************ Atributos ************/

	// Frases
	public String[] frases = new String[2];
	public boolean showFrases = false;
	public static boolean firstTime = true;

	// Animação das frases
	public static int fraseAtual = 0;
	public static int letraAtual = 0;
	public int time = 0;
	public int maxTime = 5;

	/************ Construtor ************/

	public NPC(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "Pegue o machado e derrube essas árvores";
		frases[1] = "Pressione Enter para continuar";

	}

	/************ Lógica ************/

	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		int xNpc = (int) x;
		int yNpc = (int) y;
		if (Math.abs(xPlayer - xNpc) < 50 && Math.abs(yPlayer - yNpc) < 50) { // Distancia NPC->PLAYER
			showFrases = true;
		}

		if (Game.gameState == "JOGANDO") {
			if (showFrases) {
				this.time++;
				if (this.time >= this.maxTime) {
					this.time = 0;
					if (letraAtual < frases[fraseAtual].length()) {
						this.letraAtual++;
					} else {
						if (fraseAtual < frases.length - 1) {
							this.fraseAtual++;
							this.letraAtual = 0;
						}
					}
				}
			}
		}
	}

	/************ Renderização ************/

	public void render(Graphics g) {
		super.render(g);
		if (showFrases && firstTime) {
			UI.dialogo(frases, g);
			tick();
		}
	}

}
