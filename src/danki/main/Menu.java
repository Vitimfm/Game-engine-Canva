 package danki.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options = {"New Game", "Load Game", "Exit"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
	public boolean pause = false;
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "New Game" || options[currentOption] == "Continue") {
				Game.gameState = "NORMAL";
				pause = false;
			}else if(options[currentOption] == "Exit") {
				System.exit(1);
			}
		}
	}
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.red);
		g.setFont(new Font("arial", Font.BOLD, 40));
		g.drawString("SEFIAM KILLER", (Game.WIDTH * Game.SCALE) / 2 - 150, (Game.HEIGHT * Game.SCALE) / 2 - 140);
		
		// Menu Options 
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 24));
		
		if(pause == false) {
			g.drawString("New Game", (Game.WIDTH * Game.SCALE) / 2 - 70, (Game.HEIGHT * Game.SCALE) / 2 - 40);
		}else {
			g.drawString("Resume", (Game.WIDTH * Game.SCALE) / 2 - 60, (Game.HEIGHT * Game.SCALE) / 2 - 40);
		}
		g.drawString("Load Game", (Game.WIDTH * Game.SCALE) / 2 - 75, (Game.HEIGHT * Game.SCALE) / 2);
		g.drawString("Exit", (Game.WIDTH * Game.SCALE) / 2 - 30, (Game.HEIGHT * Game.SCALE) / 2 + 40);
		
		if(options[currentOption] == "New Game") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 90, (Game.HEIGHT * Game.SCALE) / 2 - 40);
		}else if(options[currentOption] == "Load Game") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 95, (Game.HEIGHT * Game.SCALE) / 2);
		}else {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT * Game.SCALE) / 2 + 40);
		}
	}
}
