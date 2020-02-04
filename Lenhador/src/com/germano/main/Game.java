package com.germano.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.germano.entities.Enemy;
import com.germano.entities.Entity;
import com.germano.entities.Player;
import com.germano.entities.Shoot;
import com.germano.graficos.Spritesheet;
import com.germano.graficos.UI;
import com.germano.world.WallTile;
import com.germano.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	/************ Atributos ************/

	private boolean isRunning = true;

	// Tamanho da tela
	public static final int WIDTH = 400;
	public static int HEIGHT = 320;
	public static final int SCALE = 3;

	// Leveis do jogo
	private int currentLevel = 1;
	private int maxLevel = 2;

	public static JFrame frame;
	public static List<WallTile> walls;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Shoot> shoot;
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public static Random rand;
	public static UI ui;
	public static String gameState = "MENU";
	public static String dificult = "EASY";

	public Menu menu;
	private Thread thread;
	private BufferedImage image;

	// Animação de Game Over
	public static boolean showMessageGameOver = false;
	private int framesGameOver = 0;
	
	// Verifica se é necessario recomeçar o jogo depois de morrer
	public boolean reiniciado = false;


	/************************************/

	/************ Construtor ************/

	public Game() {

		// Ativa a musica de fundo
		Sound.musicBackground.loop();

		// Comunica ao canvas que recebe entrada por teclado e mouse
		addKeyListener(this);
		addMouseListener(this);

		// Seleciona o tamanho da janela
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();

		// Inicializa os Objetos
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		shoot = new ArrayList<Shoot>();
		walls = new ArrayList<WallTile>();

		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);

		world = new World("/level1.png");
		rand = new Random();
		ui = new UI();
		menu = new Menu();
	}

	/************************************/

	/************ Inicializar ************/

	public void initFrame() {
		// Cria uma instancia de janela
		frame = new JFrame("Game #1");
		// Adiciona o canvas na janela
		frame.add(this);
		// Não permite redimencionar a janela
		frame.setResizable(false);
		// Calcula as dimensões da janela
		frame.pack();
		// Localiza a janela, null está no centro
		frame.setLocationRelativeTo(null);
		// Fecha a janela ao clicar em fechar.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Permite ver a janela
		frame.setVisible(true);
	}

	private synchronized void start() {
		Thread thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	private synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	/************************************/

	/************ Lógica ************/

	// Atualiza o jogo
	public void tick() {
		// Atualiza o jogo durante gameplay
		if (gameState.equals("NORMAL")) {
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for (int i = 0; i < shoot.size(); i++) {
				shoot.get(i).tick();
			}
			// Verifica se pode passar de level
			if (enemies.size() == 0) {
				currentLevel++;
				if (currentLevel > maxLevel) {
					currentLevel = 1;
				}
				String newWorld = "level" + currentLevel + ".png";
				World.restartGame(newWorld);
			}
			// Faz a animação de pressionar Enter para reiniciar
		} else if (gameState.equals("GAME_OVER")) {
			this.framesGameOver++;
			if (this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if (this.showMessageGameOver) {
					this.showMessageGameOver = false;
				} else {
					this.showMessageGameOver = true;
				}
			}
			// Atualiza o menu se estiver nele
		} else if (gameState.equals("MENU")) {
			// Reinicia o jogo 
			if (reiniciado == true) {
				reiniciado = false;
				String newWorld = "level" + currentLevel + ".png";
				World.restartGame(newWorld);
			}
			menu.tick();
		}

		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).tick();
		}

	}

	/************************************/

	/************ Renderização ************/

	public void render() { // Renderiza o jogo

		// Sequencia de buffers para otimizar a renderização
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		// Cria Objeto grafico para renderizar na tela
		Graphics g = image.getGraphics();

		// Renderiza o mundo
		world.render(g);

		// Renderiza as entidades
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}

		// Renderiza as balas
		for (int i = 0; i < shoot.size(); i++) {
			shoot.get(i).render(g);
		}

		// Renderiza a interface
		ui.render(g);

		// Renderiza na tela
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		// Desenha a tela de Game Over ou o Menu
		if (gameState.equals("GAME_OVER") ) {
			UI.telaGameOver(g);
		} else if (gameState.equals("MENU")) {
			menu.render(g);
		}
		// Limpa dados das imagens utilizadas antes
		g.dispose();
		// Exibe toda renderização
		bs.show();
	}

	/************************************/

	/************ Game Looping ************/

	public void run() {
		// Guarda o tempo atual do computador com precisão de nanosegundo
		long lastTime = System.nanoTime();
		// Quantidade de frames por segundo
		double amountOfTicks = 60.0;
		// Converte segundo em nanosegundo
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		// FPS
		int frames = 0;
		// Guarda o tempo atual do computador com menos precisão
		double timer = System.currentTimeMillis();
		// Focaliza a janela sem necessitar clicar
		requestFocus();
		while (isRunning) {
			// Guarda o tempo atual do computador com precisão de nanosegundo
			long now = System.nanoTime();
			// Calcula o intervalo do tick
			delta += (now - lastTime) / ns;
			// Atualiza agora
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			// Imprime a mensagem a cada 1 segundo
			if (System.currentTimeMillis() - timer >= 1000) {
				//System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		// Para as Thread
		stop();

	}

	/************************************/

	/************ IO Teclado ************/

	@Override
	// Ativado se a tecla foi apertada
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if (gameState.equals("MENU")) {
				menu.up = true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if (gameState.equals("MENU")) {
				menu.down = true;
			}
		}

	}

	@Override
	// Ativado se a tecla foi solta
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			player.shoot = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_X) {
			player.jump = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (gameState.equals("MENU")) {
				menu.enter = true;
			}
			if (gameState.equals("GAME_OVER")) {
				gameState = "MENU";
				reiniciado = true;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	/************************************/

	/************ IO Mouse ************/

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	// Ativado se o mouse foi apertado
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.pos_mouse_x = (e.getX() / SCALE);
		player.pos_mouse_y = (e.getY() / SCALE);
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	/************************************/

}