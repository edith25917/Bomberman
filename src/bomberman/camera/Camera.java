/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.camera;

import bomberman.gameobj.GameObject;
import bomberman.tile.Tile;
import bomberman.tile.World;
import bomberman.util.Delay;
import bomberman.util.Global;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Edith
 */
public class Camera {
    //記得所有物件在paint的時候都要減camera的xOffset和yOffset，物件才會跟著鏡頭移動，可參考bomberMan paint的寫法

    private int visionState;  //(0)單一鏡頭(1)左鏡頭(2)右鏡頭(3)startscene鏡頭
    //地圖範圍
    private int mapLeft;
    private int mapTop;
    private int mapBottom;
    private int mapRight;

    private int x = 0;
    private int y = 0;
    private int width;
    private int height;
    private int targetX;
    private int targetY;
    //相機範圍
    private int left;
    private int right;
    private int top;
    private int bottom;
    private int startX;
    private int totalMapWidth;
    private int totalMapHeight;
    private Delay cameraDelay;

    public Camera(int width, int height, int state, int startX, int totalMapWidth, int totalMapHeight) {
        this.width = width;
        this.height = height;
        this.visionState = state;
        this.startX = startX;
        cameraDelay = new Delay(1);//要跟catman的delayFrame一樣
        cameraDelay.start();
        this.totalMapWidth = totalMapWidth;
        this.totalMapHeight = totalMapHeight;
        initBound();
    }

    public void initBound() {
        if (this.visionState == 0) //單一
        {
            this.mapLeft = 0;
            this.mapRight = this.totalMapWidth;
            this.mapTop = 0;
            this.mapBottom = this.totalMapHeight;
        } else if (this.visionState == 1) //左
        {
            this.mapLeft = 0;
            this.mapRight = this.totalMapWidth;
            this.mapTop = 0;
            this.mapBottom = this.totalMapHeight;
        } else if (this.visionState == 2)//右
        {
            this.mapLeft = 88;
            this.mapRight = 88 + this.totalMapWidth;
            this.mapTop = 0;
            this.mapBottom = this.totalMapHeight;
        } else if (this.visionState == 3) //startScene
        {
            this.mapLeft = 0;
            this.mapRight = this.totalMapWidth;
            this.mapTop = 0;
            this.mapBottom = this.totalMapHeight;
        }

    }

    public boolean isInCamera(GameObject e) {
        if (e.isCollision(e.getRect(), this.left, this.right, this.top, this.bottom)) {
            return true;
        }
        return false;
    }

    public void checkBounds() {
        if (this.x < mapLeft) {
            this.x = mapLeft;
        }
        if (x >= mapRight - width) {
            this.x = mapRight - width;
        }
        if (this.y < mapTop) {
            this.y = mapTop;
        }
        if (y >= mapBottom - height) {
            this.y = mapBottom - height;
        }
    }

    private void setCameraRange() {
        if (this.visionState == 0 || this.visionState == 1) {
            this.left = this.x + 88;
            this.right = this.left + this.width;
            this.top = this.y + 105;
            this.bottom = this.top + this.height;
        } else if (this.visionState == 2) {
            this.left = this.x;
            this.right = this.left + this.width;
            this.top = this.y + 105;
            this.bottom = this.top + this.height;
        }
    }

    public void centerOnObj(GameObject obj) {
        this.targetX = obj.getX() - this.width / 2;
        this.targetY = obj.getY() - this.height / 2;

    }

    public void targetMove() {
        this.x += (this.targetX - this.x) / 7;
        this.y += (this.targetY - this.y) / 7;
    }

    public void update() {

        targetMove();
        checkBounds();
        setCameraRange();
    }

    public int getCameraX() {
        return this.x;
    }

    public int getCameraY() {
        return this.y;
    }

    public void setCameraX(int x) {
        this.x = x;
    }

    public int getTargetX() {
        return this.targetX;
    }

    public void paint(Graphics g, GameObject obj) {
        if (isInCamera(obj)) {
            obj.paint(g, this, this.startX);
        }
    }

}
