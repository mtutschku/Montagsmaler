package components.handler;

import components.neuralnetwork.Training;
import components.neuralnetwork.Matrix;

import java.awt.image.BufferedImage;
import java.lang.Double;

/**
 * Diese Klasse ist verantwortlich für das Übersetzen eines BufferedImage in ein Data-Objekt, also das Bild
 * nach einem in schwarzer Farbe bemalten Bereich abzusuchen und entsprechende Informationen in eine quadratische
 * Matrix zu übertragen.
 * 
 * @author Jakob Hiestermann
 * @version 19.Juli 2021
 * 
 */
public class Translator {

	/** Beinhaltet Seitenlänge m der mxm matrix mat (in translateImage). */
	private int matrixSideLength;
	
	/** Beinhaltet den Wert der für Höhe und Breite eines Clusters entsprechend der Größe des Subimages, welches in subtractEmpty erstellt wird. */
	private int clusterSideLength;

	/** Beinhaltet die Gesamtzahl an Pixeln in einem Cluster - clusterSideLength * clusterSideLength */
	private int clusterSize;


	/**
	 * Konstruktor
	 * 
	 * @author 	Jakob Hiestermann
	 * @param matrixSideLength notwendigerweise größer als Null.
	 */
	public Translator(int matrixSideLength_) {
		if (matrixSideLength_ <= 0) {
			System.out.println("matrixSideLength_ needs to be greater than zero.");
		} else {
			this.matrixSideLength = matrixSideLength_;
		}
	}

	/**
	 * Übersetzt ein BufferedImage in eine Matrix mit Doubles bzw. ein Data-Objekt, welches
	 * diese Matrix als Inputmatrix enthält. Eine Position der Matrix ist = 1.0, wenn in dem entsprechenden Cluster mindestens
	 * ein schwarzes Pixel vorlag, andernfalls ist steht dort eine 0.0 .
	 * 
	 * @author 	Jakob Hiestermann
	 * @param 	image eine schwarze Zeichnung auf weißem Grund
	 * @return	Data-Objekt mit übersetzten BufferedImage (sprich Matrix) als Input-parameter und leerem Output-parameter
	 */
	public Data translateImage(BufferedImage image) {
		double[] cluster;
		Double content;
		Matrix mat = new Matrix(this.matrixSideLength, this.matrixSideLength);

		image = subtractEmpty(image);

		for (int i = 0; i < this.matrixSideLength; i++) {
			for (int j = 0; j < this.matrixSideLength; j++) {
				cluster = makeCluster(j * image.getWidth() / this.matrixSideLength, i * image.getHeight() / this.matrixSideLength, image);
				content = checkForContent(cluster);
				mat.setValue(i + 1, j + 1, content);
			}
		}
		mat = Training.center(mat);
		mat = Matrix.toSingleColumn(mat);
		Data translatedInput = new Data(mat);
		return translatedInput;
	}


	/**
	 * Diese Methode scannt ein BufferedImage und entfernt leere (weiße) Bereiche um einen quadratischen Bereich, der
	 * schwarzes Gezeichnetes enthält - effektiv wird auf den eingefärbten Bereich herangezoomt.
	 * Zurückgegeben wird ein neues BufferedImage, welches - falls Daten entfernt wurden - von geringerer Größe ist, als
	 * das Ausgangsbild. Die neue Seitenlänge wird jedoch ohne Rest durch matrixSideLength teilbar sein, so dass ohne Verlust
	 * geclustert werden kann. Die korrekte Clustergröße wird in clusterSideLength und clusterSize gespeichert.
	 * 
	 * @author Jakob Hiestermann
	 * @param image
	 * @return a BufferedImage with as much white space removed around drawing as possible.
	 */
	private BufferedImage subtractEmpty(BufferedImage image) {
		int drawingTopBorder = scanYofX(image, true);
		int drawingRightBorder = scanXofY(image, false);
		int drawingBotBorder = scanYofX(image, false);
		int drawingLeftBorder = scanXofY(image, true);
		int drawingCenterX = (drawingRightBorder + drawingLeftBorder) / 2;
		int drawingCenterY = (drawingTopBorder + drawingBotBorder) / 2;

		int minSidelength = (drawingBotBorder - drawingTopBorder > drawingRightBorder - drawingLeftBorder) ? drawingBotBorder - drawingTopBorder : drawingRightBorder - drawingLeftBorder;
		minSidelength += minSidelength % this.matrixSideLength;

		int boxInitX = drawingCenterX - (minSidelength / 2);
		if (boxInitX < 0) {		// überprüfen von Fällen in denen die Grenze des Originalbildes in x-Richtung überschritten werden würde
			boxInitX = 0;
		} else if (boxInitX + minSidelength > image.getWidth()) {
			boxInitX -= (drawingCenterX + (minSidelength / 2) - image.getWidth()); 
		}

		int boxInitY = drawingCenterY - (minSidelength / 2);
		if (boxInitY < 0) {		// überprüfen von Fällen in denen die Grenze des Originalbildes in y-Richtung überschritten werden würde
			boxInitY = 0;
		} else if (boxInitY + minSidelength > image.getHeight()) {
			boxInitY -= (drawingCenterY + (minSidelength / 2) - image.getHeight()); 
		}

		image = image.getSubimage(boxInitX, boxInitY, minSidelength, minSidelength);
		this.clusterSideLength = minSidelength / this.matrixSideLength;
		this.clusterSize = clusterSideLength * clusterSideLength;
		return image;
	}

