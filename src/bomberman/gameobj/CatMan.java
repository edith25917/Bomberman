package bomberman.gameobj;

import bomberman.controllers.ImageResourceController;
import bomberman.camera.Camera;
import bomberman.controllers.AudioResourceController;
import bomberman.tile.World;
import bomberman.util.AudioPath;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import bomberman.windows.GameOver;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class CatMan extends GameObject {

    public static final int[] ABILITY_0 = {3, 2, 2, 1};
    public static final int[] ABILITY_1 = {3, 1, 1, 1};
    public static final int[] ABILITY_2 = {3, 1, 1, 2};
    public static final int[] ABILITY_3 = {3, 1, 2, 1};
    public static final int[] ABILITY_4 = {3, 2, 1, 1};

    //角色能力
    private int[] ability;
    private int heartTotal;
    private int bombTotal;
    private int intialBombTotal;
    private int fireTotal;
    private int intialFireTotal;
    private int speedTotal;
    private int intialSpeedTotal;
    private int characterIndex;
    private int x;
    private int y;
    private int dir = 2;
    private Renderer renderer;
    private Delay moveDelay;
    private boolean isStand;
    private int[][] tileID;
    private boolean isAlive;
    private LinkedList<Bomb2> bomb2;
    private int bombCount;  //放過的炸彈次數

    private double chaseSpeed = 0.6;//
    private int moveDistance = 6;//7是正常移動的距離 12是吃到道具後變快的移動距離

    //計分
    private int task1_Score = 0;//lv1: wood, lv2: key, lv3: letters
    private int task2_Score = 0;//lv2: monsters

    //消失特效
    private BufferedImage disapperEffects;
    private Delay disapperDelay;
    private int disapperIndex;
    private int moveStep;
    private Delay disapperDelay2;
    private int restartStep;
    //輸贏
    private boolean isWin = false;
    private boolean isOver;
    private BufferedImage win;
    private BufferedImage lose;
    private BufferedImage timeup;
    private int move = 0;
    private int paintState = 0;//判斷要paint win/lose 還是gameOver的state
    private int appearState = 0;
    private AudioClip dieAudio;
    private boolean paintTimeUp = false;
    private int TimeUpAppear = 60;
    private boolean isLeft;

    public CatMan(int characterIndex, int[] steps, int delayFrame, int x, int y, int[][] tileID, boolean isLeft) {

        super(x, y, Global.UNIT_X, Global.UNIT_Y, Global.UNIT_X, Global.UNIT_Y);
        this.getCollider().resetCenter(x, y + 10, Global.UNIT_X - 36, Global.UNIT_Y / 2);
        renderer = new Renderer(ImagePath.CATMAN, characterIndex, steps, 3);
        this.renderer.setStepDelay(3);
        this.tileID = tileID;
        this.characterIndex = characterIndex;
        this.x = x;
        this.y = y;
        setAbility(characterIndex);
        this.isLeft = isLeft;

        this.moveDelay = new Delay(delayFrame);
        this.dir = 0;
        this.isStand = true;//一開始是站著的
        this.isAlive = true;
        this.bombCount = 0;//炸彈放置的數量

        this.bomb2 = new LinkedList<>();

        //消失特效
        this.disapperEffects = ImageResourceController.getInstance().tryGeyImage(ImagePath.DISAPPER);
        this.disapperDelay = new Delay(3);
        this.disapperIndex = 0;
        this.moveStep = 0;
        this.disapperDelay2 = new Delay(2);

        this.restartStep = 0;
        this.win = ImageResourceController.getInstance().tryGeyImage(ImagePath.WIN);
        this.lose = ImageResourceController.getInstance().tryGeyImage(ImagePath.LOSE);

        this.timeup = ImageResourceController.getInstance().tryGeyImage(ImagePath.TIMEUP);
        dieAudio = Applet.newAudioClip(getClass().getResource(AudioPath.DIE));

    }

    public void setAbility(int abilityIndex) {
        switch (abilityIndex) {
            case 0:
                this.ability = CatMan.ABILITY_0;
                break;
            case 1:
                this.ability = CatMan.ABILITY_1;
                break;
            case 2:
                this.ability = CatMan.ABILITY_2;
                break;
            case 3:
                this.ability = CatMan.ABILITY_3;
                break;
            case 4:
                this.ability = CatMan.ABILITY_4;
                break;
        }
        this.intialBombTotal = this.ability[1];
        this.intialFireTotal = this.ability[2];
        this.intialSpeedTotal = this.ability[3];
        this.heartTotal = this.ability[0];
        this.bombTotal = this.intialBombTotal;
        this.fireTotal = this.intialFireTotal;
        this.speedTotal = this.intialSpeedTotal;
    }

    public boolean getIsAlive() {
        return this.isAlive;
    }

    public void addTask1Score() {
        this.task1_Score++;

    }

    public void addTask2Score() {
        this.task2_Score++;
    }

    public int getTask1Score() {
        return this.task1_Score;
    }

    public int getTask2Score() {
        return this.task2_Score;
    }

    public boolean getIsStand() {
        return this.isStand;
    }

    public void setHeartTotal(int heart) {
        this.heartTotal += heart;
    }

    public void addBombTotal() {
        if (this.bombTotal < 5) {
            this.bombTotal++;
        }
    }

    public void addFireTotal() {
        if (this.fireTotal < 5) {
            this.fireTotal++;
        }
    }

    public void addSpeedTotal() {
        if (this.speedTotal < 5) {
            this.speedTotal++;
        }
    }

    public Renderer getRenderer() {
        return this.renderer;
    }

    public int getBombCount() {
        return this.bombCount;
    }

    public LinkedList<Bomb2> getBomb() {
        return this.bomb2;
    }

    public Bomb2 getBomb(int index) {

        return this.bomb2.get(index);
    }

    public void putBomb() {

        if (this.bombCount < this.bombTotal && this.tileID[(this.getX() - 88) / 64][(this.getY() - 105) / 64] != -99) {
            if (this.bombCount == 0) {
                this.bomb2.add(new Bomb2(120 + 64 * ((this.getX() - 88) / 64), 137 + 64 * ((this.getY() - 105) / 64), this.tileID));
                this.bomb2.get(this.bombCount).setFireTotal(this.fireTotal);
                this.bombCount++;
            } else {
                boolean isPutted = false;
                for (int i = 0; i < this.bomb2.size(); i++) {
                    if (this.getTileX() == this.bomb2.get(i).getTileX() && this.getTileY() == this.bomb2.get(i).getTileY()) {
                        isPutted = true;
                    }
                }
                if (!isPutted) {
                    this.bomb2.add(new Bomb2(120 + 64 * ((this.getX() - 88) / 64), 137 + 64 * ((this.getY() - 105) / 64), this.tileID));
                    this.bomb2.get(this.bombCount).setFireTotal(this.fireTotal);
                    this.bombCount++;
                }
            }

        }
    }

    public int getHeartNumber() {
        return this.heartTotal;
    }

    public int getBombNumber() {
        return this.bombTotal;
    }

    public int getFireNumber() {
        return this.fireTotal;
    }

    public int getSpeedNumber() {
        return this.speedTotal;
    }

    public void setMoveDelay(int delay) {
        this.moveDelay = new Delay(delay);
    }

    public void setSteps(int[] steps) {
        this.renderer.setSteps(steps);
    }

    public void move() {
        int distance = this.moveDistance + this.speedTotal;
        switch (this.dir) {
            case Global.LEFT:
                left(distance);
                break;
            case Global.A:
                left(distance);
                break;
            case Global.RIGHT:
                right(distance);
                break;
            case Global.D:
                right(distance);
                break;
            case Global.DOWN:
                down(distance);
                break;
            case Global.S:
                down(distance);
                break;
            case Global.UP:
                up(distance);
                break;
            case Global.W:
                up(distance);
                break;
        }
        isBound();
    }

    public void moveBack() {
        int distance = this.moveDistance + this.speedTotal;
        switch (this.dir) {
            case Global.LEFT:
                right(distance);
                break;
            case Global.A:
                right(distance);
                break;
            case Global.RIGHT:
                left(distance);
                break;
            case Global.D:
                left(distance);
                break;
            case Global.DOWN:
                up(distance);

                break;
            case Global.S:
                up(distance);
                break;
            case Global.UP:
                down(distance);
                break;
            case Global.W:
                down(distance);
                break;
        }
        isBound();
    }

    public void moveBack(int dir) {

        int distance = this.moveDistance + this.speedTotal;
        switch (dir) {
            case Global.LEFT:
                right(distance);
                break;
            case Global.A:
                right(distance);
                break;
            case Global.RIGHT:
                left(distance);
                break;
            case Global.D:
                left(distance);
                break;
            case Global.DOWN:
                up(distance);
                break;
            case Global.S:
                up(distance);
                break;
            case Global.UP:
                down(distance);
                break;
            case Global.W:
                down(distance);
                break;
        }
        isBound();
    }

    private void right(int distance) {
        do {
            distance = (int) (distance * this.chaseSpeed);
            super.setX(super.getX() + distance);
        } while (distance != 0);
    }

    private void left(int distance) {
        do {
            distance = (int) (distance * this.chaseSpeed);
            super.setX(super.getX() - distance);
        } while (distance != 0);
    }

    private void down(int distance) {
        do {
            distance = (int) (distance * this.chaseSpeed);
            super.setY(super.getY() + distance);
        } while (distance != 0);
    }

    private void up(int distance) {
        do {
            distance = (int) (distance * this.chaseSpeed);
            super.setY(super.getY() - distance);
        } while (distance != 0);
    }

    public void isBound() {
        if (super.getX() > Global.totalWorldWidth + Global.forestWidth - Global.UNIT_X / 2) {
            super.setX(Global.totalWorldWidth + Global.forestWidth - Global.UNIT_X / 2);
        } else if (super.getX() < Global.forestWidth + 16) {
            super.setX(Global.forestWidth + 16);
        } else if (super.getY() > Global.totalWorldHeight + Global.forestHeight - Global.UNIT_Y / 2) {
            super.setY(Global.totalWorldHeight + Global.forestHeight - Global.UNIT_Y / 2);
        } else if (super.getY() < Global.forestHeight + 24) {
            super.setY(Global.forestHeight + 24);
        }
    }

    public void setStand(boolean stand) {
        this.isStand = stand;
        if (this.isStand) {
            this.moveDelay.pause();
        } else {
            this.moveDelay.start();
        }
    }

    private void moveUP() {
        this.setY(this.getY() - 1);
    }

    public boolean isOver(int task1TargetScore, int task2TargetScore, String counter) {
        switch (Global.CURRENT_LEVEL) {
            case 1:
                if (super.getX() > 1048 && this.task1_Score >= task1TargetScore) {
                    this.isOver = true;
                    this.isWin = true;
                    return true;
                }
                break;
            case 2:
                if (super.getTileX() == 17 && super.getTileY() == 1 && this.task1_Score >= task1TargetScore && this.task2_Score >= task2TargetScore) {
                    this.isOver = true;
                    this.isWin = true;
                    return true;
                }
                break;
            case 3:
                if (this.task1_Score >= task1TargetScore) {
                    this.isOver = true;
                    this.isWin = true;
                    return true;
                }
                break;
        }
        if (this.heartTotal <= 0) {
            this.isOver = true;
            this.isWin = false;
            return true;
        }
        if (counter.equals("00:00")) {
            this.isOver = true;
            this.isWin = false;
            this.paintTimeUp = true;
            return true;
        }
        return false;
    }

    public boolean isOverMultiPlayers(CatMan c2, String counter) {
        if (this.heartTotal <= 0 && c2.heartTotal > 0) {
            this.isWin = false;
            c2.isWin = true;
            return this.isOver = c2.isOver = true;

        } else if (c2.heartTotal <= 0 && this.heartTotal > 0) {
            c2.isWin = false;
            this.isWin = true;
            return this.isOver = c2.isOver = true;

        } else if (counter.equals("00:00")) {
            if (this.task1_Score > c2.task1_Score) {
                this.isWin = true;
                c2.isWin = false;
            } else if (this.task1_Score < c2.task1_Score) {
                this.isWin = false;
                c2.isWin = true;
            } else if (this.task1_Score == c2.task1_Score) {
                this.isWin = true;
                c2.isWin = true;
            }
            return this.isOver = c2.isOver = true;
        }
        return false;
    }

    private void isDie() {
        if (!this.isActive() && this.isAlive) {
            AudioResourceController.getInstance().play(AudioPath.DIE);//播放音效
            this.disapperDelay.start();
            this.renderer.setCharacterIndex(5);
            this.renderer.pause();
            this.renderer.setCurrentStep(1);//死掉後變成中間的狀態
            this.renderer.setDir(0);

            this.setStand(true);
            this.setHeartTotal(-1);
            this.isAlive = false;
        }
        if (this.disapperDelay.isTrig()) {
            this.disapperIndex++;
            if (this.disapperIndex == 4) {
                this.disapperDelay.stop();
            }
        }
        if (this.disapperIndex == 4) {
            this.disapperDelay2.start();
            if (this.disapperDelay2.isTrig() && this.moveStep < 20) {
                this.moveStep++;
                if (this.moveStep % 2 == 0) {
                    this.renderer.setWidthHeight(0, 0);
                } else {
                    this.renderer.setWidthHeight(Global.UNIT_X, Global.UNIT_Y);
                }
                moveUP();
            }
        }
    }

    private void resetCatman() {
        if (!this.isActive() && this.moveStep == 20) {

            this.renderer.setWidthHeight(Global.UNIT_X, Global.UNIT_Y);
            this.renderer.setCharacterIndex(this.characterIndex);
            this.renderer.setSteps(Renderer.NORMAL_STEPS);
            this.renderer.start();
            this.setX(this.x);
            this.setY(this.y);
            this.bombTotal = this.intialBombTotal;
            this.fireTotal = this.intialFireTotal;
            this.speedTotal = this.intialSpeedTotal;
        }

        if (this.moveStep == 20) {

            if (this.disapperDelay2.isTrig() && this.restartStep < 20) {
                this.restartStep++;
                if (this.restartStep % 2 == 0) {
                    this.renderer.setWidthHeight(Global.UNIT_X, Global.UNIT_Y);
                } else {
                    this.renderer.setWidthHeight(0, 0);
                }

            }
            if (this.restartStep == 20) {
                this.setActive(true);
                this.isAlive = true;
                this.disapperDelay2.stop();
                this.moveStep = 0;
                this.disapperIndex = 0;
                this.restartStep = 0;
            }
        }
    }

    private void bombUpdate() {
        if (this.bombCount > 0) {
            for (int i = 0; i < this.bombCount; i++) {
                this.bomb2.get(i).update();
                if (this.bomb2.get(i).getBoom().get(0).getPictureIndex() == 4) {
                    for (int j = 0; j < this.bomb2.get(i).getBoom().size(); j++) {
                        this.bomb2.get(i).getBoom().remove(j);
                    }
                    this.tileID[this.bomb2.get(i).getTileX()][this.bomb2.get(i).getTileY()] = 0;
                    this.bomb2.remove(i);
                    this.bombCount--;
                }
            }
        }
    }

    @Override
    public void update() {

        bombUpdate();
        this.renderer.update();
        if (!isStand) {
            move();
        }

        if (isOver) {
            this.appearState++;
            if (this.paintState != 120) {
                this.move += 15;
                if (this.move >= 300) {
                    this.move = 300;
                    this.paintState++;
                }
            }
        }

        for (int i = 0; i < this.bomb2.size(); i++) {
            if (((this.bomb2.get(i).getX() - 120) / 64 != (this.getX() - 88) / 64) || ((this.bomb2.get(i).getY() - 137) / 64 != (this.getY() - 105) / 64)) {
                this.bomb2.get(i).setBombStayWithCatMan(false);
            }
        }
        isDie();
        resetCatman();
    }

    @Override

    public void paintComponent(Graphics g, Camera camera, int startX) {
        if (this.disapperDelay.getIsPause()) {
            this.renderer.paint(g, getLeft() + startX - camera.getCameraX(), getTop() - camera.getCameraY(), super.getWidth(), super.getHeight());
        } else {
            g.drawImage(this.disapperEffects, this.getX() + startX - camera.getCameraX() - 50, this.getY() - camera.getCameraY() - 48, this.getX() + startX - camera.getCameraX() + 50, this.getY() - camera.getCameraY() + 48,
                    this.disapperIndex * 100, 0, 100 * (1 + this.disapperIndex), 96, null);
        }

        if (isOver) {
            pWinState(g, startX);
        }

    }

    public void pWinState(Graphics g, int startX) {
        if (isLeft && startX == 632) {
            return;
        } else if (!isLeft && startX == 0) {
            return;
        }

        if (this.isWin && this.appearState < 120) {
            if (this.paintState != 120) {
                //youWin
                g.setColor(new Color(255, 255, 255, 150));
                if (!Global.IS_MUlTIPLAYERS) {
                    g.fillRect(0, 0, Global.SCREEN_X, Global.SCREEN_Y);
                    g.drawImage(this.win, Global.SCREEN_X / 2 - 300, this.move, null);
                } else {
                    if (startX == 0) {
                        startX = 88;
                    }
                    g.fillRect(0 + startX, 0, Global.SCREEN_X / 2, Global.SCREEN_Y);
                    g.drawImage(this.win, startX + 100, this.move, startX + 100 + 350, this.move + 150, 0, 0, 550, 150, null);
                }
            }
        } else if (!this.isWin && this.appearState < 120) {
            //youLose
            g.setColor(new Color(255, 255, 255, 150));
            if (!Global.IS_MUlTIPLAYERS) {
                g.fillRect(0, 0, Global.SCREEN_X, Global.SCREEN_Y);
                g.drawImage(this.lose, Global.SCREEN_X / 2 - 300, this.move, null);
            } else {
                if (startX == 0) {
                    startX = 88;
                }
                g.fillRect(0 + startX, 0, Global.SCREEN_X / 2, Global.SCREEN_Y);
                g.drawImage(this.lose, startX + 100, this.move, startX + 100 + 350, this.move + 150, 0, 0, 550, 150, null);
            }
        }
        if (paintTimeUp && this.appearState < 120) {
            g.drawImage(this.timeup, 250, this.move - 200, null);
        }

    }

    public void paintComponent(Graphics g) {//for startScene
        this.renderer.paint(g, getLeft(), getTop(), super.getWidth(), super.getHeight());
    }

    public void setDir(int dir) {
        this.dir = dir;
        this.renderer.setDir(dir);
    }

    public int getDir() {
        return this.dir % 4;
    }

    public boolean getIsWin() {
        return this.isWin;
    }

}
