package bomberman.tile;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.gameobj.GameObject;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile extends GameObject {

    //STATE　STUFF HERE
    public static Tile[] tiles = new Tile[3];
    private final int id = 99;
    //private id type;
    protected BufferedImage img;

    public Tile(int x, int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, true);

    }

    public int getId() {
        return this.id;
    }

    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean isCollision(GameObject gameobject) {
        if (!isSolid()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g, Camera camera, int startX) {
        g.drawImage(this.img, this.getLeft() + startX - camera.getCameraX(), this.getTop() - camera.getCameraY(), Global.UNIT_X, Global.UNIT_Y, null);

    }

}
