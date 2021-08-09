/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.monsters;

/**
 *
 * @author Edith
 */
public class Node {

    public int x;
    public int y;

    public int F;
    public int G;
    public int H;
    private Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void calcF() {
        this.F = this.G + this.H;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node tmp) {
        this.parent = tmp;
    }

    public int getG() {
        return this.G;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
