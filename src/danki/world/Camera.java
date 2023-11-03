package danki.world;

//import danki.main.Game;

public class Camera {
	public static int x = 0;
	public static int y = 0;	
	
	/*
	public static void updateCamera() {
        int cameraX = Camera.x;
        int cameraY = Camera.y;
        int playerX = Game.player.getX();
        int playerY = Game.player.getY();

        // Calculate camera limits based on the game world's dimensions
        int minCameraX = 0;
        int minCameraY = 0;
        int maxCameraX = World.WIDTH * World.TILE_SIZE - Game.WIDTH;
        int maxCameraY = World.HEIGHT * World.TILE_SIZE - Game.HEIGHT;

        // Calculate the camera's new position
        cameraX = playerX - (Game.WIDTH / 2);
        cameraY = playerY - (Game.HEIGHT / 2);

        // Ensure the camera stays within its limits
        if (cameraX < minCameraX) {
            cameraX = minCameraX;
        } else if (cameraX > maxCameraX) {
            cameraX = maxCameraX;
        }

        if (cameraY < minCameraY) {
            cameraY = minCameraY;
        } else if (cameraY > maxCameraY) {
            cameraY = maxCameraY;
        }

        Camera.x = cameraX;
        Camera.y = cameraY;
    }
    */
}

