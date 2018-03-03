package com.polytech.bdsm.takenoko.engine.player;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class Statistic {

    private int nbGame = 0;

    private int wonGame = 0;

    private int lostGame = 0;

    private int tiedGame = 0;

    private int totalScore = 0;

    private String player;

    /**
     * Constructor of stat
     *
     * @param player The Player's name (with the strategy if the player is a bot)
     */
    public Statistic(String player) {
        this.player = player;
    }

    /**
     * Update the average score, the number of game that was ether won, lost or tied
     *
     * @param hasPayerWon   If the Player has won or not
     * @param equals        If more then one Player has won the Game
     * @param score         The score of the Player for this Game
     */
    public void update(boolean hasPayerWon, boolean equals, int score) {
        wonGame += !equals && hasPayerWon ? 1 : 0;
        lostGame += !equals && !hasPayerWon ? 1 : 0;
        tiedGame += equals ? 1 : 0;
        totalScore += score;
        nbGame++;
    }

    /**
     * Getter of the average score of the {@code Player}
     *
     * @return The average score
     */
    private double getAverageScore() {
        return (double) totalScore / nbGame;
    }

    /**
     * Getter of the rate of games tied
     *
     * @return The rate of games tied
     */
    private double getTiedGame() {
        return (double) (tiedGame * 100) / nbGame;
    }

    /**
     * Getter for the rate of games lost
     *
     * @return The rate of games lost
     */
    private double getLostGame() {
        return (double) (lostGame * 100) / nbGame;
    }

    /**
     * Getter for the rate of games won
     *
     * @return The rate of games won
     */
    private double getWonGame() {
        return (double) (wonGame * 100) / nbGame;
    }

    /**
     * Setter for the name player
     *
     * @param player The Player name
     */
    public void setPlayer(String player) {
        this.player = player;
    }

    /**
     * Clone of Statistic with the same value as the current object
     *
     * @return The new clone object
     */
    @Override
    public Object clone() {
        Statistic stat = new Statistic(player);
        stat.lostGame = lostGame;
        stat.nbGame = nbGame;
        stat.tiedGame = tiedGame;
        stat.totalScore = totalScore;
        stat.wonGame = wonGame;
        return stat;
    }

    /**
     * ToString method
     *
     * @return A representation of the current Object in a String form
     */
    @Override
    public String toString() {
        return player + " game stats :" +
                "\n\t\t\t\t\tWon rate : \t\t" + Math.round((getWonGame() * 100) / 100) + "% \t with " + wonGame + " won game" + (wonGame > 1 ? "s" : "") +
                "\n\t\t\t\t\tLost rate : \t" + Math.round((getLostGame() * 100) / 100) + "% \t with " + lostGame + " lost game" + (lostGame > 1 ? "s" : "") +
                "\n\t\t\t\t\tTied rate : \t" + Math.round((getTiedGame() * 100) / 100) + "% \t\t with " + tiedGame + " tied game" + (tiedGame > 1 ? "s" : "") +
                "\n\t\t\t\t\tAverage score : " + Math.round((getAverageScore() * 100) / 100);
    }
}
