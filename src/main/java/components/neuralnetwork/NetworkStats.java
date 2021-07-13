package components.neuralnetwork;

import java.util.Random;
import java.util.ArrayList;

import components.handler.Data;
import javafx.application.Application;


/** Klasse für Statistiken und zum Testen eines Netzwerks.
 * 
 * Hier befinden sich Tools zum Testen der Effizienz eines Netzwerks und
 * Berechnen einer (fast) optimalen Architektur eines Netzwerks mit gegebenen
 * Inputs und Outputs (dank sei der Heuristik).
 * 
 * @version 10. Juni 2021
 * @author Morris Tutschku
 */
public class NetworkStats {

    /** Outputs für verschiedene Statistiken regulieren */
    static boolean printGCA = true;
    static boolean printGTS = true;
    static boolean printGAS = true;

    /** Outputs optional weiter eindämmen */
    final static int skip = 80;
    static int skipCounter = 0;

    /** Maß, ob Netzwerk "schnell genug" lernt */
    final static int NO_RESULT = 500000;

    /** wenn counter bis zu NO_RESULT hochzählt, lernt das Netzwerk nicht schnell genug */
    static int noResultCounter = 0;

    /** true wenn Netzwerk stecken bleibt */
    static boolean noResult = false;

    /** zur Berechnung der Abbruch-Wahrscheinlichkeit (= Feststecken eines Netzwerks) */
    static double maxQuotient = 0.0;

    /** absoluter Fehler eines Netzwerks bei bestimmtem Datenset */
    static double errorAbs = 0.0;

    /** relativer Fehler eines Netzwerks (= Fehler pro Output) */
    static double errorRel = 0.0;

    /** Genauigkeit eines Netzwerks */
    public static double accuracy = 0.0;

    /** Startzeit für getTrainingStats() */
    static long start = 0;

    /** Endzeit */
    static long finish = 0;

    /** Sekunden, bis Netzwerk bestimmte Genauigkeit erreicht */
    public static double diff = 0.0;

    /** Epochen pro Sekunde, die das Netzwerk erreicht (kein genaues Maß!) */
    public static int eps = 0;

    /** Schneidet Nachkommastellen einer Kommazahl ab.
     * 
     * @param d Kommazahl
     * @param length Anzahl Nachkommastellen
     * @return gekürzte Kommazahl
     */
    public static double cut(double d, int length){
        if(length < 0){
            System.err.println("Anzahl der Nachkommastellen darf nicht negativ sein.");
            return 0.0;
        }

        int tmp = (int) (d * Math.pow(10, length));
        return tmp/Math.pow(10.0, length);
    }

    /** Setzt Fehlerwerte auf 0. */
    public static void resetError(){
        errorAbs = 0.0;
        errorRel = 0.0;
        accuracy = 0.0;
    }

    /** Setzt Trainingstats auf 0. */
    public static void resetTrainingStats(){
        start = 0;
        finish = 0;
        diff = 0.0;
        eps = 0;
        noResultCounter = 0;
        noResult = false;
        maxQuotient = 0.0;
    }

    /** Berechnet die durchschnittliche Genauigkeit eines Netzwerks.
     * 
     * @param n Netzwerk
     * @param dataSet Datenset mit bekannten Outputs
     * @param print Genauigkeit auf Konsole ausgeben
     */
    public static void getCurrentAccuracy(Network n, Data[] dataSet){
        resetError();
        int length = dataSet.length;

        for(int j = 0; j < length; j++){
            Matrix guess = n.feedForward(dataSet[j].getInputs());
            Matrix output = dataSet[j].getOutputs();
            Matrix diff = Matrix.subtract(guess, output);
            for(int k = 0; k < diff.getData().length; k++){
                errorAbs += Math.abs(diff.getData()[k][0]);
            }
        }
        
        errorRel = errorAbs / dataSet.length;
        accuracy = cut((1.0 - errorRel) * 100.0, 1);    // Achtung: wird bei hohen errorRel-Werten unsinnig lol

        if(printGCA){
            System.out.println("-> Genauigkeitstest (" + dataSet.length + " Tests durchgeführt):");
            System.out.println("     - absoluter Fehler: " + errorAbs);
            System.out.println("     - relativer Fehler pro Output: " + errorRel);
            System.out.println("     - Genauigkeit: " + accuracy + "%");
        }
    }

