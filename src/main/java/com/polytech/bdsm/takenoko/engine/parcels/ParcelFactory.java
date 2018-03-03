package com.polytech.bdsm.takenoko.engine.parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class ParcelFactory {

    private List<Parcel> deck;
    private final Random randomizer;

    /**
     * Normal constructor for a ParcelFactory
     *
     * @param possibleParcels A List of the initial parcels to make the deck
     */
    public ParcelFactory(List<Parcel> possibleParcels, Random randomizer) {
        this.deck = possibleParcels;
        this.randomizer = randomizer;
    }

    /**
     * Draw a Parcel and remove it from the deck
     *
     * @param parcel The parcel to draw
     *
     * @return The drawn parcel
     */
    public Parcel drawParcel(Parcel parcel) throws IllegalArgumentException {
        if (!this.deck.contains(parcel)) throw new IllegalArgumentException("This parcel is not in the deck");

        Parcel result = this.deck.get(this.deck.indexOf(parcel));
        deck.remove(result);
        return result;
    }

    /**
     * Get a number of parcels that exists in the deck,
     * or the maximum number of parcels matching the given number
     *
     * @param numberOfParcels The number of parcels to get in the deck
     *
     * @return A List of the parcels gotten in the deck
     */
    public List<Parcel> getParcelsWithoutDrawing(int numberOfParcels) throws NullPointerException {
        if (this.isEmpty()) throw new NullPointerException("The deck is empty");

        int n;
        List<Parcel> result = new ArrayList<>();
        for (int i = 0; i < deck.size() && i < numberOfParcels; i++) {
            n = randomizer.nextInt(deck.size());
            result.add((Parcel) deck.get(n).clone());
        }

        return result;
    }

    /**
     * Check if the parcels deck is empty or not
     *
     * @return True if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.deck.isEmpty();
    }

    /**
     * Clone method for {@code ParcelFactory}
     *
     * @return a copy of the {@code ParcelFactory}
     */
    @Override
    public Object clone() {
        ArrayList<Parcel> clonedPossibleParcels = new ArrayList<>();
        for (Parcel p : this.deck) {
            clonedPossibleParcels.add((Parcel) p.clone());
        }

        ParcelFactory cloned = new ParcelFactory(clonedPossibleParcels, this.randomizer);
        return cloned;
    }
}
