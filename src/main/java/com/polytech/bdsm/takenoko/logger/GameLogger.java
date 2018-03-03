package com.polytech.bdsm.takenoko.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.*;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class GameLogger {

    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_BLACK  = "\u001B[30m";
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE   = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN   = "\u001B[36m";
    private static final String ANSI_WHITE  = "\u001B[37m";

    private final static Logger LOGGER = Logger.getLogger(GameLogger.class.getName());
    private Level levelLog = Level.INFO;
    private boolean active = true;
    private CustomFormatter customFormat = new CustomFormatter();

    private OutputStream outputStream;

    /**
     * Default Constructor that setup the fileHandler and the SimpleFormatter to save log into a txt file that is named after the current date
     */
    public GameLogger() {
        try {
            LOGGER.setUseParentHandlers(false);

            //Date format creation
            GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            //File handler for the txt file
            FileHandler fileTxt = new FileHandler(format.format(calendar.getTime()) + ".txt");

            //Add file formatter to the file handler
            fileTxt.setFormatter(customFormat);

            //Console handler for display
            ConsoleHandler ch = new ConsoleHandler();

            //Set formatter for console handler
            ch.setFormatter(customFormat);

            //add both handler to the logger
            LOGGER.addHandler(fileTxt);
            LOGGER.addHandler(ch);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@code logMessage} is used to display a message to the standard exit
     * Each log will begin with the timestamp of the application
     * Ex :
     * <p>
     * The game is on
     * Player 1 will be playing
     * Player 1 turn to play
     * Player 1 decided to move the Panda
     * Player 2 turn to play
     * ...
     * Player 1 won the game
     * </p>
     *
     * @param message String to be display
     */
    public void logMessage(String message) {
        logMessage(message, levelLog);
    }

    /**
     * {@code logMessage} is used to display a message to the standard exit
     * Each log will begin with the timestamp of the application
     * Ex :
     * <p>
     * The game is on
     * Player 1 will be playing
     * Player 1 turn to play
     * Player 1 decided to move the Panda
     * Player 2 turn to play
     * ...
     * Player 1 won the game
     * </p>
     *
     * @param message String to be display
     * @param level   The Level to be display (not display if Level is at Finest)
     */
    public void logMessage(String message, Level level) {
        LOGGER.log(level, message);
        this.write(message);
    }

    /**
     * Set the default log Level to FINEST to make them invisible
     */
    public void disableLogs() {
        this.levelLog = Level.FINEST;
        this.active = false;
    }

    /**
     * Getter of the logger activation
     *
     * @return True if the logs are active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Print a loading bar in with the logger
     *
     * @param progress  The current progress relative to the total
     * @param total     The total of the progress bar
     */
    public void printLoading(int progress, int total, String otherMessage) {
        StringBuilder loading = new StringBuilder();
        loading.append("\r|");
        for (int i = 0; i < total; i++) {
            if (i < progress) {
                loading.append(getRandomColor()).append("#");
            } else {
                loading.append(" ");
            }
        }

        loading.append(ANSI_RED).append("|\t");
        loading.append(otherMessage);

        System.out.print(ANSI_RED + loading.toString() + ANSI_RESET);
    }

    /**
     * Getter for a random string that change the string output color
     * There should always be a ANSI_RESET at the end of the String if you don't want all your text to have the same color
     * The ANSI_RESET cancel all color for the text after
     *
     * @return A String that change the color of the text after
     */
    private String getRandomColor() {
        int rand = new Random().nextInt(8);
        String res = null;
        switch (rand) {
            case 0: res = ANSI_GREEN;   break;
            case 1: res = ANSI_BLACK;   break;
            case 2: res = ANSI_BLUE;    break;
            case 3: res = ANSI_CYAN;    break;
            case 4: res = ANSI_PURPLE;  break;
            case 5: res = ANSI_YELLOW;  break;
            case 6: res = ANSI_WHITE;   break;
            case 7: res = ANSI_RED;     break;
        }
        return res;
    }

    /**
     * Setter for a Stream Handler to the Logger
     *
     * @param out The Stream where the Logger would write
     */
    public void setStreamHandler(OutputStream out) {
        StreamHandler streamHandler = new StreamHandler(out, customFormat);
        LOGGER.addHandler(streamHandler);
        this.outputStream = out;
    }

    /**
     * Write the given string to the given output stream
     *
     * @param s The String to be write
     */
    public void write(String s) {
        if (outputStream == null) return;
        try (PrintStream printStream = new PrintStream(outputStream)) {
            printStream.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
