package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.graph.GraphNode;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public enum ParcelGoalPattern {

    LINE("line") {
        public boolean isValid(Board g, Color... colors) {
            return threeIsValid(g, 3, true, colors);
        }

        @Override
        public boolean isAlmostValid(Board g, Color... colors) {
            return threeAlmostValid(g, 3, colors);
        }
    },
    SEMILINE("curved line") {
        public boolean isValid(Board g, Color... colors) {
            return threeIsValid(g, 2, true, colors);
        }

        @Override
        public boolean isAlmostValid(Board g, Color... colors) {
            return threeAlmostValid(g, 2, colors);
        }
    },
    TRIANGLE("triangle") {
        public boolean isValid(Board g, Color... colors) {
            return threeIsValid(g, 1, true, colors);
        }

        @Override
        public boolean isAlmostValid(Board g, Color... colors) {
            return threeAlmostValid(g, 1, colors);
        }
    },
    DIAMOND("diamond") {
        public boolean isValid(Board board, Color... colors) {
            List<GraphNode> allNodes = board.asList();
            Parcel leftNeighbour, alignedParcel, lastParcel;

            for (GraphNode gn : allNodes) {
                for (int i = 0; i < gn.getDegree(); i++) {
                    if (((Parcel) gn).getColor() == colors[0] && BoardUtils.isParcelIrrigated(board, (Parcel) gn)) {
                        leftNeighbour = (Parcel) gn.getNeighbours()[i];
                        if (leftNeighbour != null && leftNeighbour.getColor() == colors[0] && BoardUtils.isParcelIrrigated(board, leftNeighbour)) {
                            alignedParcel = (Parcel) gn.getNeighbours()[(i + 1) % 6];
                            if (alignedParcel != null && (colors.length > 1 ? alignedParcel.getColor() == colors[1] : alignedParcel.getColor() == colors[0]) && BoardUtils.isParcelIrrigated(board, alignedParcel)) {
                                lastParcel = (colors.length > 1 ? alignedParcel.getNeighbours()[i] == null || ((Parcel) alignedParcel.getNeighbours()[i]).getColor() != colors[1]
                                        ? (Parcel) alignedParcel.getNeighbours()[(i + 3) % 6] : (Parcel) alignedParcel.getNeighbours()[i] : (Parcel) alignedParcel.getNeighbours()[i]);
                                if (lastParcel != null && (colors.length > 1 ? lastParcel.getColor() == colors[1] : lastParcel.getColor() == colors[0]) && BoardUtils.isParcelIrrigated(board, lastParcel)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }

        /**
         * Check if the Diamond shape is almost valid, meaning with a triangle or with a curved line
         * @param board  The current board
         * @param colors The given colors
         * @return
         */
        @Override
        public boolean isAlmostValid(Board board, Color... colors) {
            if (colors.length == 1) {
                return threeIsValid(board, 1, false, colors) || threeIsValid(board, 2, false, colors);

            } else {
                List<GraphNode> allNodes = board.asList();
                boolean checkColors = false;

                for (GraphNode gn : allNodes) {
                    for (int i = 0; i < gn.getDegree(); i++) {
                        checkColors = checkBothColors(gn, i, colors[0], colors[1]) || checkBothColors(gn, i, colors[1], colors[0]);
                        if (checkColors) return true;
                    }

                }

                return false;
            }
        }
    };

    private String name;

    /**
     * Method to construct a parcel pattern
     *
     * @param name the name of the pattern
     */
    ParcelGoalPattern(String name) {
        this.name = name;
    }

    /**
     * Method to get the name of the pattern
     *
     * @return the name of the pattern
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param board  The current Board
     * @param n      index depending on the pattern needed
     * @param colors the given colors for the pattern
     * @return true if the parcels correspond to the pattern, false otherwise
     */
    private static boolean threeIsValid(Board board, int n, boolean checkirrigations, Color... colors) {

        List<GraphNode> allNodes = board.asList();
        Parcel leftNeighbour, alignedParcel;

        for (GraphNode gn : allNodes) {
            for (int i = 0; i < gn.getDegree(); i++) {
                if (((Parcel) gn).getColor() == colors[0] && (!checkirrigations || BoardUtils.isParcelIrrigated(board, (Parcel) gn))) {
                    leftNeighbour = (Parcel) gn.getNeighbours()[i];
                    if (leftNeighbour != null && leftNeighbour.getColor() == colors[0] && (!checkirrigations || BoardUtils.isParcelIrrigated(board, leftNeighbour))) {
                        alignedParcel = (Parcel) gn.getNeighbours()[(i + n) % 6];
                        if (alignedParcel != null && alignedParcel.getColor() == colors[0] && (!checkirrigations || BoardUtils.isParcelIrrigated(board, alignedParcel))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param board  The current Board
     * @param n      index depending on the pattern needed
     * @param colors the given color for the pattern
     * @return true if to parcels are in the right place for a pattern and the
     * third one can be placed, false otherwise
     */
    private static boolean threeAlmostValid(Board board, int n, Color... colors) {

        List<GraphNode> allNodes = board.asList();
        Parcel leftNeighbour, alignedParcel;

        for (GraphNode gn : allNodes) {
            for (int i = 0; i < gn.getDegree(); i++) {
                if (((Parcel) gn).getColor() == colors[0]) {
                    leftNeighbour = (Parcel) gn.getNeighbours()[i];
                    alignedParcel = (Parcel) gn.getNeighbours()[(i + n) % 6];

                    if ((leftNeighbour != null && leftNeighbour.getColor() == colors[0] && alignedParcel == null)
                            || (alignedParcel != null && alignedParcel.getColor() == colors[0] && leftNeighbour == null)) {
                        return true;
                    } else if (leftNeighbour == null && alignedParcel == null) {
                        //Si il n'y a pas de voisin
                        //On clone le board
                        Board boardCpy = (Board) board.clone();

                        //Si on cherche un type LINE
                        if (n == 3) {
                            try {

                                //On ajoute un GraphNode là où pourrait se situer les parcelles à placer (dans le bon alignement)
                                GraphNode toTest = (GraphNode) gn.clone();
                                boardCpy.getParcel((Parcel) gn).addNode(i, toTest);
                                boardCpy.getParcel((Parcel) gn).addNode(((i + n) % 6), toTest);

                                //On vérifie que dans l'alignement de cette parcelle il existe une autre parcelle existante et de la bonne couleur
                                if ((gn.getNeighbours()[i].getNeighbours()[i] != null && ((Parcel) gn.getNeighbours()[i].getNeighbours()[i]).getColor() == colors[0]) ||
                                        (gn.getNeighbours()[(i + n) % 6].getNeighbours()[(i + n) % 6] != null && ((Parcel) gn.getNeighbours()[(i + n) % 6].getNeighbours()[(i + n) % 6]).getColor() == colors[0])) {
                                    return true;
                                }

                            } catch (Exception ignored) { }

                            //Si on cherche un type CURVED LINE (SEMI LINE)
                        } else if (n == 2) {
                            try {
                                //On ajoute un GraphNode là où pourrait se situer les parcelles à placer (dans le bon alignement)
                                GraphNode toTest = (GraphNode) gn.clone();
                                boardCpy.getParcel((Parcel) gn).addNode(i, toTest);
                                boardCpy.getParcel((Parcel) gn).addNode(((i + n) % 6), toTest);

                                //On vérifie que dans l'alignement de cette parcelle il existe une autre parcelle existante et de la bonne couleur
                                if ((gn.getNeighbours()[i].getNeighbours()[i] != null && ((Parcel) gn.getNeighbours()[i].getNeighbours()[i]).getColor() == colors[0]) ||
                                        (gn.getNeighbours()[(i + n) % 6].getNeighbours()[(i + n) % 6] != null && ((Parcel) gn.getNeighbours()[(i + n) % 6].getNeighbours()[(i + n) % 6]).getColor() == colors[0])) {
                                    return true;
                                }
                            } catch (Exception ignored) { }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check an "almost-diamond" pattern with 2 combinaisons of colors
     *
     * @param gn     current GraphNode
     * @param i      index given
     * @param colors colors to check
     * @return true if the pattern is valid with both combinaisons of color
     */
    private static boolean checkBothColors(GraphNode gn, int i, Color... colors) {
        Parcel leftNeighbour, rightNeighbour;

        if (((Parcel) gn).getColor() == colors[0]) {

            //Get the neighbour of current parcel at index i
            leftNeighbour = (Parcel) gn.getNeighbours()[i];

            //if it is not null, irrigated and has the same color as the current parcel's color (color[0])
            if (leftNeighbour != null && leftNeighbour.getColor() == ((Parcel) gn).getColor()) {

                //check the parcels on the right and on the left of the leftNeighbour parcel (triangle pattern)
                rightNeighbour = (Parcel) gn.getNeighbours()[(i + 1) % 6];

                //if it is not null, irrigated and has the second color given
                if (rightNeighbour != null && (colors.length > 1 ? rightNeighbour.getColor() == colors[1] : rightNeighbour.getColor() == colors[0])) {
                    return true;
                } else {
                    //check the parcels in a semi line pattern
                    rightNeighbour = (Parcel) gn.getNeighbours()[(i + 2) % 6];

                    //if it is not null, irrigated and has the second color given
                    if (rightNeighbour != null && (colors.length > 1 ? rightNeighbour.getColor() == colors[1] : rightNeighbour.getColor() == colors[0])) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /**
     * Method to check if the pattern is valid
     *
     * @param board  the current board
     * @param colors the colors given
     * @return true if the pattern is found, false otherwise
     */
    public abstract boolean isValid(Board board, Color... colors);

    /**
     * Method to check if the pattern is almost valid
     *
     * @param board  The current board
     * @param colors The given colors
     * @return true if the pattern is almost valid, false otherwise
     */
    public abstract boolean isAlmostValid(Board board, Color... colors);
}
