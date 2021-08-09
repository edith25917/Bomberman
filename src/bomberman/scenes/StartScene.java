package bomberman.scenes;

import bomberman.background.BackGround;
import bomberman.background.ButtonAnyPress;
import bomberman.background.ButtonSelectPlayer;
import bomberman.background.ButtonStart;
import bomberman.camera.Camera;
import bomberman.controllers.AudioResourceController;
import bomberman.controllers.SceneController;
import bomberman.gameobj.Bomb2;
import bomberman.gameobj.CatMan;
import bomberman.gameobj.Renderer;
import bomberman.monsters.Monster;
import bomberman.util.AudioPath;
import gamekernal.CommandSolver;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import static gamekernal.CommandSolver.MouseState.CLICKED;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class StartScene extends Scene {

    private BackGround backgroundSky1;
    private BackGround backgroundSky2;
    private BackGround backgroundSky3;
    private BackGround backgroundSky4;
    private BackGround backTop;//主畫面
    private BackGround backTop2;//遊戲說明
    private BackGround backTop3;//角色選擇
    private CatMan catman1P;
    private CatMan catman2P;
    private ButtonStart buttonstart;
    private ButtonAnyPress anyButton;
    private ButtonSelectPlayer buttonSelect1P;
    private ButtonSelectPlayer buttonSelect2P;
    private Delay sceneDelay;
    private Delay button2Delay;
    private int changeScene;
    private Camera camera;
    private int catmanTotal;
    private Delay backgroundDelay;
    private Delay catmanMoveDelay;
    private boolean catmanMovePause;
    private int state;//判斷按鍵上下的state
    private AudioClip bgAudio;

    public StartScene(SceneController sceneController) {
        super(sceneController);
        bgAudio = Applet.newAudioClip(getClass().getResource(AudioPath.STARTSCENE));
    }

    @Override
    public void sceneBegin() {
        this.backgroundSky1 = new BackGround(ImagePath.BACKGROUND_1, 0, 0);
        this.backgroundSky2 = new BackGround(ImagePath.BACKGROUND_1, this.backgroundSky1.getX() + Global.SCREEN_X, 0);
        this.backgroundSky3 = new BackGround(ImagePath.BACKGROUND_1, this.backgroundSky2.getX() + Global.SCREEN_X, 0);
        this.backgroundSky4 = new BackGround(ImagePath.BACKGROUND_1, this.backgroundSky3.getX() + Global.SCREEN_X, 0);
        this.backTop = new BackGround(ImagePath.BACKGROUND_2, 0, 0);
        this.backTop2 = new BackGround(ImagePath.BACKGROUND_3, this.backgroundSky1.getX() + Global.SCREEN_X, 0);
        this.backTop3 = new BackGround(ImagePath.BACKGROUND_5, this.backgroundSky2.getX() + Global.SCREEN_X, 0);
        this.anyButton = new ButtonAnyPress(ImagePath.BUTTON, this.backTop2.getX() + Global.SCREEN_X / 2 - 175, 430);
        this.buttonstart = new ButtonStart();
        this.buttonSelect1P = new ButtonSelectPlayer(0);
        this.buttonSelect2P = new ButtonSelectPlayer(1);
        this.catman1P = new CatMan(0, Renderer.UNMOVED_STEPS, 4, 650, 720, null,true);
        this.catman1P.getRenderer().setWidthHeight(85, 85);
        this.catman2P = new CatMan(0, Renderer.UNMOVED_STEPS, 4, 270, 720, null,false);
        this.catman2P.getRenderer().setWidthHeight(85, 85);
        this.sceneDelay = new Delay(90);
        this.button2Delay = new Delay(90);
        this.camera = new Camera(Global.SCREEN_X, Global.SCREEN_Y, 3, 0, Global.SCREEN_X * 3, Global.SCREEN_Y);
        this.catmanTotal = 0;
        this.backgroundDelay = new Delay(10);
        this.backgroundDelay.start();
        this.catmanMoveDelay = new Delay(30);
        this.catmanMovePause = false;
        Global.setTotalWorldWidthHeight(Global.SCREEN_X * 3, Global.SCREEN_Y);
        this.bgAudio.loop();
    }

    public Delay getDelay() {
        return this.sceneDelay;
    }

    private void catMansUpdate() {
        this.catman1P.update();
        this.buttonSelect1P.setButton1(this.catman1P.getLeft(), this.catman1P.getTop());
        if (this.catman2P != null) {
            this.buttonSelect2P.setButton1(this.catman2P.getLeft(), this.catman2P.getTop());
            this.catman2P.update();
        }

        if (Global.IS_MUlTIPLAYERS) {

            if (this.catman2P.getX() > 450 && this.catman1P.getX() < 1862) {
                this.catman1P.setStand(false);
                this.camera.centerOnObj(this.catman1P);
                this.catman1P.setDir(2);

            }
            if (this.catman1P.getX() > 1855 && this.catman1P.getX() < 1862) {
                this.catman1P.setStand(true);
                this.catman2P.setStand(true);
                this.catman1P.setDir(3);
                this.catman2P.setDir(3);
                this.backgroundDelay.start();
                this.anyButton.getDelay().start();
                this.catman1P.setX(1863);
                this.catman2P.setX(1661);
                this.buttonstart = null;
            }
            if (this.catman1P.getX() > 1863) {
                this.camera.centerOnObj(this.catman1P);
            }
            if (this.catman1P.getX() > 1863 + Global.SCREEN_X) {
                this.catman1P.setStand(true);
                this.catman2P.setStand(true);
                this.catman1P.setDir(0);
                this.catman2P.setDir(0);
                this.backgroundDelay.start();
                this.buttonSelect1P.getPictureDelay2().start();
                this.buttonSelect2P.getPictureDelay2().start();
                this.buttonSelect1P.setButtonState(2);
                this.buttonSelect2P.setButtonState(2);
                this.catman1P.getRenderer().setCharacterIndex(this.buttonSelect1P.getButton2Index());
                this.catman2P.getRenderer().setCharacterIndex(this.buttonSelect2P.getButton2Index());
            }
            if (this.buttonSelect1P.getButton4State() == 10 && this.buttonSelect2P.getButton4State() == 10) {
                this.catman1P.setDir(2);
                this.catman2P.setDir(2);
                this.catman1P.setStand(false);
                this.catman2P.setStand(false);
            }

        } else {

            if (!this.catman1P.getIsStand() && this.catman1P.getX() < 1856) {//從第一個畫面走到第二個
                this.camera.centerOnObj(this.catman1P);
                this.catman1P.setDir(2);

            }
            if (this.catman1P.getX() > 1840 && this.catman1P.getX() < 1856) {//走到第二個中間要停止
                this.catman1P.setStand(true);
                this.catman1P.setDir(3);
                this.backgroundDelay.start();
                this.anyButton.getDelay().start();
                this.catman1P.setX(1857);
                this.buttonstart = null;
                this.buttonSelect1P.setButtonState(1);
            }
            if (!this.catman1P.getIsStand() && this.catman1P.getX() == 1857) {//任意鍵按下再繼續往右走
                this.catman1P.setDir(2);
            }
            if (this.catman1P.getX() > 1857) {
                this.camera.centerOnObj(this.catman1P);
            }
            if (this.catman1P.getX() > 1856 + Global.SCREEN_X && this.buttonSelect1P.getPictureDelay4().getIsPause()) {//走到第三個畫面停止
                this.buttonSelect1P.getPictureDelay2().start();
                this.catman1P.setStand(true);
                this.catman1P.setDir(0);
                this.backgroundDelay.start();
                this.buttonSelect1P.setButtonState(2);
                this.catman1P.getRenderer().setCharacterIndex(this.buttonSelect1P.getButton2Index());
            }
            if (this.buttonSelect1P.getButton4State() == 10) {
                this.catman1P.setDir(2);
                this.catman1P.setStand(false);

            }

        }

    }

    private void buttonUpdate() {
        if (this.buttonstart != null) {
            this.buttonstart.update();
        }
        this.anyButton.update();
        this.buttonSelect1P.update();
        this.buttonSelect2P.update();

    }

    private void skyUpdate() {

        if (this.camera.getCameraX() == this.backgroundSky2.getX()) {
            this.backgroundSky3.setXsetY(this.backgroundSky2.getX() + Global.SCREEN_X, 0);
        }
        if (this.camera.getCameraX() == this.backgroundSky3.getX()) {
            this.backgroundSky4.setXsetY(this.backgroundSky3.getX() + Global.SCREEN_X, 0);
        }
        if (this.camera.getCameraX() == this.backgroundSky1.getX()) {
            this.backgroundSky2.setXsetY(this.backgroundSky1.getX() + Global.SCREEN_X, 0);
        }
        if (this.camera.getCameraX() == this.backgroundSky4.getX()) {
            this.backgroundSky1.setXsetY(this.backgroundSky4.getX() + Global.SCREEN_X, 0);
        }
        if (this.backgroundDelay.isTrig()) {
            this.backgroundSky1.setXsetY(this.backgroundSky1.getX() - 1, 0);
            this.backgroundSky2.setXsetY(this.backgroundSky2.getX() - 1, 0);
            this.backgroundSky3.setXsetY(this.backgroundSky3.getX() - 1, 0);
            this.backgroundSky4.setXsetY(this.backgroundSky4.getX() - 1, 0);
        }
    }

    @Override
    public void sceneUpdate() {

        catMansUpdate();
        if (!this.catman1P.getIsStand()) {
            this.camera.update();
        }
        skyUpdate();
        buttonUpdate();

        if (this.sceneDelay.isTrig()) {//離開遊戲
            System.exit(0);
        }
        if (Global.IS_MUlTIPLAYERS) {
            if (this.catman2P.getX() > Global.SCREEN_X * 3 + 35) {
                this.sceneController.changeScene(new MainScene(sceneController, this.buttonSelect2P.getButton2Index(), this.buttonSelect1P.getButton2Index()));
            }
        } else {
            if (this.catman1P.getX() > Global.SCREEN_X * 3 + 30) {
                this.sceneController.changeScene(new MainScene(sceneController, this.buttonSelect1P.getButton2Index(), 0));
            }
        }

    }

    @Override
    public void sceneEnd() {
        this.backgroundSky1 = null;
        this.backgroundSky2 = null;
        this.backgroundSky3 = null;
        this.backTop = null;
        this.backTop2 = null;
        this.catman1P = null;
        this.catman2P = null;
        this.buttonstart = null;

        this.camera = null;
        this.anyButton = null;
        this.backgroundDelay = null;
        this.backgroundSky4 = null;
        this.button2Delay = null;
        this.buttonSelect1P = null;
        this.buttonSelect2P = null;
        this.catmanMoveDelay = null;
        this.sceneDelay = null;
        this.bgAudio.stop();
    }

    @Override
    public void paint(Graphics g) {
        paintBackGroundBack(g);
        paintBackGroundTop(g);
        paintCatMans(g);

    }

    private void paintBackGroundBack(Graphics g) {
        this.backgroundSky1.paintComponent(g, this.backgroundSky1.getX(), 0, Global.SCREEN_X, Global.SCREEN_Y, (int) this.camera.getCameraX(), (int) this.camera.getCameraY());
        this.backgroundSky2.paintComponent(g, this.backgroundSky2.getX(), 0, Global.SCREEN_X, Global.SCREEN_Y, (int) this.camera.getCameraX(), (int) this.camera.getCameraY());
        this.backgroundSky3.paintComponent(g, this.backgroundSky3.getX(), 0, Global.SCREEN_X, Global.SCREEN_Y, (int) this.camera.getCameraX(), (int) this.camera.getCameraY());
        this.backgroundSky4.paintComponent(g, this.backgroundSky4.getX(), 0, Global.SCREEN_X, Global.SCREEN_Y, (int) this.camera.getCameraX(), (int) this.camera.getCameraY());
        if (this.buttonstart != null) {
            this.buttonstart.paintComponent(g, camera);
        }

        this.anyButton.paintComponent(g, this.anyButton.getX(), this.anyButton.getY(), (int) this.camera.getCameraX(), (int) this.camera.getCameraY());
    }

    private void paintBackGroundTop(Graphics g) {
        this.backTop.paintComponent(g, this.backTop.getX(), 0, Global.SCREEN_X, Global.SCREEN_Y, (int) this.camera.getCameraX(), (int) this.camera.getCameraY());
        this.backTop2.paintComponent(g, this.backTop2.getX(), 0, Global.SCREEN_X, Global.SCREEN_Y, (int) this.camera.getCameraX(), (int) this.camera.getCameraY());
        this.backTop3.paintComponent(g, this.backTop3.getX(), 0, Global.SCREEN_X, Global.SCREEN_Y, (int) this.camera.getCameraX(), (int) this.camera.getCameraY());

    }

    private void paintCatMans(Graphics g) {

        if (Global.IS_MUlTIPLAYERS) {
            this.catman2P.paint(g, camera, 0);
            this.buttonSelect2P.paintComponent(g, (int) this.camera.getCameraX());
        }
        this.buttonSelect1P.paintComponent(g, (int) this.camera.getCameraX());
        this.catman1P.paint(g, camera, 0);
        if (this.buttonstart != null && !Global.IS_MUlTIPLAYERS && this.buttonstart.getbuttonTwo() == 4) {
            this.catman2P.paint(g, camera, 0);
            this.buttonSelect2P.paintComponent(g, (int) this.camera.getCameraX());
        }

    }

    @Override
    public CommandSolver.KeyListener getKeyListener() {
        return new MyKeyListener();
    }

    @Override
    public CommandSolver.MouseCommandListener getMouseListener() {
        return new MyMouseListener();
    }

    public class MyKeyListener implements CommandSolver.KeyListener {

        @Override
        public void keyPressed(int commandCode, long trigTime) {

        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {
            if (catman1P.getIsStand() && buttonstart != null) {

                if (commandCode == Global.UP || commandCode == Global.W) {
                    buttonstart.setbuttonState(1);
                }
                if (commandCode == Global.DOWN || commandCode == Global.S) {
                    buttonstart.setbuttonState(0);
                }
                if (commandCode == Global.ENTER || commandCode == Global.SPACE || commandCode == Global.M || commandCode == Global.V) {
                    AudioResourceController.getInstance().play(AudioPath.CLICK);
                    buttonstart.getDelay().start();
                    buttonstart.setPictureState();
                    if (buttonstart.getbuttonOne() == 3) {
                        backgroundDelay.pause();
                        catman1P.setStand(false);
                        catman2P = null;
                    }
                    if (buttonstart.getbuttonTwo() == 4) {
                        button2Delay.start();
                        Global.IS_MUlTIPLAYERS = true;
                        backgroundDelay.pause();
                        if (catman2P.getX() == 270) {
                            catman2P.setDir(2);
                            catman2P.setStand(false);
                        }
                        backTop2 = new BackGround(ImagePath.BACKGROUND_4, backTop.getX() + Global.SCREEN_X, 0);
                        buttonSelect1P.setIS_MUlTIPLAYERS();
                        buttonSelect2P.setIS_MUlTIPLAYERS();
                    }
                    if (buttonstart.getbuttonThree() == 5) {
                        sceneDelay.start();
                    }
                }
            }
            if (catman1P.getX() > 3000) {//走到第三個地圖後可以用操作按鍵選擇

                if (commandCode == Global.LEFT && buttonSelect1P.getButton4State() == 0) {
                    buttonSelect1P.setButton2_X(-1);
                }
                if (commandCode == Global.RIGHT && buttonSelect1P.getButton4State() == 0) {
                    buttonSelect1P.setButton2_X(1);
                }
                if (Global.IS_MUlTIPLAYERS && commandCode == Global.M) {
                    AudioResourceController.getInstance().play(AudioPath.ROLESELECT);
                    buttonSelect1P.getPictureDelay4().start();
                }
                if (commandCode == Global.SPACE && !Global.IS_MUlTIPLAYERS) {
                    AudioResourceController.getInstance().play(AudioPath.ROLESELECT);
                    buttonSelect1P.getPictureDelay4().start();
                }
                if (commandCode == Global.A && buttonSelect2P.getButton4State() == 0) {
                    buttonSelect2P.setButton2_X(-1);
                }
                if (commandCode == Global.D && buttonSelect2P.getButton4State() == 0) {
                    buttonSelect2P.setButton2_X(1);
                }
                if (commandCode == Global.V) {
                    buttonSelect2P.getPictureDelay4().start();
                    AudioResourceController.getInstance().play(AudioPath.ROLESELECT);
                }
            }

        }

        @Override
        public void keyTyped(char c, long trigTime) {
            if (catman1P.getIsStand() && catman1P.getX() >= 650 + Global.SCREEN_X && catman1P.getX() < 1857 + Global.SCREEN_X) {
                AudioResourceController.getInstance().play(AudioPath.CLICK);
                catman1P.setDir(2);
                catman1P.setStand(false);
                backgroundDelay.pause();
                if (catman2P != null) {
                    catman2P.setDir(2);
                    catman2P.setStand(false);
                }
            }

        }
    }

    public class MyMouseListener implements CommandSolver.MouseCommandListener {

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
            buttonSelect(e, state);
        }

        public void buttonSelect(MouseEvent e, CommandSolver.MouseState state) {

        }
    }
}
