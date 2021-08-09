package bomberman.background;

import bomberman.controllers.ImageResourceController;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author user
 */
public class BackGround {

    private BufferedImage img;
    private int x;
    private int y;

    public BackGround(String path, int x, int y) {
        this.img = ImageResourceController.getInstance().tryGeyImage(path);
        this.x = x;
        this.y = y;

    }

    public void setXsetY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void update() {
    }

    public void paintComponent(Graphics g, int x, int y, int width, int height, int cameraX, int cameraY) {
        g.drawImage(img, x - cameraX, y - cameraY, width, height, null);
    }
}
