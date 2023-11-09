package danki.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
//import java.util.ArrayList;

//import danki.grafics.Spritesheet;
import danki.main.Game;
import danki.world.Camera;
import danki.world.World;

public class Player extends Entity{
	
	public boolean right, up, down, left;
	public double speed = 2;

	public int right_direction = 0, left_direction = 1;
	public int dir = right_direction;
	
	private int frame = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer, leftPlayer;
	
	private BufferedImage playerDamage;
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public int ammo = 0;
	
	private boolean hasGun = false;
	
	public double life = 100, maxLife = 100;
	
	public boolean shoot = false, mouseShoot = false;
		
	public int mouse_X, mouse_Y;
	
	//Fake Jump Technique
	public boolean jump = false, isJumping = false;
	public int z = 0;
	public int jumpHEIGHT = 50, jumpCur = 0;
	public int jumpSpeed = 2;
	public boolean jumpUP = false, jumpDOWN = false;
		
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);	
		}
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);	
		}
		
		
	}
	
	public void tick() {
		
		if(jump) {
			if(isJumping == false) {
				jump = false;
				isJumping = true;
				jumpUP = true;
			}
		}
		if(isJumping == true) {
			if(jumpUP){
				jumpCur += jumpSpeed;
			}else if(jumpDOWN) {
				jumpCur -= jumpSpeed;
				if(jumpCur <= 0){
					isJumping = false;
					jumpDOWN = false;
					jumpUP = false;
				}
			}
			z = jumpCur;
			if(jumpCur >= jumpHEIGHT) {
				jumpUP = false;
				jumpDOWN = true;
			}
		}
		
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_direction;
			x += speed;
		}
		else if (left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_direction;
			x-= speed;
		}
		if(up && World.isFree(this.getX(), (int)(y-speed))) {
			moved = true;
			y -= speed;
		}
		else if(down && World.isFree(this.getX(), (int)(y+speed))) {
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
		
		LifePackCollision();
		AmmoCollision();
		GunCollision();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			if(hasGun && ammo > 0 ) {
				ammo--;
				// Create bullet and shoot
				
				int dx = 0; //just shooting to the sides so just dx
				int fixPositionX = 0;
				int fixPositionY = 8;
				if(dir == right_direction) {
					fixPositionX = 15;
					dx = 1;
				}else {
					fixPositionX = -3;
					dx = -1;
				}
			
				BulletShoot bullet = new BulletShoot(this.getX() + fixPositionX, this.getY() + fixPositionY, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
			//Mouse shoot event 
			mouseShoot = false;
			if(hasGun && ammo > 0 ) {
				ammo--;
				
				double angle = 0;
				int offsetX = 0;
				int offsetY = 8;
				
				if(dir == right_direction) {
					offsetX = 15;
					angle = Math.atan2(mouse_Y - (this.getY() + offsetY - Camera.y), mouse_X - (this.getX() + offsetX - Camera.x));
				}else {
					offsetX = -3;
					angle = Math.atan2(mouse_Y - (this.getY() + offsetY - Camera.y), mouse_X - (this.getX() + offsetX - Camera.x));
				}
				
				
				double dx = Math.cos(angle); 
				double dy = Math.sin(angle);
		
				BulletShoot bullet = new BulletShoot(this.getX() + offsetX, this.getY() + offsetY, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}
		
		if(life <= 0) { //Game Over
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		Camera.x = this.getX() - (Game.WIDTH / 2);
		Camera.y = this.getY() - (Game.HEIGHT / 2);
		//centralize camera in player
		//camera start point (0,0)
	}
	
	public void AmmoCollision() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity CurrentEntity = Game.entities.get(i);
			if(CurrentEntity instanceof Bullets) {
				if(Entity.isCollinding(CurrentEntity, this)) {
					ammo += 20;
					if(ammo > 60) {
						ammo = 60;
					}
					Game.entities.remove(CurrentEntity);
				}
			}
		}
	}
	
	public void LifePackCollision() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity CurrentEntity = Game.entities.get(i);
			if(CurrentEntity instanceof Lifepack) {
				if(Entity.isCollinding(CurrentEntity, this)) {
					life += 30;
					if(life > 100) {
						life = 100;
					}
					Game.entities.remove(CurrentEntity);
				}
			}
		}
	}
	
	public void GunCollision() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity CurrentEntity = Game.entities.get(i);
			if(CurrentEntity instanceof Weapon) {
				if(Entity.isCollinding(CurrentEntity, this)) {
					hasGun = true; //Player gets the gun
					Game.entities.remove(CurrentEntity);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
			if(dir == right_direction) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) { //Gun right side
					g.drawImage(Entity.GUN_RIGHT, this.getX() +6 - Camera.x, this.getY() + 1 - Camera.y - z, null);
				}
			}else if(dir == left_direction) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) { //Gun left side
					g.drawImage(Entity.GUN_LEFT, this.getX() - 6 - Camera.x, this.getY() + 1 - Camera.y - z, null);
				}
			}
		}else {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
		}
		if(isJumping) {
			g.setColor(Color.black);
			g.fillOval(this.getX() - Camera.x + 5, this.getY() - Camera.y +8 , 8, 8);
		}
	}
}
