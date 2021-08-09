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
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author user
 */
public class UserInterface extends GameObject {

    private final int POWER_UNIT_X = 96;
    private final int POWER_UNIT_Y = 46;

    private BufferedImage clock;
    private BufferedImage data;
    private BufferedImage power;
    private BufferedImage heart;
    private int heartIndex;
    private int min;
    private int sec;
    private String minString;
    private String secString;
    private Delay delay;//計時器的Delay
    private Delay heartPictureDelay;
    private int heartNumber;
    private int bombNumber;
    private int fireNumber;
    private int speedNumber;

    private TaskBar taskbar;
    private boolean showTaskbar;//從外面存過關條件的bar是否還在

    public UserInterface(int min, int heartNumber, int bombNumber, int fireNumber, int speedNumber) {
        super(0, 0, 64, 64, true);
        this.clock = ImageResourceController.getInstance().tryGeyImage(ImagePath.CLOCK);
        this.data = ImageResourceController.getInstance().tryGeyImage(ImagePath.DATA);
        this.power = ImageResourceController.getInstance().tryGeyImage(ImagePath.POWER);
        this.heart = ImageResourceController.getInstance().tryGeyImage(ImagePath.HEART);
        setMin(min);
        this.sec = 0;
        this.delay = new Delay(60);
        this.delay.start();
        this.heartPictureDelay = new Delay(30);
        this.heartPictureDelay.start();
        this.heartIndex = 0;
        setminString();
        setsecString();

        this.heartNumber = heartNumber;//生命數量
        this.bombNumber = bombNumber;//炸彈1~5顆
        this.fireNumber = fireNumber;//範圍1~5
        this.speedNumber = speedNumber;//範圍1~5

    }

    public void setHeartNumber(int heartNumber) {
        this.heartNumber = heartNumber;
    }
    
    public int getHeartNumber() {
        return this.heartNumber;
    }


    public void setBombNumber(int bombNumber) {
        this.bombNumber = bombNumber;
    }

    public void setFireNumber(int fireNumber) {
        this.fireNumber = fireNumber;
    }

    public void setSpeedNumber(int speedNumber) {
        this.speedNumber = speedNumber;
    }

    public boolean setMin(int min) {
        if (min < 0 || min > 99) {
            return false;
        } else {
            this.min = min;
            return true;
        }
    }

    private void setsecString() {
        if (this.sec <= 0) {
            this.secString = "00";
        } else if (this.sec < 10 && this.sec > 0) {
            this.secString = "0" + String.valueOf(this.sec);
        } else {
            this.secString = String.valueOf(this.sec);
        }
    }

    private void setminString() {
        if (this.min >= 10) {
            this.minString = String.valueOf(this.min);
        } else if (this.min < 10) {
            this.minString = "0" + String.valueOf(this.min);
        } else if (this.min <= 0) {
            this.minString = "00";
        }
    }

    @Override
    public String toString() {
        return this.minString + ":" + this.secString;
    }

    public boolean showTaskbar(boolean showTaskbar) {
        return this.showTaskbar = showTaskbar;
    }

    public void update() {
        if (!showTaskbar && this.delay.isTrig()) {
            if (this.sec == 0 && this.min != 0) {
                this.min--;
                setminString();
                this.sec = 59;
                setsecString();
            } else {
                this.sec--;
                setsecString();
            }
        }
        if (this.heartPictureDelay.isTrig()) {
            this.heartIndex = (this.heartIndex + 1) % 6;
        }
    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {
        //時鐘
        g.drawImage(clock, Global.SCREEN_X / 2 - 130, 23, null);
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 50));
        g.setColor(Color.WHITE);
        g.drawString(toString(), Global.SCREEN_X / 2 - 64, 72);//已算好正中間

        if (!Global.IS_MUlTIPLAYERS) {//單人的data & heart位置
            g.drawImage(data, 650, 770, 500, 50, null);
            for (int i = 0; i < this.heartNumber; i++) {
                g.drawImage(heart, 90 + 40 * i, 40, 90 + 40 * i + 40, 40 + 40, 40 * this.heartIndex, 0, 40 * (1 + this.heartIndex), 40, null);
            }
            g.drawImage(power, 710, 774, 710 + this.POWER_UNIT_X, 774 + this.POWER_UNIT_Y,
                    0, 0 + this.POWER_UNIT_Y * (this.bombNumber - 1), this.POWER_UNIT_X, this.POWER_UNIT_Y * this.bombNumber, null);
            g.drawImage(power, 880, 774, 880 + this.POWER_UNIT_X, 774 + this.POWER_UNIT_Y,
                    0, 0 + this.POWER_UNIT_Y * (this.fireNumber - 1), this.POWER_UNIT_X, this.POWER_UNIT_Y * this.fireNumber, null);
            g.drawImage(power, 1050, 774, 1050 + this.POWER_UNIT_X, 774 + this.POWER_UNIT_Y,
                    0, 0 + this.POWER_UNIT_Y * (this.speedNumber - 1), this.POWER_UNIT_X, this.POWER_UNIT_Y * this.speedNumber, null);
        } else {//雙人的data & heart位置
            g.drawImage(data, 18 + startX, 770, 500, 50, null);
            for (int i = 0; i < this.heartNumber; i++) {
                g.drawImage(heart, 50+startX + 40 * i, 40, 50+startX + 40 * i + 40, 40 + 40, 40 * this.heartIndex, 0, 40 * (1 + this.heartIndex), 40, null);
            }
//            for (int i = 0; i < this.heartNumber; i++) {
//                g.drawImage(heart, 55 + startX + 60 * i, 23, null);
//            }
            g.drawImage(power, 78 + startX, 774, 78 + startX + this.POWER_UNIT_X, 774 + this.POWER_UNIT_Y,
                    0, 0 + this.POWER_UNIT_Y * (this.bombNumber - 1), this.POWER_UNIT_X, this.POWER_UNIT_Y * this.bombNumber, null);
            g.drawImage(power, 248 + startX, 774, 248 + startX + this.POWER_UNIT_X, 774 + this.POWER_UNIT_Y,
                    0, 0 + this.POWER_UNIT_Y * (this.fireNumber - 1), this.POWER_UNIT_X, this.POWER_UNIT_Y * this.fireNumber, null);
            g.drawImage(power, 418 + startX, 774, 418 + startX + this.POWER_UNIT_X, 774 + this.POWER_UNIT_Y,
                    0, 0 + this.POWER_UNIT_Y * (this.speedNumber - 1), this.POWER_UNIT_X, this.POWER_UNIT_Y * this.speedNumber, null);
        }

    }
}
