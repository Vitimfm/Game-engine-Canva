package danki.grafics;

import java.awt.Color;
import java.awt.Graphics;

import danki.entities.Player;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8,  4, 50, 8);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int)((Player.life / Player.maxLife) * 50), 8);
	}

}
