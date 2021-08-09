package bomberman.scenes;

import gamekernal.CommandSolver;
import bomberman.background.BackGround;
import bomberman.controllers.SceneController;
import bomberman.gameobj.*;
import bomberman.tile.World;
import bomberman.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MainScene extends Scene {

    private static final int worldX = 120;//地圖左上角的絕對位置X
    private static final int worldY = 137;//地圖左上角的絕對位置Y
    private int characterIndex1P;
    private int characterIndex2P;
    //場景
    private boolean currentstate;
    private World world;

    public MainScene(SceneController sceneController, int characterIndex1P, int characterIndex2P) {
        super(sceneController);
        this.currentstate = true;
        Global.setTotalWorldWidthHeight(1536, 960);
        this.characterIndex1P = characterIndex1P;
        this.characterIndex2P = characterIndex2P;
    }

    @Override
    public void sceneBegin() {

        if (!Global.IS_MUlTIPLAYERS) {
            this.world = new World(1, "map/forest1-3.txt", this.characterIndex1P, this.characterIndex2P);
        } else {
            this.world = new World(4, "map/forest1-4.txt", this.characterIndex1P, this.characterIndex2P);
        }
    }

    @Override
    public void sceneUpdate() {
        this.world.update();
    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public void paint(Graphics g) {
        this.world.paint(g);

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
            if (Global.IS_MUlTIPLAYERS) {
                if (commandCode == Global.W || commandCode == Global.S || commandCode == Global.A || commandCode == Global.D) {
                    if (world.getCatMan1P().isActive() && !world.getTaskBar().showTaskbar() && !world.gameOver().isOver()) {
                        world.getCatMan1P().setStand(false);
                        world.getCatMan1P().setSteps(Renderer.NORMAL_STEPS);
                        world.getCatMan1P().setDir(commandCode);
                    }
                }
                if (commandCode == Global.UP || commandCode == Global.DOWN || commandCode == Global.LEFT || commandCode == Global.RIGHT) {
                    if (world.getCatMan2P().isActive() && !world.getTaskBar().showTaskbar() && !world.gameOver().isOver()) {
                        world.getCatMan2P().setStand(false);
                        world.getCatMan2P().setSteps(Renderer.NORMAL_STEPS);
                        world.getCatMan2P().setDir(commandCode);
                    }
                }
            } else {
                if (commandCode == Global.UP || commandCode == Global.DOWN || commandCode == Global.LEFT || commandCode == Global.RIGHT) {
                    if (world.getCatMan1P().isActive() && !world.getTaskBar().showTaskbar() && !world.gameOver().isOver()) {
                        world.getCatMan1P().setStand(false);
                        world.getCatMan1P().setSteps(Renderer.NORMAL_STEPS);
                        world.getCatMan1P().setDir(commandCode);
                    }

                }
            }

        }

        @Override
        public void keyReleased(int commandCode, long trigTime) {

            //雙人模式
            if (Global.IS_MUlTIPLAYERS) {
                if (commandCode == Global.W || commandCode == Global.S || commandCode == Global.A || commandCode == Global.D) {
                    if (world.getCatMan1P().isActive() && !world.getTaskBar().showTaskbar() && !world.isOver()) {
                        world.getCatMan1P().setStand(true);
                        world.getCatMan1P().setSteps(Renderer.UNMOVED_STEPS);
                    }
                }
                if (commandCode == Global.V) {
                    if (world.getCatMan1P().isActive() && !world.getTaskBar().showTaskbar() && !world.isOver()) {
                        world.getCatMan1P().putBomb();
                    }
                }
                if (commandCode == Global.UP || commandCode == Global.DOWN || commandCode == Global.LEFT || commandCode == Global.RIGHT) {
                    if (world.getCatMan2P().isActive() && !world.getTaskBar().showTaskbar() && !world.isOver()) {
                        world.getCatMan2P().setStand(true);
                        world.getCatMan2P().setSteps(Renderer.UNMOVED_STEPS);
                    }
                }
                if (commandCode == Global.M) {
                    if (world.getCatMan2P().isActive() && !world.getTaskBar().showTaskbar() && !world.isOver()) {
                        world.getCatMan2P().putBomb();
                    }
                }
            } else {
                if (commandCode == Global.UP || commandCode == Global.DOWN || commandCode == Global.LEFT || commandCode == Global.RIGHT) {
                    if (world.getCatMan1P().isActive() && !world.getTaskBar().showTaskbar() && !world.isOver()) {
                        world.getCatMan1P().setStand(true);
                        world.getCatMan1P().setSteps(Renderer.UNMOVED_STEPS);
                    }
                }
                if (commandCode == Global.SPACE && world.getCatMan1P().isActive() && !world.getTaskBar().showTaskbar() && !world.isOver()) {
                    world.getCatMan1P().putBomb();
                }

            }

            if (world.isOver()) {

                //gameover / nextlevel按鍵
                if (commandCode == Global.UP) {
                    world.gameOver().setState(1);
                }
                if (commandCode == Global.DOWN) {
                    world.gameOver().setState(0);
                }
                if (commandCode == Global.ENTER) {
                    world.gameOver().getClickAudio().play();
                    world.gameOver().getDelay().start();
                    world.gameOver().setPictureState();
                    if (world.gameOver().getIsWin() && world.gameOver().getState() == 1) {//如果贏 又選擇nextLevel
                        if (Global.CURRENT_LEVEL == 3) {
                            sceneController.changeScene(new StartScene(sceneController));//回主選單
                        }
                        int newLevel = world.getCurrentLevel() + 1;
                        world = new World(Global.CURRENT_LEVEL + 1,
                                "map/forest1-" + (Global.CURRENT_LEVEL + 1) + ".txt", characterIndex1P, characterIndex2P);//進入下一關
                    } else if (!world.gameOver().getIsWin() && world.gameOver().getState() == 1) {//如果輸 又選擇Restart
                        world = new World(Global.CURRENT_LEVEL,
                                "map/forest1-" + Global.CURRENT_LEVEL + ".txt", characterIndex1P, characterIndex2P);//重新開始這一關
                    } else {
                        sceneController.changeScene(new StartScene(sceneController));//回主選單
                    }
                }
            }
        }

        @Override
        public void keyTyped(char c, long trigTime) {
        }
    }

    public static class MyMouseListener implements CommandSolver.MouseCommandListener {

        @Override
        public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
            if (e.getButton() == MouseEvent.BUTTON1) {
            }
        }
    }
}
