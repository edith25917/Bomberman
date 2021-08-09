package bomberman.scenes;

import bomberman.controllers.SceneController;
import bomberman.gameobj.CatMan;
import gamekernal.CommandSolver.KeyListener;
import gamekernal.CommandSolver.MouseCommandListener;
import bomberman.util.Delay;
import java.awt.Graphics;

public abstract class Scene {

    protected SceneController sceneController;
 
    public Scene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public abstract void sceneBegin();

    public abstract void sceneUpdate();

    public abstract void sceneEnd();

    public abstract void paint(Graphics g);

    public abstract KeyListener getKeyListener();

    public abstract MouseCommandListener getMouseListener();

}
