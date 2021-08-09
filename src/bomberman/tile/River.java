/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.tile;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.gameobj.GameObject;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author user
 */
public class River extends GameObject {

    private BufferedImage img;
    private BufferedImage img2;
    private int id;

    public River(int x, int y, int id) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, true);
        this.id = id;
        this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.WATERTILE + String.valueOf(this.id) + ".png");

    }

 

    @Override
    public void update() {
    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {
        g.drawImage(img, super.getLeft() + startX - camera.getCameraX(), super.getTop() - camera.getCameraY(), super.getWidth(), super.getHeight(), null);
    }

}
