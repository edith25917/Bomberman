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
public class Tree extends GameObject {

    private boolean isSolid;
    private BufferedImage img;

    public Tree(int x, int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, Global.UNIT_X - 2, Global.UNIT_Y - 2);

        setImage();

        isSolid = true;
    }

    public void setImage() {
        switch (Global.CURRENT_LEVEL) {
            case 1:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV1_TREE);
                break;
            case 2:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV2_TREE);
                break;
            case 3:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV3_TREE);
                break;
            case 4:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV1_TREE);
                break;
        }
    }

    public boolean isSolid() {
        return this.isSolid;
    }

    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {
        g.drawImage(img, super.getLeft() + startX - camera.getCameraX(), super.getTop() - camera.getCameraY(), super.getWidth(), super.getHeight(), null);
    }

}
