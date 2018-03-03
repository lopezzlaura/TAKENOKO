package com.polytech.bdsm.takenoko.ai.neuralnet;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.Random;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */

public final class MathUtils {

    private static Random random = new Random();

    private MathUtils() {}

    public static RealMatrix sigmoid(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                matrix.setEntry(i, j, (1 / (1 + Math.exp(-matrix.getEntry(i, j)))));
            }
        }
        return matrix;
    }
    public static double sigmoid(double x) {
        return (1 / (1 + Math.exp(-x)));
    }

    public static RealMatrix sigmoidDerivative(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                matrix.setEntry(i, j, matrix.getEntry(i, j) * (1 - matrix.getEntry(i, j)));
            }
        }
        return matrix;
    }

    public static double sigmoidDerivative(double x) {
        return x * (1 - x);
    }

    public static double getRandomValue(int min, int max) {
        return (random.nextDouble() * (max - min)) - max;
    }


}
