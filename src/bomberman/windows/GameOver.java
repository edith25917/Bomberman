/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.windows;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.gameobj.Bomb2;
import bomberman.util.AudioPath;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Edith
 */
public class GameOver {

    private boolean isOver = false;
    private boolean isWin;
    private int buttonWidth;
    private int buttonHeight;
    //按鍵
    private int button_1_x;
    private int button_1_y;
    private int button_2_x;
    private int button_2_y;
    private int buttonOne;
    private int buttonTwo;
    private Bomb2 buttonbomb;
    private Delay buttonDelay;
    private boolean pictureState;

    private BufferedImage gameover;
    private BufferedImage goverButton;
    private BufferedImage nextLevelButton;
    private BufferedImage continueImg;
    private BufferedImage bomb;
    private BufferedImage explode;
    private BufferedImage missionComplete;

    private int state = 1;//判斷按鍵往上或往下的state
    private AudioClip clickAudio;
    private int gostate;
    private int gameOverState = 150;
    private boolean paintWin = false;
    private int continueState = 0;

    public GameOver() {

        this.gameover = ImageResourceController.getInstance().tryGeyImage(ImagePath.GAMEOVER);
        this.goverButton = ImageResourceController.getInstance().tryGeyImage(ImagePath.GOVERBUTTON);
        this.nextLevelButton = ImageResourceController.getInstance().tryGeyImage(ImagePath.NEXTLEVEL);
        this.continueImg = ImageResourceController.getInstance().tryGeyImage(ImagePath.CONTINUE);
        this.missionComplete = ImageResourceController.getInstance().tryGeyImage(ImagePath.MISSIONCOMPLETE);

        //炸彈動畫
        this.bomb = ImageResourceController.getInstance().tryGeyImage(ImagePath.DATA);
        this.buttonbomb = new Bomb2(Global.SCREEN_X / 2 - 140, 485, null);
        this.buttonbomb.setPICTURE_DRLAY(5);
        //按鍵位置
        this.button_1_x = Global.SCREEN_X / 2 - 175;
        this.button_1_y = 460;
        this.button_2_x = Global.SCREEN_X / 2 - 175;
        this.button_2_y = 540;
        this.buttonWidth = 350;
        this.buttonHeight = 60;
        this.buttonOne = 0;
        this.buttonTwo = 3;
        this.buttonDelay = new Delay(10);
        this.pictureState = false;
        this.clickAudio = Applet.newAudioClip(getClass().getResource(AudioPath.CLICK));

    }

    public void setState(int state) {
        this.state = state;
    }

    public void setButton() {
        if (Global.CURRENT_LEVEL == 3 && this.isWin) {
                if (!pictureState) {
                    this.buttonTwo = 2;
                } else {//enter後
                    if (this.buttonTwo == 2 && this.buttonDelay.isTrig()) {
                        this.buttonTwo = 4;
                    }
                    if (this.buttonTwo == 4 && this.buttonDelay.isTrig()) {
                        this.buttonTwo = 2;
                    }
                }
        } else {
            if (this.state == 0) {//回主選單
                this.buttonbomb.setY(565);
                //enter前狀態
                this.buttonOne = 1;
                if (!pictureState) {
                    this.buttonTwo = 2;
                } else {//enter後
                    if (this.buttonTwo == 2 && this.buttonDelay.isTrig()) {
                        this.buttonTwo = 4;
                    }
                    if (this.buttonTwo == 4 && this.buttonDelay.isTrig()) {
                        this.buttonTwo = 2;
                    }
                }
            } else if (state == 1) {//輸 restart or 贏 nextlevel
                this.buttonbomb.setY(485);
                //enter前
                this.buttonTwo = 3;
                if (!pictureState) {
                    this.buttonOne = 0;
                } else {//enter後
                    if (this.buttonOne == 0 && this.buttonDelay.isTrig()) {
                        this.buttonOne = 4;
                    }
                    if (this.buttonOne == 4 && this.buttonDelay.isTrig()) {
                        this.buttonOne = 0;
                    }
                }
            }
        }
    }

