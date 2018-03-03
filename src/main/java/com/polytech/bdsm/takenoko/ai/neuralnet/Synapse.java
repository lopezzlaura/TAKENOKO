package com.polytech.bdsm.takenoko.ai.neuralnet;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */

public class Synapse {

    private double weight;
    private double value = 0;

    public Synapse(double weight) {
        this.weight = weight;
    }

    public void adjustWeight(double value) {
        this.weight += value;
    }

    public double getWeight() {
        return weight;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    protected Object clone() {
        Synapse synapse = new Synapse(this.weight);
        synapse.setValue(this.value);
        return synapse;
    }

    @Override
    public String toString() {
        return "\n\t\t\t\t\tSynapse{" +
                "\tweight=" + weight +
                "\tvalue=" + value +
                '}';
    }
}
