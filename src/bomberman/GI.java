/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

import bomberman.controllers.SceneController;
import bomberman.scenes.MainScene;
import bomberman.scenes.StartScene;
import gamekernal.CommandSolver;
import gamekernal.CommandSolver.KeyListener;
import gamekernal.CommandSolver.MouseCommandListener;
import bomberman.util.Delay;
import gamekernal.GameKernel;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 *
 * @author Edith
 */
public class GI implements CommandSolver.KeyListener, CommandSolver.MouseCommandListener, GameKernel.GameInterface {

    private SceneController sceneController;

    public GI() {
        sceneController = new SceneController();
        sceneController.changeScene(new StartScene(sceneController));
//        sceneController.changeScene(new MainScene(sceneController));
    }

    public void update() {
        sceneController.sceneUpdate();

    }

    @Override
    public void paint(Graphics g) {
        sceneController.paint(g);
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
        if (sceneController.getKL() != null) {
            sceneController.getKL().keyPressed(commandCode, trigTime);
        }
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        if (sceneController.getKL() != null) {
            sceneController.getKL().keyReleased(commandCode, trigTime);
        }
    }

    @Override
    public void keyTyped(char c, long trigTime) {
        if (sceneController.getKL() != null) {
            sceneController.getKL().keyTyped(c, trigTime);
        }
    }

    @Override
    public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if (state != null && sceneController.getML() != null) {
            sceneController.getML().mouseTrig(e, state, trigTime);
        }
    }

}
