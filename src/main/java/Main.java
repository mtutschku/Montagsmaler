import components.gui.*;
import components.handler.*;
import components.neuralnetwork.*;

import java.io.IOException;
import javafx.application.Application;

public class Main {
    
    public static void main(String[] args) throws IOException {
        /** holds constant value m that defines the mxm matrix size used in this project. */
        final int M = 28;

        /** GUI setup and launch */
        Translator translator = new Translator(M);
        Network network = new Network(M*M, 8, 4); // TODO: to be changed to actual number of categories

        Matrix biasH = PreTrained.getTrainedBiasH();
        Matrix biasO = PreTrained.getTrainedBiasO();
        Matrix weightsIH = PreTrained.getTrainedWeightsIH();
        Matrix weightsHO = PreTrained.getTrainedWeightsHO();

        network.setParams(biasH, biasO, weightsIH, weightsHO);

        GUI.setTranslator(translator);
        GUI.setNetwork(network);
        Application.launch(GUI.class, args);
    }

}
