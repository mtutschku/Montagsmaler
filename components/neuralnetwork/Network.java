package components.neuralnetwork;

/** Neuronales Netzwerk.
 * 
 * Funktionen zum Bedienen, Trainieren und Modifizieren des Netzwerks.
 * Das Netzwerk hat zunächst eine Input-Layer (= Grauwerte der Leinwandpixel), eine Hidden-Layer und
 * Output-Layer (= Klassifikation).
 * 
 * @version 8. Juni 2021
 * @author Morris Tutschku
 */
public class Network {

    /** Anzahl Inputs */
    int INPUT_SIZE;

    /** Anzahl Neuronen in der Hidden-Layer */
    int HIDDEN_SIZE;

    /** Anzahl Outputs (= Anzahl mögl. Klassifikationen) */
    int OUTPUT_SIZE;

    /** Matrix für Gewichte zwischen Input-Layer und Hidden-Layer */
    Matrix weightsIH;

    /** Matrix für Gewichte zwischen Hidden-Layer und Output-Layer */
    Matrix weightsHO;

    /** Matrix für bias der Hidden-Layer-Neuronen */
    Matrix biasH;

    /** Matrix für bias der Output-Layer-Neuronen */
    Matrix biasO;

    /** Konstruktor
     * 
     * Initialisiert die Architektur des Netzwerks. Erzeugt zufällige Gewichts- und bias-Matrizen.
     * 
     * @param in Anzahl Inputs
     * @param hidden Anzahl Hidden-Layer-Neuronen
     * @param out Anzahl Outputs
     */
    public Network(int in, int hidden, int out){
        if(in <= 0 || hidden <= 0 || out <= 0){
            System.err.println("Ungültiges Netzwerk-Layout");
            return;
        }
        
        INPUT_SIZE = in;
        HIDDEN_SIZE = hidden;
        OUTPUT_SIZE = out;
        weightsIH = new Matrix(HIDDEN_SIZE, INPUT_SIZE);
        weightsHO = new Matrix(OUTPUT_SIZE, HIDDEN_SIZE);
        biasH = new Matrix(HIDDEN_SIZE, 1);
        biasO = new Matrix(OUTPUT_SIZE, 1);

        // Gewichte zufällig initialisieren
        weightsIH.randomize();
        weightsHO.randomize();
        biasH.randomize();
        biasO.randomize();
    }

    /** Gibt alle Informationen zum Netzwerk auf der Konsole aus. */
    public void print(){
        System.out.println("\nweightsIH:");
        weightsIH.print();
        System.out.println("\nweightsHO:");
        weightsHO.print();
    }

    /** Aktivierungsfunktion für das Netzwerk.
     * 
     * Hier wird die Sigmoidfunktion verwendet.
     * sigmoid(x) := 1/(1+e^-x)
     *  
     * @param x Input
     * @return Output (sigmoid(x))
     */
    public static double sigmoid(double x){
        return 1.0/(1.0 + Math.exp(-x));
    }

    /** Aktivierungsfunktion auf einer gesamten Matrix anwenden.
     * 
     * @param m Matrix
     * @return Sigmoid-Matrix
     */
    public static Matrix sigmoid(Matrix m){
        Matrix output = new Matrix(m.getRows(), m.getCols());

        for(int i = 0; i < m.getRows(); i++){
            for(int j = 0; j < m.getCols(); j++){
                output.setValue(i+1, j+1, sigmoid(m.getData()[i][j])); 
            }
        }

        return output;
    }

    /** Funktion zum Verarbeiten einer Inputmatrix (= Pixel der Leinwand). */
    public Matrix feedForward(Matrix input){
        if(input.getRows() != INPUT_SIZE || input.getCols() != 1){
            System.err.println("Ungültiger Input. (Muss" + INPUT_SIZE + "x1 Matrix sein)");
            return new Matrix(0,0);
        }
        
        // Inputs -> Hidden-Layer
        Matrix h = Matrix.multiply(weightsIH, input);
        h = Matrix.add(h, biasH);
        h = Network.sigmoid(h);

        // Hidden-Layer -> Output
        Matrix o = Matrix.multiply(weightsHO, h);
        o = Matrix.add(o, biasO);
        o = Network.sigmoid(o);

        return o;
    }

}
