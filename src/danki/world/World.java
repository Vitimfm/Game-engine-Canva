package danki.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class World {
	
	private Tile[] tiles; //array of tiles 
	public static int WIDTH, HEIGHT;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path)); 
			//map image path (instantiated in the main Game constructor meth)
			int[] pixel = new int[map.getWidth() * map.getHeight()];
			//pixel array to keep the color values of the map image 
			
			WIDTH = map.getHeight();
			HEIGHT = map.getHeight();
			
			tiles = new Tile[WIDTH * HEIGHT]; 
			//tiles array will have the size of the map
			
			map.getRGB(0, 0, WIDTH, HEIGHT, pixel, 0, WIDTH);
			//get every single pixels colors of the map and keep it in the array 'pixel'
			
			for(int xx = 0; xx < WIDTH; xx++) {
				for(int yy = 0; yy < HEIGHT; yy++) {
					//Double Loop that loops through each pixel in the image
					int currentPixel = pixel[xx + (yy * WIDTH)];
					//(yy * WIDTH) -> vertical iteration 
										
					switch(currentPixel) {
					  case 0xFF000000:
						 tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					    break;
					  case 0xFFFFFFFF:
						 tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_WALL);
					    break;
					  case 0xFF0094FF:
						  tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
						  break;
					  default:
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//render every single tile
	public void render(Graphics g) {
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
