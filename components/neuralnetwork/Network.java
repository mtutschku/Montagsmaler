package components.neuralnetwork;

/** Neuronales Netzwerk.
 * 
 * Funktionen zum Bedienen, Trainieren und Modifizieren des Netzwerks.
 * Das Netzwerk hat zunächst eine Input-Layer (= Grauwerte der Leinwandpixel), eine Hidden-Layer und
 * Output-Layer (= Klassifikation).
 * 
 * @version 9. Juni 2021
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

    /** globale Matrizen zum Trainieren */
    Matrix MI, MH, MO;

    /** Lernrate des Netzwerks (0,1) */
    final static double LR = 0.1;

    /** Anzahl der Trainingsdurchläufe */
    final static int epochs = 10000;

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
        System.out.println("NN-Architektur:");
        System.out.println("  - Anzahl Inputneuronen: " + INPUT_SIZE);
        System.out.println("  - Anzahl Hidden-Layer-Neuronen: " + HIDDEN_SIZE);
        System.out.println("  - Anzahl Outputneuronen: " + OUTPUT_SIZE);
        System.out.println("\nGewichte:");
        System.out.println("  - Input-Hidden:");
        weightsIH.print();
        System.out.println("  - Hidden-Output:");
        weightsHO.print();
        System.out.println("\nbias:");
        System.out.println("  - Input-Hidden:");
        biasH.print();
        System.out.println("  - Hidden-Output:");
        biasO.print();
        System.out.println("\nLernrate: " + LR);
        System.out.println("Epochen: " + epochs);
        System.out.println("Aktivierungsfunktion: Sigmoid");
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

    /** Ableitung der Sigmoidfunktion.
     * 
     * Benötigt zum Berechnen des Gradienten des Fehlers.
     * ACHTUNG: Bei feedForward() wird x bereits zu sigmoid(x) umgewandelt,
     * deswegen steht hier x statt sigmoid(x).
     * 
     * @param x Input
     * @return Output (Ableitung von sigmoid(x))
     */
    public static double sigmoid_dx(double x){
        return x * (1.0 - x);
    }

    /** Ableitung der Sigmoidfunktion auf einer gesamten Matrix.
     * 
     * @param m Input
     * @return Output (Matrix mit Ableitung von sigmoid(x))
     */
    public static Matrix sigmoid_dx(Matrix m){
        Matrix output = new Matrix(m.getRows(), m.getCols());

        for(int i = 0; i < m.getRows(); i++){
            for(int j = 0; j < m.getCols(); j++){
                output.setValue(i+1, j+1, sigmoid_dx(m.getData()[i][j])); 
            }
        }

        return output;
    }

    /** Funktion zum Verarbeiten einer Inputmatrix (= Pixel der Leinwand).
     * 
     * @param input Inputs für das Netzwerk in (INPUT_SIZE x 1)-Matrix
     * @return Klassifikation ("guess") des Netzwerks
     */
    public Matrix feedForward(Matrix input){
        if(input.getRows() != INPUT_SIZE || input.getCols() != 1){
            System.err.println("Ungültiger Input. (Muss " + INPUT_SIZE + "x1 Matrix sein)");
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

        MI = input;
        MH = h;
        MO = o;

        return o;
    }

    /** Trainiert das Netzwerk mittels supervised learning.
     * 
     * Inputs mit bekannten (= gewünschten) Outputs werden dem Netzwerk übergeben und
     * die Gewichte entsprechend angepasst werden.
     * 
     * @param inputs Inputs
     * @param outputs Outputs
     */
    public void train(Matrix inputs, Matrix outputs){
        Matrix guess = feedForward(inputs);
        Matrix errorO = Matrix.subtract(outputs, guess); // Fehler der Outputs
        Matrix weightsHO_t = Matrix.transpose(weightsHO);
        Matrix errorH = Matrix.multiply(weightsHO_t, errorO); // Fehler der Hidden-Layer-Neuronen
        
        // delta_weightsHO = LR * error * sigmoid_dx * h

        // Gewichte und bias zwischen Output und Hidden-Layer anpassen
        Matrix gradientHO = sigmoid_dx(MO);
        gradientHO = Matrix.multiplyElement(gradientHO, errorO);
        gradientHO = Matrix.scale(gradientHO, LR);

        Matrix ho_t = Matrix.transpose(MH); // transponierte Gewichtsmatrix zwischen Hidden-Layer und Output
        
        Matrix weightsHO_delta = Matrix.multiply(gradientHO, ho_t);

        weightsHO = Matrix.add(weightsHO, weightsHO_delta);
        biasO = Matrix.add(biasO, gradientHO);

        // Gewichte und bias zwischen Hidden-Layer und Inputs anpassen
        Matrix gradientIH = sigmoid_dx(MH);
        gradientIH = Matrix.multiplyElement(gradientIH, errorH);
        gradientIH = Matrix.scale(gradientIH, LR);

        Matrix ih_t = Matrix.transpose(MI);

        Matrix weightsIH_delta = Matrix.multiply(gradientIH, ih_t);

        weightsIH = Matrix.add(weightsIH, weightsIH_delta);
        biasH = Matrix.add(biasH, gradientIH);
    }

}
