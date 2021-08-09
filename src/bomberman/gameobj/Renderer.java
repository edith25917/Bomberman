/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.gameobj;

import bomberman.controllers.ImageResourceController;
import bomberman.util.Delay;
import bomberman.util.Global;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Edith
 */
public class Renderer {

    public static final int[] NORMAL_STEPS = {0, 1, 2, 1};
    public static final int[] UNMOVED_STEPS = {0, 0, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1};

    private BufferedImage img;
    private int stepindex;
    private int characterIndex;
    private int currentStep;
    private int dir = 0;
    private int[] steps;
    private Delay stepDelay;

    private int stepDelayFrame;
    private int width;
    private int height;

    public Renderer(String path, int characterIndex, int[] steps, int delayFrame) {
        this.img = ImageResourceController.getInstance().tryGeyImage(path);
        this.steps = steps;
        this.characterIndex = characterIndex;
        this.stepDelayFrame = delayFrame;
        stepDelay = new Delay(delayFrame);

        stepDelay.start();
        this.width = Global.UNIT_X;
        this.height = Global.UNIT_Y;
    }

    public void setCharacterIndex(int characterIndex) {
        this.characterIndex = characterIndex;
    }

    public void setSteps(int[] steps) {
        this.steps = steps;
    }

    public void setCurrentStep(int step) {
        this.currentStep = step;
    }

    public void setStepDelay(int delay) {
        this.stepDelay = new Delay(delay);
        this.stepDelay.start();
    }

    public void setDir(int dir) {
        this.dir = dir % 4;//因為加上雙人模式會有超過0 1 2 3的數字，所以要%4讓他恢復正常
    }

    public void pause() {
        this.stepDelay.pause();
    }

    public void start() {
        this.stepDelay.start();
    }

    public void update() {

        if (stepDelay.isTrig()) {
            stepindex = (stepindex + 1) % steps.length;
            this.currentStep = steps[stepindex];
        }
    }

    public void setWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void paint(Graphics g, int x, int y, int w, int h) {
        g.drawImage(img, x, y, x + this.width, y + this.height,
                96 * (characterIndex % 6) + 32 * currentStep, //x1
                128 * (characterIndex / 6) + 32 * dir, //y1
                32 + 96 * (characterIndex % 6) + 32 * currentStep,//x2
                32 + 128 * (characterIndex / 6) + 32 * dir,//y2
                null);

    }

}
