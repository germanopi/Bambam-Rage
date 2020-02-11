package com.germano.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.germano.entities.Enemy;
import com.germano.entities.Entity;
import com.germano.entities.Player;
import com.germano.entities.Shoot;
import com.germano.graficos.Spritesheet;
import com.germano.graficos.UI;
import com.germano.world.WallTile;
import com.germano.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	/************ Atributos ************/

	// Verifica
	private boolean isRunning = true; // O jogo está sendo executado
	public boolean saveGame = false; // Há um save pronto para ser carregado
	public static boolean reiniciado = false; // Necessidade recomeçar jogo após morrer

	// Gerencia de tela
	public static final int WIDTH = 400;
	public static int HEIGHT = 320;
	public static final int SCALE = 3;
	private BufferedImage image;
	public int[] pixels;
	public int xx;
	public int yy;
	public BufferedImage lightmap;
	public int[] lightMapPixels;

	// Leveis
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

	// Fonte
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
	public static Font newfont;

	// Animação de Game Over
	public static boolean showMessageGameOver = false;
	private int framesGameOver = 0;

	// Pontos do jogador, está aqui porque reiniciar apaga o jogador atual
	public static int pontos = 0;

	// Gerencia do mouse
	public static int mouse_x;
	public static int mouse_y;

	// Gerencia MiniMapa
	public static BufferedImage minimapa;
	public static int[] minimapaPixels;

	// Colisão por pixel
	public BufferedImage sprite1;
	public BufferedImage sprite2;
	public int[] pixels1;
	public int[] pixels2;
	public int x1 = 90;
	public int y1 = 90;
	public int x2 = 100;
	public int y2 = 100;

	// Cutscene
	public static int entrada = 1;
	public static int jogando = 2;
	public static int estado_cena = entrada;
	public int timeCena = 0;
	public int maxtimeCena = 40;

	/************ Construtor ************/

	public Game() {

		// Sound.background.loop(); // Ativa a musica de fundo

		// Comunica ao canvas que recebe entrada por teclado e mouse
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		// Seleciona o tamanho da janela para FullScreen
		// setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));

		// Seleciona o tamanho da janela Customizado
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();

		// Inicializa os Objetos
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels, 0, lightmap.getWidth());
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		shoot = new ArrayList<Shoot>();
		walls = new ArrayList<WallTile>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		minimapa = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		minimapaPixels = ((DataBufferInt) minimapa.getRaster().getDataBuffer()).getData();
		rand = new Random();
		ui = new UI();
		menu = new Menu();
		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(16f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Verificar colisão por pixel
		// try {
		// sprite1 = ImageIO.read(getClass().getResource("/sprite1.png"));
		// sprite2 = ImageIO.read(getClass().getResource("/sprite2.png"));
		// } catch (IOException e2) {
		// e2.printStackTrace();
		// }
		// pixels1 = new int[sprite1.getWidth() * sprite1.getHeight()];
		// sprite1.getRGB(0, 0, sprite1.getWidth(), sprite1.getHeight(), pixels1, 0,
		// sprite1.getWidth());
		// pixels2 = new int[sprite2.getWidth() * sprite2.getHeight()];
		// sprite2.getRGB(0, 0, sprite2.getWidth(), sprite2.getHeight(), pixels2, 0,
		// sprite2.getWidth());
	}

	/************ Inicializar ************/

	public void initFrame() {
		frame = new JFrame("Lumber Jack");// Cria uma instancia de janela
		frame.add(this); // Adiciona o canvas na janela
		frame.setUndecorated(true);// Remove barra de tarefas da janela
		frame.setResizable(false);// Não permite redimencionar a janela
		frame.pack();// Calcula as dimensões da janela
		Image imageIcon = null; // Objeto do tipo imagem
		try {
			imageIcon = ImageIO.read(getClass().getResource("/icon.png"));// Lê onde está o icone
		} catch (IOException e) {
			e.printStackTrace();
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit(); // Objeto do tipo toolkit
		Image imageCursor = toolkit.getImage(getClass().getResource("/icon.png"));
		Cursor c = toolkit.createCustomCursor(imageCursor, new Point(0, 0), "img");
		frame.setCursor(c); // troca o icone pelo customizado
		frame.setIconImage(imageIcon); // troca o icone pelo customizado
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);// Localiza a janela, null está no centro
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Fecha a janela ao clicar em fechar.
		frame.setVisible(true);// Permite ver a janela
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

	/************ Lógica ************/

	public void tick() {
		if (gameState.equals("JOGANDO")) {

			// if (Game.estado_cena == Game.entrada) { // Faz Cutscene de entrada
			// timeCena++;
			// Game.player.setY(Game.player.getY() - 1);
			// if (timeCena == this.maxtimeCena) {
			// Game.estado_cena = jogando;
			// }
			// }

			if (this.saveGame) { // Salva o jogo
				this.saveGame = false;
				String[] opt1 = { "level", "vida", "ammo", "x", "y", "pontos" };
				int[] opt2 = { this.currentLevel, (int) this.player.life, (int) player.ammo, player.getX(),
						player.getY(), Game.pontos };
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Jogo Salvo");
			}

			for (int i = 0; i < entities.size(); i++) { // Carrega as entidades
				Entity e = entities.get(i);
				e.tick();
			}
			for (int i = 0; i < shoot.size(); i++) { // Atualiza os tiros
				shoot.get(i).tick();
			}

			Collections.sort(entities, Entity.nodeSorter); // Ordena as entidades pela depth

			if (enemies.size() == 0) {// Verifica se pode passar de level
				currentLevel++;
				if (currentLevel > maxLevel) {
					currentLevel = 1;
				}
				String newWorld = "level" + currentLevel + ".png";
				World.restartGame(newWorld);
			}
		} else if (gameState.equals("GAME OVER")) { // Animação do Enter ao morrer
			this.framesGameOver++;
			if (this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if (this.showMessageGameOver) {
					this.showMessageGameOver = false;
				} else {
					this.showMessageGameOver = true;
				}
			}
		} else if (gameState.equals("MENU")) {
			if (reiniciado == true) {// Reinicia o jogo
				reiniciado = false;
				String newWorld = "level" + currentLevel + ".png";
				World.restartGame(newWorld);
			}
			player.updateCamera(); // Centraliza a camera
		}

		menu.tick(); // Atualiza o menu

		for (int i = 0; i < walls.size(); i++) {// Atualiza as paredes
			walls.get(i).tick();
		}

		// Verifica colisão por pixel
		// if (this.isCollidingPixelPerfect(x1, y1, x2, y2, pixels1, pixels2, sprite1,
		// sprite2)) {
		// System.out.println("Estão colidindo por pixel");
		// }

	}

	public void drawRectangle(int xoff, int yoff) {// Desenha retangulo manipulando pixels
		for (int xx = 0; xx < 32; xx++) {
			for (int yy = 0; yy < 32; yy++) {
				int xOff = xx + xoff;
				int yOff = yy + yoff;
				if (xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT) {
					continue;
				}
				pixels[xOff + (yOff * WIDTH)] = 0xff0000;
			}
		}
	}

	public void applyLight() {// Aplica efeito de luz
		for (int xx = 0; xx < Game.WIDTH; xx++) {
			for (int yy = 0; yy < Game.HEIGHT; yy++) {
				if (lightMapPixels[xx + (yy * Game.WIDTH)] == 0xffffffff) {
					int pixel = Pixel.getLightBlend(pixels[xx + (yy * WIDTH)], 0x808080, 0);
					pixels[xx + (yy * Game.WIDTH)] = pixel;
				} else {
					continue;
				}
			}
		}
	}

	// Verifica colisão por pixel
	public boolean isCollidingPixelPerfect(int x1, int y1, int x2, int y2, int[] pixels1, int[] pixels2,
			BufferedImage sprite1, BufferedImage sprite2) {
		for (int xx1 = 0; xx1 < sprite1.getWidth(); xx1++) {
			for (int yy1 = 0; yy1 < sprite1.getHeight(); yy1++) {
				for (int xx2 = 0; xx2 < sprite1.getWidth(); xx2++) {
					for (int yy2 = 0; yy2 < sprite1.getHeight(); yy2++) {
						int PixelAtual1 = pixels1[xx1 + yy1 * sprite1.getWidth()];
						int PixelAtual2 = pixels2[xx2 + yy2 * sprite2.getWidth()];
						if (PixelAtual1 == 0x00ffffff || PixelAtual2 == 0x00ffffff) {
							continue;
						}
						if (xx1 + x1 == xx2 + x2 && yy1 + y1 == yy2 + y2) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/************ Renderização ************/

	public void render() {

		// Sequencia de buffers para otimizar a renderização
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = image.getGraphics();// Objeto grafico que renderiza na tela
		world.render(g);// Renderiza o mundo

		for (int i = 0; i < entities.size(); i++) { // Renderiza as entidades
			Entity e = entities.get(i);
			e.render(g);
		}

		for (int i = 0; i < shoot.size(); i++) {// Renderiza as balas
			shoot.get(i).render(g);
		}

		// applyLight(); // Aplica efeito de luz

		ui.render(g);// Renderiza a interface

		player.updateCamera(); // Centraliza a camera

		// drawRectangle(xx, yy); // Desenha retangulo manipulando pixels

		// Renderiza na tela Customizada
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		// Renderiza na tela FullScreen
		// g = bs.getDrawGraphics();
		// g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
		// Toolkit.getDefaultToolkit().getScreenSize().height, null);

		if (gameState.equals("GAME OVER")) { // Cria tela GameOver
			UI.telaGameOver(g);
		} else if (gameState.equals("MENU")) {// Cria tela Menu
			menu.render(g);
		} else if (gameState.equals("DIFICULDADE")) {// Cria tela Menu
			menu.render(g);
		}

		// UI.rotacionaRetangulo(g); // Cria retangulo que rotaciona pelo mouse

		World.renderMiniMap(); // Renderiza o minimapa
		g.drawImage(minimapa, 45, 100, World.WIDTH * 5, World.HEIGHT * 5, null); // Desenha o minimapa

		// Desenha os sprites para colisão por pixel
		g.drawImage(sprite1, x1, y1, null);
		g.drawImage(sprite2, x2, y2, null);

		g.dispose();// Limpa dados das imagens utilizadas antes
		bs.show();// Exibe toda renderização

	}

	/************ Game Looping ************/

	public void run() {
		long lastTime = System.nanoTime();// Guarda o tempo atual do computador com precisão de nanosegundo
		double amountOfTicks = 60.0;// Quantidade de frames por segundo
		double ns = 1000000000 / amountOfTicks;// Converte segundo em nanosegundo
		double delta = 0;
		int frames = 0;// FPS
		double timer = System.currentTimeMillis();// Guarda o tempo atual do computador com menos precisão
		requestFocus();// Focaliza a janela sem necessitar clicar
		while (isRunning) {
			long now = System.nanoTime();// Guarda o tempo atual do computador com precisão de nanosegundo
			delta += (now - lastTime) / ns;// Calcula o intervalo do tick
			// Atualiza agora
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) { // Imprime fps
				// System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();// Para as Thread
	}

	/************ IO Teclado ************/

	@Override
	public void keyPressed(KeyEvent e) {// Ativado se a tecla foi apertada
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if (Game.gameState.equals("MENU") || Game.gameState.equals("DIFICULDADE")) {
				menu.up = true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if (Game.gameState.equals("MENU") || Game.gameState.equals("DIFICULDADE")) {
				menu.down = true;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (gameState.equals("JOGANDO")) {
				this.saveGame = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { // Ativado se a tecla foi solta
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
			if (Game.gameState.equals("MENU")) {
				menu.enter = true;
			}
			if (Game.gameState.equals("GAME OVER")) {
				menu.enter = true;
			}
			if (Game.gameState.equals("DIFICULDADE")) {
				menu.enter = true;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (Game.gameState.equals("DIFICULDADE")) {
				menu.escape = true;
			}
			if (Game.gameState.equals("JOGANDO")) {
				menu.escape = true;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

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
	public void mousePressed(MouseEvent e) {// Ativado se o mouse foi apertado
		player.mouseShoot = true;
		player.pos_mouse_x = (e.getX() / SCALE);
		player.pos_mouse_y = (e.getY() / SCALE);
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mouse_x = e.getX();
		this.mouse_y = e.getY();
	}

	/************************************/

}