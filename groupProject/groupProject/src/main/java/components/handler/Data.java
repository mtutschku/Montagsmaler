package components.handler;

import components.neuralnetwork.Matrix;

/** Daten-Objekt.
 *
 * Besteht entweder nur aus einer Input-Matrix oder auch einer Output-Matrix (Training).
 *
 * @version 9. Juni 2021
 * @author Morris Tutschku
 */
public class Data {

    Matrix inputs;
    Matrix outputs;

    /** Konstruktor
     *
     * @param inputs Inputs
     * @param outputs Outputs
     */
    public Data(Matrix inputs, Matrix outputs){
        this.inputs = inputs;
        this.outputs = outputs;
    }

    /** Konstruktor ohne Outputs
     *
     * @param inputs Inputs
     */
    public Data(Matrix inputs){
        this(inputs, null);
    }

    /** Konstruktor
     *
     * @param inputs Inputs als 2D-Array
     * @param outputs Outputs als 2D-Array
     */
    public Data(double[][] inputs, double[][] outputs){
        this(new Matrix(inputs), new Matrix(outputs));
    }

    /** Konstruktor ohne Outputs
     *
     * @param inputs Inputs als 2D-Array
     */
    public Data(double[][] inputs){
        this(inputs, null);
    }

    /** Gibt die Inputs des Objekts zurück.
     *
     * @return Inputs
     */
    public Matrix getInputs(){
        return inputs;
    }

    /** Gibt die Outputs des Objekts zurück.
     *
     * @return Outputs
     */
    public Matrix getOutputs(){
        if(outputs == null){
            System.err.println("Keine Outputs vorhanden.");
        }
        return outputs;
    }

    /** Gibt die Inputs als Array zurück.
     *
     * @return Inputs
     */
    public double[][] getInputsAsArray(){
        return inputs.getData();
    }

    /** Gibt die Outputs als Array zurück.
     *
     * @return Outputs
     */
    public double[][] getOutputsAsArray(){
        if(outputs == null){
            System.err.println("Keine Outputs vorhanden.");
        }
        return outputs.getData();
    }

}
