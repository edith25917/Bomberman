/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.background;

import bomberman.controllers.ImageResourceController;
import bomberman.util.Delay;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author user
 */
public class ButtonAnyPress {

    private BufferedImage img;

    private int x;
    private int y;
    private int width;
    private int height;
    private Delay delay;

    public ButtonAnyPress(String path, int x, int y) {
        this.img = ImageResourceController.getInstance().tryGeyImage(path);
        this.x = x;
        this.y = y;
        this.width = 0;
        this.height = 0;
        this.delay = new Delay(30);

    }

    public void setWidthsetHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Delay getDelay() {
        return this.delay;
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
        if (this.delay.isTrig() && this.width == 336 && this.height == 60) {

            this.setWidthsetHeight(0, 0);
        }
        if (this.delay.isTrig() && this.width == 0 && this.height == 0) {
            this.setWidthsetHeight(336, 60);

        }

    }

    public void paintComponent(Graphics g, int x, int y, int cameraX, int cameraY) {
        g.drawImage(img, x - cameraX, y - cameraY, this.width, this.height, null);
    }
}
