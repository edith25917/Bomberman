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
public class Task extends GameObject {

    private BufferedImage taskImg;
    private Delay pictureDelay;
    private Delay stateDelay;
    private Delay lv3_taskDelay;
    private int lv3_picIndex;
    private int width;
    private int height;
    private int imgWidth;
    private int imgHeight;
    private boolean state;
    private String imgPath;

    public Task(int x, int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, true);
        initTask();
        this.pictureDelay = new Delay(20);
        this.stateDelay = new Delay(60);
        this.width = Global.UNIT_X;
        this.height = Global.UNIT_Y;
        this.state = true;
    }

    public Task(String path, int x, int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, true);
        this.taskImg = ImageResourceController.getInstance().tryGeyImage(path);
        this.imgPath = path;
        initTask();
        this.pictureDelay = new Delay(20);
        this.stateDelay = new Delay(60);
        this.width = Global.UNIT_X;
        this.height = Global.UNIT_Y;
        this.state = true;
        this.lv3_taskDelay = new Delay(10);
        this.lv3_taskDelay.start();
        this.lv3_picIndex = 0;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    private void initTask() {
        switch (Global.CURRENT_LEVEL) {
            case 1:
                this.taskImg = ImageResourceController.getInstance().tryGeyImage(ImagePath.WOODS);
                this.imgWidth = 46;
                this.imgHeight = 46;
                break;
            case 2:
                this.taskImg = ImageResourceController.getInstance().tryGeyImage(ImagePath.KEY);
                this.imgWidth = 46;
                this.imgHeight = 46;
                break;
            case 3:
                this.imgWidth = 64;
                this.imgHeight = 64;
                break;
        }
    }

    @Override
    public void update() {
        if (Global.CURRENT_LEVEL == 3 && this.lv3_taskDelay.isTrig()) {
            this.lv3_picIndex = (this.lv3_picIndex + 1) % 10;
        }
        if (this.pictureDelay.isTrig() && this.width == Global.UNIT_X) {
            this.width = 0;
            this.height = 0;
        }
        if (this.pictureDelay.isTrig() && this.width == 0) {
            this.width = Global.UNIT_X;
            this.height = Global.UNIT_Y;
        }

    }

    public BufferedImage getTaskImg() {
        return this.taskImg;
    }

    private void moveUP() {
        this.setY(this.getY() - 3);
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }

    public Delay getPictureDelay() {
        return this.pictureDelay;
    }

    public Delay getStateDelay() {
        return this.stateDelay;
    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {
        if (Global.CURRENT_LEVEL == 3) {
            g.drawImage(this.taskImg, this.getLeft() - camera.getCameraX() + startX, this.getTop() - camera.getCameraY(),
                    this.getLeft() - camera.getCameraX() + startX + this.width, this.getTop() - camera.getCameraY() + this.height,
                    this.imgWidth * this.lv3_picIndex, 0, this.imgWidth * (1 + this.lv3_picIndex), this.imgHeight, null);
        } else {
            g.drawImage(this.taskImg, this.getLeft() - camera.getCameraX() + startX, this.getTop() - camera.getCameraY(),
                    this.getLeft() - camera.getCameraX() + startX + this.width, this.getTop() - camera.getCameraY() + this.height,
                    0, 0, this.imgWidth, this.imgHeight, null);
        }

    }

}
