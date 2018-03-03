package com.polytech.bdsm.takenoko.engine.board;

import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.graph.GraphNode;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PondParcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public final class BoardUtils {

    /**
     * Private Constructor to block instantiation
     */
    private BoardUtils() {}

    /**
     * Find all parcels with a bamboo of the given size
     *
     * @param bambooSize The size of bamboos searched on all parcels
     *
     * @return A List of all the parcels that contains a bamboo of the given size
     */
    public static List<Parcel> findParcelWithBamboos(Board board, int bambooSize) {
        List<Parcel> parcelsWithBamboos = new ArrayList<>();
        board.BFS(gn -> {
            if (((Parcel) gn).getBambooStack().size() == bambooSize) {
                parcelsWithBamboos.add((Parcel) gn);
            }
        });
        return parcelsWithBamboos;
    }

    /**
     * Get the different <code>Parcel</code> possible to move a PNJ on
     *
     * @return Three directional HashMap containing all possible parcels where a <code>PNJ</code> can be moved on
     */
    public static PossibleMovesResult getPossibleMoves(Parcel start) {
        HashMap<Integer, List<Parcel>> result = new HashMap<>();

        // Three directions parcel filter
        GraphNode current;
        for (int i = 0; i < start.getDegree(); i++) {
            current = start;
            while (current.getNeighbours()[i] != null) {
                if (!result.containsKey(i % 3)) {
                    result.put(i % 3, new ArrayList<>());
                }

                current = current.getNeighbours()[i];
                result.get(i % 3).add((Parcel) current);
            }
        }

        return new PossibleMovesResult(result);
    }

    /**
     * Get the different parcels that can be placed on the board
     *
     * @return An HashMap containing the different placeable parcels, and the possible indexes of each one of them
     */
    public static PossiblePlacingResult getPossibleParcelsToPlace(Board board) {
        HashMap<Parcel, List<Integer>> result = new HashMap<>();

        board.BFS(gn -> IntStream.range(0, 6).filter(((Parcel) gn)::isPlaceable).forEach(value -> {
            if (!result.containsKey(gn)) result.put((Parcel) gn, new ArrayList<>());
            result.get(gn).add(value);
        }));

        return new PossiblePlacingResult(result);
    }

    /**
     * Method to detect if a Parcel is Irrigated or not
     *
     * @param parcel The Parcel to check the irrigation
     *
     * @return true if the Parcel is irrigated, false either
     *
     * @see Parcel#isIrrigated()
     */
    public static boolean isParcelIrrigated(Board board, Parcel parcel) {
        if (parcel.isIrrigated()) return true;

        for (Irrigation i : board.irrigations) {
            try{
                if (board.getParcel(i.getParcelToTheLeft()).equals(board.getParcel(parcel)) || board.getParcel(i.getParcelToTheRight()).equals(board.getParcel(parcel))) {
                    return true;
                }
            } catch (Exception e){
                return false;
            }
        }

        return false;
    }

    /**
     * Get the different possible irrigation to placed on the {@code Board}
     *
     * @return A {@code List} that contain irrigation that can be placed on the board
     *
     * @see PossibleIrrigationResult
     */
    public static PossibleIrrigationResult getPossibleIrrigations(Board board) {
        List<Irrigation> result = new ArrayList<>();
        List<GraphNode> allNodes = board.asList();

        // On enlève le pond de allNodes
        allNodes.remove(board.getRoot());

        int i;
        GraphNode leftNeighbour, rightNeighbour;
        Irrigation irrigationToCheck, toAddLeft, toAddRight;

        for (GraphNode node : allNodes) {
            // On parcours les voisins d'une node
            for (i = 0; i < node.getDegree(); i++) {
                leftNeighbour = node.getNeighbours()[i];
                rightNeighbour = node.getNeighbours()[(i + 1) % 6];

                // Si un des deux voisins selectionnés n'existe pas, on ne crée pas d'irrigation
                if (leftNeighbour != null && rightNeighbour != null) {
                    irrigationToCheck = new Irrigation((Parcel) leftNeighbour, (Parcel) rightNeighbour);

                    // Si cette irrigation est présente dans les irrigations du plateau,
                    // on peut ajouter deux nouvelles irrigations possibles
                    if (board.irrigations.contains(irrigationToCheck)) {
                        toAddLeft = new Irrigation((Parcel) node, (Parcel) leftNeighbour);
                        toAddRight = new Irrigation((Parcel) node, (Parcel) rightNeighbour);

                        if (!result.contains(toAddLeft)) {
                            result.add(toAddLeft);
                        }

                        if (!result.contains(toAddRight)) {
                            result.add(toAddRight);
                        }
                    }
                }
            }
        }

        // On ajoute les irrigations autour du PondParcel
        for (i = 0; i < 6; i++) {
            if (board.getPond().getNeighbours()[i] != null && board.getPond().getNeighbours()[(i + 1) % 6] != null) {
                irrigationToCheck = new Irrigation((Parcel) board.getPond().getNeighbours()[i], (Parcel) board.getPond().getNeighbours()[(i + 1) % 6]);
                if (!board.irrigations.contains(irrigationToCheck) && !result.contains(irrigationToCheck)) {
                    result.add(irrigationToCheck);
                }
            }
        }

        result.removeAll(board.irrigations);
        return new PossibleIrrigationResult(result);
    }

    /**
     * Get all the parcels of a Board
     *
     * @param board The board to get the parcels
     *
     * @return A List of all the parcels in the specified Board
     */
    public static List<Parcel> getAllParcels(Board board) {
        List<GraphNode> nodes = board.asList();
        List<Parcel> allParcels = new ArrayList<>();
        for (GraphNode node : nodes) {
            allParcels.add((Parcel) node);
        }

        return allParcels;
    }

    /**
     * Get all the parcel that have no Facility
     *
     * @param board The board to get the parcels from
     *
     * @return All the parcels that have no Facility on them
     */
    public static List<Parcel> getPossibleFacilityParcels(Board board) {
        return BoardUtils.getAllParcels(board).stream()
                .filter(parcel -> parcel.getBambooStack().empty())
                .filter(parcel -> parcel.getFacility() == Facility.NO_FACILITY && !(parcel instanceof PondParcel))
                .collect(Collectors.toList());
    }
}
