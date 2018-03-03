package com.polytech.bdsm.takenoko.engine.board;

import com.polytech.bdsm.takenoko.engine.facilities.FacilityFactory;
import com.polytech.bdsm.takenoko.engine.facilities.PossibleFacilities;
import com.polytech.bdsm.takenoko.engine.goals.GoalFactory;
import com.polytech.bdsm.takenoko.engine.goals.PossibleGoals;
import com.polytech.bdsm.takenoko.engine.graph.Graph;
import com.polytech.bdsm.takenoko.engine.graph.GraphNode;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.ParcelFactory;
import com.polytech.bdsm.takenoko.engine.parcels.PondParcel;
import com.polytech.bdsm.takenoko.engine.parcels.PossibleParcels;
import com.polytech.bdsm.takenoko.engine.pnj.Gardener;
import com.polytech.bdsm.takenoko.engine.pnj.PNJ;
import com.polytech.bdsm.takenoko.engine.pnj.Panda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class Board extends Graph {

    private final int seed;
    private final Random globalRandom;
    private PNJ panda, gardener;
    private GoalFactory goalsDeck;
    private ParcelFactory parcelsDeck;
    private FacilityFactory facilityDeck;
    private int irrigationsDeck = 20;
    List<Irrigation> irrigations = new ArrayList<>();

    /**
     * Normal constructor
     *
     * @param seed The random seed
     */
    public Board(int seed) {
        super(new PondParcel());
        this.seed = seed;
        this.globalRandom = new Random(seed);

        this.goalsDeck = new GoalFactory(PossibleGoals.getPossibleGoals(), this.globalRandom);
        this.parcelsDeck = new ParcelFactory(PossibleParcels.getPossibleParcels(), this.globalRandom);
        this.facilityDeck = new FacilityFactory(PossibleFacilities.getPossibleFacilities());

        this.panda = new Panda((Parcel) this.root);
        this.gardener = new Gardener((Parcel) this.root);
    }

    /**
     * Normal Constructor
     */
    public Board() {
        this(new Random().nextInt());
    }

    /**
     * Constructor for the Board
     *
     * @param root the root of the board
     * @param seed the random seed
     */
    private Board(Parcel root, int seed) {
        super(root);
        this.seed = seed;
        this.globalRandom = new Random(seed);
    }

    /**
     * Getter of the seed
     *
     * @return The seed used for this board
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Getter of the globalRandom
     *
     * @return The random used in this game
     */
    public Random getGlobalRandom() {
        return globalRandom;
    }

    /**
     * Getter for the ParcelFactory
     *
     * @return The Parcel Deck
     */
    public ParcelFactory getParcelsDeck() {
        return parcelsDeck;
    }

    /**
     * Getter for the FacilityFactory
     *
     * @return The Facility Deck
     */
    public FacilityFactory getFacilityDeck() {
        return facilityDeck;
    }

    /**
     * Getter of the PNJ <code>Gardener</code>
     *
     * @return The Gardener
     */
    public PNJ getGardener() {
        return gardener;
    }

    /**
     * Getter of the PNJ <code>Panda</code>
     *
     * @return The Panda
     */
    public PNJ getPanda() {
        return panda;
    }

    /**
     * Getter of the irrigations
     *
     * @return A List of irrigations
     */
    public List<Irrigation> getIrrigations() {
        return new ArrayList<>(irrigations);
    }

    /**
     * Method to add an irrigation to the board
     *
     * @param irrigation The irrigation to add
     *
     * @return true if the irrigation was added correctly, false otherwise
     */
    public boolean addIrrigation(Irrigation irrigation) {
        return this.irrigations.add(irrigation);
    }

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    @Override
    public String toString() {
        return "Board : " + super.toString();
    }

    /**
     * Getter of the <code>Pond</code> whose the root of the <code>Graph</code>
     *
     * @return The Pond of the Board
     */
    public PondParcel getPond() {
        return (PondParcel) root;
    }

    /**
     * Get a Parcel present in this Board
     *
     * @param p A Parcel equals to a Parcel in this Board
     *
     * @return The reference of the Parcel found
     *
     * @throws Exception If the parcel is not found in this Board
     */
    public Parcel getParcel(Parcel p) throws Exception {
        List<GraphNode> nodes = this.asList();
        for (GraphNode gn : nodes) {
            if (gn.equals(p)) {
                return (Parcel) gn;
            }
        }

        throw new Exception("The parcel was not found in this board : " + p);
    }

    /**
     * Getter of the goalsDeck
     *
     * @return The GoalFactory goalsDeck
     */
    public GoalFactory getGoalsDeck() {
        return goalsDeck;
    }

    /**
     * Getter of the irrigations left to draw
     *
     * @return The number of irrigations in the deck
     */
    public int getIrrigationsDeck() {
        return this.irrigationsDeck;
    }

    /**
     * Draw an irrigation from the irrigations deck
     *
     * @return The number of irrigations
     */
    public int drawIrrigation() {
        if (this.irrigationsDeck > 0) {
            irrigationsDeck--;
            return 1;
        }

        return 0;
    }

    /**
     * Method to clone the board into another board object
     *
     * @return a cloned board of the actual board
     */
    @Override
    public Object clone() {
        Board copy = new Board((PondParcel) this.cloneGraph(this.getPond()), this.seed);
        for (Irrigation irrigation : this.irrigations) {
            try {
                copy.irrigations.add(new Irrigation(copy.getParcel(irrigation.getParcelToTheLeft()), copy.getParcel(irrigation.getParcelToTheRight())));
            } catch (Exception ignored) {}
        }

        try {
            copy.gardener = new Gardener(copy.getParcel(this.getGardener().getPosition()));
            copy.panda = new Panda(copy.getParcel(this.getPanda().getPosition()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        copy.goalsDeck = (GoalFactory) this.goalsDeck.clone();
        copy.parcelsDeck = (ParcelFactory) this.parcelsDeck.clone();
        copy.irrigationsDeck = this.irrigationsDeck;
        return copy;
    }

}
