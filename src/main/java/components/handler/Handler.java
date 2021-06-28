package components.handler;

import components.neuralnetwork.Training;
import components.neuralnetwork.Matrix;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.Double;

/**
 * This class handles transmission of data between GUI and Network
 * There's two main tasks:
 * 1. 	Feeding the canvas data from GUI into Network which means transforming the data into
 * 		a 28x28 matrix holding grayscale values: Q E [0,1]
 * 2. 	Returning the Networks guess to the GUI so that it can be displayed to the user.
 * 
 * The Handler clusters an image of the canvas into similar sized boxes and averages out the amount of
 * black and white pixels.
 * 
 * Idea:
 * Besides that, Handler might transfer meta info about the networks training data towards GUI
 * -> array of data names, to determine drawings, the network should be able to guess correctly
 * 
 * 
 * @author Jakob Hiestermann
 * @version 19 May 2021
 * 
 */
public class Handler {
	/** matrix holding grayscale info about a drawing on a canvas */
    private Matrix mat;
	
	// /** holds the value used for width and height of a cluster */
	private int clusterSideLength;

	// /** holds overall amount of pixels inside a cluster */
	private int clusterSize;

	/**
	 * Constructor method.
	 * 
	 * Initializes the variable clusterSize and the a matrix mat of specifed size.
	 * Both parameters have to be greater than zero.
	 * 
	 * @author 	Jakob Hiestermann
	 * @param matrixSideLength
	 * @param clusterSize_
	 */
	public Handler(int matrixSideLength, int clusterSideLength_) {
		if (matrixSideLength <= 0) {
			System.out.println("matrixSideLength needs to be greater than zero.");
		} else {
			this.mat = new Matrix(matrixSideLength, matrixSideLength);
		}
		if (clusterSideLength_ <= 0) {
			System.out.println("clusterSize_ needs to be greater than zero.");
		} else {
			this.clusterSideLength = clusterSideLength_;
			this.clusterSize = clusterSideLength_ * clusterSideLength_;
		}
	}

	/**
	 * Translates an image into a matrix of doubles. 
	 * These doubles each represent the average darkness inside a cluster.
	 * 
	 * @author 	Jakob Hiestermann
	 * @param 	image
	 * @return	Data-Objekt ohne outputs parameter
	 */
	public Data translateImage(BufferedImage image) {
		double[] cluster;
		Double average;

		for (int i = 0; i < image.getHeight() / this.clusterSideLength; i++) {
			for (int j = 0; j < image.getWidth() / this.clusterSideLength; j++) {
				cluster = makeCluster(j * this.clusterSideLength % (int) image.getWidth(), i * this.clusterSideLength % (int) image.getHeight(), image);
				average = averageArray(cluster);
				mat.setValue(i, j, average);
			}
		}

		mat = Matrix.toSingleColumn(mat);
		Data translatedInput = new Data(mat);
		return translatedInput;
	}

	/**
	 * Constructs a cluster of length specified in clusterSideLength
	 * 
	 * @author 	Jakob Hiestermann
	 * @param	x		x-coordinate to start clustering from
	 * @param	y		y-coordinate to start clustering from	
	 * @param	image	
	 * @return	array (cluster) holding pixeldata (0 for White, 1 for Black);
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

	// currently unused, may be discarded later after safety-check
	// /**
	//  * Translates a color into a corresponding integer, only usable for colors WHITE and BLACK.
	//  * 
	//  * @author 	Jakob Hiestermann
	//  * @param	rgb
	//  * @return	1 if color is black, 0 if color is white
	//  */
	// private int convertRGB(int rgb) {
	// 	if (rgb == Color.WHITE.getRGB()) {
	// 		return 0;
	// 	} else if (rgb == Color.BLACK.getRGB()) {
	// 		return 1;
	// 	} else {
	// 		// notify, but proceed
	// 		System.out.println("received color that's neither white nor black in colorToInt, Handler.java\n proceeded with return -1");
	// 		return -1;
	// 	}
	// }

	/**
	 * averages out integers in a array/cluster received, correspondingly to clusterSize
	 * 
	 * @author 	Jakob Hiestermann
	 * @param	cluster	
	 * @return	average value of ints inside cluster, required to be inside [0,1]
	 */
	private double averageArray(double[] cluster) {
		double average = 0.0;
		for(int i = 0; i < cluster.length; i++) {
			average += cluster[i];
		}
		average /= this.clusterSize;		
		assert(average <= 1 && average >= 0);
		return average;
	}
}
