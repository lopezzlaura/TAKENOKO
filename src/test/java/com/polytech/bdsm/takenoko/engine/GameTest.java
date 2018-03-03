package com.polytech.bdsm.takenoko.engine;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.GardenerStrategy;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.ai.strategies.ParcelStrategy;
import com.polytech.bdsm.takenoko.ai.strategies.RandomStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.PandaGoal;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Bureau De Sébastien Mosser
 */


class GameTest {

    private Game game;
    private Player sherlock, moriarty;
    private Board board;


    @BeforeEach
    void setUp() {
        board = new Board();
        sherlock = new Bot("Sherlock", board, new PandaStrategy(), new Random());
        moriarty = new Bot("Moriarty", board, new PandaStrategy(), new Random());
        game = new Game(board, 1, sherlock, moriarty);

    }

    @Test
    void emperorVisitTest() throws Exception {
        //Test avec 1 objectif
        List<Goal> goals = sherlock.getGoals();
        sherlock.getGoals().removeAll(goals);
        List<Goal> goals2 = moriarty.getGoals();
        moriarty.getGoals().removeAll(goals2);
        List<Bamboo> bamboos = new ArrayList<>();
        bamboos.add(new Bamboo(Color.GREEN));
        bamboos.add(new Bamboo(Color.GREEN));
        sherlock.getGoals().add(new PandaGoal(2, bamboos));
        moriarty.getGoals().add(new PandaGoal(2, bamboos));
        int turn_count = 0;
        Player[] players = {sherlock, moriarty};
        boolean running = true;
        Player firstToFinish = null;
        while (running) {
            for (Player p : players) {
                game.nextTurn(p);
                running = p.getInventory().getAccomplishedGoal() < 1;
                if (!running) {
                    firstToFinish = p;
                    break;
                }
            }
            if (!running) break;
            turn_count++;
        }
        int score = firstToFinish.getScore();
        game.emperorVisit(firstToFinish);
        assertEquals(score + 2, firstToFinish.getScore());

    }

    @Disabled
    @Test
    void gameNotTooLong() {
        int turnMax = 50;
        int maxThread = Runtime.getRuntime().availableProcessors() * 4;
        int maxVal = Integer.MAX_VALUE;
        AtomicInteger nbGame = new AtomicInteger(0);
        // Test 2 147 483 647 Games (value will probably be changed over time to a smaller number because it is way to much)
        int nbGamePerThread = maxVal / maxThread;
        int rest = maxVal - (nbGamePerThread * maxThread);

        ExecutorService executor = Executors.newFixedThreadPool(maxThread);

        for (int i = 0; i < maxThread; i++) {
            executor.execute(new Thread(() -> {
                int nbTurn = 0;
                Game g = new Game(board);
                Board b = new Board();
                Player s = new Bot("", b, new PandaStrategy(), new Random());
                Player m = new Bot("", b, new PandaStrategy(), new Random());
                for (int j = 0; j < nbGamePerThread; j++) {
                    while ((s.getInventory().getAccomplishedGoal() <= 1) && (m.getInventory().getAccomplishedGoal() <= 1)) {
                        try {
                            g.nextTurn(s);
                            g.nextTurn(m);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        nbTurn++;
                    }
                    assertFalse(nbTurn > turnMax);
                    g = new Game(board);
                    nbTurn = 0;
                    nbGame.incrementAndGet();
                }
            }));
        }

        executor.shutdown();

        while (!executor.isTerminated()) ;

        assertEquals(maxVal, nbGame.get() + rest);
    }

    @Test
    void getCurrentWinnersTest() throws Exception {
        Player sherlock = new Bot("Sherlock",   board, new PandaStrategy(), 183, new Random());
        Player moriarty = new Bot("Moriarty",   board, new PandaStrategy(), 173, new Random());
        Player watson   = new Bot("Watson",     board, new PandaStrategy(), new Random());
        Player mycroft  = new Bot("Mycroft",    board, new PandaStrategy(), new Random());

        List<Player> winners = new ArrayList<>();

        Game game = new Game(board, sherlock, moriarty, watson, mycroft);

        sherlock.addPoints(50);
        moriarty.addPoints(30);
        watson.addPoints(20);
        mycroft.addPoints(25);

        winners.add(sherlock);

        assertEquals(winners, game.getCurrentWinners());

        sherlock.clear(board);
        watson.clear(board);
        moriarty.clear(board);
        mycroft.clear(board);

        sherlock.addPoints(50);
        moriarty.addPoints(50);
        watson.addPoints(50);
        mycroft.addPoints(50);

        winners.clear();
        winners.add(sherlock);
        winners.add(moriarty);
        winners.add(watson);
        winners.add(mycroft);

        assertEquals(winners, game.getCurrentWinners());

        winners.clear();
        winners.add(sherlock);

        sherlock.getInventory().addGoal(new PandaGoal(5, new ArrayList<>()));

        assertEquals(winners, game.getCurrentWinners());

        winners.clear();
        winners.add(sherlock);
        winners.add(moriarty);
        winners.add(watson);
        winners.add(mycroft);

        moriarty.getInventory().addGoal(new PandaGoal(5, new ArrayList<>()));
        watson.getInventory().addGoal(new PandaGoal(5, new ArrayList<>()));
        mycroft.getInventory().addGoal(new PandaGoal(5, new ArrayList<>()));

        assertEquals(winners, game.getCurrentWinners());

        // Test cas de non égalité a cause des points ses panda goal

        sherlock.clear(board);
        watson.clear(board);
        moriarty.clear(board);
        mycroft.clear(board);

        sherlock.addPoints(50);
        moriarty.addPoints(50);
        watson.addPoints(50);
        mycroft.addPoints(50);


        winners.clear();
        winners.add(moriarty);

        moriarty.getInventory().addGoal(new PandaGoal(10, new ArrayList<>()));
        watson.getInventory().addGoal(new PandaGoal(5, new ArrayList<>()));
        mycroft.getInventory().addGoal(new PandaGoal(5, new ArrayList<>()));

        assertEquals(winners, game.getCurrentWinners());

        sherlock.clear(board);
        watson.clear(board);
        moriarty.clear(board);
        mycroft.clear(board);

        sherlock.addPoints(50);
        moriarty.addPoints(50);
        watson.addPoints(50);
        mycroft.addPoints(50);

        winners.clear();
        winners.add(watson);
        winners.add(mycroft);

        moriarty.getInventory().addGoal(new PandaGoal(5, new ArrayList<>()));
        watson.getInventory().addGoal(new PandaGoal(10, new ArrayList<>()));
        mycroft.getInventory().addGoal(new PandaGoal(10, new ArrayList<>()));

        assertEquals(winners, game.getCurrentWinners());
    }

    @Disabled
    @Test
    void gameSeedTest() throws Exception {
        Board board = new Board(0);

        Player sherlock = new Bot("Sherlock", board, new PandaStrategy(), board.getGlobalRandom());
        Player moriarty = new Bot("Moriarty", board, new GardenerStrategy(), board.getGlobalRandom());
        Player watson = new Bot("Watson", board, new ParcelStrategy(), board.getGlobalRandom());
        Player mycroft = new Bot("Mycroft", board, new RandomStrategy(true), board.getGlobalRandom());

        Game game = new Game(board, sherlock, moriarty, watson, mycroft);

        game.startGame();

        assertEquals(sherlock, game.getCurrentWinners().get(0));

        assertEquals(17, game.getTurnCount());
    }
}