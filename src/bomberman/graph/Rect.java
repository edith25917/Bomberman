/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.graph;

/**
 *
 * @author Edith
 */
public class Rect {

    private int left;
    private int top;
    private int right;
    private int bottom;

    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public static Rect genWithCenter(int x, int y, int width, int height) {
        int left = x - width / 2;
        int right = left + width;
        int top = y - height / 2;
        int bottom = top + height;
        return new Rect(left, top, right, bottom);
    }

    public void resetCenter(int x, int y, int width, int height) {
        this.left = x - width / 2;
        this.right = this.left + width;
        this.top = y - height / 2;
        this.bottom = this.top + height;
    }

    public boolean intersects(int left, int top, int right, int bottom) {
        if (this.left() > right) {
            return false;
        }
        if (this.right() < left) {
            return false;
        }
        if (this.top() > bottom) {
            return false;
        }
        if (this.bottom() < top) {
            return false;
        }
        return true;
    }

    public static boolean intersects(Rect a, Rect b) {
        return a.intersects(b.left, b.top, b.right, b.bottom);
    }

    public static boolean intersects(Rect a, int left, int top, int right, int bottom) {
        return a.intersects(left, top, right, bottom);
    }

    public int centerX() {
        return (left + right) / 2;
    }

    public int centerY() {
        return (top + bottom) / 2;
    }

    public float exactCenterX() {
        return (left + right) / 2f;
    }

    public float exactCenterY() {
        return (top + bottom) / 2f;
    }

    public void offset(int dx, int dy) {
        this.left += dx;
        this.right += dx;
        this.top += dy;
        this.bottom += dy;
    }

    public int left() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int top() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int right() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int bottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int width() {
        return this.right - this.left;
    }

    public int height() {
        return this.bottom - this.top;
    }

}
