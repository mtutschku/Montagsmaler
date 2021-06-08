package components.neuralnetwork;

import java.util.Random;

/** Matrix-Objekt für das neuronale Netzwerk
 * 
 * Beinhaltet Funktionen zum Rechnen mit Matrizen, die für das Netzwerk benötigt werden.
 * 
 * @version 8. Juni 2021
 * @author Morris Tutschku
 */
public class Matrix {
    
    int rows;
    int cols;

    double data[][];


    /** Konstruktor
     * 
     * Initialisiert eine Matrix mit bestimmter Anzahl an Reihen und Spalten.
     * Matrixeinträge werden zunächst auf 0 initialisiert.
     * @param rows Anzahl Reihen
     * @param cols Anzahl Spalten
     */
    public Matrix(int rows, int cols){

        if(rows < 0 || cols < 0){
            System.err.println("Ungültige Matrixgröße.");
            return;
        }

        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                data[i][j] = 0.0;
            }
        }

    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public double[][] getData(){
        return data;
    }

    
    /** Setzt die Matrixeinträge auf bestimmte Werte.
     * 
     * @param data Werte
     */
    public void setData(double[][] data){
        if(data.length != rows || data[0].length != cols){
            System.err.println("double[][] data hat die falschen Dimensionen.");
            return;
        }

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                this.data[i][j] = data[i][j];
            }
        }
    }



    /** Gibt die Matrix auf der Konsole aus. */
    public void print(){
        for(int i = 0; i < rows; i++){
            System.out.print("[ ");
            for(int j = 0; j < cols; j++){
                System.out.print(data[i][j] + " ");
            }
            System.out.println("]");
        }
    }

    /** Matrixwerte werden auf zufällige Werte zwischen 0 und 1 gesetzt. */
    public void randomize(){
        Random r = new Random();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                data[i][j] = r.nextDouble();
            }
        }
    }

}
