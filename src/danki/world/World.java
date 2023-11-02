package danki.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import danki.entities.*;
import danki.main.Game;

public class World {
	
	private Tile[] tiles; //array of tiles 
	public static int WIDTH;
	public static int HEIGHT;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path)); 
			//map image path (instantiated in the main Game constructor meth)
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			//pixel array to keep the color values of the map image 
			
			WIDTH = map.getHeight();
			HEIGHT = map.getHeight();
			
			tiles = new Tile[map.getWidth() * map.getHeight()]; 
			//tiles array will have the size of the map
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			//get every single pixels colors of the map and keep it in the array 'pixel'
			
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					//Double Loop that loops through each pixel in the image
					int currentPixel = pixels[xx + (yy * map.getWidth())];
					//(yy * WIDTH) -> vertical iteration 
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					if(currentPixel == 0xFF000000) { //Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}else if(currentPixel == 0xFFFFFFFF) { // Wall
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_WALL);	
					}else if(currentPixel == 0xFF0094FF) { //Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
						//floor under the player
					}else if(currentPixel == 0xFFFF0000) { //Enemy
						Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN));
						
					}else if(currentPixel == 0xFF00FFFF) { //Weapon
						Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN)); 
						
					}else if(currentPixel == 0xFF00FF21) { //Lifepack
						Game.entities.add(new Lifepack(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN));
						
					}else if(currentPixel == 0xFFFFD800) { //Bullets
						Game.entities.add(new Bullets(xx*16, yy*16, 16, 16, Entity.BULLETS_EN));
					}
					
					/*					
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
	
	//render every single tile
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.WIDTH >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
