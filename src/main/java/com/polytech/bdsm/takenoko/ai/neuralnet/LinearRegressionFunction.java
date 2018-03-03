package com.polytech.bdsm.takenoko.ai.neuralnet;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */

public class LinearRegressionFunction implements Function<Double[], Double> {

    private final double[] thetaVector;

    LinearRegressionFunction(double[] thetaVector) {
        this.thetaVector = Arrays.copyOf(thetaVector, thetaVector.length);
    }

    public Double apply(Double[] featureVector) {
        // for computational reasons the first element has to be 1.0
        assert featureVector[0] == 1.0;

        // simple, sequential implementation
        double prediction = 0;
        for (int j = 0; j < thetaVector.length; j++) {
            prediction += thetaVector[j] * featureVector[j];
        }
        return prediction;
    }

    public double[] getThetas() {
        return Arrays.copyOf(thetaVector, thetaVector.length);
    }
}


