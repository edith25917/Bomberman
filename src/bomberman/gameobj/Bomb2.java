package bomberman.gameobj;

import bomberman.camera.Camera;
import bomberman.controllers.AudioResourceController;
import bomberman.controllers.ImageResourceController;
import bomberman.tile.Stone;
import bomberman.tile.World;
import bomberman.util.AudioPath;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bomb2 extends GameObject {

    private BufferedImage bomb;
    private int[][] tileID;
    //炸彈動畫
    private Delay pictureDelay;
    private int pictureIndex;

    private int fireTotal;//火力大小

    private ArrayList<Boom2> boomArr;

    private int width;
    private int height;

    private boolean bombStayWithCatMan;//炸彈跟人在同一個tile;
    private boolean bombStayWithMonster;

    public Bomb2(int x, int y, int[][] tileID) {
        super(x, y, Global.UNIT_X, 76, Global.UNIT_X - 16, Global.UNIT_Y - 16);
        this.bomb = ImageResourceController.getInstance().tryGeyImage(ImagePath.BOMB);
        this.pictureDelay = new Delay(8);//原本設定是7
        this.pictureDelay.start();
        this.tileID = tileID;
        this.pictureIndex = 0;

        this.boomArr = new ArrayList<>();

        this.width = Global.UNIT_X;
        this.height = 76;

        this.bombStayWithCatMan = true;
        
    }

    private int getTile(int x, int y) {
        return this.tileID[(x - 88) / 64][(y - 105) / 64];

    }

  

    public void setBombStayWithCatMan(boolean state) {
        this.bombStayWithCatMan = state;
    }

    public boolean getBombStayWithCatMan() {
        return this.bombStayWithCatMan;
    }

    public void setBoom2() {
        this.boomArr.add(new Boom2(this.getX(), this.getY()));
        for (int i = 1; i <= this.fireTotal; i++) {

            if (this.getY() - 64 * i > Global.forestHeight && this.getTile(this.getX(), (this.getY() - 64 * i)) < 2) {//上
                this.boomArr.add(new Boom2(this.getX(), this.getY() - 64 * i));
                if (this.getTile(this.getX(), (this.getY() - 64 * i)) == 1) {
                    break;
                }
            } else {
                break;
            }
        }
        for (int j = 1; j <= this.fireTotal; j++) {
            if (this.getY() + 64 * j < Global.totalWorldHeight + Global.forestHeight && this.getTile(this.getX(), this.getY() + 64 * j) < 2) {//下

                this.boomArr.add(new Boom2(this.getX(), this.getY() + 64 * j));
                if (this.getTile(this.getX(), this.getY() + 64 * j) == 1) {
                    break;
                }
            } else {
                break;
            }
        }
        for (int k = 1; k <= this.fireTotal; k++) {
            if (this.getX() - 64 * k > Global.forestWidth && this.getTile(this.getX() - 64 * k, this.getY()) < 2) {//左

                this.boomArr.add(new Boom2(this.getX() - 64 * k, this.getY()));
                if (this.getTile(this.getX() - 64 * k, this.getY()) == 1) {
                    break;
                }
            } else {
                break;
            }
        }
        for (int l = 1; l <= this.fireTotal; l++) {
            if (this.getX() + 64 * l < Global.totalWorldWidth + Global.forestWidth && this.getTile(this.getX() + 64 * l, this.getY()) < 2) {//右

                this.boomArr.add(new Boom2(this.getX() + 64 * l, this.getY()));
                if (this.getTile(this.getX() + 64 * l, this.getY()) == 1) {

                    break;
                }
            } else {
                break;
            }

        }

    }

    private boolean boomState = false;

    public boolean boomState() {
//        return this.boomArr.get(0).getPictureIndex() == 3;
        return boomState;
    }

    public void setWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setPICTURE_DRLAY(int delay) {
        this.pictureDelay = new Delay(delay);
        this.pictureDelay.start();
    }

    public ArrayList<Boom2> getBoom() {
        return this.boomArr;
    }

    public Boom2 getBoom(int index) {
        return this.boomArr.get(index);
    }

    public void setFireTotal(int fireTotal) {
        this.fireTotal = fireTotal;
        this.setBoom2();
    }

    public void resetPictureIndex() {
        this.pictureIndex = 0;
    }

    public int getPictureIndex() {
        return this.pictureIndex;
    }

    public Delay getPictureDelay() {
        return this.pictureDelay;
    }

    public void setPictureIndex(int index) {
        this.pictureIndex = index;
    }

    public void playAudio() {
   
    }

    @Override
    public void update() {
        playAudio();
        if (this.pictureDelay.isTrig()) {
            this.pictureIndex = (this.pictureIndex + 1) % 10;
        }
        if (this.pictureIndex == 9) {

            boomState = true;
            for (int i = 0; i < this.boomArr.size(); i++) {

                this.boomArr.get(i).update();
            }
            this.pictureDelay.stop();

        }

    }

    public void paint(Graphics g) {
        g.drawImage(bomb, this.getLeft(), this.getTop(), this.getLeft() + Global.UNIT_X, this.getTop() + 76,
                0 + Global.UNIT_X * this.pictureIndex, 0, Global.UNIT_X * (1 + this.pictureIndex), 76, null);
    }

    public void paintComponent(Graphics g, Camera camera, int startX) {
        if (this.pictureIndex == 9) {

        } else {
            g.drawImage(bomb, this.getLeft() + startX - camera.getCameraX(), this.getTop() - camera.getCameraY(),
                    this.getLeft() + this.width - camera.getCameraX() + startX, this.getTop() + this.height - camera.getCameraY(),
                    0 + Global.UNIT_X * this.pictureIndex, 0, Global.UNIT_X * (1 + this.pictureIndex), 76, null);

        }

    }
}
