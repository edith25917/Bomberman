package bomberman.gameobj;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Treasure extends GameObject {

    private BufferedImage img;

    private Delay stateDelay;//寶箱存在多久
    private Delay pictureDelay;//打開寶箱
    private int pictureIndex;
    private Props prop;

    public Treasure(int x, int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, Global.UNIT_X - 2, Global.UNIT_Y - 2);
        this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.TREASURE);
        this.pictureDelay = new Delay(20);
        this.pictureIndex = 0;
        this.stateDelay = new Delay(30);
    }

    public Delay getStateDelay() {
        return this.stateDelay;
    }

    public int getPictureIndex() {
        return this.pictureIndex;
    }

    public Delay getPictureDelay() {
        return this.pictureDelay;
    }

    public Props getProp() {
        return this.prop;
    }

    @Override
    public void update() {

        if (this.pictureDelay.isTrig() && this.pictureIndex == 0) {
            this.stateDelay.start();
            this.pictureIndex = 1;//寶箱打開
            if (Global.random(70)) {
                this.prop = new Props(this.getX(), this.getY() + 16);//創建寶箱裡的道具
            }
            
            this.pictureDelay.stop();

        }
        if (this.pictureIndex == 1 && this.prop != null) {
            this.prop.update();
        }
    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {
        g.drawImage(this.img, this.getLeft() - camera.getCameraX() + startX, this.getTop() - camera.getCameraY(),
                this.getLeft() - camera.getCameraX() + startX + Global.UNIT_X, this.getTop() - camera.getCameraY() + Global.UNIT_Y,
                this.pictureIndex * Global.UNIT_X, 0, Global.UNIT_X * (1 + this.pictureIndex), Global.UNIT_Y, null);

    }

}
