import java.util.Random;

import components.gui.*;
import components.handler.*;
import components.neuralnetwork.Matrix;
import components.neuralnetwork.Network;
import components.neuralnetwork.NetworkStats;
import javafx.application.Application;


public class Main {

    public static void main(String[] args){
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

        // @author Jakob Hiestermann - test Array-class:

        System.out.println("------------\nMeta test:");

        Meta meta = new Meta();
        System.out.println(meta.toString());
        String metaRandom1 = meta.getRandomNext(false);
        System.out.println(metaRandom1);
        System.out.println(meta.toString());

        String metaRandom2 = meta.getRandomNext(true);
        System.out.println(metaRandom2);
        System.out.println(meta.toString());

        // @author Pascal Uhlendorff - GUI

        Application.launch(GUI.class, args);

    }

}