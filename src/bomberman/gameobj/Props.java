package bomberman.gameobj;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Props extends GameObject {

    private BufferedImage img;
    private BufferedImage bomb;
    private BufferedImage fire;
    private BufferedImage speed;

    private int moveStep;
    private int pictureSize;
    private int toolNumber;
    private boolean state;

    public Props(int x, int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_X, Global.UNIT_X, Global.UNIT_X);
        this.bomb = ImageResourceController.getInstance().tryGeyImage(ImagePath.TREASURE_BOMB);
        this.fire = ImageResourceController.getInstance().tryGeyImage(ImagePath.TREASURE_FIRE);
        this.speed = ImageResourceController.getInstance().tryGeyImage(ImagePath.TREASURE_SPEED);
        setProp();

        this.moveStep = 0;
        this.pictureSize = 16;
        this.state = true;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getPropNumber() {
        return this.toolNumber;
    }

    private void moveUP() {
        this.setY(this.getY() - 3);
    }

    private void setProp() {
        this.toolNumber = Global.random(0, 2);
        
        switch (this.toolNumber) {
            case 0:
                this.img = this.bomb;
                break;
            case 1:
                this.img = this.fire;
                break;
            case 2:
                this.img = this.speed;
                break;         
        }
    }

    @Override
    public void update() {
        if (this.pictureSize > 0) {
            this.pictureSize--;
        }
        if (this.moveStep < 8) {
            this.moveStep++;
            moveUP();
        }
    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {
        g.drawImage(this.img,
                this.getLeft() - camera.getCameraX() + startX + this.pictureSize,
                this.getTop() - camera.getCameraY() + this.pictureSize,
                this.getLeft() - camera.getCameraX() + startX + Global.UNIT_X - this.pictureSize,
                this.getTop() - camera.getCameraY() + Global.UNIT_Y - this.pictureSize,
                0, 0, Global.UNIT_X, Global.UNIT_Y - this.pictureSize, null);
    }

}