    /** Gibt Statistiken zum Lernprozess eines Netzwerks an.
     * 
     * @param n Netzwerk (sollte untrainiert sein)
     * @param dataSet Datenset mit bekannten Outputs
     * @param accuracy gewünschte Genauigkeit
     */
    public static void getTrainingStats(Network n, Data[] dataSet, double accuracy){
        if(accuracy <= 0){
            System.err.println("Genauigkeit muss positiv sein.");
            return;
        }
        
        resetTrainingStats();
        printGCA = false;
        int counter = 0;
        double oldAcc = 0.0;

        if(printGTS) System.out.println("-> Training-Statistik:");
        getCurrentAccuracy(n, dataSet);
        start = System.currentTimeMillis();

        while(NetworkStats.accuracy < accuracy){
            Random r = new Random();
            int rc = r.nextInt(dataSet.length);
            oldAcc = NetworkStats.accuracy;
            n.train(dataSet[rc].getInputs(), dataSet[rc].getOutputs());
            getCurrentAccuracy(n, dataSet);
            counter++;
            
            if(!String.valueOf(oldAcc).equals(String.valueOf(NetworkStats.accuracy)) && !skip()){  // Jaja, nicht schön
                if(printGTS){
                    System.out.println("     " + NetworkStats.accuracy + "% Genauigkeit nach " + counter + " Epochen");
                    noResultCounter = 0;
                }
            } else {
                noResultCounter++;
                double currentQuotient = Double.valueOf(noResultCounter) / Double.valueOf(NO_RESULT);
                if(currentQuotient > maxQuotient) maxQuotient = cut(currentQuotient, 3);
                if(noResultCounter >= NO_RESULT){
                    if(printGTS){
                        System.err.println("[!] Das Netzwerk steckt fest oder lernt nicht schnell genug.");
                        System.err.println("[!] Entweder NetworkStats.NO_RESULT hochschrauben, oder Netzwerkarchitektur ändern.");
                    
                        // TODO: Feststecken eines Netzwerks besser erkennen
                    }
                    noResult = true;
                    break;
                }
            }
        }

        // Achtung: Vergangene Zeit darf nur mit anderen Zeiten von getTrainingStats() verglichen werden, sonst kein sinnvolles Maß für Effizienz.
        finish = System.currentTimeMillis();
        diff = (finish - start)/1000.0;
        eps = (int) cut((double) counter/(diff), 2);
        if(printGTS){
            System.out.println("\n     " + NetworkStats.accuracy + "% Genauigkeit nach " + counter + " Epochen");
            System.out.println("     (" +  diff + " Sekunden, " + eps + " Epochen/Sekunde)");
        }

        printGCA = true;
    }

    /** Findet das Netzwerk mit der (fast) effizientesten Architektur.
     * 
     * @param dataSet Datenset mit bekannten Outputs
     * @param maxNeurons maximale Anzahl an Hidden-Layer-Neuronen
     * @param acc gewünschte Genauigkeit
     */
    public static void getArchStats(Data[] dataSet, int maxNeurons, double acc){
        if(dataSet[0].getOutputs() == null){
            System.err.println("Datenset muss bekannte Outputs enthalten.");
            return;
        }

        if(maxNeurons <= 2){
            System.err.println("Anzahl an Hidden-Layer-Neuronen muss mindestens 2 sein.");
            return;
        }

        int inputSize = dataSet[0].getInputs().getRows();
        int outputSize = dataSet[0].getOutputs().getRows();
        double bestTime = Double.MAX_VALUE;
        int bestNeuronCount = 0;
        String bestStats = "";
        printGTS = false;



        if(printGAS) System.out.println("-> Architektur-Statistik:"); 

        for(int hidden = 2; hidden <= maxNeurons; hidden++){
            Network n = new Network(inputSize, hidden, outputSize);
            getTrainingStats(n, dataSet, acc);
            if(printGAS){
                String arch = "(" + inputSize + "," + hidden + "," + outputSize + ")";
                System.out.print("     - " + arch + ": ");
                if(noResult){
                    System.out.println("zu langsam (Abbruch nach " + NO_RESULT + " Trainings ohne Verbesserung)");
                } else {
                    String stats = acc + "% Genauigkeit in " + diff + " Sekunden mit " + eps + " Epochen/Sekunde, Abbruchniveau: " + cut(maxQuotient * 100.0, 1) + "%";
                    System.out.println(stats);
                    if(diff < bestTime){
                        bestTime = diff;
                        bestNeuronCount = hidden;
                        bestStats = arch + ": " + stats;    
                    }
                }
            } 
        }

        if(printGAS){
            System.out.println("\n     Das schnellste Netzwerk hatte " + bestNeuronCount + " Hidden-Layer-Neuronen:");
            System.out.println("     " + bestStats);
        }

        printGTS = true;
    }

    /** Gibt an, wie wenig getTrainingStats() ausgeben soll (alle skipCounter Mal statt jedes mal) */
    public static boolean skip(){
        if(skipCounter < skip){
            skipCounter++;
            return true;
        } else {
            skipCounter = 0;
            return false;
        }
    }
}
