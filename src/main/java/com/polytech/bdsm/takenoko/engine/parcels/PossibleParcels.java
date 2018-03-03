package com.polytech.bdsm.takenoko.engine.parcels;

import com.polytech.bdsm.takenoko.engine.facilities.FertilizerLeaf;
import com.polytech.bdsm.takenoko.engine.facilities.PaddockLeaf;
import com.polytech.bdsm.takenoko.engine.facilities.PondLeaf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public final class PossibleParcels {

    /**
     * Private Constructor to block instantiation
     */
    private PossibleParcels() {}

    /**
     * Getter for all the possible parcels in the parcel deck
     *
     * @return a list of all the possible parcels in the parcel deck
     */
    public static List<Parcel> getPossibleParcels() {
        List<Parcel> possibleParcels = new ArrayList<>();

        // 11 green Parcels (later with facilities )
        IntStream.range(0, 6).forEach(i -> possibleParcels.add(new GreenParcel()));
        IntStream.range(0, 2).forEach(i -> possibleParcels.add(new PaddockLeaf(new GreenParcel())));
        IntStream.range(0, 2).forEach(i -> possibleParcels.add(new PondLeaf(new GreenParcel())));
        possibleParcels.add(new FertilizerLeaf(new GreenParcel()));

        // 7 pink Parcels (later with facilities)
        IntStream.range(0, 4).forEach(i -> possibleParcels.add(new PinkParcel()));
        possibleParcels.add(new PaddockLeaf(new PinkParcel()));
        possibleParcels.add(new PondLeaf(new PinkParcel()));
        possibleParcels.add(new FertilizerLeaf(new PinkParcel()));

        // 9 yellow Parcels (later with facilities)
        IntStream.range(0, 6).forEach(i -> possibleParcels.add(new YellowParcel()));
        possibleParcels.add(new PaddockLeaf(new YellowParcel()));
        possibleParcels.add(new PondLeaf(new YellowParcel()));
        possibleParcels.add(new FertilizerLeaf(new YellowParcel()));

        return possibleParcels;
    }
}
