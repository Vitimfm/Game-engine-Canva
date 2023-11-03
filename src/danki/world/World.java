package danki.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import danki.entities.*;
import danki.main.Game;

public class World {
	
	public static Tile[] tiles; //array of tiles 
	public static int WIDTH;
	public static int HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path)); 
			//map image path (instantiated in the main Game constructor meth)
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			//pixel array to keep the color values of the map image 
			
			WIDTH = map.getHeight();
			HEIGHT = map.getHeight();
			
			tiles = new Tile[WIDTH * HEIGHT]; 
			//tiles array will have the size of the map
			
			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			//get every single pixels colors of the map and keep it in the array 'pixel'
			
			for(int xx = 0; xx < WIDTH; xx++) {
				for(int yy = 0; yy < HEIGHT; yy++) {
					//Double Loop that loops through each pixel in the image
					int currentPixel = pixels[xx + (yy * WIDTH)];
					//(yy * WIDTH) -> vertical iteration 
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*TILE_SIZE, yy*TILE_SIZE, Tile.TILE_FLOOR);
					
					if(currentPixel == 0xFFFFFFFF) { // Wall
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*TILE_SIZE, yy*TILE_SIZE, Tile.TILE_WALL);	
					}else if(currentPixel == 0xFF0094FF) { //Player
						Game.player.setX(xx*TILE_SIZE);
						Game.player.setY(yy*TILE_SIZE);
						//floor under the player
					}else if(currentPixel == 0xFFFF0000) { //Enemy
						Game.entities.add(new Enemy(xx*TILE_SIZE, yy*TILE_SIZE, 16, 16, Entity.ENEMY_EN));
						
					}else if(currentPixel == 0xFF00FFFF) { //Weapon
						Game.entities.add(new Weapon(xx*TILE_SIZE, yy*TILE_SIZE, 16, 16, Entity.WEAPON_EN)); 
						
					}else if(currentPixel == 0xFF00FF21) { //Lifepack
						Game.entities.add(new Lifepack(xx*TILE_SIZE, yy*TILE_SIZE, 16, 16, Entity.LIFEPACK_EN));
						
					}else if(currentPixel == 0xFFFFD800) { //Bullets
						Game.entities.add(new Bullets(xx*TILE_SIZE, yy*TILE_SIZE, 16, 16, Entity.BULLETS_EN));
					}
					
					/*	? 				
					switch(currentPixel) {
					  case 0xFF000000:
						 tiles[xx + (yy * MAP_WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					     break;
					  case 0xFFFFFFFF:
						 tiles[xx + (yy * MAP_WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_WALL);
					     break;
					  case 0xFF0094FF: //Player
						  
						  Game.player.setX(xx*16);
						  Game.player.setY(yy*16);
						  tiles[xx + (yy * MAP_WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
						  //floor under the player
						  break;
					  case 0xFFFF0000: //Enemy
						  Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN)); 
						  
					  case 0xFF00FFFF: //Weapon
						  Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN)); 
						  
					  case 0xFF00FF21: //Lifepack 
						  Game.entities.add(new Lifepack(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN));
						  
					  case 0xFFFFD800: //Bullets
						  Game.entities.add(new Bullets(xx*16, yy*16, 16, 16, Entity.BULLETS_EN));
						  
					  default:
						tiles[xx + (yy * MAP_WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
					*/
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Collision with tiles walls 
	public static boolean isFree(int xnext, int ynext) {
		//Determine the tile coordinates for the player's next position
		//Player's x or y + speed as a parameter of this function 
		int EDGE = 1;
		
		int x1 = xnext / TILE_SIZE;  //x-coordinate of the top-left corner of the player's hitbox
		int y1 = ynext / TILE_SIZE;  //y-coordinate of the top-left corner of the player's hitbox
		
		int x2 = (xnext + TILE_SIZE - EDGE) / TILE_SIZE; //x-coordinate of the top-right corner of the player's hitbox
		int y2 = ynext / TILE_SIZE; //y-coordinate of the top-right corner of the player's hitbox
		
		int x3 = xnext / TILE_SIZE; //x-coordinate of the bottom-left corner of the player's hitbox
		int y3 = (ynext + TILE_SIZE  - EDGE) / TILE_SIZE; //y-coordinate of the bottom-left corner of the player's hitbox
		
		int x4 = (xnext + TILE_SIZE - EDGE) / TILE_SIZE; //x-coordinate of the bottom-right corner of the player's hitbox
		int y4 = (ynext + TILE_SIZE - EDGE) / TILE_SIZE;  //y-coordinate of the bottom-right corner of the player's hitbox
		
		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)||
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
	}
	
	//render every single tile
	public void render(Graphics g) {
		//render titles in the camera ranger
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.WIDTH >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				//Case Camera with negative values
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
