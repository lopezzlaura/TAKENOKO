package com.polytech.bdsm.takenoko.ai.neuralnet;

import java.util.Arrays;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class Neuron {

    private Synapse[] inputs;
    private Synapse[] outputs;
    private double slope = 0.3;

    public Neuron() {
        this.inputs = new Synapse[0];
        this.outputs = new Synapse[0];
    }

    public void addInputs(Synapse input) {
        Synapse[] inputClone = new Synapse[this.inputs.length + 1];

        for (int i = 0; i < this.inputs.length; i++) {
            inputClone[i] = this.inputs[i];
        }

        inputClone[this.inputs.length] = input;

        this.inputs = inputClone;
    }

    public void addOutputs(Synapse output) {
        Synapse[] outputClone = new Synapse[this.outputs.length + 1];

        for (int i = 0; i < this.outputs.length; i++) {
            outputClone[i] = this.outputs[i];
        }

        outputClone[this.outputs.length] = output;

        this.outputs = outputClone;
    }

    public Synapse[] getInputs() {
        return inputs;
    }

    public Synapse[] getOutputs() {
        return outputs;
    }

    private double inputFunction() {
        double[] inputs = new double[this.inputs.length];
        double[] weights = new double[this.inputs.length];

        for (int i = 0; i < this.inputs.length; i++) {
            weights[i] = this.inputs[i].getWeight();
            inputs[i] = this.inputs[i].getValue();
        }

        double sum = 0;

        for (int i = 0; i < this.inputs.length; i++) {
            sum += weights[i] * inputs[i];
        }

        return sum;
    }

    public double getError(double output) {
        return output - this.getPrediction();
    }

    public void adjustSynapseInputWeight(double error, double output) {
        double x = error * MathUtils.sigmoidDerivative(output);
        for (int i = 0; i < this.inputs.length; i++) {
            this.inputs[i].adjustWeight(this.inputs[i].getWeight() + x);
        }
    }

    public double getPrediction() {
        double in = this.inputFunction();
        return MathUtils.sigmoid(in);
    }

    public void processInputs() {
        double output = this.getPrediction();
        for (Synapse synapse : this.outputs) {
            synapse.setValue(output);
        }
    }

    @Override
    public String toString() {
        return "\n\t\t\tNeuron{" +
                "\n\t\t\t\tinputs=" + Arrays.toString(inputs) +
                "\n\t\t\t\toutputs=" + Arrays.toString(outputs) +
                "\n\t\t\t\tslope=" + slope +
                '}';
    }
}
