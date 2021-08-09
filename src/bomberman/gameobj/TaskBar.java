package bomberman.gameobj;

import bomberman.camera.Camera;
import bomberman.controllers.ImageResourceController;
import bomberman.util.Delay;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Edith
 */
public class TaskBar {

    private BufferedImage taskbar;
    private BufferedImage task1;
    private BufferedImage task2;
    private BufferedImage complete;
    private boolean showTaskbar = true;
    private Delay removeTaskbar;
    private Delay lv3_taskDelay;
    private int taskBarDistance = -200;
    private int state = 0;

    //targetScore
    private int task1_TargetScore;//lv1: wood, lv2: monsters, lv3: letters
    private int task2_TargetScore;//lv2: key
    private int totalTask;
    private int task1Score = 0;
    private int task2Score = 0;

    //lv3_task
    private BufferedImage[] taskArrImg;
    private int lv3_picIndex;
    private int[] coinIndex;
    private int count;
    private boolean canAdd;

    public TaskBar(int level) {
        this.complete = ImageResourceController.getInstance().tryGeyImage(ImagePath.COMPLETE);//開頭的灰色bar
        this.removeTaskbar = new Delay(180);
        this.removeTaskbar.start();
        initTask(level);

    }

    private void initTask(int level) {
        switch (Global.CURRENT_LEVEL) {
            case 1:
                this.task1 = ImageResourceController.getInstance().tryGeyImage(ImagePath.WOODS);//任務圖片
                this.task1_TargetScore = 3;
                break;
            case 2:
                this.task1 = ImageResourceController.getInstance().tryGeyImage(ImagePath.KEY);//任務圖片
                this.task2 = ImageResourceController.getInstance().tryGeyImage(ImagePath.MONSTER_2);//任務圖片
                this.task1_TargetScore = 1;//鑰匙
                this.task2_TargetScore = 10;// 怪物
                break;
            case 3:
                this.task1 = ImageResourceController.getInstance().tryGeyImage(ImagePath.COIN);//任務圖片
                this.taskArrImg = new BufferedImage[8];
                this.coinIndex = new int[8];
                setTaskArrImg();
                this.task1_TargetScore = 8;//字
                this.lv3_taskDelay = new Delay(10);
                this.lv3_taskDelay.start();
                break;
            case 4:
                switch (level) {
                    case 1:
                        this.task1 = ImageResourceController.getInstance().tryGeyImage(ImagePath.MONSTER_1);
                        break;
                    case 2:
                        this.task1 = ImageResourceController.getInstance().tryGeyImage(ImagePath.MONSTER_2);
                        break;
                    case 3:
                        this.task1 = ImageResourceController.getInstance().tryGeyImage(ImagePath.MONSTER_3);
                        break;
                }
                this.task2 = ImageResourceController.getInstance().tryGeyImage(ImagePath.TASKBAR_LV4);
                this.task1_TargetScore = 20;
        }

    }

    public boolean showTaskbar() {
        return this.showTaskbar;
    }

    public void setTaskArr(ArrayList<Task> taskArr) {
        for (int i = 0; i < taskArr.size(); i++) {
            if (!taskArr.get(i).getState()) {
                String[] tmp = taskArr.get(i).getImgPath().split("-");
                char cha = tmp[1].charAt(0);
                int m = Integer.parseInt("" + cha);
                if (this.count == 0) {
                    coinIndex[this.count++] = m;
                }
                for (int k = 0; k < this.count; k++) {
                    if (coinIndex[k] != m) {
                        this.canAdd = true;
                    } else {
                        this.canAdd = false;
                        break;
                    }
                }
                if (this.canAdd) {
                    coinIndex[this.count++] = m;
                }
            }
        }
    }

    private void setTaskArrImg() {
        this.taskArrImg[0] = ImageResourceController.getInstance().tryGeyImage(ImagePath.COIN_1);
        this.taskArrImg[1] = ImageResourceController.getInstance().tryGeyImage(ImagePath.COIN_2);
        this.taskArrImg[2] = ImageResourceController.getInstance().tryGeyImage(ImagePath.COIN_3);
        this.taskArrImg[3] = ImageResourceController.getInstance().tryGeyImage(ImagePath.COIN_4);
        this.taskArrImg[4] = ImageResourceController.getInstance().tryGeyImage(ImagePath.COIN_5);
        this.taskArrImg[5] = ImageResourceController.getInstance().tryGeyImage(ImagePath.COIN_6);
        this.taskArrImg[6] = ImageResourceController.getInstance().tryGeyImage(ImagePath.COIN_7);
        this.taskArrImg[7] = ImageResourceController.getInstance().tryGeyImage(ImagePath.COIN_8);

    }

    public int task1TargetScore() {
        return this.task1_TargetScore;
    }

    public int task2TargetScore() {
        return this.task2_TargetScore;
    }

    public int getTotalTask() {
        return this.totalTask;
    }

    public void setTask1Score() {
        if (this.task1Score >= this.task1_TargetScore) {
            return;
        }
        this.task1Score++;
    }

