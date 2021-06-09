package components.neuralnetwork;

import components.handler.Data;

/** Klasse für Statistiken und zum Testen eines Netzwerks.
 * 
 * Hier befinden sich Tools zum Testen der Effizienz eines Netzwerks und
 * Berechnen einer (fast) optimalen Architektur eines Netzwerks mit gegebenen
 * Inputs und Outputs (dank sei der Heuristik).
 * 
 * @version 9. Juni 2021
 * @author Morris Tutschku
 */
public class NetworkStats {

    /** Berechnet die durchschnittliche Genauigkeit eines Netzwerks
     *  und gibt diese auf der Konsole aus.
     * 
     * @param n Netzwerk
     * @param dataSet Datenset mit bekannten Outputs
     */
    public static void getCurrentAccuracy(Network n, Data[] dataSet){
        int length = dataSet.length;
        double errorRel = 0.0;
        double errorAbs = 0.0;
        double accuracy = 0.0;

        for(int j = 0; j < length; j++){
            Matrix guess = n.feedForward(dataSet[j].getInputs());
            Matrix output = dataSet[j].getOutputs();
            Matrix diff = Matrix.subtract(guess, output);
            for(int k = 0; k < diff.getData().length; k++){
                errorAbs += Math.abs(diff.getData()[k][0]);
            }
        }
        
        errorRel = errorAbs / dataSet.length;
        accuracy = (1.0 - errorRel) * 100.0;    // Achtung: wird bei hohen Werten unsinnig lol
        String accuracyS = String.valueOf(accuracy);
        if(accuracyS.length() > 4){
            accuracyS = accuracyS.substring(0, 4);
        }

        System.out.println("-> Genauigkeitstest (" + dataSet.length + " Tests durchgeführt):");
        System.out.println("     - absoluter Fehler: " + errorAbs);
        System.out.println("     - relativer Fehler pro Output: " + errorRel);
        System.out.println("     - Genauigkeit: " + accuracyS + "%");
    }

    // TODO
}
