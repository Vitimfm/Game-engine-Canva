package danki.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import danki.entities.Entity;
import danki.entities.Player;
import danki.grafics.Spritesheet;
import danki.world.World;

public class Game extends Canvas implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private final int WIDTH = 240, HEIGHT = 160, SCALE = 3; 
	//constants frame dimensions 
	//SCALE -> pixel effect while rendering
	private Thread thread;
	private boolean isRunning = true;
	
	private BufferedImage image;
	
	public List<Entity> entities;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	private Player player;
	
	//Constructor
	public Game() {
		addKeyListener(this); //for Key Events
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		//Create objects (Right Sequence)
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		world = new World("/map.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
	}
	
	public void initFrame() {
		frame = new JFrame("New One");
		frame.add(this);
		frame.setResizable(false); //not resizable
		frame.pack(); //pack after set size
		frame.setLocationRelativeTo(null); //center window 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() { 
		thread = new Thread(this); //(this) to execute the method 'run()' in this 'Game' class
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		for(int i = 0 ; i < entities.size(); i++) {
			Entity e = entities.get(i);
			//if(e instanceof Player) {
				
			//}
			e.tick();
		}
	}
	
	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);		
		
		//Render Objects
		
		world.render(g);
		
		for(int i = 0 ; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		g.dispose(); //clean data in image (performace)
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
	}
	
	//GAME LOOPING ADVANCED
	public void run() {
	    long lastTime = System.nanoTime(); 
	    //Get the current system time in nanoseconds (precision).
	    double amountOfTicks = 60.0; 
	    //Set the desired game update rate to 60 FPS.
	    double ns = 1000000000 / amountOfTicks; 
	    //Convert 1 second to nanoseconds and divide by the update rate to get the number of nanoseconds between each update.
	    double delta = 0;
	    int frames = 0; //Frame counter.
	    double timer = System.currentTimeMillis(); 
	    //Get the current system time in milliseconds (lower precision).

	    while (isRunning) {
	        long now = System.nanoTime(); 
	        //Get the current time in nanoseconds on each iteration of the loop.
	        delta += (now - lastTime) / ns; 
	        //Calculate how much time has passed since the last update relative to the expected time for the next update.
	        lastTime = now;

	        if (delta >= 1) {
	            tick(); //Call the game's update method.
	            render(); //Call the game's render method.
	            frames++; //Increment the frame counter.

	            delta--; //Decrease delta to prepare for the next update.
	        }

	        if (System.currentTimeMillis() - timer >= 1000) {
	            System.out.println("FPS: " + frames); 
	            //Print the number of frames processed in 1 second.
	            frames = 0; //Reset the frame counter.
	            timer += 1000; //Increment the timer by 1 second.
	        }
	    }
	    stop(); //out of the game looping
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		//diferent 'if's in case douple key pressed
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
	}
}