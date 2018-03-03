package com.polytech.bdsm.takenoko;

import com.polytech.bdsm.takenoko.engine.Game;
import com.polytech.bdsm.takenoko.gui.GameViewer;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class Main {

    /**
     * Starting point of the program
     *
     * @param args  The possible argument of the program
     */
    public static void main(String[] args) {
        boolean gui = false;
        for (String arg : args) {
            if (arg.equals("--sherlock") || arg.equals("-s")) {
                easterEgg(Main.class.getResource("/musics/the-game-is-on.wav"));
            }
            if (arg.equals("--visualize") || arg.equals("-v")) {
                try {
                    GameViewer.main(args);
                    gui = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!gui) Game.main(args);
    }

    /**
     * Easter Egg method to play a music related to the Sherlock TV series
     *
     * @param file The music file to be played
     */
    private static void easterEgg(URL file) {
        try {
            Thread musicThread = new Thread(() -> {
                try (AudioInputStream in = AudioSystem.getAudioInputStream(file)) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(in);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }, "Music Thread");
            musicThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save an Object at the file path
     *
     * @param o     The Object to be saved
     * @param file  The file path
     */
    public static void save(Object o, String file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(o);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Load an Object from a file
     *
     * @param file The file to load the object from
     * @return The Object saved at in the file
     */
    public static Object load(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
