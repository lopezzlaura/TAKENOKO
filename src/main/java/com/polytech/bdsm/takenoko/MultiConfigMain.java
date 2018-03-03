package com.polytech.bdsm.takenoko;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class MultiConfigMain {

    /**
     * Starting point of the program
     *
     * --config option:
     *      followed by other arguments of a classic Main
     *
     * Each --config will launch a Main
     *
     * @param args  The possible argument of the program
     */
    public static void main(String[] args) {
        int nbConfig = 0;
        String arg;
        List<String> newArgs;

        for (int i = 0, j; i < args.length; i++) {
            arg = args[i];
            if (arg.equals("--config") || arg.equals("-c")) {

                if (i + 1 >= args.length) {
                    System.err.println("Error syntax: no arguments after --config or -c");
                    break;
                }

                nbConfig++;
                newArgs = new ArrayList<>();

                for (j = i + 1; j < args.length && !(args[j].equals("--config") || args[j].equals("-c")); j++) {
                    newArgs.add(args[j]);
                }

                System.out.println("Configuration " + nbConfig + " will begin:");
                Main.main(newArgs.toArray(new String[newArgs.size()]));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("\nConfiguration " + nbConfig + " is finished...\n");
            }
        }
    }
}
