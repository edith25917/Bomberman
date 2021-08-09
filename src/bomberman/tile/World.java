package bomberman.tile;

import bomberman.background.BackGround;
import bomberman.camera.Camera;
import bomberman.controllers.AudioResourceController;
import bomberman.controllers.ImageResourceController;
import bomberman.gameobj.Castle;
import bomberman.gameobj.CatMan;
import bomberman.gameobj.GameObject;
import bomberman.monsters.Monster;
import bomberman.gameobj.Renderer;
import bomberman.gameobj.Task;
import bomberman.gameobj.TaskBar;
import bomberman.gameobj.Treasure;
import bomberman.gameobj.UserInterface;
import bomberman.gameobj.WoodBridge;
import bomberman.graph.Rect;
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class World {

    private int clockTime;
    private Delay testDelay;
    private int width, height;//二維陣列長度
    private int spawnX, spawnY;//左上角一開始產生的位置
    private int[][] tileID;
    private ArrayList<Tile> tiles;
    private ArrayList<Tree> trees;
    private ArrayList<Stone> stones;
    private ArrayList<Box> boxes;
    private ArrayList<Barrel> barrels;
    private ArrayList<Monster> monsters;
    private ArrayList<Treasure> treasures;
    private int treasureTotal;
    private ArrayList<Task> task;
    private Task task_lv2;//鑰匙只有一支
    private String[] task_lv3;//第三關金幣的圖片
    private int task_lv3_count;
    private int taskTotal;
    private ArrayList<River> rivers;
    private CatMan catman;
    private Camera camera;
    private BackGround backgroundUp;
    private UserInterface ui;
    private UserInterface ui2;
    private TaskBar taskbar;
    private Delay sceneDelay;
    private WoodBridge woodbridge;
    private Castle castle;
    private int level;

    //音效
    private AudioClip backGroundAudio;
    private AudioClip passAudio;
    private boolean passState;
    //gameover
    private GameOver gameOver;
    private int levelPassState;

//雙人模式
    private CatMan catman2;
    private Camera camera2;
    private TaskBar taskbar2;
    private boolean isCollisionMonster = false;
    private int taskIndex;

    public World(int level, String path, int characterIndex1P, int characterIndex2P) {
        this.taskIndex = Global.random(1, 3);
        passAudio = Applet.newAudioClip(getClass().getResource(AudioPath.PASS));
        this.testDelay = new Delay(120);
        this.testDelay.start();
        this.trees = new ArrayList<>();
        this.stones = new ArrayList<>();
        this.boxes = new ArrayList<>();
        this.barrels = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.tiles = new ArrayList<>();
        this.treasures = new ArrayList<>();
        this.task = new ArrayList<>();
        this.rivers = new ArrayList<>();
        this.sceneDelay = new Delay(1);
        sceneDelay.start();
        gameOver = new GameOver();
        this.level = level;
        if (this.level == 4) {
            this.clockTime = 2;
        } else {
            this.clockTime = level;//計時器時間
        }
        initLevel(level, path, characterIndex1P, characterIndex2P);
        this.treasureTotal = 0;
        setBackGroundAudio();
        this.passState = false;

    }

    private void setBackGroundAudio() {

        switch (Global.CURRENT_LEVEL) {
            case 1:
                backGroundAudio = Applet.newAudioClip(getClass().getResource(AudioPath.LV1));
                break;
            case 2:
                backGroundAudio = Applet.newAudioClip(getClass().getResource(AudioPath.LV2));
                break;
            case 3:
                backGroundAudio = Applet.newAudioClip(getClass().getResource(AudioPath.LV3));
                break;
            case 4:
                backGroundAudio = Applet.newAudioClip(getClass().getResource(AudioPath.LV4));
        }
        backGroundAudio.loop();
    }

    private void setTaskLv3() {
        this.task_lv3[0] = ImagePath.COIN_1;
        this.task_lv3[1] = ImagePath.COIN_2;
        this.task_lv3[2] = ImagePath.COIN_3;
        this.task_lv3[3] = ImagePath.COIN_4;
        this.task_lv3[4] = ImagePath.COIN_5;
        this.task_lv3[5] = ImagePath.COIN_6;
        this.task_lv3[6] = ImagePath.COIN_7;
        this.task_lv3[7] = ImagePath.COIN_8;

        for (int i = 0; i < 8; i++) {
            int random = Global.random(0, 7);
            int random2 = Global.random(0, 7);
            String tmp = this.task_lv3[random];
            this.task_lv3[random] = this.task_lv3[random2];
            this.task_lv3[random2] = tmp;
        }
    }

    public void initLevel(int level, String path, int characterIndex1P, int characterIndex2P) {
        switch (level) {
            case 1:
                Global.CURRENT_LEVEL = 1;
                loadWorld(path);
                this.taskbar = new TaskBar(1);//過關條件(imagepath)和數量int 自己設定
                //單人
                this.catman = new CatMan(characterIndex1P, Renderer.UNMOVED_STEPS, 1, 248, 265, this.tileID,true);
                this.ui = new UserInterface(clockTime, this.catman.getHeartNumber(), this.catman.getBombNumber(), this.catman.getFireNumber(), this.catman.getSpeedNumber());
                this.backgroundUp = new BackGround(ImagePath.LV1_MAP, 0, 0);//樹
                this.woodbridge = new WoodBridge(986, 446);
                initCamera();
                Global.setTotalWorldWidthHeight(this.width * 64, this.height * 64);
                break;
            case 2:
                Global.CURRENT_LEVEL = 2;
                loadWorld(path);
                this.taskbar = new TaskBar(2);
                //單人
                this.catman = new CatMan(characterIndex1P, Renderer.UNMOVED_STEPS, 1, 248, 265, this.tileID,true);
                this.ui = new UserInterface(clockTime, this.catman.getHeartNumber(), this.catman.getBombNumber(), this.catman.getFireNumber(), this.catman.getSpeedNumber());
                this.backgroundUp = new BackGround(ImagePath.LV2_MAP, 0, 0);//要改成第二關的外圍
                initCamera();
                Global.setTotalWorldWidthHeight(this.width * 64, this.height * 64);
                this.woodbridge = new WoodBridge(664, 385);
                this.castle = new Castle(1208, 169);
                for (int i = 0; i < this.stones.size(); i++) {
                    if (this.stones.get(i).getTileX() == 8 && this.stones.get(i).getTileY() == 4) {
                        this.stones.remove(i);
                        i--;
                    }
                    if (this.stones.get(i).getTileX() == 9 && this.stones.get(i).getTileY() == 4) {
                        this.stones.remove(i);
                        i--;
                    }
                }
                break;
            case 3:
                Global.CURRENT_LEVEL = 3;
                loadWorld(path);
                this.taskbar = new TaskBar(3);
                //單人
                this.catman = new CatMan(characterIndex1P, Renderer.UNMOVED_STEPS, 1, 120, 137, this.tileID,true);
                this.ui = new UserInterface(clockTime, this.catman.getHeartNumber(), this.catman.getBombNumber(), this.catman.getFireNumber(), this.catman.getSpeedNumber());
                this.backgroundUp = new BackGround(ImagePath.LV3_MAP, 0, 0);
                this.catman = new CatMan(characterIndex1P, Renderer.UNMOVED_STEPS, 1, 248, 265, this.tileID,true);
                this.ui = new UserInterface(clockTime, this.catman.getHeartNumber(), this.catman.getBombNumber(), this.catman.getFireNumber(), this.catman.getSpeedNumber());
                this.backgroundUp = new BackGround(ImagePath.LV3_MAP, 0, 0);
                initCamera();
                Global.setTotalWorldWidthHeight(this.width * 64, this.height * 64);
                break;
            case 4:
                Global.CURRENT_LEVEL = 4;
                loadWorld(path);
                this.taskbar = new TaskBar(this.taskIndex);//過關條件(imagepath)和數量int 自己設定
                this.catman = new CatMan(characterIndex1P, Renderer.UNMOVED_STEPS, 1, 248, 265, this.tileID,true);
                this.ui = new UserInterface(clockTime, this.catman.getHeartNumber(), this.catman.getBombNumber(), this.catman.getFireNumber(), this.catman.getSpeedNumber());
                this.catman2 = new CatMan(characterIndex2P, Renderer.UNMOVED_STEPS, 1, 1272, 841, this.tileID,false);
                this.ui2 = new UserInterface(clockTime, this.catman2.getHeartNumber(), this.catman2.getBombNumber(), this.catman2.getFireNumber(), this.catman2.getSpeedNumber());
                this.backgroundUp = new BackGround(ImagePath.LV1_MAP_2P, 0, 0);//樹
                this.taskbar2 = new TaskBar(this.taskIndex);
                this.castle = new Castle(1144, 233);
                initCamera();
                Global.setTotalWorldWidthHeight(this.width * 64, this.height * 64);
                break;
        }
        loadGameObject(Global.CURRENT_LEVEL);
        initCamera();
        Global.setTotalWorldWidthHeight(this.width * 64, this.height * 64);
    }

    public void initCamera() {
        if (!Global.IS_MUlTIPLAYERS) {
            camera = new Camera(Global.SCREEN_X - 88 * 2, Global.SCREEN_Y - 105 * 2, 0, 0, this.width * Global.UNIT_X, this.height * Global.UNIT_Y);
        } else {
            camera = new Camera(Global.SCREEN_X / 2 - 120, Global.SCREEN_Y - 105 - 105, 1, 0, this.width * Global.UNIT_X, this.height * Global.UNIT_Y);
            camera2 = new Camera(Global.SCREEN_X / 2 - 120, Global.SCREEN_Y - 105 - 105, 2, 632, this.width * Global.UNIT_X, this.height * Global.UNIT_Y);
        }
    }

    private void loadGameObject(int level) {
        int barrelTotal = 0;
        int boxTotal = 0;
        int monsterTotal = 0;
        int treasureTotal = 0;
        int taskTotal = 0;
        switch (level) {
            case 1:
                barrelTotal = 10;
                boxTotal = 10;
                monsterTotal = 5;
                treasureTotal = 8;
                taskTotal = 5;
                break;
            case 2:
                barrelTotal = 20;
                boxTotal = 20;
                monsterTotal = 20;
                treasureTotal = 30;
                taskTotal = 1;
                break;
            case 3:
                this.task_lv3 = new String[8];
                this.task_lv3_count = 0;
                setTaskLv3();
                barrelTotal = 30;
                boxTotal = 40;
                monsterTotal = 20;
                treasureTotal = 10;
                taskTotal = 8;
                break;
            case 4:
                barrelTotal = 25;//25
                boxTotal = 25;//25
                monsterTotal = 20;//20
                treasureTotal = 40;
                taskTotal = 0;
                break;
        }
        while (this.barrels.size() < barrelTotal) {//改外面的數量裡面也要改
            Global.log("barrel done");
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    if (Global.CURRENT_LEVEL == 1) {
                        if ((x < 4 && y < 4) || x > 13) {

                        } else if (this.tileID[x][y] == 0 && Global.random(20) && this.barrels.size() < barrelTotal) {
                            this.barrels.add(new Barrel(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y));
                            this.tileID[x][y] = 1;
                        }
                    } else if (Global.CURRENT_LEVEL == 2) {
                        if (x < 4 && y < 4) {

                        } else if (this.tileID[x][y] == 0 && Global.random(20) && this.barrels.size() < barrelTotal) {
                            this.barrels.add(new Barrel(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y));
                            this.tileID[x][y] = 1;
                        }
                    } else if (Global.CURRENT_LEVEL == 3) {
                        if (x < 4 && x < y) {

                        } else if (this.tileID[x][y] == 0 && Global.random(20) && this.barrels.size() < barrelTotal) {
                            this.barrels.add(new Barrel(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y));
                            this.tileID[x][y] = 1;
                        }
                    } else if (Global.CURRENT_LEVEL == 4) {
                        if ((x < 4 && y < 4) || (x > 15 && y > 8)) {

                        } else if (this.tileID[x][y] == 0 && Global.random(20) && this.barrels.size() < barrelTotal) {
                            this.barrels.add(new Barrel(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y));
                            this.tileID[x][y] = 1;
                        }
                    }
                }
            }
        }

        while (this.boxes.size() < boxTotal) {//改外面的數量裡面也要改
            Global.log("box done");
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    if (Global.CURRENT_LEVEL == 1) {
                        if ((x < 4 && y < 4) || x > 13) {

                        } else if (this.tileID[x][y] == 0 && Global.random(20) && this.boxes.size() < boxTotal) {
                            this.boxes.add(new Box(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y));
                            this.tileID[x][y] = 1;
                        }
                    } else if (Global.CURRENT_LEVEL == 2) {
                        if (x < 4 && y < 4) {

                        } else if (this.tileID[x][y] == 0 && Global.random(20) && this.boxes.size() < boxTotal) {
                            this.boxes.add(new Box(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y));
                            this.tileID[x][y] = 1;
                        }
                    } else if (Global.CURRENT_LEVEL == 3) {
                        if (x < 4 && y < 4) {

                        } else if (this.tileID[x][y] == 0 && Global.random(20) && this.boxes.size() < boxTotal) {
                            this.boxes.add(new Box(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y));
                            this.tileID[x][y] = 1;
                        }
                    } else if (Global.CURRENT_LEVEL == 4) {
                        if ((x < 4 && y < 4) || (x > 15 && y > 8)) {

                        } else if (this.tileID[x][y] == 0 && Global.random(20) && this.boxes.size() < boxTotal) {
                            this.boxes.add(new Box(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y));
                            this.tileID[x][y] = 1;
                        }
                    }

                }
            }
        }

        while (this.monsters.size() < monsterTotal) {//改外面的數量裡面也要改
            Global.log("monsters done");
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {

                    if (Global.CURRENT_LEVEL == 1) {

                        if ((x < 6 && y < 6) || x > 13) {
                        } else if (this.monsters.size() < monsterTotal && this.tileID[x][y] == 0 && Global.random(20)) {
                            this.monsters.add(new Monster(Global.CURRENT_LEVEL - 1, Renderer.UNMOVED_STEPS, 1, this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y, 4, this.tileID, 130));
                        }
                    }
                    if (Global.CURRENT_LEVEL == 2) {
                        if (x < 6 && y < 6) {

                        } else if (this.monsters.size() < monsterTotal && this.tileID[x][y] == 0 && (x > 4 || y > 3) && Global.random(10)) {
                            this.monsters.add(new Monster(Global.CURRENT_LEVEL - 1, Renderer.UNMOVED_STEPS, 1, this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y, 5, this.tileID, Global.random(0, 250)));

                        }
                    }
                    if (Global.CURRENT_LEVEL == 3) {
                        if (x < 6 && y < 6) {

                        } else if (this.monsters.size() < monsterTotal && this.tileID[x][y] == 0 && Global.random(15)) {
                            this.monsters.add(new Monster(Global.CURRENT_LEVEL - 1, Renderer.UNMOVED_STEPS, 1, this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y, 6, this.tileID, Global.random(0, 300)));

                        }
                    }
                    if (Global.CURRENT_LEVEL == 4) {
                        if ((x < 6 && y <6 ) || (x > 13 && y > 6)) {

                        } else if (this.monsters.size() < monsterTotal && this.tileID[x][y] == 0 && Global.random(10)) {
                            this.monsters.add(new Monster(this.taskIndex - 1, Renderer.UNMOVED_STEPS, 1, this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y, 6, this.tileID, Global.random(0, 200)));
                        }
                    }
                }
            }
        }
        while (this.treasureTotal < treasureTotal / 2) {
            for (int i = 0; i < this.boxes.size(); i++) {
                if (Global.random(30)) {
                    if (this.treasureTotal >= (treasureTotal / 2)) {//木箱最多五個寶箱三個道具
                        i = this.boxes.size();
                        break;
                    } else if (this.treasureTotal < (treasureTotal / 2) && !this.boxes.get(i).getTreasureExist() && !this.boxes.get(i).getWoodExist()) {
                        this.boxes.get(i).setTreasureExist();
                        this.treasureTotal++;
                    }
                }
            }
        }
        while (this.treasureTotal < treasureTotal) {
            for (int i = 0; i < this.barrels.size(); i++) {
                if (Global.random(30)) {
                    if (this.treasureTotal >= treasureTotal) {
                        i = this.barrels.size();
                        break;
                    } else if (this.treasureTotal < treasureTotal && !this.barrels.get(i).getTreasureExist() && !this.barrels.get(i).getWoodExist()) {
                        this.barrels.get(i).setTreasureExist();
                        this.treasureTotal++;
                    }
                }

            }
        }
        while (this.taskTotal < taskTotal / 2) {
            for (int i = 0; i < this.boxes.size(); i++) {
                if (Global.random(40)) {
                    if (this.taskTotal >= taskTotal / 2) {
                        i = this.boxes.size();
                        break;
                    } else if (this.taskTotal < taskTotal / 2 && !this.boxes.get(i).getTreasureExist() && !this.boxes.get(i).getWoodExist()) {
                        this.boxes.get(i).setWoodExist();
                        this.taskTotal++;
                    }
                }
            }
        }
        while (this.taskTotal < taskTotal) {

            //當寶箱小於10或是任務道具小於5 //改外面的數量裡面也要改
            for (int i = 0; i < this.barrels.size(); i++) {

                if (Global.random(40)) {
                    if (this.taskTotal >= taskTotal) {
                        i = this.barrels.size();
                        break;
                    } else if (this.taskTotal < taskTotal && !this.barrels.get(i).getTreasureExist() && !this.barrels.get(i).getWoodExist()) {
                        this.barrels.get(i).setWoodExist();
                        this.taskTotal++;
                    }
                }

            }
        }
    }

    private void loadWorld(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String str = br.readLine();
            String[] tmp = str.split(" ");
            this.spawnX = Integer.parseInt(tmp[0]);
            this.spawnY = Integer.parseInt(tmp[1]);
            this.width = Integer.parseInt(tmp[2]);
            this.height = Integer.parseInt(tmp[3]);
            this.tileID = new int[this.width][this.height];
            for (int y = 0; y < this.height; y++) {
                str = br.readLine();
                tmp = str.split(" ");
                for (int x = 0; x < this.width; x++) {
                    this.tileID[x][y] = Integer.parseInt(tmp[x]);
                    tiles.add(new GrassTile(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y, Global.CURRENT_LEVEL, tileID[x][y]));//全部都先鋪一層草地
                    if (tileID[x][y] == 2 || tileID[x][y] >= 21 && tileID[x][y] <= 30) {
                        this.stones.add(new Stone(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y, tileID[x][y]));//tile=2鋪一層岩石
                    }
                    if (tileID[x][y] == 3) {
                        this.trees.add(new Tree(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y));//tile=3鋪一層樹
                    }
                    if (tileID[x][y] >= 40 && tileID[x][y] <= 60) {
                        this.rivers.add(new River(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y, tileID[x][y]));//鋪一層河流
                        this.stones.add(new Stone(this.spawnX + x * Global.UNIT_X, this.spawnY + y * Global.UNIT_Y, tileID[x][y]));//我偷懶不想再寫一層判斷碰撞所以多一層石頭
                    }

                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getTile(int x, int y) {
        return tileID[(x - 88) / 64][(y - 105) / 64];

    }

    //paint
    public void p(Graphics g, Camera camera) {

        for (int i = 0; i < this.tiles.size(); i++) {
            camera.paint(g, tiles.get(i));
        }
        for (int i = 0; i < this.stones.size(); i++) {
            camera.paint(g, stones.get(i));
        }
        for (int i = 0; i < this.trees.size(); i++) {
            camera.paint(g, trees.get(i));
        }
        for (int i = 0; i < this.rivers.size(); i++) {
            camera.paint(g, this.rivers.get(i));
        }
        if (this.woodbridge != null) {
            camera.paint(g, this.woodbridge);
        }
        if (this.castle != null) {
            camera.paint(g, this.castle);
        }
        //寶箱要畫在箱子之前才會在箱子後面
        for (int i = 0; i < this.treasures.size(); i++) {
            camera.paint(g, this.treasures.get(i));
        }
        for (int i = 0; i < this.task.size(); i++) {
            camera.paint(g, this.task.get(i));
        }

        for (int i = 0; i < this.boxes.size(); i++) {
            camera.paint(g, boxes.get(i));
        }
        for (int i = 0; i < this.barrels.size(); i++) {
            camera.paint(g, barrels.get(i));
        }
        for (int i = 0; i < this.monsters.size(); i++) {
            camera.paint(g, monsters.get(i));
        }

        camera.paint(g, catman);
        pBomb(g, camera, catman);

        if (Global.IS_MUlTIPLAYERS) {
            camera.paint(g, catman2);
            pBomb(g, camera, catman2);
        }
        if (this.task_lv2 != null && this.task_lv2.isActive()) {
            camera.paint(g, this.task_lv2);
        }
        pProps(g, camera, this.treasures);

    }//all object

    public void pBomb(Graphics g, Camera camera, CatMan catman) {
        for (int m = 0; m < catman.getBomb().size(); m++) {
            camera.paint(g, catman.getBomb(m));
            for (int n = 0; n < catman.getBomb(m).getBoom().size(); n++) {
                if (catman.getBomb(m).getPictureIndex() == 9) {
                    camera.paint(g, catman.getBomb(m).getBoom(n));
                }
            }
        }

    }

    public void pProps(Graphics g, Camera camera, ArrayList<Treasure> treasures) {
        for (int i = 0; i < this.treasures.size(); i++) {
            if (this.treasures.get(i).getProp() != null) {
                camera.paint(g, this.treasures.get(i).getProp());
            }
        }
    }

    public void paint(Graphics g) {//主要的paint方法
        p(g, camera);
        if (Global.IS_MUlTIPLAYERS) {
            p(g, camera2);
        }
        this.backgroundUp.paintComponent(g, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, 0, 0);
        this.ui.paint(g, camera, 0);
        this.taskbar.paintComponent(g, 0);
        if (Global.IS_MUlTIPLAYERS) {
            this.ui2.paint(g, camera2, 632);
            this.taskbar2.paintComponent(g, 632);
        }
        if (isOver()) {
            gameOver.paintComponent(g, camera, 0);
        }

    }

    //update
    private void boxesUpdate() {
        for (int i = 0; i < this.boxes.size(); i++) {
            this.boxes.get(i).update();
            if (this.boxes.get(i).getDisapperIndex() == 4) {
                if (this.boxes.get(i).getTreasureExist()) {
                    this.treasures.add(new Treasure(this.boxes.get(i).getX(), this.boxes.get(i).getY()));
                    this.treasureTotal++;

                }
                if (this.boxes.get(i).getWoodExist()) {
                    if (Global.CURRENT_LEVEL == 3 && this.task_lv3_count != 8) {
                        this.task.add(new Task(this.task_lv3[this.task_lv3_count++], this.boxes.get(i).getX(), this.boxes.get(i).getY()));
                    }
                    if (Global.CURRENT_LEVEL == 1) {
                        this.task.add(new Task(this.boxes.get(i).getX(), this.boxes.get(i).getY()));
                    }
                }
                this.boxes.remove(i);
                i--;
            }
        }
    }

    private void barrlesUpdate() {
        for (int i = 0; i < this.barrels.size(); i++) {
            this.barrels.get(i).update();
            if (this.barrels.get(i).getDisapperIndex() == 4) {
                if (this.barrels.get(i).getTreasureExist()) {
                    this.treasures.add(new Treasure(this.barrels.get(i).getX(), this.barrels.get(i).getY()));
                    this.treasureTotal++;
                }
                if (this.barrels.get(i).getWoodExist()) {
                    if (Global.CURRENT_LEVEL == 3 && this.task_lv3_count != 8) {
                        this.task.add(new Task(this.task_lv3[this.task_lv3_count++], this.barrels.get(i).getX(), this.barrels.get(i).getY()));
                    }
                    if (Global.CURRENT_LEVEL == 1) {
                        this.task.add(new Task(this.barrels.get(i).getX(), this.barrels.get(i).getY()));
                    }
                }
                this.barrels.remove(i);
                i--;
            }
        }
    }

    private void test() {
        if (this.testDelay.isTrig()) {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    System.out.print(this.tileID[x][y] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private void monstersUpdate() {
        for (int i = 0; i < monsters.size(); i++) {

            this.monsters.get(i).isInRange(catman);
            if (Global.IS_MUlTIPLAYERS) {
                this.monsters.get(i).isInRange(this.catman2);
            }
            this.monsters.get(i).update();
            if (this.monsters.get(i).getDisapperIndex() == 4) {
                this.tileID[(this.monsters.get(i).getX() - 120) / 64][(this.monsters.get(i).getY() - 137) / 64] = 0;
                this.monsters.remove(i);
                if (Global.CURRENT_LEVEL == 2) {
                    this.catman.addTask2Score();//加分
                    this.taskbar.setTask2Score();
                }
                i--;
            }
        }
    }

    private void taskUpdate() {
        for (int i = 0; i < this.task.size(); i++) {
            this.task.get(i).update();
            if (this.task.get(i).getStateDelay().isTrig()) {
                this.task.remove(i);
                i--;
            }
        }
    }

    private void treasureUpdate() {
        for (int i = 0; i < this.treasures.size(); i++) {
            this.treasures.get(i).update();
            if (this.treasures.get(i).getStateDelay().isTrig()) {

                if ((Global.CURRENT_LEVEL == 2 && Global.random(this.treasureTotal * 2)) && (this.treasures.get(i).getProp() == null && this.task_lv2 == null)) {
                    this.task_lv2 = new Task(this.treasures.get(i).getX(), this.treasures.get(i).getY());
                    this.task_lv2.getPictureDelay().start();
                }
                this.treasures.remove(i);
                i--;
                if (this.task_lv2 != null) {
                }
            }
        }
        if (this.task_lv2 != null) {
            this.task_lv2.update();
        }
    }

    private void uiUpdate() {
        this.ui.showTaskbar(this.taskbar.showTaskbar());
        this.ui.update();
        this.ui.setBombNumber(this.catman.getBombNumber());
        this.ui.setFireNumber(this.catman.getFireNumber());
        this.ui.setSpeedNumber(this.catman.getSpeedNumber());
        this.ui.setHeartNumber(this.catman.getHeartNumber());
        if (Global.IS_MUlTIPLAYERS) {
            this.ui2.showTaskbar(this.taskbar.showTaskbar());
            this.ui2.update();
            this.ui2.setBombNumber(this.catman2.getBombNumber());
            this.ui2.setFireNumber(this.catman2.getFireNumber());
            this.ui2.setSpeedNumber(this.catman2.getSpeedNumber());
            this.ui2.setHeartNumber(this.catman2.getHeartNumber());

        }
    }

    public void update() {
   
        taskUpdate();
        uiUpdate();
        treasureUpdate();

        if (Global.CURRENT_LEVEL == 3) {
            this.taskbar.setTaskArr(this.task);
        }
        this.taskbar.update();
        if (this.sceneDelay.isTrig()) {
            boxesUpdate();
            barrlesUpdate();
            //單人模式
            monstersUpdate();
            this.catman.update();

            catManisCollision(this.catman, null);
            this.monsterisCollision(this.catman);
            if (Global.CURRENT_LEVEL == 2 && this.castle.isReady()) {
                this.camera.centerOnObj(this.castle);
            } else {
                this.camera.centerOnObj(this.catman);
            }
            this.camera.update();
            //雙人模式
            if (Global.IS_MUlTIPLAYERS) {
                this.catman2.update();

                catManisCollision(this.catman, this.catman2);
                catManisCollision(this.catman2, this.catman);
                monsterisCollision(this.catman2);
                boomCollision(this.catman, this.catman2, this.taskbar);
                boomCollision(this.catman2, this.catman, this.taskbar2);
                this.camera2.centerOnObj(this.catman2);
                this.camera2.update();
            } else {
                this.boomCollision(this.catman, null, this.taskbar);
                if (this.castle != null) {
                    this.castle.update();
                }
                missionComplete();
            }

        }
        if (isOver()) {
            gameOver.update();
        }
    }

    private void missionComplete() {
        switch (Global.CURRENT_LEVEL) {
            case 1:
                if (catman.getTask1Score() >= taskbar.task1TargetScore() && !this.passState) {
                    AudioResourceController.getInstance().play(AudioPath.PASS); //播放音效
                    this.passState = true;
                }
                if (catman.getTask1Score() >= taskbar.task1TargetScore()) {
                    for (int i = 0; i < this.stones.size(); i++) {
                        if (this.stones.get(i).getTileX() == 13 && this.stones.get(i).getTileY() == 5) {
                            this.stones.remove(i);
                            i--;
                        }
                        if (this.stones.get(i).getTileX() == 14 && this.stones.get(i).getTileY() == 5) {
                            this.stones.remove(i);
                            i--;
                        }
                    }
                    this.woodbridge.setIsOver();
                    this.woodbridge.update();
                }
                break;
            case 2:
                if (catman.getTask1Score() >= taskbar.task1TargetScore() && catman.getTask2Score() >= taskbar.task2TargetScore()) {
                    for (int i = 0; i < this.stones.size(); i++) {
                        if (this.stones.get(i).getTileX() == 17 && this.stones.get(i).getTileY() == 1) {
                            this.stones.remove(i);
                            i--;
                        }
                    }
                    this.castle.setIsOver();
                    this.castle.update();
                }
                break;
        }

    }

    public boolean isOver() {
        if (!Global.IS_MUlTIPLAYERS && catman.isOver(this.taskbar.task1TargetScore(), this.taskbar.task2TargetScore(), this.ui.toString())) {
            gameOver.setIsWin(catman.getIsWin());//要進下一關
            this.backGroundAudio.stop();
            gameOver.setIsOver();
            return true;
        } else if (Global.IS_MUlTIPLAYERS && catman.isOverMultiPlayers(catman2, this.ui.toString())) {
            gameOver.setIsWin(false);//可以restart
            gameOver.setIsOver();
            return true;
        }
        return false;
    }

    //collision
    private void boomCollision(CatMan catman1P, CatMan catman2P, TaskBar taskbar) {

        if (catman1P.getBombCount() > 0 && catman1P.getBombCount() <= catman1P.getBombNumber()) {

            for (int i = 0; i < this.barrels.size(); i++) {
                for (int j = 0; j < catman1P.getBomb().size(); j++) {
                    for (int k = 0; k < catman1P.getBomb().get(j).getBoom().size(); k++) {
                        if (catman1P.getBomb().get(j).boomState() && catman1P.getBomb().get(j).getBoom().get(k).isCollision(this.barrels.get(i))) {
                            this.tileID[(this.barrels.get(i).getX() - 88) / 64][(this.barrels.get(i).getY() - 105) / 64] = 0;
                            this.barrels.get(i).getDisapperDelay().start();
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < this.boxes.size(); i++) {
                for (int j = 0; j < catman1P.getBomb().size(); j++) {
                    for (int k = 0; k < catman1P.getBomb().get(j).getBoom().size(); k++) {
                        if (catman1P.getBomb().get(j).boomState() && catman1P.getBomb().get(j).getBoom().get(k).isCollision(this.boxes.get(i))) {
                            this.tileID[(this.boxes.get(i).getX() - 88) / 64][(this.boxes.get(i).getY() - 105) / 64] = 0;
                            this.boxes.get(i).getDisapperDelay().start();
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < this.monsters.size(); i++) {
                for (int j = 0; j < catman1P.getBomb().size(); j++) {
                    for (int k = 0; k < catman1P.getBomb().get(j).getBoom().size(); k++) {
                        if (catman1P.getBomb().get(j).boomState() && this.monsters.get(i).isCollision(catman1P.getBomb().get(j).getBoom().get(k))) {
                            this.monsters.get(i).getDisapperDelay().start();
                            if (Global.IS_MUlTIPLAYERS && !monsters.get(i).getIsCollisionMonster()) {
                                catman1P.addTask1Score();
                                taskbar.setTask1Score();
                                monsters.get(i).setIsCollisionMonster();
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < catman1P.getBomb().size(); i++) {
                for (int j = 0; j < catman1P.getBomb().get(i).getBoom().size(); j++) {
                    if (catman2P != null && catman1P.getBomb().get(i).boomState() && catman1P.getBomb().get(i).getBoom().get(j).isCollision(catman2P)) {
                        catman2P.setActive(false);

                    }
                    if (catman2P != null && catman2P.getBombCount() > 0 && catman2P.getBombCount() <= catman2P.getBombNumber()) {
                        for (int k = 0; k < catman2P.getBomb().size(); k++) {
                            if (catman1P.getBomb().get(i).boomState() && catman1P.getBomb().get(i).getBoom().get(j).isCollision(catman2P.getBomb().get(k))) {
                                catman2P.getBomb().get(k).getPictureDelay().stop();
                                catman2P.getBomb().get(k).setPictureIndex(9);
                                break;
                            }
                        }
                    }
                }

            }
            for (int i = 0; i < catman1P.getBomb().size(); i++) {                        //第一顆炸彈
                for (int j = 0; j < catman1P.getBomb().get(i).getBoom().size(); j++) {   //第一顆炸彈的爆炸
                    if (catman1P.getBomb().get(i).boomState() && catman1P.getBomb().get(i).getBoom().get(j).isCollision(catman1P)) {
                        catman1P.setActive(false);//被自己的炸彈炸到

                    }
                    for (int k = 0; k < catman1P.getBomb().size(); k++) {                //與其他的炸彈碰撞
                        if (catman1P.getBomb().get(i) != catman1P.getBomb().get(k)) {
                            if (catman1P.getBomb().get(i).boomState() && catman1P.getBomb().get(i).getBoom().get(j).isCollision(catman1P.getBomb().get(k))) {
                                catman1P.getBomb().get(k).getPictureDelay().stop();
                                catman1P.getBomb().get(k).setPictureIndex(9);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void monsterisCollision(CatMan catman) {
        for (int i = 0; i < this.monsters.size(); i++) {
            for (int j = 0; j < this.stones.size(); j++) {
                if (this.monsters.get(i).isCollision(this.stones.get(j))) {
                    this.monsters.get(i).moveBack();
              

                }
            }
            for (int j = 0; j < this.boxes.size(); j++) { //怪物跟箱子碰撞 怪物後退
                if (this.monsters.get(i).isCollision(this.boxes.get(j))) {
                    this.monsters.get(i).moveBack();
              
                }
            }
            for (int j = 0; j < this.barrels.size(); j++) {
                if (this.monsters.get(i).isCollision(this.barrels.get(j))) {
                    this.monsters.get(i).moveBack();
               
                }
            }
            for (int j = 0; j < this.monsters.size(); j++) {
                if (this.monsters.get(i) != this.monsters.get(j)) {
                    if (this.monsters.get(i).isCollision(this.monsters.get(j))) {
//                       跟別的怪物碰撞
                    }
                }
            }
            for (int j = 0; j < catman.getBomb().size(); j++) {
                if (this.monsters.get(i).isCollision(catman.getBomb().get(j))) {//怪物跟炸彈碰撞

                    int dir = 0;
                    if ((this.monsters.get(i).getY() - 89) / 64 > (catman.getBomb(j).getY() - 137) / 64) {
                        dir = Global.UP;
                    }
                    if ((this.monsters.get(i).getY() - 89) / 64 < (catman.getBomb(j).getY() - 137) / 64) {
                        dir = Global.DOWN;
                    }
                    if ((this.monsters.get(i).getX() - 88) / 64 > (catman.getBomb(j).getX() - 120) / 64) {
                        dir = Global.LEFT;
                    }
                    if ((this.monsters.get(i).getX() - 88) / 64 < (catman.getBomb(j).getX() - 120) / 64) {
                        dir = Global.RIGHT;
                    }
                    this.monsters.get(i).moveBack(dir);              
                }
            }
            for (int j = 0; j < this.trees.size(); j++) {
                if (this.monsters.get(i).isCollision(this.trees.get(j))) {
                    this.monsters.get(i).moveBack();
                }
            }
        }
    }

    private void catManisCollision(CatMan catman, CatMan catman2P) {

        if (catman.getIsAlive()) {
            for (int i = 0; i < this.getBarrelSize(); i++) {//人物跟木桶碰撞
                if (catman.isCollision(this.getBarrel(i))) {
                    catman.moveBack();
                }
            }

            for (int i = 0; i < this.getStoneSize(); i++) {
                if (catman.isCollision(this.getStone(i))) { //人物跟岩石的碰撞
                    catman.moveBack();
                }
            }
            for (int i = 0; i < this.getBoxSize(); i++) {
                if (catman.isCollision(this.getBox(i))) { // 人物跟箱子碰撞
                    this.tileID[(this.getBox(i).getX() - 88) / 64][(this.getBox(i).getY() - 105) / 64] = 0;
                    if (this.getBox(i).move(catman.getDir())) {//判斷式中箱子會移動 如果箱子撞到牆人後退      
                        catman.moveBack();
                    } else {
                        this.tileID[(this.getBox(i).getX() - 88) / 64][(this.getBox(i).getY() - 105) / 64] = 1;
                    }

                    for (int j = 0; j < this.stones.size(); j++) { //人推箱子撞到石頭
                        if (this.boxes.get(i).isCollision(this.stones.get(j))) {
                            this.boxes.get(i).moveBack(catman.getDir());
                            catman.moveBack();
                        }
                    }
                    for (int j = 0; j < this.trees.size(); j++) {
                        if (this.boxes.get(i).isCollision(this.trees.get(j))) {
                            this.boxes.get(i).moveBack(catman.getDir());
                            catman.moveBack();
                        }
                    }
                    for (int j = 0; j < this.barrels.size(); j++) {
                        if (this.boxes.get(i).isCollision(this.barrels.get(j))) {
                            this.boxes.get(i).moveBack(catman.getDir());
                            catman.moveBack();
                        }
                    }
                    for (int j = 0; j < this.rivers.size(); j++) {
                        if (this.boxes.get(i).isCollision(this.rivers.get(j))) {
                            this.boxes.get(i).moveBack(catman.getDir());
                            catman.moveBack();
                        }
                    }
                    for (int j = 0; j < this.boxes.size(); j++) {
                        if (this.boxes.get(i) != this.boxes.get(j) && this.boxes.get(i).isCollision(this.boxes.get(j))) {
                            this.boxes.get(i).moveBack(catman.getDir());
                            catman.moveBack();
                        }
                    }
                    for (int j = 0; j < this.monsters.size(); j++) {
                        if (this.boxes.get(i).isCollision(this.monsters.get(j))) {
                            this.boxes.get(i).moveBack(catman.getDir());
                            catman.moveBack();
                        }
                    }
                }
            }

        }
        if (catman2P != null && catman2P.getBombCount() > 0) {
            for (int i = 0; i < catman2P.getBomb().size(); i++) {
                if (catman.isCollision(catman2P.getBomb(i))) {

                    int dir = 0;
                    if ((catman.getY() - 105) / 64 > (catman2P.getBomb(i).getY() - 137) / 64) {
                        dir = Global.UP;
                    }
                    if ((catman.getY() - 105) / 64 < (catman2P.getBomb(i).getY() - 137) / 64) {
                        dir = Global.DOWN;
                    }
                    if ((catman.getX() - 88) / 64 > (catman2P.getBomb(i).getX() - 120) / 64) {
                        dir = Global.LEFT;
                    }
                    if ((catman.getX() - 88) / 64 < (catman2P.getBomb(i).getX() - 120) / 64) {
                        dir = Global.RIGHT;
                    }
                    catman.moveBack(dir);
                }
            }
        }
        for (int i = 0; i < this.getBarrelSize(); i++) {
            if (catman.isCollision(this.getBarrel(i))) {
                catman.moveBack();
            }
        }
        for (int j = 0; j < this.monsters.size(); j++) {
            if (catman.isCollision(this.monsters.get(j))) { //跟怪物碰撞
                catman.setActive(false);

            }
        }

        for (int k = 0; k < this.treasures.size(); k++) {//人跟寶箱碰撞
            if (catman.isCollision(this.treasures.get(k))) {
                this.treasures.get(k).getPictureDelay().start();
                if (this.treasures.get(k).getPictureIndex() == 1 && this.treasures.get(k).getProp() != null && this.treasures.get(k).getProp().getState()) {//寶箱是打開的狀態且道具還在
                    AudioResourceController.getInstance().play(AudioPath.GET);//播放音效
                    switch (this.treasures.get(k).getProp().getPropNumber()) {
                        case 0:
                            catman.addBombTotal();//撞到寶箱的那隻貓增加炸彈數量 
                            break;
                        case 1:
                            catman.addFireTotal();
                            break;
                        case 2:
                            catman.addSpeedTotal();
                            break;
                    }
                    this.treasures.get(k).getProp().setState(false);
                }

            }
        }
        for (int n = 0; n < catman.getBomb().size(); n++) { //跟炸彈碰撞

            if (!catman.getBomb(n).getBombStayWithCatMan() && catman.isCollision(catman.getBomb(n))) {

                int dir = 0;
                if ((catman.getY() - 105) / 64 > (catman.getBomb(n).getY() - 137) / 64) {
                    dir = Global.UP;
                }
                if ((catman.getY() - 105) / 64 < (catman.getBomb(n).getY() - 137) / 64) {
                    dir = Global.DOWN;
                }
                if ((catman.getX() - 88) / 64 > (catman.getBomb(n).getX() - 120) / 64) {
                    dir = Global.LEFT;
                }
                if ((catman.getX() - 88) / 64 < (catman.getBomb(n).getX() - 120) / 64) {
                    dir = Global.RIGHT;
                }
                catman.moveBack(dir);
            }

        }

        for (int m = 0; m < this.trees.size(); m++) {//跟樹碰撞
            if (catman.isCollision(this.trees.get(m))) {
                catman.moveBack();
            }
        }
        for (int p = 0; p < this.task.size(); p++) {
            if (catman.isCollision(this.task.get(p)) && this.task.get(p).getState()) {
                AudioResourceController.getInstance().play(AudioPath.GET);//播放音效
                catman.addTask1Score();//加分
                this.taskbar.setTask1Score();
                this.task.get(p).getPictureDelay().start();
                this.task.get(p).getStateDelay().start();
                this.task.get(p).setState(false);
            }
        }
        if (this.task_lv2 != null && catman.isCollision(this.task_lv2)) {
            this.task_lv2.setActive(false);
            this.task_lv2.getPictureDelay().pause();
            catman.addTask1Score();//加分
            taskbar.setTask1Score();
        }

    }

    //getters & setters
    public void setBackGround(BackGround bg) {
        this.backgroundUp = bg;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public CatMan getCatMan1P() {
        return this.catman;
    }

    public CatMan getCatMan2P() {
        return this.catman2;
    }

    public Treasure getTreasure(int index) {
        return this.treasures.get(index);
    }

    public Box getBox(int index) {
        return this.boxes.get(index);
    }

    public Stone getStone(int index) {
        return this.stones.get(index);
    }

    public Barrel getBarrel(int index) {
        return this.barrels.get(index);
    }

    public int getBarrelSize() {
        return this.barrels.size();
    }

    public int getBoxSize() {
        return this.boxes.size();
    }

    public int getStoneSize() {
        return this.stones.size();
    }

    public TaskBar getTaskBar() {
        return this.taskbar;
    }

    public GameOver gameOver() {
        return this.gameOver;
    }

    public int getCurrentLevel() {
        return this.level;
    }

}