    public void setPaintWin() {
        this.paintWin = true;
    }

    public void update() {
        setButton();
        this.buttonbomb.update();
        if (this.buttonbomb.getPictureIndex() == 9) {
            this.buttonbomb.resetPictureIndex();
            this.buttonbomb.getPictureDelay().start();
        }
        this.gostate++;

    }

    public void paintComponent(Graphics g, Camera camera, int startX) {
        if (!this.isWin && this.gostate > gameOverState) {//GameOver
            //白色背景
            g.setColor(new Color(255, 255, 255, 220));
            g.fillRect(0, 0, Global.SCREEN_X, Global.SCREEN_Y);
            //gameover
            g.drawImage(this.gameover, Global.SCREEN_X / 2 - 350, Global.SCREEN_Y / 2 - 200, null);
            //按鍵
            g.drawImage(this.goverButton, this.button_1_x, this.button_1_y,//restart
                    this.button_1_x + this.buttonWidth, this.button_1_y + this.buttonHeight,
                    0, 0 + 60 * this.buttonOne,
                    350, 59 * (1 + this.buttonOne), null);
            g.drawImage(this.goverButton, this.button_2_x, this.button_2_y,//mainmenu
                    this.button_2_x + this.buttonWidth, this.button_2_y + this.buttonHeight,
                    0, 0 + 61 * this.buttonTwo,
                    350, 60 * (1 + this.buttonTwo), null);
            this.buttonbomb.paint(g);

        } else if (this.isWin && this.gostate > gameOverState) {//isWin=true
            //白色背景
            g.setColor(new Color(255, 255, 255, 220));
            g.fillRect(0, 0, Global.SCREEN_X, Global.SCREEN_Y);
            //continue
            if (Global.CURRENT_LEVEL == 3) {
                g.drawImage(this.missionComplete, 200, Global.SCREEN_Y / 2 - 200, null);
            } else {
                g.drawImage(this.continueImg, Global.SCREEN_X / 2 - 320, Global.SCREEN_Y / 2 - 200, null);
            }
            //按鍵
            if (Global.CURRENT_LEVEL == 3) {
                g.drawImage(this.nextLevelButton, this.button_1_x, this.button_1_y,//mainmenu
                        this.button_1_x + this.buttonWidth, this.button_1_y + this.buttonHeight,
                        0, 0 + 61 * this.buttonTwo,
                        350, 60 * (1 + this.buttonTwo), null);
            } else {
                g.drawImage(this.nextLevelButton, this.button_1_x, this.button_1_y,//nextLevel
                        this.button_1_x + this.buttonWidth, this.button_1_y + this.buttonHeight,
                        0, 0 + 61 * this.buttonOne,
                        350, 60 * (1 + this.buttonOne), null);
                g.drawImage(this.nextLevelButton, this.button_2_x, this.button_2_y,//mainmenu
                        this.button_2_x + this.buttonWidth, this.button_2_y + this.buttonHeight,
                        0, 0 + 61 * this.buttonTwo,
                        350, 60 * (1 + this.buttonTwo), null);
            }
            this.buttonbomb.paint(g);
        }
    }

    public void setIsWin(boolean isWin) {
        this.isWin = isWin;
    }

    public boolean getIsWin() {
        return this.isWin;
    }

    public boolean isOver() {
        return this.isOver;
    }
    
    public void setIsOver() {
        this.isOver = true;
    }

    public void setButtonTwo(int i) {
        this.buttonTwo = i;
    }

    public void setButtonOne(int i) {
        this.buttonOne = i;
    }

    public int getState() {
        return this.state;
    }

    public Delay getDelay() {
        return this.buttonDelay;
    }

    public void setPictureState() {
        this.pictureState = true;
    }

    public AudioClip getClickAudio() {
        return this.clickAudio;
    }

}
