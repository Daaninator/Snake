import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	int x[] = new int[GAME_UNITS];
	int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten = 0;
	int deaths = 0;
	int appleX;
	int appleY;
	int highscore = 0;
	char direction = 'R';
	boolean running = false;
	int color = 0;
	Timer timer;
	Random random;
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		//drawt alle graphics
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if (running) {
			//zal rooster drawe
			/*
			for (int i = 0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
			}
			*/
			//zal apple drawe
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//zal snake drawe
			for (int i = bodyParts-1; i>=0; i--) {
				if (i ==0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					if (45+i*4<160) g.setColor(new Color(45+i*4, 180, 0));
					else g.setColor(new Color(160, 180, 0));
					
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}

		}
		else {
			gameOver(g);
		}
		//game punten text
		g.setColor(Color.white);
		g.setFont( new Font("Comic Sans MS", Font.BOLD, 30));
		//om het in het midden te krijgen
		FontMetrics metrics = getFontMetrics(g.getFont());
		String score = "score: " + applesEaten;
		int heightScore = g.getFont().getSize();
		g.drawString(score, (SCREEN_WIDTH - metrics.stringWidth(score))/2, heightScore);
		
		//highscore tekst
		g.setFont( new Font("Comic Sans MS", Font.BOLD, 15));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		String score2 = "highscore: " + highscore;
		g.drawString(score2, (g.getFont().getSize())-5, g.getFont().getSize());
		
		//deaths counter tekst
		String score3 = "deaths: " + deaths;
		g.drawString(score3, (g.getFont().getSize())-5, g.getFont().getSize()*2+4);
	}
	public void newApple() {
		//zorgt voor random palats van appel
		boolean bool = true;
		while (bool){
			appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
			appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
			
			bool = false;
			for (int i = 0; i<bodyParts; i++) {
				if (x[i] == appleX && y[i] == appleY) {
					bool = true;
					break;
				}
			}
		}
	}
	public void move() {
		//zal elke bodypart van de snake shifte
		for(int i = bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		//zal plaats vinden voor kop in de juiste richting
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		//als snake apple raakt zal de body parts verhogen met 1 en zal de snake langer worden door de draw snake in draw
		if ((x[0] == appleX) && (y[0] == appleY)) {
			
			
			bodyParts++;
			applesEaten++;
			newApple();
		}
		
	}
	public void checkCollisions() {
		//checkt of hoofd colides met body
		for (int i = bodyParts; i>0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
				deaths++;
			}
		}
		//checks als hoofd  border raakt
		if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] > SCREEN_HEIGHT || y[0] < 0) {
			running = false;
			deaths++;
		}
		//als running false is zal de game stoppen 
		/*
		if (!running) {
			timer.stop();
		}
		*/
	}
	public void gameOver(Graphics g) {
		
		g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		g.setFont( new Font("Comic Sans MS", Font.BOLD, 45));
		//om het in het midden te krijgen
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		String gameOver = "lol get gud";
		g.drawString(gameOver, (SCREEN_WIDTH - metrics1.stringWidth(gameOver))/2, SCREEN_HEIGHT/2);
		
		g.setFont( new Font("Comic Sans MS", Font.BOLD, 15));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		gameOver = "subscribe 2 daaninator";
		g.drawString(gameOver, (SCREEN_WIDTH - metrics2.stringWidth(gameOver))/2, SCREEN_HEIGHT/2+45);
		
		g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		gameOver = "press enter to restart";
		g.drawString(gameOver, (SCREEN_WIDTH - metrics2.stringWidth(gameOver))/2, SCREEN_HEIGHT/2+25);	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		//om delay te geven in ms
		else {
			try {
			    Thread.sleep(500);
			} catch (InterruptedException ie) {
			    Thread.currentThread().interrupt();
			}
		}
		repaint();
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			//ALS ENTER DAN GAME RESTART
			case KeyEvent.VK_ENTER:
				if ((!running) || (running)){ 
					running = true;	
					//
					if (applesEaten>highscore) highscore = applesEaten;
					bodyParts = 6;
					applesEaten = 0;
					direction = 'R';
					for (int i = 0; i<GAME_UNITS; i++) {
						x[i] = 0;
						y[i] = 0;
					}
				}
				break;	
			
		}
	}
	}

}
