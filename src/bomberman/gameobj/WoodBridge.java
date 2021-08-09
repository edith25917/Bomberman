/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.gameobj;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author user
 */
public class WoodBridge extends GameObject {

    private BufferedImage img;
    private BufferedImage light;
    private BufferedImage arrow;
    private int arrowWidth = 942;
    private int arrowHeight = 350;
    private boolean paintlight = false;
    private boolean paintarrow = false;
    private int x;
    private int y;
    private int left;
    private int top;
    private int width;
    private int height;
    private Delay pictureDelay;
    private Delay arrowDelay;
    private int pictureIndex;
    private int appearState;
    private boolean isOver = false;

    public WoodBridge(int x, int y) {
        super(x, y, 210, 100, true);
        this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.WOODBRIDGE);
        this.light = ImageResourceController.getInstance().tryGeyImage(ImagePath.SPOTLIHT);
        this.arrow = ImageResourceController.getInstance().tryGeyImage(ImagePath.ARROW);
        this.x = x;
        this.y = y;
        this.width = 210;
        this.height = 100;
        this.left = x - this.width / 2;
        this.top = y - this.height / 2;
        this.pictureDelay = new Delay(10);
        this.pictureIndex = 0;
        this.pictureDelay.start();

        if (Global.CURRENT_LEVEL == 2||Global.IS_MUlTIPLAYERS) {
            this.pictureIndex = 3;
        }
  

        this.arrowDelay = new Delay(50);
        this.arrowDelay.start();

    }

    public void update() {
        if (isOver) {
            this.paintlight = true;
            this.appearState++;
            if (this.appearState > 40) {//over後bridge出現的時機
                if (this.pictureIndex == 3) {
                    this.paintlight = false;
                }
                if (this.pictureDelay.isTrig()) {
                    if (this.pictureIndex == 3) {
                        this.paintarrow = true;
                        this.pictureIndex = 3;
                        return;
                    }
                    this.pictureIndex = (this.pictureIndex + 1) % 4;
                }
            }
        }
        if (this.paintarrow && this.arrowDelay.isTrig() && this.arrowWidth == 942 && this.arrowHeight == 350) {
            this.setArrow(1032, 400);
        }
        if (this.paintarrow && this.arrowDelay.isTrig() && this.arrowWidth == 1032 && this.arrowHeight == 400) {
            this.setArrow(942, 350);
        }
    }

    public void setIsOver() {
        this.isOver = true;
    }

    public Delay getPictureDelay() {
        return this.pictureDelay;
    }

    private void setArrow(int width, int height) {
        this.arrowWidth = width;
        this.arrowHeight = height;
    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {

        g.drawImage(img, this.left + startX - camera.getCameraX(), this.top - camera.getCameraY(),
                this.left + startX + this.width - camera.getCameraX(), this.top + this.height - camera.getCameraY(),
                0, this.height * this.pictureIndex, this.width, this.height * (1 + this.pictureIndex), null);
        if (this.paintlight) {
            g.drawImage(light, 88, 105, null);
        }
        g.drawImage(arrow, 942, 350, this.arrowWidth, this.arrowHeight, 0, 0, 160, 100, null);

    }
}
