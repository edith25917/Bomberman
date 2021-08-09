package bomberman.monsters;

import bomberman.controllers.ImageResourceController;

import bomberman.camera.Camera;

import bomberman.gameobj.GameObject;
import bomberman.gameobj.Renderer;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Monster extends GameObject {

    private Renderer renderer;
    private Delay moveDelay;
    private Delay dirDelay;
    private int dir;
    private boolean isStand;
    private boolean stopMove;
    private int speed;
    private int tileID[][];
    //鎖定對象
    private int range;
    private boolean findTarget = false;
    private double chaseSpeed = 0.7;
    private int moveDistance;//6是怪物未鎖定目標的移動距離, 9是找到鎖定目標的移動距離
    //消失特效
    private BufferedImage disapperEffects;
    private Delay disapperDelay;
    private int disapperIndex;
    private boolean isCollisionMonster;

    public Monster(int characterIndex, int[] steps, int delayFrame, int x, int y, int moveDistance, int[][] tileID, int range) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y, Global.UNIT_X - 32, Global.UNIT_Y - 32);
        this.getCollider().resetCenter(x, y + Global.UNIT_Y / 4, Global.UNIT_X - 32, Global.UNIT_Y - 32);
        this.dir = 0;
        this.renderer = new Renderer(ImagePath.BOMBERMAN, characterIndex, steps, delayFrame);
        this.renderer.setStepDelay(1);
        this.stopMove = false;
        this.isStand = false;
        this.moveDistance = moveDistance;
        this.range = range;
        this.moveDelay = new Delay(1);
        this.moveDelay.start();
        this.dirDelay = new Delay(Global.random(60 - Global.CURRENT_LEVEL * 10, 30));
        this.dirDelay.start();

        this.disapperEffects = ImageResourceController.getInstance().tryGeyImage(ImagePath.DISAPPER);
        this.disapperDelay = new Delay(2);
        this.disapperIndex = 0;
        this.tileID = tileID;
        this.isCollisionMonster = false;
    }

    public boolean getIsCollisionMonster() {
        return this.isCollisionMonster;
    }

    public void setIsCollisionMonster() {
        this.isCollisionMonster = true;
    }

    public Delay getDisapperDelay() {
        return this.disapperDelay;
    }

    public int getDisapperIndex() {
        return this.disapperIndex;
    }

    public Renderer getRenderer() {
        return this.renderer;
    }

    public void setSteps(int[] steps) {
        this.renderer.setSteps(steps);
    }

    public void stopDirDelay() {
        this.dirDelay.stop();
    }

    public void setStopMove(boolean stopMove) {
        this.stopMove = stopMove;

    }

    public void setStand(boolean stand) {
        this.isStand = stand;
        if (this.isStand) {
            this.moveDelay.pause();
        } else {
            this.moveDelay.start();
        }
    }

    public void setDir(int dir) {
        this.dir = dir;
        this.renderer.setDir(dir);
    }

    public int getDir() {
        return this.dir;
    }

    public void move() {
        int distance = this.moveDistance;
        switch (this.dir) {
            case Global.LEFT:
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setX(super.getX() - distance);
                } while (distance != 0);
                isBound();
                break;
            case Global.RIGHT:
              
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setX(super.getX() + distance);
                } while (distance != 0);
                isBound();
                break;
            case Global.DOWN:
            
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setY(super.getY() + distance);
                } while (distance != 0);
                isBound();
                break;
            case Global.UP:
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setY(super.getY() - distance);
                } while (distance != 0);
                isBound();
                break;
        }

    }

    public void moveBack() {
           int distance = this.moveDistance;
        switch (this.dir) {
            case Global.LEFT:
             
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setX(super.getX() + distance );
                } while (distance != 0);
                isBound();
                break;
            case Global.RIGHT:
               
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setX(super.getX() - distance );
                } while (distance != 0);
                isBound();
                break;
            case Global.DOWN:
         
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setY(super.getY() - distance );
                } while (distance != 0);
                isBound();
                break;
            case Global.UP:
       
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setY(super.getY() + distance );
                } while (distance != 0);
                isBound();
                break;
        }
    }

    public void moveBack(int dir) {
          int distance = this.moveDistance;
        switch (dir) {
            case Global.LEFT:
              
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setX(super.getX() + distance );
                } while (distance != 0);
                isBound();
                break;
            case Global.RIGHT:
              
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setX(super.getX() - distance );
                } while (distance != 0);
                isBound();
                break;
            case Global.DOWN:
             
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setY(super.getY() - distance );
                } while (distance != 0);
                isBound();
                break;
            case Global.UP:
            
                do {
                    distance = (int) (distance * this.chaseSpeed);
                    super.setY(super.getY() + distance );
                } while (distance != 0);
                isBound();
                break;
        }
    }

    public void isBound() {
        if (super.getX() > Global.totalWorldWidth + Global.forestWidth - Global.UNIT_X / 2) {
            super.setX(Global.totalWorldWidth + Global.forestWidth - Global.UNIT_X / 2);
        } else if (super.getX() < Global.forestWidth + 16) {
            super.setX(Global.forestWidth + 16);
        } else if (super.getY() > Global.totalWorldHeight + Global.forestHeight - Global.UNIT_Y / 2) {
            super.setY(Global.totalWorldHeight + Global.forestHeight - Global.UNIT_Y / 2);
        } else if (super.getY() < Global.forestHeight + 16) {
            super.setY(Global.forestHeight + 16);
        }
    }

    //鎖定目標
    public boolean isInRange(GameObject e) {
        int distanceX = e.getX() - super.getX();//人跟怪物的距離
        int distanceY = e.getY() - super.getY();
        int n = Global.random(0, 1);
        if (Math.sqrt(Math.pow(e.getX() - super.getX(), 2) + Math.pow(e.getY() - super.getY(), 2)) <= this.range) {
            this.findTarget = true;
            findRoute(e);
            return true;
        }
        this.findTarget = false;
        return false;
    }

    public void findRoute(GameObject e) {
        AStar aStar = new AStar(this.tileID);
        Node startNode = new Node(this.getTileX(), this.getTileY());//怪物
        Node endNode = new Node(e.getTileX(), e.getTileY());//target
        Node parent = new AStar(this.tileID).findPath(startNode, endNode);
        ArrayList<Node> routeArr = new ArrayList<Node>();
        while (parent != null) {
            routeArr.add(new Node(parent.x, parent.y));
            parent = parent.getParent();
        }

        if (Global.IS_DEBUG) {
            System.out.println("-----------路線start-------------");
            for (int i = routeArr.size() - 1; i >= 0; i--) {
                System.out.println(routeArr.get(i).x + " ," + routeArr.get(i).y);
            }
            System.out.println("-----------end-------------");
        }

        for (int i = routeArr.size() - 1; i >= 0; i--) {
            if (routeArr.get(i).y == super.getTileY() && convertTileXtoX(routeArr.get(i).x) < super.getX()) {//如果要去的路y相等，就移動x
                if ((super.getY() - 89) % 64 < 24) {
              
                    setDir(Global.DOWN);
                } else if ((super.getY() - 89) % 64 > 36) {
              
                    setDir(Global.UP);

                } else {
           
                    setDir(Global.LEFT);
                }
            } else if (routeArr.get(i).y == super.getTileY() && convertTileXtoX(routeArr.get(i).x) > super.getX()) {
                if ((super.getY() - 89) % 64 < 24) {
            
                    setDir(Global.DOWN);
                } else if ((super.getY() - 89) % 64 > 36) {
                  
                    setDir(Global.UP);
                } else {
                   
                    setDir(Global.RIGHT);
                }
            } else if (routeArr.get(i).x == super.getTileX() && convertTileYtoY(routeArr.get(i).y) < super.getY()) {//如果要去的路x相等，就移動y
                if ((super.getX() - 88) % 64 < 16) {
                 
                    setDir(Global.RIGHT);
                } else if ((super.getX() - 88) % 64 > 48) {
                   
                    setDir(Global.LEFT);
                } else {
                   
                    setDir(Global.UP);
                }
            } else if (routeArr.get(i).x == super.getTileX() && convertTileYtoY(routeArr.get(i).y) > super.getY()) {
                if ((super.getX() - 88) % 64 < 16) {
                   
                    setDir(Global.RIGHT);
                } else if ((super.getX() - 88) % 64 > 48) {
                  
                    setDir(Global.LEFT);
                } else {
                   
                    setDir(Global.DOWN);
                }
            }
        }
    }

    public boolean checkfindTarget() {
        return findTarget;
    }

    public void setfindTarget(boolean findTarget) {
        this.findTarget = findTarget;
    }

    @Override
    public void update() {
        this.renderer.update();
//        this.setStand(false);
        if (!findTarget && this.dirDelay.isTrig()) {
            this.dir = Global.random(0, 3);
            this.renderer.setDir(this.dir);
        }
        if ( !isStand&&this.moveDelay.isTrig()) {
            move();
        }
        if (this.disapperDelay.isTrig()) {
            this.disapperIndex++;
            if (this.disapperIndex == 4) {
                this.disapperDelay.stop();
            }
        }
    }

    @Override

    public void paintComponent(Graphics g, Camera camera, int startX) {
        if (!this.disapperDelay.getIsPause()) {
            g.drawImage(this.disapperEffects, this.getX() + startX - camera.getCameraX() - 50, this.getY() - camera.getCameraY() - 48, this.getX() + startX - camera.getCameraX() + 50, this.getY() - camera.getCameraY() + 48,
                    this.disapperIndex * 100, 0, 100 * (1 + this.disapperIndex), 96, null);
        } else {
            this.renderer.paint(g, getLeft() + startX - camera.getCameraX(), getTop() - camera.getCameraY(), super.getWidth(), super.getHeight());
        }

    }

}
