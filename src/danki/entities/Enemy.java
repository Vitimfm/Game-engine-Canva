package danki.entities;

//import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import danki.main.Game;
import danki.world.Camera;
//import danki.world.Camera;
import danki.world.World;

public class Enemy extends Entity{
	
	private double speed = 1;
	
	private int mask_X = 2, mask_Y = 2, maskWidth = 14, maskHeight = 14;
	
	private int frame = 0, maxFrames = 20, index = 0, maxIndex = 3;
	private BufferedImage[] sprites;
	
	private int life = 5;
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites = new BufferedImage[4];
		sprites[0] = Game.spritesheet.getSprite(96, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(96+16, 16, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(96+32, 16, 16, 16);
		sprites[3] = Game.spritesheet.getSprite(96+48, 16, 16, 16);
			
	}
	
	public void tick() {
		//mask_X = 1; 
		//mask_Y = 1; 
		//maskWidth = 14; 
		//maskHeight = 14;
		if(isCollindingWithPlayer() == false) {
			
		if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY()) 
				&& !isCollinding((int)(x+speed), this.getY())) {
			x+=speed;
		}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
				&& !isCollinding((int)(x-speed), this.getY())) {
			x-=speed;
		}
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
				&& !isCollinding(this.getX(), (int)(y+speed))) {
			y+=speed;
		}else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
				&& !isCollinding(this.getX(), (int)(y-speed))) {
			y-=speed;
		}
		}else {
			// Collinding with player 
			if(Game.rand.nextInt(100) < 10) {
				Game.player.life --;
				Game.player.isDamaged = true;
			}
		}
		
		frame++;
			if(frame == maxFrames) {
				frame = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
			
			isCollidingWithBullet();
			if(life <= 0) {
				destroySelf();
				return;
			}
			
			if(isDamaged) {
				this.damageCurrent++;
				if(this.damageCurrent == this.damageFrames) {
					this.damageCurrent = 0;
					this.isDamaged = false;
				}
			}
			
		}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void isCollidingWithBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				// Check if Bullet Shooted is collinding Enemy
				if(Entity.isCollinding(this, e)) {
					isDamaged = true;
					life--;
					Game.bullets.remove(i);
					return;
				}
			}
		}
	}
	
	public boolean isCollindingWithPlayer(){
		Rectangle CurrentEnemy = new Rectangle(this.getX() + mask_X, this.getY() + mask_Y, maskWidth, maskHeight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		return CurrentEnemy.intersects(player);
	}
	
	public boolean isCollinding(int xnext, int ynext) {
		Rectangle CurrentEnemy = new Rectangle(xnext + mask_X, ynext + mask_Y, maskWidth, maskHeight);
		
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this) {
				continue;	
			}
			Rectangle targetEnemy = new Rectangle(e.getX() + mask_X, e.getY() + mask_Y, maskWidth, maskHeight);
			if(CurrentEnemy.intersects(targetEnemy)) {
				return true;
			}
		}
			return false;
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}	
		// Enable Collision Mask: 
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + mask_X - Camera.x, this.getY() + mask_Y - Camera.y, maskWidth, maskHeight);
	}

}
