package bomberman.gameobj;

import bomberman.camera.Camera;
import bomberman.controllers.AudioResourceController;
import bomberman.controllers.ImageResourceController;
import bomberman.util.AudioPath;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Boom2 extends GameObject {

    private BufferedImage img;
    private boolean state;
    private int pictureIndex;
    private Delay pictureDelay;
    private AudioClip boomClip;

    public Boom2(int x, int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, Global.UNIT_X - 2, Global.UNIT_Y - 2);
        this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.BOOM);
        this.pictureDelay = new Delay(3);
        this.pictureDelay.start();
        this.pictureIndex = 0;
        this.state = true;
        this.boomClip=Applet.newAudioClip(getClass().getResource(AudioPath.BOOM));

    }

    @Override
    public void update() {
        if (this.pictureDelay.isTrig()) {
            this.pictureIndex = (this.pictureIndex + 1) % 5;

        }
        if (this.pictureIndex == 1) {
//            AudioResourceController.getInstance().play(AudioPath.BOOM);//播放音效
            this.boomClip.play();
        }
        if (this.pictureIndex == 4) {
            this.boomClip.stop();
            this.pictureDelay.stop();
        }

    }

    public int getPictureIndex() {
        return this.pictureIndex;
    }

    @Override

    public void paintComponent(Graphics g, Camera camera, int startX) {

        if (pictureIndex == 4) {

        } else {
            g.drawImage(img, this.getLeft() + startX - camera.getCameraX(), this.getTop() - camera.getCameraY(),
                    this.getLeft() + startX + Global.UNIT_X - camera.getCameraX(), this.getTop() + Global.UNIT_Y - camera.getCameraY(),
                    0 + Global.UNIT_X * this.pictureIndex, 0, Global.UNIT_X * (1 + this.pictureIndex), Global.UNIT_Y, null);
        }

    }

}
