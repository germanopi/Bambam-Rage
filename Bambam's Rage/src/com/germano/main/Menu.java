package com.germano.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.germano.world.World;

public class Menu {

	/************ Atributos ************/

	// Controle da tela de menu
	public boolean isMenu = true;
	public String[] options = { "Novo Jogo", "Carregar Jogo", "Sair" };
	public int currentOption = 0;
	public int maxOptionsMenu = options.length - 1;

	// Controle da tela de dificuldade
	public boolean isDific = false;
	public String[] difficult = { "EASY", "MEDIUM", "HARD" };
	public int currentDific = 0;
	public int maxDific = difficult.length - 1;

	public boolean up;
	public boolean down;
	public boolean enter;
	public boolean sair;

	// Verifica se o jogo foi pausado no meio
	public boolean pause = false;

	/************************************/

	/************ L�gica ************/

	public void tick() {
		// Quando apertar para cima suba a op��o
		if (up) {
			up = false;
			// no Menu
			if (isMenu) {
				currentOption--;
				if (currentOption < 0) {
					currentOption = maxOptionsMenu;
				}
				// nas Dificuldades
			} else if (isDific) {
				currentDific--;
				if (currentDific < 0) {
					currentDific = maxDific;
				}
			}
		}

		// Quando apertar para baixo des�a a op��o
		if (down) {
			down = false;
			// no Menu
			if (isMenu) {
				currentOption++;
				if (currentOption > maxOptionsMenu) {
					currentOption = 0;
				}
				// nas Dificuldades
			} else if (isDific) {
				currentDific++;
				if (currentDific > maxDific) {
					currentDific = 0;
				}
			}
		}

		// Quando apertar enter entre ....
		if (enter) {
			enter = false;
			// ... no Novo Jogo
			if (isMenu && options[currentOption].equals("Novo Jogo")) { // Se escolher Novo Jogo
				// Se vier ao menu apertando esc
				if (pause) {
					isDific = false;
					Game.gameState = "NORMAL";
					pause = false;
					// Se vier do menu principal
				} else {
					isDific = true;
					isMenu = false;
				}
				// ... nas Dificuldades
			} else if (isDific && difficult[currentDific].contentEquals("EASY")) {
				Sound.come�o.play();
				Game.dificult = "EASY";
				Game.gameState = "NORMAL";
				isDific = false;
				isMenu = true;
				pause = false;
			} else if (isDific && difficult[currentDific].contentEquals("MEDIUM")) {
				Sound.come�o.play();
				Game.dificult = "MEDIUM";
				Game.gameState = "NORMAL";
				isDific = false;
				isMenu = true;
				pause = false;
			} else if (difficult[currentDific].contentEquals("HARD")) {
				Sound.come�o.play();
				Game.dificult = "HARD";
				Game.gameState = "NORMAL";
				isDific = false;
				isMenu = true;
				pause = false;
				// ... Na sele��o de saves
			} else if (options[currentOption].equals("Carregar")) {
				// ... Saia do jogo
			}  if (options[currentOption].equals("Sair")) {
				System.exit(1);
			} 
		}

	}

	/************************************/

	/************ Renderiza��o ************/

	public void render(Graphics g) {
		// Criar background transparente
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(new Color(0, 0, 0, 100));

		// Cria o titulo do menu
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 70));
		g.drawString("Bambam's Rage", ((Game.WIDTH * Game.SCALE) / 2) - 240, ((Game.HEIGHT * Game.SCALE) / 2) - 150);

		// Escolhe a cor da fonte do Menu e Dificuldade
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 36));

		// Cria as op��es do menu
		// Se n�o estiver no menu de dificuldades
		if (!isDific) {
			// Se o jogo n�o foi pausado no meio
			if (pause == false) {
				g.drawString("Novo Jogo", ((Game.WIDTH * Game.SCALE) / 2) - 150,
						((Game.HEIGHT * Game.SCALE) / 2) + 150);
				// Se o jogo foi pausado no meio
			} else {
				g.drawString("Continuar", ((Game.WIDTH * Game.SCALE) / 2) - 150,
						((Game.HEIGHT * Game.SCALE) / 2) + 150);
			}
			g.drawString("Carregar Jogo", ((Game.WIDTH * Game.SCALE) / 2) - 150,
					((Game.HEIGHT * Game.SCALE) / 2) + 200);
			g.drawString("Sair", ((Game.WIDTH * Game.SCALE) / 2) - 150, ((Game.HEIGHT * Game.SCALE) / 2) + 250);

			// Cria o selecionador de op��es
			if (options[currentOption].equals("Novo Jogo")) {
				g.drawString(">", ((Game.WIDTH * Game.SCALE) / 2) - 180, ((Game.HEIGHT * Game.SCALE) / 2) + 150);
			} else if (options[currentOption].equals("Carregar Jogo")) {
				g.drawString(">", ((Game.WIDTH * Game.SCALE) / 2) - 180, ((Game.HEIGHT * Game.SCALE) / 2) + 200);
			} else if (options[currentOption].equals("Sair")) {
				g.drawString(">", ((Game.WIDTH * Game.SCALE) / 2) - 180, ((Game.HEIGHT * Game.SCALE) / 2) + 250);
			}
			// Se estiver no menu de dificuldades
		} else if (isDific) {
			// Cria as op��es de dificuldade
			g.drawString("Easy", ((Game.WIDTH * Game.SCALE) / 2) - 150, ((Game.HEIGHT * Game.SCALE) / 2) + 150);
			g.drawString("Medium", ((Game.WIDTH * Game.SCALE) / 2) - 150, ((Game.HEIGHT * Game.SCALE) / 2) + 200);
			g.drawString("Hard", ((Game.WIDTH * Game.SCALE) / 2) - 150, ((Game.HEIGHT * Game.SCALE) / 2) + 250);
			// Cria o selecionador de dificuldade
			if (difficult[currentDific].equals("EASY")) {
				g.drawString(">", ((Game.WIDTH * Game.SCALE) / 2) - 180, ((Game.HEIGHT * Game.SCALE) / 2) + 150);
			} else if (difficult[currentDific].equals("MEDIUM")) {
				g.drawString(">", ((Game.WIDTH * Game.SCALE) / 2) - 180, ((Game.HEIGHT * Game.SCALE) / 2) + 200);
			} else if (difficult[currentDific].equals("HARD")) {
				g.drawString(">", ((Game.WIDTH * Game.SCALE) / 2) - 180, ((Game.HEIGHT * Game.SCALE) / 2) + 250);
			}
		}
	}

	/************************************/
}
