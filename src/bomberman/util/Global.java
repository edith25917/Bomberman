package bomberman.util;

import bomberman.tile.Tile;
import bomberman.tile.World;

public class Global {

    // Debug Mode
    public static final boolean IS_DEBUG = false;

    public static void log(String str) {
        if (IS_DEBUG) {
            System.out.println(str);
        }
    } 

    //current level
    public static int CURRENT_LEVEL=0;
    //single / double mode
    public static boolean IS_MUlTIPLAYERS = false;

    //tree background
    public static final int forestWidth = 88;
    public static final int forestHeight = 105;

    // frame size
    public static final int FRAME_X = 1216;
    public static final int FRAME_Y = 889;
    public static final int SCREEN_X = FRAME_X - 8 - 8;//1200
    public static final int SCREEN_Y = FRAME_Y - 31 - 8;//850
    public static int totalWorldWidth = 1536;//=world.width*tileWidth
    public static int totalWorldHeight = 960;//=world.height*tileHeight

    // data refresh time
    public static final int UPDATE_TIMES_PER_SEC = 60;// 每秒更新60次遊戲邏輯
    public static final int NANO_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC;// 每一次要花費的毫秒數
    // frame refresh time
    public static final int FRAME_LIMIT = 60;
    public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT;
    // game unit size
    public static final int UNIT_X = 64;//要畫的大小
    public static final int UNIT_Y = 64;

    // direction keys
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int S = 4;
    public static final int A = 5;
    public static final int D = 6;
    public static final int W = 7;
    //bomb keys
    public static final int SPACE = -1;
    public static final int M = -2;
    public static final int V = -3;

    //main menu key
    public static final int ENTER = 98;
    


    //set map size
    public static void setTotalWorldWidthHeight(int width, int height) {
        Global.totalWorldWidth = width;
        Global.totalWorldHeight = height;
    }


    public static boolean random(int rate) {
        return random(1, 100) <= rate;
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

}
