package bomberman.tile;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.gameobj.GameObject;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Barrel extends GameObject {

    private BufferedImage img;
    private boolean tresasureExist;
    private boolean woodExist;
    //消失特效
    private BufferedImage disapperEffects;
    private Delay disapperDelay;
    private int disapperIndex;

    public Barrel(int x, int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, Global.UNIT_X - 2, Global.UNIT_Y - 2);

        this.disapperEffects = ImageResourceController.getInstance().tryGeyImage(ImagePath.DISAPPER);
        this.disapperDelay = new Delay(2);
        this.disapperIndex = 0;
        this.tresasureExist = false;
        setImage();
    }

    public void setImage() {
        switch (Global.CURRENT_LEVEL) {
            case 1:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV1_BARREL);
                break;
            case 2:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV2_BARREL);
                break;
            case 3:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV3_BOX);
                break;
            case 4:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV1_BARREL);
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
