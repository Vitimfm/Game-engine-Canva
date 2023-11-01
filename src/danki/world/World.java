package danki.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import danki.entities.Enemy;
import danki.entities.Entity;
import danki.main.Game;

public class World {
	
	private Tile[] tiles; //array of tiles 
	public static int MAP_WIDTH, MAP_HEIGHT;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path)); 
			//map image path (instantiated in the main Game constructor meth)
			int[] pixel = new int[map.getWidth() * map.getHeight()];
			//pixel array to keep the color values of the map image 
			
			MAP_WIDTH = map.getHeight();
			MAP_HEIGHT = map.getHeight();
			
			tiles = new Tile[MAP_WIDTH * MAP_HEIGHT]; 
			//tiles array will have the size of the map
			
			map.getRGB(0, 0, MAP_WIDTH, MAP_HEIGHT, pixel, 0, MAP_WIDTH);
			//get every single pixels colors of the map and keep it in the array 'pixel'
			
			for(int xx = 0; xx < MAP_WIDTH; xx++) {
				for(int yy = 0; yy < MAP_HEIGHT; yy++) {
					//Double Loop that loops through each pixel in the image
					int currentPixel = pixel[xx + (yy * MAP_WIDTH)];
					//(yy * WIDTH) -> vertical iteration 
										
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
						  
					  case 0xFF00FF21: //Lifepack 
						    
					  default:
						tiles[xx + (yy * MAP_WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//render every single tile
	public void render(Graphics g) {
		for(int xx = 0; xx < MAP_WIDTH; xx++) {
			for(int yy = 0; yy < MAP_HEIGHT; yy++) {
				Tile tile = tiles[xx + (yy*MAP_WIDTH)];
				tile.render(g);
			}
		}
	}
}
