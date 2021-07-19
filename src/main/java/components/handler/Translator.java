package components.handler;

import components.neuralnetwork.Training;
import components.neuralnetwork.Matrix;

import java.awt.image.BufferedImage;
import java.lang.Double;

/**
 * This class is responible for translating a BufferedImage into a Data object, hence
 * scanning the image for black drawing data and collecting corresponding values inside a square Matrix.
 * 
 * @author Jakob Hiestermann
 * @version July 12th 2021
 * 
 */
public class Translator {

	/** holds sidelength m of mxm matrix mat */
	private int matrixSideLength;
	
	/** holds the value used for width and height of a cluster */
	private int clusterSideLength;

	/** holds overall amount of pixels inside a cluster */
	private int clusterSize;


	/**
	 * Constructor method.
	 * 
	 * @author 	Jakob Hiestermann
	 * @param matrixSideLength needs to be greater than zero.
	 */
	public Translator(int matrixSideLength_) {
		if (matrixSideLength_ <= 0) {
			System.out.println("matrixSideLength_ needs to be greater than zero.");
		} else {
			this.matrixSideLength = matrixSideLength_;
		}
	}

	/**
	 * Translates an image into a matrix of doubles. 
	 * These doubles each represent the average darkness inside a cluster.
	 * 
	 * @author 	Jakob Hiestermann
	 * @param 	image a black drawing on white background
	 * @return	Data object with translated image (-> matrix) as input-parameter and empty output-parameter
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
	 * This method scans through a BufferedImage removing empty (white) parts around black drawing,
	 * effectively zooming into the images parts that actually contain the drawing.
	 * Returns a new BufferedImage that in case of data removal has a different size,
	 * however the new sidelength will still be divisible by matrixSideLength without remainder,
	 * in order to secure the reduced image can be clustered completely.
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
		if (boxInitX < 0) {		// checking for cases where the new image would try to exceed the original image's borders x-wise
			boxInitX = 0;
		} else if (boxInitX + minSidelength > image.getWidth()) {
			boxInitX -= (drawingCenterX + (minSidelength / 2) - image.getWidth()); 
		}

		int boxInitY = drawingCenterY - (minSidelength / 2);
		if (boxInitY < 0) {		// checking for cases where the new image would try to exceed the original image's borders x-wise
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
	 * This method scans a BufferedImage for either the first or last instance of a black pixel from top to bottom.
	 * Returns the corresponding y-value, to determine drawing borders.
	 * 
	 * @author Jakob Hiestermann
	 * @param image
	 * @param first if true, the first instance of black pixel is returned, else the last
	 */
	private int scanYofX(BufferedImage image, Boolean first) {
		int y = 0;
		for (int i = 1; i < image.getHeight() - 1; i++) {
			for (int j = 1; j < image.getWidth() - 1; j++) {
				if (Training.getGrayscale(image.getRGB(j, i)) == 1.0) {
					y = i;
					if(first) {		// returns first encounter if first is true, else keeps iterating
						return y;
					}
				}
			}
		}
		return y;
	}


	/**
	 * This method scans a BufferedImage for either the first or last instance of a black pixel from left to right.
	 * Returns the corresponding x-value, to determine drawing borders.
	 * 
	 * @author Jakob Hiestermann
	 * @param image
	 * @param first if true, the first instance of black pixel is returned, else the last
	 */
	private int scanXofY(BufferedImage image, Boolean first) {
		int x = 0;
		for (int i = 1; i < image.getWidth() - 1; i++) {
			for (int j = 1; j < image.getHeight() - 1; j++) {
				if (Training.getGrayscale(image.getRGB(i, j)) == 1.0) {
					x = i;
					if(first) {		// returns first encounter if first is true, else keeps iterating
						return x;
					}
				}
			}
		}
		return x;
	}

	

	/**
	 * Constructs array/cluster of sidelength currently saved in clusterSideLength.
	 * A cluster is a square region of an image to be examined as a subunit.
	 * 
	 * @author 	Jakob Hiestermann
	 * @param	x		x-coordinate to start clustering from
	 * @param	y		y-coordinate to start clustering from	
	 * @param	image	
	 * @return	array holding pixeldata (0.0 for White, 1.0 for Black);
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
	 * Scans an array of doubles, checking whether it contains at least one instance of the value "1.0".
	 * 
	 * @author Jakob Hiestermann
	 * @param	cluster	
	 * @return	0.0 if cluster contained only zeros, 1.0 if cluster contained at least a single "1.0"
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
