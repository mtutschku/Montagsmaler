package components.neuralnetwork;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import components.handler.Data;

/** Diese Klasse ist zuständig zum Trainieren des Netzwerks.
 * 
 * Hier werden Trainingssets erstellt, die mittels python konvertierte Bilder im 28x28 Format bereitstellt.
 * Hier wird das trainierte Netzwerk bereitgestellt.
 * 
 * @author Morris Tutschku
 * @version 2. Juli 2021
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

    /** Trainiert das Netzwerk auf hardgecodete Kategorien und gibt
     * dessen Parameter (Matrizen) auf der Konsole aus. Diese Parameter sind schlussendlich die,
     * die zum Erkennen der Zeichnungen verwendet werden.
     * 
     * ACHTUNG: Hier aufgeführte Variablen sind sehr spezifisch und funktionieren nur bei mir (Morris).
     */
    public static void getTrainedNetwork(){
        // hardcoded Sachen
        final int SIZE = 500;
        final int CATEGORIES = 4;
        final String IMGLOC = "D:/Downloads/quickdraw_python-master/imgjava/";

        Data[] dataEye = new Data[SIZE];
        Data[] dataGiraffe = new Data[SIZE];
        Data[] dataHourglass = new Data[SIZE];
        Data[] dataEiffel = new Data[SIZE];

        Matrix expectedEiffel = new Matrix(CATEGORIES, 1);
        expectedEiffel.setValue(1, 1, 1.0);

        Matrix expectedEye = new Matrix(CATEGORIES, 1);
        expectedEye.setValue(2, 1, 1.0);

        Matrix expectedGiraffe = new Matrix(CATEGORIES, 1);
        expectedGiraffe.setValue(3, 1, 1.0);

        Matrix expectedHourglass = new Matrix(CATEGORIES, 1);
        expectedHourglass.setValue(4, 1, 1.0);
        
        // feedForward sieht folgendermaßen aus:
        // Matrix = [eiffel]
        //          [eye]
        //          [giraffe]
        //          [hourglass]
        //
        // höchster Wert = guess des Netzwerks

        // Trainingssets füllen

        // Eiffelturm
        for(int i = 0; i < SIZE; i++){
            File current = new File(IMGLOC + "The Eiffel Tower" + i + ".png");
            BufferedImage img = null;
            try{
                img = ImageIO.read(current);
            } catch(IOException e){
                System.err.println("Fehler beim Laden des BufferedImage.");
                return;
            }

            dataEiffel[i] = new Data(getMatrix(img), expectedEiffel);
        }

        // Auge
        for(int i = 0; i < SIZE; i++){
            File current = new File(IMGLOC + "eye" + i + ".png");
            BufferedImage img = null;
            try{
                img = ImageIO.read(current);
            } catch(IOException e){
                System.err.println("Fehler beim Laden des BufferedImage.");
                return;
            }

            dataEye[i] = new Data(getMatrix(img), expectedEye);
        }

        // Giraffe
        for(int i = 0; i < SIZE; i++){
            File current = new File(IMGLOC + "giraffe" + i + ".png");
            BufferedImage img = null;
            try{
                img = ImageIO.read(current);
            } catch(IOException e){
                System.err.println("Fehler beim Laden des BufferedImage.");
                return;
            }

            dataGiraffe[i] = new Data(getMatrix(img), expectedGiraffe);
        }

        // Sanduhr
        for(int i = 0; i < SIZE; i++){
            File current = new File(IMGLOC + "hourglass" + i + ".png");
            BufferedImage img = null;
            try{
                img = ImageIO.read(current);
            } catch(IOException e){
                System.err.println("Fehler beim Laden des BufferedImage.");
                return;
            }

            dataHourglass[i] = new Data(getMatrix(img), expectedHourglass);
        }

        // Netzwerk aufsetzen - Anzahl HL-Neuronen sind hardcoded!
        Network n = new Network(dataEiffel[0].getInputs().getRows(), 8, CATEGORIES);
        
        // komplettes Trainingsset
        Data[] fullDataSet = new Data[SIZE * CATEGORIES];

        for(int i = 0; i < SIZE; i++){
            fullDataSet[i] = dataEiffel[i];
        }
        for(int i = SIZE; i < SIZE * 2; i++){
            fullDataSet[i] = dataEye[i - SIZE];
        }
        for(int i = SIZE * 2; i < SIZE * 3; i++){
            fullDataSet[i] = dataGiraffe[i - SIZE * 2];
        }
        for(int i = SIZE * 3; i < SIZE * 4; i++){
            fullDataSet[i] = dataHourglass[i - SIZE * 3];
        }

        NetworkStats.getTrainingStats(n, fullDataSet, 90.0);

        // Parameter auf Konsole ausgeben
        paramsList.clear();
        paramsToList(n);
        saveParams();

    }

    static ArrayList<String> paramsList = new ArrayList<>();

    /** Gibt alle bias- und Gewichtsmatrizen als generierten Code auf der Konsole aus.
     * 
     *  Wird benötigt, um das trainierte Netzwerk hardgecodet zu implementieren.
     * 
     * @param n Netzwerk
     */
    public static void paramsToList(Network n){
        matrixToList(n.getBiasH(), "biasH");
        matrixToList(n.getBiasO(), "biasO");
        matrixToList(n.getWeightsIH(), "weightsIH");
        matrixToList(n.getWeightsHO(), "weightsHO");
    }

    /** Gibt eine Matrix als generierten Code auf der Konsole aus.
     * 
     * @param m Matrix
     * @param name Name der Matrix
     */
    public static void matrixToList(Matrix m, String name){
        for(int i = 1; i <= m.getCols(); i++){
            for(int j = 1; j <= m.getRows(); j++){
                String line = name + ".setValue(" + j + ", " + i + ", " + m.getValue(j-1, i-1) + ");";
                paramsList.add(line);
            }
        }
    }

    /** Speichert den generierten Code mit den Parametern des Netzwerks in einer txt-Datei.*/
    public static void saveParams(){
        try {
            FileWriter writer = new FileWriter(new File("PARAMS.txt"));
            for(String line : paramsList){
                writer.write(line + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Datei konnte nicht beschrieben werden.");
            return;
        }
    }

}
