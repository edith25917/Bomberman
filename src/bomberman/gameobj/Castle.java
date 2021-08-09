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
public class Castle extends GameObject {

    private BufferedImage img;
    private BufferedImage door;
    private BufferedImage flag;
    private BufferedImage light;
    private BufferedImage arrow;
    private Delay flagDelay;
    private Delay doorDelay;
    private Delay arrowDelay;
    private int flag_left;
    private int flag_top;
    private int flag_width;
    private int flag_height;
    private int flagIndex;
    private int door_left;
    private int door_top;
    private int door_width;
    private int door_height;
    private int doorIndex;
    private int appearState;
    private boolean isOver = false;
    private boolean isReady = false;//確認是否要讓camera改centerOnObj
    private boolean paintlight = false;

    public Castle(int x, int y) {
        super(x, y, 320, 128, true);
        this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.CASTLE);
        this.door = ImageResourceController.getInstance().tryGeyImage(ImagePath.DOOR);
        this.flag = ImageResourceController.getInstance().tryGeyImage(ImagePath.FLAG);
        this.light = ImageResourceController.getInstance().tryGeyImage(ImagePath.SPOTLIHT2);

        this.flag_left = this.getLeft() + 16;
        this.flag_top = this.getY();
        this.flag_width = 32;
        this.flag_height = 64;
        this.flagIndex = 0;
        this.flagDelay = new Delay(10);
        this.flagDelay.start();
        this.door_left = this.getX() - 26;
        this.door_top = this.getY();
        this.door_width = 52;
        this.door_height = 64;
        this.doorIndex = 0;
        this.doorDelay = new Delay(20);
        this.doorDelay.start();

    }

    @Override
    public void update() {
        if (this.flagDelay.isTrig()) {
            this.flagIndex = (this.flagIndex + 1) % 6;
        }

        if (isOver) {
            this.isReady = true;
            this.paintlight = true;
            this.appearState++;
            if (this.appearState > 60) {
                if (this.doorDelay.isTrig()) {
                    this.doorIndex++;
                }
                if (this.doorIndex == 2) {
                    this.doorDelay.stop();
                }
            }
            if (this.appearState > 150) {
                this.paintlight = false;
                this.isReady = false;
            }
        }
    }

    public void setIsOver() {
        this.isOver = true;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean isReady() {
        return this.isReady;
    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {

        g.drawImage(img, this.getLeft() + startX - camera.getCameraX(), this.getTop() - camera.getCameraY(),
                this.getLeft() + startX + this.getWidth() - camera.getCameraX(), this.getTop() + this.getHeight() - camera.getCameraY(),
                0, 0, this.getWidth(), this.getHeight(), null);
        g.drawImage(this.flag, this.flag_left + startX - camera.getCameraX(), this.flag_top - camera.getCameraY(),
                this.flag_left + startX + this.flag_width - camera.getCameraX(), this.flag_top + this.flag_height - camera.getCameraY(),
                this.flag_width * this.flagIndex, 0, this.flag_width * (1 + this.flagIndex), this.flag_height, null);
        g.drawImage(this.flag, this.flag_left + startX - camera.getCameraX() + Global.UNIT_X, this.flag_top - camera.getCameraY(),
                this.flag_left + startX + this.flag_width - camera.getCameraX() + Global.UNIT_X, this.flag_top + this.flag_height - camera.getCameraY(),
                this.flag_width * this.flagIndex, 0, this.flag_width * (1 + this.flagIndex), this.flag_height, null);
        g.drawImage(this.flag, this.flag_left + startX - camera.getCameraX() + Global.UNIT_X * 3, this.flag_top - camera.getCameraY(),
                this.flag_left + startX + this.flag_width - camera.getCameraX() + Global.UNIT_X * 3, this.flag_top + this.flag_height - camera.getCameraY(),
                this.flag_width * this.flagIndex, 0, this.flag_width * (1 + this.flagIndex), this.flag_height, null);
        g.drawImage(this.flag, this.flag_left + startX - camera.getCameraX() + Global.UNIT_X * 4, this.flag_top - camera.getCameraY(),
                this.flag_left + startX + this.flag_width - camera.getCameraX() + Global.UNIT_X * 4, this.flag_top + this.flag_height - camera.getCameraY(),
                this.flag_width * this.flagIndex, 0, this.flag_width * (1 + this.flagIndex), this.flag_height, null);
        g.drawImage(this.door, this.door_left + startX - camera.getCameraX(), this.door_top - camera.getCameraY(),
                this.door_left + startX - camera.getCameraX() + this.door_width, this.door_top - camera.getCameraY() + this.door_height,
                this.door_width * this.doorIndex, 0, this.door_width * (1 + this.doorIndex), this.door_height, null);
        if (this.paintlight) {
            g.drawImage(light, 88, 105, null);
        }

    }
}
