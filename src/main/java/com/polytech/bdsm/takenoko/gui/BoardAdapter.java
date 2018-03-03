package com.polytech.bdsm.takenoko.gui;

import com.polytech.bdsm.takenoko.engine.actions.weather.WeatherDice;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.board.Irrigation;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.graph.Coordinate;
import com.polytech.bdsm.takenoko.engine.graph.GraphNode;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public final class BoardAdapter {

    private BoardAdapter() {}

    /**
     * Compare the actual board with the newBoard and modify it consequently
     *
     * @param newBoard The new board to deal with
     */
    public static void updateBoardViewer(BoardViewer boardViewer, Board newBoard) {

        // Browse into the newBoard's parcels to find differences with current Parcel Views
        for (GraphNode graphNode : newBoard.asList()) {
            boolean alreadyExists = false;
            for (ParcelView parcelView : boardViewer.getParcels()) {
                if (coordinateAdapterEquals(boardViewer.getCenterParcel().getPosition(), parcelView.getPosition(), graphNode.getCoordinate(), boardViewer.getCenterParcel().getEdgeSize())) {
                    alreadyExists = true;
                    break;
                }
            }

            Point2D newCoordinates = coordinateToPoint2D(graphNode.getCoordinate(), boardViewer.getCenterParcel().getPosition(), boardViewer.getCenterParcel().getEdgeSize());
            if (!alreadyExists) {
                boardViewer.addParcelView(parcelToParcelView((Parcel) graphNode,
                        boardViewer.getCenterParcel().getPosition(),
                        boardViewer.getDefaultParcelEdgeSize(),
                        parcelColorToColor(((Parcel) graphNode).getColor())));
            }

            // Check for the bamboos on this parcel
            boardViewer.findParcel(newCoordinates).setBamboos(((Parcel) graphNode).getBambooStack().size());
        }

        // Browse for the irrigations
        for (Irrigation irrigation : newBoard.getIrrigations()) {
            boolean alreadyExists = false;
            for (IrrigationView irrigationView : boardViewer.getIrrigations()) {
                if (areIrrigationsEquals(irrigation, irrigationView, boardViewer.getCenterParcel().getPosition(), boardViewer.getDefaultParcelEdgeSize())) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                boardViewer.addIrrigation(irrigationToIrrigationView(irrigation, boardViewer.getCenterParcel().getPosition(), boardViewer.getDefaultParcelEdgeSize()));
            }
        }

        // Check if the panda position is the same
        if (!coordinateAdapterEquals(boardViewer.getCenterParcel().getPosition(), boardViewer.getPanda().getCurrentPosition(), newBoard.getPanda().getPosition().getCoordinate(), boardViewer.getCenterParcel().getEdgeSize())) {
            boardViewer.movePanda(coordinateToPoint2D(newBoard.getPanda().getPosition().getCoordinate(), boardViewer.getCenterParcel().getPosition(), boardViewer.getCenterParcel().getEdgeSize()));
        }

        // Check if the gardener position is the same
        if (!coordinateAdapterEquals(boardViewer.getCenterParcel().getPosition(), boardViewer.getGardener().getCurrentPosition(), newBoard.getGardener().getPosition().getCoordinate(), boardViewer.getCenterParcel().getEdgeSize())) {
            boardViewer.moveGardener(coordinateToPoint2D(newBoard.getGardener().getPosition().getCoordinate(), boardViewer.getCenterParcel().getPosition(), boardViewer.getCenterParcel().getEdgeSize()));
        }
    }

    public static Color parcelColorToColor(com.polytech.bdsm.takenoko.engine.parcels.Color color) {
        switch (color) {
            case GREEN:
                return Color.GREEN;
            case PINK:
                return Color.PINK;
            case YELLOW:
                return Color.YELLOW;
            default:
                return Color.BLUE;
        }
    }

    public static IrrigationView irrigationToIrrigationView(Irrigation irrigation, Point2D centerReference, int newWidth) {
        return new IrrigationView(parcelToParcelView(irrigation.getParcelToTheLeft(), centerReference, newWidth, Color.WHITE),
                parcelToParcelView(irrigation.getParcelToTheRight(), centerReference, newWidth, Color.WHITE));
    }

    public static boolean areIrrigationsEquals(Irrigation irrigation, IrrigationView irrigationView, Point2D centerReference, int newWidth) {
        return (coordinateAdapterEquals(centerReference, irrigationView.getLeftParcel().getPosition(), irrigation.getParcelToTheLeft().getCoordinate(), newWidth)
                && coordinateAdapterEquals(centerReference, irrigationView.getRightParcel().getPosition(), irrigation.getParcelToTheRight().getCoordinate(), newWidth))
                || (coordinateAdapterEquals(centerReference, irrigationView.getRightParcel().getPosition(), irrigation.getParcelToTheLeft().getCoordinate(), newWidth)
                && coordinateAdapterEquals(centerReference, irrigationView.getLeftParcel().getPosition(), irrigation.getParcelToTheRight().getCoordinate(), newWidth));
    }

    public static ParcelView parcelToParcelView(Parcel parcel, Point2D centerReference, int newWidth, Color color) {
        Point2D newCoordinates = coordinateToPoint2D(parcel.getCoordinate(), centerReference, newWidth);
        return new ParcelView((int) newCoordinates.getX(),
                (int) newCoordinates.getY(),
                newWidth,
                color,
                facilityToFacilityView(parcel.getFacility()));
    }

    public static FacilityView facilityToFacilityView(Facility facility) {
        switch (facility) {
            case POND_FACILITY:
                return FacilityView.POND_FACILITY;
            case FERTILIZER_FACILITY:
                return FacilityView.FERTILIZER_FACILITY;
            case PADDOCK_FACILITY:
                return FacilityView.PADDOCK_FACILITY;
            default:
                return FacilityView.NO_FACILITY;
        }
    }

    public static WeatherView weatherToWeatherView(WeatherDice weather) {
        switch (weather) {
            case SUN:
                return WeatherView.SUN;
            case RAIN:
                return WeatherView.RAIN;
            case WIND:
                return WeatherView.WIND;
            case STORM:
                return WeatherView.STORM;
            case CLOUDS:
                return WeatherView.CLOUDS;
            default:
                return WeatherView.NONE;
        }
    }

    public static Point2D coordinateToPoint2D(Coordinate coordinate, Point2D centerReference, int newWidth) {
        return new Point2D(centerReference.getX() + (newWidth * 1.7 * coordinate.getX()), centerReference.getY() + (newWidth * coordinate.getY()));
    }

    public static boolean coordinateAdapterEquals(Point2D centerReference, Point2D point2D, Coordinate coordinate, int newWidth) {
        return (point2D.getX() - centerReference.getX()) / (newWidth * 1.7) == coordinate.getX() && (point2D.getY() - centerReference.getY()) / (newWidth) == coordinate.getY();
    }
}
