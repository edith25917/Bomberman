/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.background;

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
public class ButtonSelectPlayer {

    private BufferedImage img1;
    private BufferedImage img2;

    private BufferedImage img4;
    private BufferedImage img5;
    private BufferedImage img6;
    private int playIndex;

    private int button_1_x; //play1角色頭上的圖案
    private int button_1_y;
    private int button_2_x;//選擇角色的箭頭
    private int button_2_y;

    private int button_4_x;
    private int button_4_y;
    private int button_5_x;
    private int button_5_y;
    private int width1;
    private int height1;
    private int width2;
    private int height2;
    private int width3;
    private int height3;
    private int width4;
    private int height4;
    private int width5;
    private int height5;
    private Delay pictureDelay2;
    private Delay pictureDelay4;
    private int buttonState;
    private int button2Index;
    private int button4state;

    public ButtonSelectPlayer(int playIndex) {
        this.img1 = ImageResourceController.getInstance().tryGeyImage(ImagePath.BUTTON_SELECT_1);
        this.img2 = ImageResourceController.getInstance().tryGeyImage(ImagePath.BUTTON_SELECT_2);

        this.img4 = ImageResourceController.getInstance().tryGeyImage(ImagePath.BUTTON_SELECT_4);
        this.img5 = ImageResourceController.getInstance().tryGeyImage(ImagePath.BUTTON_SELECT_5);
        this.img6 = ImageResourceController.getInstance().tryGeyImage(ImagePath.BUTTON_SELECT_6);
        this.playIndex = playIndex;
        this.width1 = 80;
        this.height1 = 50;
        this.width2 = 56;
        this.height2 = 65;
        this.width3 = 50;
        this.height3 = 60;
        this.width4 = 500;
        this.height4 = 80;
        this.width5 = 150;
        this.height5 = 156;
        this.button_2_x = 2515;
        this.button_2_y = 165;

        this.button_4_x = 2840 - playIndex * 520;
        this.button_4_y = 470;
        this.button_5_x = 3120 - playIndex * 450;
        this.button_5_y = 600;
        this.pictureDelay2 = new Delay(30);
        this.pictureDelay4 = new Delay(8);
        this.buttonState = 0;
        this.button2Index = 0;
        this.button4state = 0;
    }

    public int getButton4State() {
        return this.button4state;
    }

    public Delay getPictureDelay2() {
        return this.pictureDelay2;
    }

    public Delay getPictureDelay4() {
        return this.pictureDelay4;
    }

    public void setIS_MUlTIPLAYERS() {
        this.width4 = 410;
        this.button_4_x = 3040 - playIndex * 520;

    }

    public void setButton1(int playX, int playY) {
        this.button_1_x = playX + 5;
        this.button_1_y = playY - 65;
    }

    public int getButtonState() {
        return this.buttonState;
    }

    public void setButtonState(int state) {
        this.buttonState = state;
    }

    public void setButton2_X(int index) {
        this.button2Index = (this.button2Index + index) % 5;
        if (this.button2Index == -1) {
            this.button2Index = 4;
        }
        switch (this.button2Index) {
            case 0:
                this.button_2_x = 2515;

                break;
            case 1:
                this.button_2_x = 2720;
                break;
            case 2:
                this.button_2_x = 2925;
                break;
            case 3:
                this.button_2_x = 3130;
                break;
            case 4:
                this.button_2_x = 3340;
                break;

        }

    }

    public int getButton2Index() {
        return this.button2Index;
    }

    public void update() {

        if (this.pictureDelay2.isTrig() && this.pictureDelay4.getIsPause() && this.button4state < 10 && this.width2 == 56 && this.height2 == 65) {
            this.width2 = 0;
            this.height2 = 0;
        }
        if (this.pictureDelay2.isTrig() && this.pictureDelay4.getIsPause() && this.buttonState < 10 && this.width2 == 0 && this.height2 == 0) {
            this.width2 = 56;
            this.height2 = 65;

        }
        if (this.pictureDelay4.isTrig() && this.button4state < 10) {

            this.pictureDelay2.pause();
            this.width2 = 56;
            this.height2 = 65;
            this.button4state++;
            if (this.button4state == 10) {
                this.pictureDelay4.pause();
            }
            if (Global.IS_MUlTIPLAYERS) {
                if (this.width4 == 0 && this.height4 == 0) {
                    this.width4 = 410;
                    this.height4 = 80;
                } else {
                    this.width4 = 0;
                    this.height4 = 0;
                }
            } else {
                if (this.width4 == 0 && this.height4 == 0) {
                    this.width4 = 500;
                    this.height4 = 80;
                } else if (this.width4 == 500 && this.height4 == 80) {
                    this.width4 = 0;
                    this.height4 = 0;
                }
            }
        }

//        }
    }

    public void paintComponent(Graphics g, int cameraX) {
        g.drawImage(img2, this.button_2_x - cameraX, this.button_2_y, this.button_2_x - cameraX + this.width2, this.button_2_y + this.height2, 0, this.playIndex * this.height2, this.width2, (1 + this.playIndex) * this.height2, null);

        if (this.buttonState == 2) {
            g.drawImage(img6, this.button_5_x - cameraX, this.button_5_y, this.button_5_x - cameraX + this.width5, this.button_5_y + this.height5, this.button2Index * this.width5, 0, this.width5 * (1 + this.button2Index), this.height5, null);
        }
        g.drawImage(img1, this.button_1_x - cameraX, this.button_1_y, this.button_1_x - cameraX + this.width1, this.button_1_y + this.height1, 0, this.playIndex * this.height1, this.width1, (1 + this.playIndex) * this.height1, null);

        if (Global.IS_MUlTIPLAYERS) {
            g.drawImage(img4, this.button_4_x - cameraX, this.button_4_y, this.button_4_x - cameraX + this.width4, this.button_4_y + this.height4, 0, this.playIndex * this.height4, this.width4, (1 + this.playIndex) * this.height4, null);
        } else {
            g.drawImage(img5, this.button_4_x - cameraX, this.button_4_y, this.button_4_x - cameraX + this.width4, this.button_4_y + this.height4, 0, this.playIndex * this.height4, this.width4, (1 + this.playIndex) * this.height4, null);

        }
    }
}
