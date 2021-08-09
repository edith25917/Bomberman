/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.background;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.gameobj.Bomb2;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author user
 */
public class ButtonStart {

    private BufferedImage img;
    private int button_1_x;
    private int button_1_y;
    private int button_2_x;
    private int button_2_y;
    private int button_3_x;
    private int button_3_y;

    private int width;
    private int height;
    private int buttonOne;
    private int buttonTwo;
    private int buttonThree;
    private Delay delay;
    private boolean pictureState;//按鍵動畫的判斷
    private int buttonState = 1;//判斷按鍵動畫
    private Bomb2 buttonbomb;

    public ButtonStart() {
        this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.BUTTONSTART);
        this.button_1_x = Global.SCREEN_X / 2 - 175;
        this.button_1_y = 380;
        this.button_2_x = Global.SCREEN_X / 2 - 175;
        this.button_2_y = 440;
        this.button_3_x = Global.SCREEN_X / 2 - 175;
        this.button_3_y = 500;
        this.width = 350;
        this.height = 60;
        this.buttonOne = 3;
        this.buttonTwo = 1;
        this.buttonThree = 2;
        this.delay = new Delay(10);
        this.pictureState = false;
        this.buttonbomb = new Bomb2(Global.SCREEN_X / 2 - 140, 400, null);
//        this.buttonbomb.setPICTURE_DRLAY(5);

    }

    public int buttonState() {
        return this.buttonState;
    }

    public void setbuttonState(int value) {
        this.buttonState = value;
    }

    public void update() {
 
        if (this.buttonState == 0 && this.buttonOne == 3) {//往下
            this.buttonbomb.setY(this.buttonbomb.getY() + 60);
            this.buttonOne = 0;
            this.buttonTwo = 4;//選到二
            this.buttonThree = 2;
            this.buttonState = -1;//才不會馬上進下一個判斷
        } else if (this.buttonState == 0 && this.buttonTwo == 4) {
            this.buttonbomb.setY(this.buttonbomb.getY() + 60);
            this.buttonOne = 0;
            this.buttonTwo = 1;
            this.buttonThree = 5;//選到3
            this.buttonState = -1;
        } else if (this.buttonState == 1 && this.buttonThree == 5) {//往上
            this.buttonbomb.setY(this.buttonbomb.getY() - 60);
            this.buttonOne = 0;
            this.buttonTwo = 4;//選到2
            this.buttonThree = 2;
            this.buttonState = -1;
        } else if (this.buttonState == 1 && this.buttonTwo == 4) {
            this.buttonbomb.setY(this.buttonbomb.getY() - 60);
            this.buttonOne = 3;//選到1
            this.buttonTwo = 1;
            this.buttonThree = 2;
            this.buttonState = -1;
        }

        if (pictureState) {
            if (this.buttonOne == 3 && this.delay.isTrig()) {
                this.buttonOne = 6;
            }
            if (this.buttonOne == 6 && this.delay.isTrig()) {
                this.buttonOne = 3;
            }

            if (this.buttonTwo == 4 && this.delay.isTrig()) {
                this.buttonTwo = 6;
            }
            if (this.buttonTwo == 6 && this.delay.isTrig()) {
                this.buttonTwo = 4;
            }
            if (this.buttonTwo == 6 && this.delay.getIsPause()) {
                this.buttonTwo = 4;
            }
            if (this.buttonThree == 5 && this.delay.isTrig()) {
                this.buttonThree = 6;
            }
            if (this.buttonThree == 6 && this.delay.isTrig()) {
                this.buttonThree = 5;
            }
        }
        this.buttonbomb.update();
        if (this.buttonbomb.getPictureIndex() == 9) {
            this.buttonbomb.resetPictureIndex();
            this.buttonbomb.getPictureDelay().start();
        }

    }

    public void paintComponent(Graphics g, Camera camera) {
        g.drawImage(img, this.button_1_x - camera.getCameraX(), this.button_1_y,
                this.button_1_x + this.width - camera.getCameraX(), this.button_1_y + this.height,
                0, 0 + 60 * this.buttonOne,
                350, 60 * (1 + this.buttonOne), null);
        g.drawImage(img, this.button_2_x - camera.getCameraX(), this.button_2_y,
                this.button_2_x - camera.getCameraX() + this.width, this.button_2_y + this.height,
                0, 0 + 60 * this.buttonTwo,
                350, 60 * (1 + this.buttonTwo), null);
        g.drawImage(img, this.button_3_x - camera.getCameraX(), this.button_3_y,
                this.button_3_x - camera.getCameraX() + this.width, this.button_3_y + this.height,
                0, 0 + 60 * this.buttonThree,
                350, 60 * (1 + this.buttonThree), null);
        this.buttonbomb.paint(g, camera, 0);

    }

    public Bomb2 getButtonBomb() {
        return this.buttonbomb;
    }

    public int getButton_1_LEFT() {
        return this.button_1_x;
    }

    public int getButton_1_TOP() {
        return this.button_1_y;
    }

    public int getButton_2_LEFT() {
        return this.button_2_x;
    }

    public int getButton_2_TOP() {
        return this.button_2_y;
    }

    public int getButton_3_LEFT() {
        return this.button_3_x;
    }

    public int getButton_3_TOP() {
        return this.button_3_y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Delay getDelay() {
        return this.delay;
    }

    public void setButtonTwo(int i) {
        this.buttonTwo = i;
    }

    public void setButtonOne(int i) {
        this.buttonOne = i;
    }

    public void setButtonThree(int i) {
        this.buttonThree = i;
    }

    public void setPictureState() {
        this.pictureState = true;
    }
    
        public boolean getPictureState() {
        return this.pictureState;
    }

    public int getbuttonOne() {
        return this.buttonOne;
    }

    public int getbuttonTwo() {
        return this.buttonTwo;
    }

    public int getbuttonThree() {
        return this.buttonThree;
    }
}
