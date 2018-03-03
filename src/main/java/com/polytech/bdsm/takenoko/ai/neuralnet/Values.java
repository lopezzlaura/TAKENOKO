package com.polytech.bdsm.takenoko.ai.neuralnet;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */

public class Values {

    private RealMatrix values;

    public Values(double[][] values) {
        this.values = new Array2DRowRealMatrix(values);
    }

    public RealMatrix getMatrix() {
        return values;
    }

    public void setMatrix(RealMatrix matrix) {
        this.values = matrix;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(values.getData());
    }
}
