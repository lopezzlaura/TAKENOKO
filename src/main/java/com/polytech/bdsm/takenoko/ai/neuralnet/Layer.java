package com.polytech.bdsm.takenoko.ai.neuralnet;

import java.util.Arrays;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */

public class Layer {

    private Neuron[] neurons;

    public Layer(Neuron[] neurons) {
        this.neurons = neurons;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public void adjustSynapsesWeight(double[] expectedOutput) {
        for (int i = 0; i < expectedOutput.length; i++) {
            neurons[i].adjustSynapseInputWeight(neurons[i].getError(expectedOutput[i]), expectedOutput[i]);
        }
    }

    public void processNeuronInputs() {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].processInputs();
        }
    }

    public double[] getOutputs() {
        double[] outputs = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            outputs[i] = neurons[i].getPrediction();
        }
        return outputs;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "\n\t\tneurons=" + Arrays.toString(neurons) +
                '}';
    }
}
