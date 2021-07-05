package components.neuralnetwork;

import java.util.Random;

/** Matrix-Objekt für das neuronale Netzwerk
 * 
 * Beinhaltet Funktionen zum Rechnen mit Matrizen, die für das Netzwerk benötigt werden.
 * 
 * @version 9. Juni 2021
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

    /** Konstruktor
     * 
     * Matrix wird durch eine 2D-Array initialisiert.
     * 
     * @param data Datenarray
     */
    public Matrix(double[][] data){
        rows = data.length;
        cols = data[0].length;
        this.data = data;
    }

    // Getter/Setter

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

    /** Ändert den Wert der Matrix an Stelle [row, col].
     * 
     * @param row Reihe
     * @param col Spalte
     * @param value Wert
     */
    public void setValue(int row, int col, double value){
        if(row < 0 || col < 0 || row > getRows() || col > getCols()){
            System.err.println("Ungültige Position.");
            return;
        }

        data[row-1][col-1] = value;
    }
    
    /** Überprüft ob die übergeben Positionen (row, col) in der Größe der Matrix liegen
     * Anschließend wird der Wert an dieser Stelle zurückgegeben
     * @param row
     * @param col
     * @return gibt den Wert an der gewünschte Stelle der Matrix zurück
     */
    public double getValue(int row, int col){
        if(row < 0 || col < 0 || row > getRows() || col > getCols()){
            System.err.println("Ungültige Position.");
            return 0.0;
        } else {
            return this.data[row][col];
        }
    }

    /**
     * Scans a matrix for its highest value.
     * Returns the row-index for the first instance of a value in case of multiple instances of the highest value.
     * 
     * @author Jakob Hiestermann
     * @return row-index of highest value position 
     */
    public int getHighestValueRow() {
        double currentMax = -1.0;
        int row = 0;
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                if (this.data[i][j] > currentMax) {
                    row = i;
                    currentMax = this.data[i][j];
                }
            }
        }
        return row;
    }

    /**
     * Scans a matrix for its highest value.
     * Returns the col-index for the first instance of a value in case of multiple instances of the highest value.
     * 
     * @author Jakob Hiestermann
     * @return col-index of highest value position 
     */
    public int getHighestValueCol() {
        transpose(this);
        int col = getHighestValueRow();
        transpose(this);
        return col;
    }

    /**
     * Scans a matrix for its highest value.
     * 
     * @author Jakob Hiestermann
     * @return the highest value inside a matrix
     */
    public double getHighestValue() {
        return this.data[getHighestValueRow()][getHighestValueCol()];
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
                data[i][j] = r.nextDouble() * 2.0 - 1.0;
            }
        }
    }

    // Rechenoperationen

    /** Multipliziert zwei Matrizen.
     * 
     * @param a Matrix 1
     * @param b Matrix 2
     * @return Matrix 1 * Matrix 2
     */
    public static Matrix multiply(Matrix a, Matrix b){
        if(a.getCols() != b.getRows()){
            System.err.println("Multiplikation nicht möglich. (Spalten a != Reihen b)");
            return new Matrix(0,0);
        }

        Matrix m = new Matrix(a.getRows(), b.getCols());

        for(int i = 0; i < m.getRows(); i++){
            for(int j = 0; j < m.getCols(); j++){
                double value = 0.0;
                for(int k = 0; k < b.getRows(); k++){
                    value += a.getData()[i][k] * b.getData()[k][j];
                }

                m.setValue(i+1, j+1, value);
            }
        }

        return m;
    }

    /** Multipliziert zwei Matrizen elementar: (a,b) * (c,d) = (a*c,b*d).
     * 
     * @param a Matrix 1
     * @param b Matrix 2
     * @return Matrix 1 * Matrix 2 (elementar)
     */
    public static Matrix multiplyElement(Matrix a, Matrix b){
        if(a.getRows() != b.getRows() || a.getCols() != b.getCols()){
            System.err.println("Multiplikation nicht möglich. (Beide Matrizen müssen die gleiche Größe haben)");
            return new Matrix(0,0);
        }

        Matrix m = new Matrix(a.getRows(), b.getCols());

        for(int i = 0; i < m.getRows(); i++){
            for(int j = 0; j < m.getCols(); j++){
                
                m.setValue(i+1, j+1, a.getData()[i][j] * b.getData()[i][j]);
            }
        }

        return m;
    }

    /**
     * Addiert zwei Matrizen miteinander und erzeugt eine neue Matrix
     * @param m1 erste Matrix, welche zur Addition verwendet wird.
     * @param m2 zweite Matrix, welche zur Addition verwendet wird.
     
     * @author Moritz Klose 
     * @return Matrix, welche sich aus der Addition von m1 und m2 zusammensetzt
     */
    public static Matrix add(Matrix m1, Matrix m2){
        double m1Data[][] = m1.getData();
        double m2Data[][] = m2.getData();
        
        if((m1.getRows() != m2.getRows()) || (m1.getCols() != m2.getCols())){
            System.err.println("Matrizen haben nicht die gleiche Größe.");
        }
            
        int sizeCols = m1.getCols();
        int sizeRows = m1.getRows();


        double addedData[][] = new double[sizeRows][sizeCols];

        for(int i = 0; i < sizeRows; i++){
            for(int j = 0; j < sizeCols; j++){
                addedData[i][j] = m1Data[i][j] + m2Data[i][j]; 
            }
        }

        Matrix addMatrix = new Matrix(addedData);
        return addMatrix;
    }

    /** Subtrahiert Matrix m2 von m1.
     * 
     * Hier wird nur das Negative der Matrix m2 zu m1 addiert: m1 - m2 = m1 + (-1) * m2.
     * 
     * @param m1 Minuend
     * @param m2 Subtrahend
     * @return m1 - m2
     */
    public static Matrix subtract(Matrix m1, Matrix m2){
        return Matrix.add(m1, Matrix.scale(m2, -1));
    }

    /** Multipliziert jeden Eintrag einer Matrix mit einem Faktor.
     * 
     * @param m Matrix
     * @param factor Skalierfaktor
     * @return skalierte Matrix
     */
    public static Matrix scale(Matrix m, double factor){
        Matrix out = new Matrix(m.getRows(), m.getCols());

        for(int i = 0; i < m.getRows(); i++){
            for(int j = 0; j < m.getCols(); j++){
                out.setValue(i+1, j+1, m.getData()[i][j] * factor);
            }
        }

        return out;
    }

    /** Transponiert eine Matrix.
     * 
     * @param m Matrix
     * @return transponierte Matrix
     */
    public static Matrix transpose(Matrix m){
        Matrix out = new Matrix(m.getCols(), m.getRows()); 
        
        for(int i = 0; i < m.getRows(); i++){
            for(int j = 0; j < m.getCols(); j++){
                out.setValue(j+1, i+1, m.getData()[i][j]);
            }
        }

        return out;
    }

    /** Konvertiert eine n x n Matrix in eine n^2 x 1 Matrix.
     * 
     * @param in Eingabematrix
     * @return konvertierte Matrix
     */
    public static Matrix toSingleColumn(Matrix in){
        Matrix m = new Matrix(in.getRows() * in.getCols(), 1);
        int counter = 0;
        
        for(int x = 0; x < in.getCols(); x++){
            for(int y = 0; y < in.getRows(); y++){
                counter++;
                m.setValue(counter, 1, in.getData()[x][y]);
            }
            
        }
        
        return m;
    }
}
