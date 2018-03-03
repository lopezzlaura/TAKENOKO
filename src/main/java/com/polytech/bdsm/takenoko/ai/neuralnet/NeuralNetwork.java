package com.polytech.bdsm.takenoko.ai.neuralnet;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */

public class NeuralNetwork {

    private Layer[] layers;
    private Synapse[] inputSynapse;
    private Synapse[] outputSynapse;

    private RealMatrix synapticWeights;

    public NeuralNetwork(int nbLayer) {
        double[][] weights = new double[3][1];
        int j = 0;
        for (double[] tab : weights) {
            for (int i = 0; i < tab.length; i++) {
                weights[j][i] = MathUtils.getRandomValue(-1, 1);
            }
            j++;
        }
        this.synapticWeights = new Array2DRowRealMatrix(weights);

        layers = new Layer[nbLayer];
        int nbInput = 3;
        int nbOutput = 1;
        buildNetwork(nbInput, nbOutput);
    }

    public void train(Values trainingSetInputs, Values trainingSetOutputs, int nbSet, int iterations) {
        for (int iteration = 1; iteration <= iterations; iteration++) {

            Values output = this.think(trainingSetInputs);

            RealMatrix error = trainingSetOutputs.getMatrix().subtract(output.getMatrix());

            RealMatrix outputSigmoid = MathUtils.sigmoidDerivative(output.getMatrix());

            for (int i = 0; i < error.getRowDimension(); i++) {
                for (int j = 0; j < error.getColumnDimension(); j++) {
                    error.multiplyEntry(i, j, outputSigmoid.getEntry(i, j));
                }
            }

            RealMatrix adjustment = trainingSetInputs.getMatrix().transpose().multiply(error);

            this.synapticWeights = this.synapticWeights.add(adjustment);
        }
    }

    public Values think(Values inputs) {
        return new Values (MathUtils.sigmoid(inputs.getMatrix().multiply(this.synapticWeights)).getData());
    }

    public RealMatrix getSynapticWeights() {
        return synapticWeights;
    }

//    public void train(Values inputTrainingSet, Values outputTrainingSet, int nbSet, int iterations) {
//        for (int i = 1; i <= iterations; i++) {
//            for (int j = 0; j < nbSet - 1; j++) {
//
//                double[] outputs = this.think(inputTrainingSet.getMatrix().getRow(j));
//                double[] expectedOutputs = outputTrainingSet.getMatrix().getRow(j);
//
//                for (int k = 0; k < outputSynapse.length; k++) {
//                    outputs[k] = outputSynapse[k].getValue();
//                    outputSynapse[k].adjustWeight((outputs[k] - expectedOutputs[k]) * MathUtils.sigmoidDerivative(outputs[k]));
//                }
//
//                for (int k = layers.length - 1; k >= 0; k--) {
//                    double[] temp = layers[k].getOutputs();
//                    layers[k].adjustSynapsesWeight(outputs);
//                    outputs = temp;
//                }
//            }
//        }
//    }
//
//    public double[] think(double[] inputs) {
//
//        for (int i = 0; i < inputs.length; i++) {
//            inputSynapse[i].setValue(inputs[i]);
//        }
//
//        for (int i = 0; i < layers.length; i++) {
//            layers[i].processNeuronInputs();
//        }
//
//        double[] outputs = new double[this.outputSynapse.length];
//
//        for (int i = 0; i < outputs.length; i++) {
//            outputs[i] = this.outputSynapse[i].getValue();
//        }
//
//        return outputs;
//    }
//
//    public double[] getSynapticWeights() {
//
//        int nbSynapse = outputSynapse.length;
//
//        for (int i = 0; i < layers.length; i++) {
//            for (int j = 0; j < layers[i].getNeurons().length; j++) {
//                nbSynapse += layers[i].getNeurons()[j].getInputs().length;
//            }
//        }
//
//        double[] synapses = new double[nbSynapse];
//
//        int i = 0;
//
//        for (int j = 0; j < layers.length; j++) {
//            for (int k = 0; k < layers[j].getNeurons().length; k++) {
//                for (int l = 0; l < layers[j].getNeurons()[k].getInputs().length; l++) {
//                    synapses[i++] = layers[j].getNeurons()[k].getInputs()[l].getWeight();
//                }
//            }
//        }
//
//        for (int j = 0; j < outputSynapse.length; j++) {
//            synapses[i++] = outputSynapse[j].getWeight();
//        }
//
//        return synapses;
//    }

    public void buildNetwork(int nbInput, int nbOutput) {
        Neuron[] neurons = new Neuron[4];

        for (int j = 0; j < layers.length - 1; j++) {
            for (int i = 0; i < neurons.length; i++) {
                neurons[i] = new Neuron();
            }

            layers[j] = new Layer(neurons);
        }

        neurons = new Neuron[nbOutput];

        for (int i = 0; i < nbOutput; i++) {
            neurons[i] = new Neuron();
        }

        layers[layers.length - 1] = new Layer(neurons);

        connectAllNeurons();


        inputSynapse = new Synapse[layers[0].getNeurons().length * nbInput];
        outputSynapse = new Synapse[nbOutput];


        for (int i = 0; i < inputSynapse.length; i++) {
            inputSynapse[i] = new Synapse(MathUtils.getRandomValue(-1, 1));
            layers[0].getNeurons()[i % nbInput].addInputs(new Synapse(MathUtils.getRandomValue(-1, 1)));
        }

        for (int i = 0; i < outputSynapse.length; i++) {
            outputSynapse[i] = new Synapse(MathUtils.getRandomValue(-1, 1));
            layers[layers.length - 1].getNeurons()[i].addOutputs(new Synapse(MathUtils.getRandomValue(-1, 1)));
        }
    }

    public void connectAllNeurons() {
        for (int i = 0; i < layers.length - 1; i++) {
            for (int j = 0; j < layers[i].getNeurons().length; j++) {
                for (int k = 0; k < layers[i + 1].getNeurons().length; k++) {
                    connect(layers[i].getNeurons()[j], layers[i + 1].getNeurons()[k]);
                }
            }
        }
    }

    public void connect(Neuron from, Neuron to) {
        Synapse synapse = new Synapse(MathUtils.getRandomValue(-1, 1));
        from.addOutputs(synapse);
        to.addInputs(synapse);
    }

    @Override
    public String toString() {
        return "NeuralNetwork{" +
                "\n\tlayers=" + Arrays.toString(layers) +
                "\n\n\tinputSynapse=" + Arrays.toString(inputSynapse) +
                "\n\n\toutputSynapse=" + Arrays.toString(outputSynapse) +
                "}\n\n";
    }
}
