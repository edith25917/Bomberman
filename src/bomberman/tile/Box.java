package bomberman.tile;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.gameobj.GameObject;
import bomberman.gameobj.Treasure;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Box extends GameObject {

    private BufferedImage img;
    private boolean tresasureExist;
    private boolean woodExist;
    //消失特效
    private BufferedImage disapperEffects;
    private Delay disapperDelay;
    private int disapperIndex;

    public Box(int x, int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, Global.UNIT_X - 2, Global.UNIT_Y - 2);
        this.disapperEffects = ImageResourceController.getInstance().tryGeyImage(ImagePath.BOXDISAPPER);
        this.disapperDelay = new Delay(2);
        this.disapperIndex = 0;
        this.tresasureExist = false;
        this.woodExist = false;
        setImage();
    }

    public void setImage() {
        switch (Global.CURRENT_LEVEL) {
            case 1:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV1_BOX);
                break;
            case 2:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV2_BOX);
                break;
            case 3:
                if (Global.random(40)) {
                    this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV3_BARREL);
                } else if (Global.random(40)) {
                    this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV3_BARREL2);
                } else if (Global.random(40)) {
                    this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV3_BARREL3);
                } else if (Global.random(40)) {
                    this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV3_BARREL4);
                } else {
                    this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV3_BARREL5);
                }
                break;
            case 4:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV1_BOX);
                break;
        }
    }

    public void setTreasureExist() {
        this.tresasureExist = true;
    }

    public boolean getTreasureExist() {
        return this.tresasureExist;
    }

    public void setWoodExist() {
        this.woodExist = true;
    }

    public boolean getWoodExist() {
        return this.woodExist;
    }

    public Delay getDisapperDelay() {
        return this.disapperDelay;
    }

    public int getDisapperIndex() {
        return this.disapperIndex;
    }

    @Override
    public void update() {
        if (this.disapperDelay.isTrig()) {
            this.disapperIndex++;
            if (this.disapperIndex == 4) {
                this.disapperDelay.stop();
            }
        }
    }

    public boolean move(int dir) {
        switch (dir) {
            case Global.LEFT:
                super.setX(super.getX() - Global.UNIT_X / 4);
                return isBound();
            case Global.RIGHT:
                super.setX(super.getX() + Global.UNIT_X / 4);
                return isBound();
            case Global.DOWN:
                super.setY(super.getY() + Global.UNIT_Y / 4);
                return isBound();

            case Global.UP:
                super.setY(super.getY() - Global.UNIT_Y / 4);
                return isBound();
        }
        return false;
    }

    public boolean moveBack(int dir) {
        switch (dir) {
            case Global.LEFT:
                super.setX(super.getX() + Global.UNIT_X / 4);
                return isBound();
            case Global.RIGHT:
                super.setX(super.getX() - Global.UNIT_X / 4);
                return isBound();
            case Global.DOWN:
                super.setY(super.getY() - Global.UNIT_Y / 4);
                return isBound();
            case Global.UP:
                super.setY(super.getY() + Global.UNIT_Y / 4);
                return isBound();
        }
        return false;
    }

    public boolean isBound() {
        if (super.getX() > Global.totalWorldWidth + Global.forestWidth - Global.UNIT_X / 2) {
            super.setX(Global.totalWorldWidth + Global.forestWidth - Global.UNIT_X / 2);
            return true;
        }
        if (super.getX() < Global.forestWidth + Global.UNIT_X / 2) {
            super.setX(Global.forestWidth + Global.UNIT_X / 2);
            return true;
        }
        if (super.getY() > Global.totalWorldHeight + Global.forestHeight - Global.UNIT_Y / 2) {
            super.setY(Global.totalWorldHeight + Global.forestHeight - Global.UNIT_Y / 2);
            return true;
        }
        if (super.getY() < Global.forestHeight + Global.UNIT_Y / 2) {
            super.setY(Global.forestHeight + Global.UNIT_Y / 2);
            return true;
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {

        if (!this.disapperDelay.getIsPause()) {
            g.drawImage(this.disapperEffects, this.getX() + startX - camera.getCameraX() - 50, this.getY() - camera.getCameraY() - 48, this.getX() + startX - camera.getCameraX() + 50, this.getY() - camera.getCameraY() + 48,
                    this.disapperIndex * 100, 0, 100 * (1 + this.disapperIndex), 96, null);
        } else {
            g.drawImage(this.img, getLeft() + startX - camera.getCameraX(), getTop() - camera.getCameraY(), Global.UNIT_X, Global.UNIT_Y, null);

        }
    }

}
