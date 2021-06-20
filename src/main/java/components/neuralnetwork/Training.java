package components.neuralnetwork;

import java.awt.image.BufferedImage;

/** Diese Klasse ist zuständig zum Trainieren des Netzwerks.
 * 
 * Hier werden Trainingssets erstellt, die mittels python konvertierte Bilder im 28x28 Format bereitstellt.
 * Hier wird das trainierte Netzwerk bereitgestellt.
 * 
 * @author Morris Tutschku
 * @version 20. Juni 2021
 * 
 */
public class Training {
    
    /** Bild vereinfacht (int) statt exakt (double) ausgeben */
    static boolean simple = true;

    /** Gibt den Grauwert eines RGB-Pixels an.
     * 
     * Der Parameter rgb enthält RGB-Werte an jeweils verschiedenen Bitstellen,
     * die durch Bit-Manipulation extrahiert werden.
     * 
     * @param rgb RGB-Wert
     * @return Grauwert
     */
    public static double getGrayscale(int rgb){
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);
        
        double grayTotal = (r+g+b)/3.0;
        double gray = grayTotal / 255.0;
        gray = NetworkStats.cut(gray, 3);
        
        // ACHTUNG:
        // Netzwerk hat Schwierigkeiten zu raten, wenn Grauwerte nicht auf 0/1 gerundet werden!
        gray = gray < 0.97 ? 1 : 0;
        
        return gray;
    }

    /** Konvertiert ein 28x28 Bild in eine Matrix.
     * 
     * @param image Bild
     * @return Matrix
     */
    public static Matrix getMatrix(BufferedImage image){
        Matrix m = new Matrix(image.getWidth() * image.getHeight(), 1);
        int counter = 0;
        
        for(int y = 0; y < image.getWidth(); y++){
            for(int x = 0; x < image.getHeight(); x++){
                counter++;
                m.setValue(counter, 1, getGrayscale(image.getRGB(x, y)));
            }
            
        }
        
        return m;
    }

    /** Gibt ein Bild auf der Konsole aus. (Debug)
     * 
     * @param image Bild
     */
    public static void print(BufferedImage image){
        for(int y = 0; y < image.getWidth(); y++){
            for(int x = 0; x < image.getHeight(); x++){
                double gray = getGrayscale(image.getRGB(x, y));
                if(simple){
                     gray = gray < 0.97 ? 0 : 1;
                     System.out.print((int) gray + " ");
                } else {
                    System.out.print(gray + " ");
                }
            }
            System.out.println("");
        }
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

    /** Analog zu getMatrix(), allerdings wird die originale n x n Matrix zurückgegeben.
     * 
     * @param image Bild
     * @return Matrix
     */
    public static Matrix getSquareMatrix(BufferedImage image){
        if(image.getWidth() != image.getHeight()){
            System.err.println("Bild muss quadratisch sein.");
            return null;
        }
        
        Matrix m = new Matrix(image.getWidth(), image.getHeight());
        
        for(int y = 0; y < image.getWidth(); y++){
            for(int x = 0; x < image.getHeight(); x++){
                m.setValue(y+1, x+1, getGrayscale(image.getRGB(x, y)));
            }
            
        }
        
        return m;
    }

    /** Zentriert ein Bild vertikal.
     * 
     * Die Originalbilder sind links oben zentriert, daher wird überprüft, ob die unteren
     * Zeilen der Bildmatrix nur aus Nullen besteht.
     * Die Bildmatrix wird dementsprechend nach unten "versetzt".
     * 
     * @param m Matrix
     * @return vertikal zentrierte Matrix
     */
    public static Matrix center(Matrix m){
        if(!isSquare(m)){
            System.err.println("Matrix muss quadratisch sein.");
            return null;
        }
        
        Matrix out = new Matrix(m.getRows(), m.getCols());
        
        // Reihen analysieren
        int zeroRows = 0;
        
        for(int i = 1; i < m.getRows(); i++){
            double[] currentRow = m.getData()[m.getRows()-i];
            if(isZero(currentRow)){
                zeroRows++;
            } else {
                break;
            }
        }
          
        int offset = zeroRows / 2;
        
        for(int x = offset; x < m.getRows() - offset; x++){
            for(int y = 0; y < m.getCols(); y++){
                out.setValue(x+1, y+1, m.getData()[x-offset][y]);
            }
        }
        
        return out;        
    }

    /** Gibt an, ob eine Matrix quadratisch ist.
     * 
     * @param m Matrix
     * @return true wenn quadratisch
     */
    public static boolean isSquare(Matrix m){
        return m.getRows() == m.getCols();
    }

    /** Gibt an, ob jeder Eintrag einer Zeile Nullen sind.
     * 
     * @param array Matrixzeile
     * @return true wenn nur Nullen
     */
    public static boolean isZero(double[] array){
        for(double d : array){
            if(d != 0.0) return false;
        }
        return true;
    }

    // TODO: getTrainedNetwork()

}
