/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.gameobj;

import bomberman.camera.Camera;
import bomberman.graph.Rect;
import bomberman.util.Global;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Edith
 */
public abstract class GameObject {

    private Rect collider;
    private Rect rect;
    private boolean active = true;

    public GameObject(int x, int y, int width, int height, int colliderWidth, int colliderHeight) {
        this.rect = Rect.genWithCenter(x, y, width, height);
        this.collider = Rect.genWithCenter(x, y, colliderWidth, colliderHeight);
    }

    public GameObject(int x, int y, int width, int height, boolean isBindCollider) {
        this.rect = Rect.genWithCenter(x, y, width, height);
        if (isBindCollider) {
            this.collider = this.rect;
        } else {
            this.collider = new Rect(x, y, width, height);
        }
    }

    public boolean isCollision(GameObject obj) {
        if (this.collider == null || obj.collider == null) {
            return false;
        }
        return Rect.intersects(this.collider, obj.collider);
    }
    
    public boolean isCollision(Rect a, int left,int right,int top,int bottom){
        if(a==null){
            return false;
        }
        return Rect.intersects(a, left, top, right, bottom);
    }

    public void offset(int dx, int dy) {
        this.rect.offset(dx, dy);
        this.collider.offset(dx, dy);
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public boolean isActive() {
        return active;
    }

    public int getLeft() {
        return this.rect.left();
    }

    public int getRight() {
        return this.rect.right();
    }

    public int getTop() {
        return this.rect.top();
    }

    public int getBottom() {
        return this.rect.bottom();
    }

    public int getX() {
        return this.rect.centerX();
    }

    public int getY() {
        return this.rect.centerY();
    }

    public int getTileX(int x) {
        return (x - 88) / 64;
    }

    public int getTileY(int y) {
        return (y - 105) / 64;
    }
    public int getWidth() {
        return this.rect.width();
    }

    public int getHeight() {
        return this.rect.height();
    }

    public int getTileX() {
        return (this.getX() - 88) / 64;
    }

    public int getTileY() {
        return (this.getY() - 89) / 64;
    }

    public int convertTileXtoX(int TileX) {
        return TileX * 64 + 88;
    }

    public int convertTileYtoY(int TileY) {
        return TileY * 64 + 89;
    }

    public void setX(int x) {
        this.collider.offset(x - this.rect.centerX(), 0);
        this.rect.offset(x - this.rect.centerX(), 0);
    }

    public void setY(int y) {
        this.collider.offset(0, y - this.rect.centerY());
        this.rect.offset(0, y - this.rect.centerY());
    }

    public Rect getCollider() {
        return this.collider;
    }

    public Rect getRect() {
        return this.rect;
    }

    public void paint(Graphics g, Camera camera,int startX) {
        paintComponent(g, camera,startX);
        if (Global.IS_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect(this.rect.left()+startX - camera.getCameraX(), this.rect.top() - camera.getCameraY(), this.rect.width(), this.rect.height());
            g.setColor(Color.BLUE);
            g.drawRect(this.collider.left()+startX - camera.getCameraX(), this.collider.top()  - camera.getCameraY(), this.collider.width(), this.collider.height());
            g.setColor(Color.BLACK);
        }
    }

    public abstract void update();

    public abstract void paintComponent(Graphics g, Camera camera,int startX);
}
