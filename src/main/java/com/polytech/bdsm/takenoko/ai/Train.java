package com.polytech.bdsm.takenoko.ai;

import com.polytech.bdsm.takenoko.logger.GameLogger;

/**
 * @author Bureau De Sébastien Mosser
 * @version 7.0
 */

public class Train {

    public static void main(String[] args) throws Exception {
//        try (Graph g = new Graph()) {
//            final String value = "Hello from TensorFlow " + TensorFlow.version();
//
//            // Construct the computation graph with a single operation, a constant
//            // named "MyConst" with a value "value".
//            try (Tensor t = Tensor.create(value.getBytes("UTF-8"))) {
//                // The Java API doesn't yet include convenience functions for adding operations.
//                g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
//            }
//
//            // Execute the "MyConst" operation in a Session.
//            try (Session s = new Session(g);
//                 Tensor output = s.runner().fetch("MyConst").run().get(0)) {
//                System.out.println(new String(output.bytesValue(), "UTF-8"));
//            }
//        }


//        NeuralNetwork network = new NeuralNetwork(2);
//
//        System.out.println("Random starting synaptic weights:");
//        System.out.println(Arrays.deepToString(network.getSynapticWeights().getData()));
//
//        double[] input_1 = {0, 0, 1};
//        double[] input_2 = {1, 1, 1};
//        double[] input_3 = {1, 0, 1};
//        double[] input_4 = {0, 1, 1};
//        Values inputs = new Values(new double[][]{input_1, input_2, input_3, input_4});
//
//        System.out.println("Inputs:");
//        System.out.println(inputs);
//
//        double[][] output = {{0, 1, 1, 0}};
//        Values outputs = new Values(output);
//        outputs.setMatrix(outputs.getMatrix().transpose());
//
//        System.out.println("Outputs:");
//        System.out.println(outputs);
//
//        network.train(inputs, outputs, 4, 10000);
//
//        System.out.println("New synaptic weights after training:");
//        System.out.println(Arrays.deepToString(network.getSynapticWeights().getData()));
//        System.out.println(network.toString());
//
//        double[][] values = network.think(new Values(new double[][]{{1, 0, 0}})).getMatrix().getData();
//        System.out.println("Reconsidering new situation [1, 0, 0] -> ?: ");
//        System.out.println(Arrays.deepToString(values));

//        long BEGIN = System.currentTimeMillis();
//
//        QLearning obj = new QLearning();
//
//        obj.run();
//        obj.printResult();
//        obj.showPolicy();
//
//        long END = System.currentTimeMillis();
//        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");

        GameLogger logger = new GameLogger();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 50; i++) {
            if (i >= 1) stringBuilder.append("\r");
            stringBuilder.append("|");
            for (int j = 0; j < i; j++) {
                stringBuilder.append("=");
            }
            for (int j = i; j < 50; j++) {
                stringBuilder.append(" ");
            }
            stringBuilder.append("|");
            logger.logMessage(stringBuilder.toString());
            Thread.sleep(100);
        }

    }


}