	/**
	 * Diese Methode durchsucht ein BufferedImage nach entweder dem ersten oder letzten schwarzen Pixel von oben nach unten (reihenweise).
	 * Gibt den korrespondierenden y-Wert zurück um die obere bzw. untere Grenzen des gemalten herausfinden zu können.
	 * 
	 * @author Jakob Hiestermann
	 * @param image
	 * @param first wenn true, wird der y-Wert des ersten gefundenen schwarzen Pixels zurückgegeben, wenn false der des letzten
	 */
	private int scanYofX(BufferedImage image, Boolean first) {
		int y = 0;
		for (int i = 1; i < image.getHeight() - 1; i++) {
			for (int j = 1; j < image.getWidth() - 1; j++) {
				if (Training.getGrayscale(image.getRGB(j, i)) == 1.0) {
					y = i;
					if(first) {
						return y;
					}
				}
			}
		}
		return y;
	}


	/**
	 * Diese Methode durchsucht ein BufferedImage nach entweder dem ersten oder letzten schwarzen Pixel von links nach rechts (zeilenweise).
	 * Gibt den korrespondierenden x-Wert zurück um die linke bzw. rechte Grenzen des gemalten herausfinden zu können.
	 * 
	 * @author Jakob Hiestermann
	 * @param image
	 * @param first wenn true, wird der x-Wert des ersten gefundenen schwarzen Pixels zurückgegeben, wenn false der des letzten
	 */
	private int scanXofY(BufferedImage image, Boolean first) {
		int x = 0;
		for (int i = 1; i < image.getWidth() - 1; i++) {
			for (int j = 1; j < image.getHeight() - 1; j++) {
				if (Training.getGrayscale(image.getRGB(i, j)) == 1.0) {
					x = i;
					if(first) {
						return x;
					}
				}
			}
		}
		return x;
	}

	

	/**
	 * Konstruiert ein Array/Cluster der Seitenlänge, die in clusterSideLength gespeichert ist.
	 * Ein Cluster meint eine quadratische Region eines Bildes, das als Untereinheit einzeln weiter untersucht werden soll.
	 * 
	 * @author 	Jakob Hiestermann
	 * @param	x		x-Koordinate, von der aus geclustert wird
	 * @param	y		y-Koordinate, von der aus geclustert wird	
	 * @param	image	
	 * @return	Array, der Pixeldaten enthält (1.0 für schwarz, 0.0 für weiß)
	 */
	private double[] makeCluster(int x, int y, BufferedImage image) {
		double[] cluster = new double[this.clusterSize];
		int rgb;

		for (int i = 0; i < this.clusterSideLength; i++) {
			for (int j = 0; j < this.clusterSideLength; j++) {
				rgb = image.getRGB(x + j, y + i);
				cluster[i * this.clusterSideLength + j] = Training.getGrayscale(rgb);
			}
		}
		return cluster;
	}

	/**
	 * Scannt ein Array mit Double-Werten, überpruft ob mindestens einmal eine "1.0" vorkommt.
	 * 
	 * @author Jakob Hiestermann
	 * @param	cluster	
	 * @return	0.0 wenn keine "1.0" in Cluster enthalten, 1.0 wenn mindestens eine "1.0" vorkommt
	 */
	private double checkForContent(double[] cluster) {
		for(int i = 0; i < cluster.length; i++) {
			if (cluster[i] == 1.0) {
				return 1.0;
			}
		}
		return 0.0;
	}
}
