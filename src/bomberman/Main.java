/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

import bomberman.controllers.AudioResourceController;
import gamekernal.CommandSolver;
import gamekernal.CommandSolver.MouseState;
import bomberman.util.Global;
import gamekernal.GameKernel;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;

/**
 *
 * @author Edith
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame f = new JFrame();
        GI gi = new GI();
//        AudioResourceController.getInstance().play(null);//播放音效

        int[][] commands = new int[][]{
            {KeyEvent.VK_UP, Global.UP},
            {KeyEvent.VK_LEFT, Global.LEFT},
            {KeyEvent.VK_DOWN, Global.DOWN},
            {KeyEvent.VK_RIGHT, Global.RIGHT},
            {KeyEvent.VK_W, Global.W},
            {KeyEvent.VK_A, Global.A},
            {KeyEvent.VK_S, Global.S},
            {KeyEvent.VK_D, Global.D},
            {KeyEvent.VK_SPACE, Global.SPACE},
            {KeyEvent.VK_M, Global.M},
            {KeyEvent.VK_V, Global.V},
            {KeyEvent.VK_ENTER, Global.ENTER}};

        GameKernel gk = new GameKernel.Builder(gi, Global.NANO_PER_UPDATE, Global.LIMIT_DELTA_TIME)
                .initListener(commands)
                .enableKeyboardTrack(gi).enableMouseTrack(gi)
                .keyCleanMode().trackChar()
                .gen();

        f.setTitle("Bomberman");
        f.setSize(Global.FRAME_X, Global.FRAME_Y);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(gk);
        f.setVisible(true);
        gk.run(Global.IS_DEBUG);

    }

}
