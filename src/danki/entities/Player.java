package danki.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import danki.main.Game;

public class Player extends Entity{
	
	public boolean right, up, down, left;
	public double speed = 1;

	public int right_direction = 0, left_direction = 1;
	public int dir = right_direction;
	
	private int frame = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer, leftPlayer;
	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);	
		}
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);	
		}
		
		
	}
	
	public void tick() {
		moved = false;
		if(right) {
			moved = true;
			dir = right_direction;
			x += speed;
			}
		else if (left) {
			moved = true;
			dir = left_direction;
			x-= speed;
		}
		if(up) {
			moved = true;
			y -= speed;
		}
		else if(down) {
			moved = true;
			y += speed;
		}
		
		if(moved) {
			frame++;
			if(frame == maxFrames) {
				frame = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
	}
	public void render(Graphics g) {
		if(dir == right_direction) {
			g.drawImage(rightPlayer[index], this.getX(), this.getY(), null);
		}else if(dir == left_direction) {
			g.drawImage(leftPlayer[index], this.getX(), this.getY(), null);
		}
	}
}
