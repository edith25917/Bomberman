/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.controllers;

import bomberman.gameobj.CatMan;
import bomberman.scenes.Scene;
import gamekernal.CommandSolver.KeyListener;
import gamekernal.CommandSolver.MouseCommandListener;
import java.awt.Graphics;

/**
 *
 * @author Edith
 */
public class SceneController {

    private Scene currentScene;
    private KeyListener kl;
    private MouseCommandListener ml;

    public void changeScene(Scene scene) {
        if (currentScene != null) {
            currentScene.sceneEnd();
        }
        currentScene = scene;
        kl = currentScene.getKeyListener();
        ml = currentScene.getMouseListener();
        currentScene.sceneBegin();
      
    }

    public Scene getScene() {
        return this.currentScene;
    }

    public KeyListener getKL() {
        return this.kl;
    }

    public MouseCommandListener getML() {
        return this.ml;
    }

    public void sceneUpdate() {
        currentScene.sceneUpdate();
    }

    public void paint(Graphics g) {
        currentScene.paint(g);
    }

}
