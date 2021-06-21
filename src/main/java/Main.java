import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import components.gui.*;
import components.handler.*;
import components.neuralnetwork.*;
import javafx.application.Application;


public class Main {
    
    public static void main(String[] args) throws IOException {
        
        ////////////////////////////
        // @author Jakob Hiestermann
        
        
        /** holds constant value m that defines the mxm matrix size used in this project */
        final int M = 28;

        /** constant image dimension, IM_DIMENSIONxIM_DIMENSION */
        final int IM_DIMENSION = 28 * 28;

        /** holds file location of current drawing created inside GUI */
        final String D_LOCATION = "";
        
        /*
        Handler handler = new Handler(M, M);
        Network network = new Network(M*M, 4, 7); // for now an arbitrary number of hidden layers (and outputs) chosen, to be changed adequately
        Data translatedInput;
        Matrix networkGuessM;
        String netWorkGuess;
        
        Application.launch(GUI.class, args);
        
        BufferedImage image_before = null;
        BufferedImage image_current = null;
        File f = null;

        try {
            f = new File(D_LOCATION);
            image_before = new BufferedImage(IM_DIMENSION, IM_DIMENSION, BufferedImage.TYPE_INT_ARGB);
            image_before = ImageIO.read(f);
            image_current = new BufferedImage(IM_DIMENSION, IM_DIMENSION, BufferedImage.TYPE_INT_ARGB);
        } catch(IOException e) {
            System.out.println("Error: " + e);
        }

        while(true) {
            try {
                image_current = ImageIO.read(f);
            } catch(IOException e) {
                System.out.println("Error: " + e);
            }
            if (!image_current.equals(image_before)) {
                image_before = image_current;
                translatedInput = handler.translateImage(image_current);
                networkGuessM = network.feedForward(translatedInput.getInputs());
                //TODO translate Matrix into String stating the drawn object
            }
        } */
        
        ////////////////////////////
        Network n = new Network(2,4,1);
        
        double[][] i1 = {{1},{0}};
        double[][] o1 = {{1}};

        double[][] i2 = {{0},{1}};
        double[][] o2 = {{1}};
        
        double[][] i3 = {{1},{1}};
        double[][] o3 = {{0}};

        double[][] i4 = {{0},{0}};
        double[][] o4 = {{0}};

        Data d1 = new Data(i1, o1);
        Data d2 = new Data(i2, o2);
        Data d3 = new Data(i3, o3);
        Data d4 = new Data(i4, o4);

        Data[] dataSet = {d1, d2, d3, d4};
        
        Random r = new Random();
        for(int i = 0; i < 50000; i++){
            int c = r.nextInt(dataSet.length);
            //n.train(dataSet[c].getInputs(), dataSet[c].getOutputs());
        }

        // Zeile 35 ist auskommentiert, deswegen beginnt das Netzwerk untrainiert.
        NetworkStats.getTrainingStats(n, dataSet, 99.0);  // Output auf Konsole bekommen
        System.out.println("\n" + NetworkStats.accuracy); // nur Genauigkeit als Wert bekommen.
                                                          // Dafür muss aber zuerst getTrainingStats() ausgeführt werden,
                                                          // damit accuracy upgedated wird.

    }

}