    public int getTask1Score() {
        return this.task1Score;
    }

    public void setTask2Score() {
        if (this.task2Score >= this.task2_TargetScore) {
            return;
        }
        this.task2Score++;
    }

    public void update() {
        if (this.state == 0 && this.showTaskbar) {//灰色taskbar出現
            this.taskBarDistance += 15;
            if (this.taskBarDistance >= 350) {
                this.taskBarDistance = 350;
            }
            if (this.removeTaskbar.isTrig()) {
                this.state = 1;
            }
        }
        if (this.state == 1 && this.showTaskbar) {//灰色taskbar縮回
            this.taskBarDistance -= 20;
            if (this.taskBarDistance <= -200) {
                this.taskBarDistance = -200;
                this.showTaskbar = false;
            }
        }
        if (Global.CURRENT_LEVEL == 3 && this.lv3_taskDelay.isTrig()) {
            this.lv3_picIndex = (this.lv3_picIndex + 1) % 10;
        }

    }

    public void paintComponent(Graphics g, int startX) {
        paintBar(g);
        paintTaskNumber(g, startX);
    }

    private void paintBar(Graphics g) {
        if (this.showTaskbar) {
            //遮住螢幕的灰色半透明塗層
//            g.setColor(new Color(204, 204, 204, 127));
//            g.fillRect(88, 105, 1024, 640);

            //任務bar
            g.setColor(new Color(51, 51, 51, 180));//Bar的顏色
            g.fillRect(0, this.taskBarDistance, 1200, 180);
//            g.drawImage(taskbar, 0, this.taskBarDistance, 1200, 180, null);//bar
            g.setColor(new Color(242, 242, 242));//字的顏色
            g.setFont(new Font("Serif Bold", Font.BOLD, 54));

            if (Global.CURRENT_LEVEL == 2) {
                g.drawImage(complete, 200, this.taskBarDistance + 20, 256, 128, null);
                g.drawImage(task1, 450, this.taskBarDistance + 40, 96, 96, null);
                g.drawString(" x ", 550, this.taskBarDistance + 100);
                g.drawString("" + this.task1_TargetScore, 600, this.taskBarDistance + 105);
                g.drawImage(task2, 650, this.taskBarDistance + 40, 96, 96, null);
                g.drawString(" x ", 750, this.taskBarDistance + 100);
                g.drawString("" + this.task2_TargetScore, 800, this.taskBarDistance + 105);
            } else if (Global.CURRENT_LEVEL == 4) {
                g.drawImage(complete, 200, this.taskBarDistance + 20, 256, 128, null);
                g.drawImage(this.task2, 466, this.taskBarDistance + 60, 664, 50, null);
            } else {
                g.drawImage(complete, 300, this.taskBarDistance + 20, 256, 128, null);
                g.drawImage(task1, 550, this.taskBarDistance + 40, 96, 96, null);//過關條件圖片
                g.drawString(" x ", 650, this.taskBarDistance + 100);//過關條件x號
                g.drawString("" + this.task1_TargetScore, 700, this.taskBarDistance + 105);//過關條件數量

            }
        }
    }

    private void paintTaskNumber(Graphics g, int startX) {        //右上角的過關條件
        if (!Global.IS_MUlTIPLAYERS) {
            g.setColor(Color.white);
            g.setFont(new Font("Serif Bold", Font.BOLD, 54));
            switch (Global.CURRENT_LEVEL) {
                case 1:
                    g.drawImage(task1, 946, 23, 64, 64, null);//過關條件圖片
                    g.drawString(" x ", 1000, 70);//過關條件x號
                    g.drawString("" + this.task1Score, 1050, 75);//過關條件數量
                    break;
                case 2:
                    g.drawImage(task1, 800, 23, 64, 64, null);//過關條件圖片
                    g.drawString(" x ", 854, 70);//過關條件x號
                    g.drawString("" + this.task1Score, 904, 75);//過關條件數量
                    g.drawImage(task2, 946, 23, 64, 64, null);//過關條件圖片
                    g.drawString(" x ", 1000, 70);//過關條件x號
                    g.drawString("" + this.task2Score, 1050, 75);//過關條件數量
                    break;
                case 3:
                    for (int i = 0; i < this.count; i++) {
                        g.drawImage(this.taskArrImg[coinIndex[i] - 1], 660 + (coinIndex[i] - 1) * 64, 35, 660 + (coinIndex[i]) * 64, 35 + 64, 64 * this.lv3_picIndex, 0, 64 * (1 + this.lv3_picIndex), 64, null);
                    }
                    break;
            }
        } else {
            g.setColor(Color.white);
            g.setFont(new Font("Serif Bold", Font.BOLD, 54));
            g.drawImage(task1, 200 + startX, 23, 64, 64, null);//過關條件圖片
            g.drawString(" x ", 200 + startX + 54, 70);//過關條件x號
            g.drawString("" + this.task1Score, 200 + startX + 100, 75);//過關條件數量
        }

    }

}
