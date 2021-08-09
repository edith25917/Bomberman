/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.controllers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Edith
 */
public class AudioResourceController {

    // �靘�

    Clip clip;
    private static AudioResourceController irc;

    private AudioResourceController() {
    }

    public static AudioResourceController getInstance() {
        if (irc == null) {
            irc = new AudioResourceController();
        }
        return irc;
    }

    public void play(String filename) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AudioInputStream audioInputStream;
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "\\src" + filename));
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.setFramePosition(0);
                    // values have min/max values, for now don't check for outOfBounds values
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(5f);
                    clip.start();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    Logger.getLogger(AudioResourceController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public void stop() {
        this.clip.stop();
    }
}
