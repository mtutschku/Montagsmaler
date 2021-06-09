package components.handler;

import components.neuralnetwork.Matrix;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.paint.*;
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
 * @version 08 May 2021
 * 
 */
public class Handler {
	/** matrix holding grayscale info about a drawing on a canvas */
    private Matrix mat;
	
	// /** holds the value used for width and height of a cluster */
	private int clusterSideLength;

	// /** holds overall amount of pixels inside a cluster */
	private int clusterSize;

	/** holds an image representation of drawing on a canvas. Uninitialized until translateCanvas is called */
	private Image image;

	/**
	 * Constructor method.
	 * 
	 * Initializes the variable clusterSize and the a matrix mat of specifed size.
	 * Both parameters have to be greater than zero.
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
	 * Translates a canvas into a matrix of doubles. 
	 * These doubles each represent the average darkness inside a cluster.
	 * 
	 * @param 	canvas Canvas to extract the data from
	 * @return	Matrix // maybe switch to Data
	 */
	public Matrix translateCanvas(Canvas canvas) {
		int[] cluster;
		Double average;

		this.image = canvas.snapshot(null, null);
		for (int i = 0; i < image.getHeight() / this.clusterSideLength; i++) {
			for (int j = 0; j < image.getWidth() / this.clusterSideLength; j++) {
				cluster = makeCluster(j * this.clusterSideLength % (int) image.getWidth(), i * this.clusterSideLength % (int) image.getHeight(), image);
				average = averageArray(cluster);
				mat.setValue(i, j, average);
			}
		}
		return mat;
	}

	/**
	 * Constructs a cluster of length specified in clusterSideLength
	 * 
	 * @param	x		x-coordinate to start clustering from
	 * @param	y		y-coordinate to start clustering from	
	 * @param	image	
	 * @return	array (cluster) holding pixeldata (0 for White, 1 for Black);
	 */
	private int[] makeCluster(int x, int y, Image image) {
		int[] cluster = new int[this.clusterSize];
		PixelReader pr = image.getPixelReader();
		Color color;

		for (int i = 0; i < this.clusterSideLength; i++) {
			for (int j = 0; j < this.clusterSideLength; j++) {
				color = pr.getColor(x + j, y + i);
				cluster[i * this.clusterSideLength + j] = colorToInt(color);
			}
		}
		return cluster;
	}

	/**
	 * Translates a color into a corresponding integer, only usable for colors WHITE and BLACK.
	 * 
	 * @param	color
	 * @return	1 if color is black, 0 if color is white
	 */
	private int colorToInt(Color color) {
		if (color.equals(Color.WHITE)) {
			return 0;
		} else if (color.equals(Color.BLACK)) {
			return 1;
		} else {
			// notify, but proceed
			System.out.println("received color that's neither white nor black in colorToInt, Handler.java\n proceeded with return -1");
			return -1;
		}
	}

	/**
	 * averages out integers in a array/cluster received, correspondingly to clusterSize
	 * 
	 * @param	cluster	
	 * @return	average value of ints inside cluster, required to be inside [0,1]
	 */
	private double averageArray(int[] cluster) {
		double average = 0.0;
		for(int i : cluster) {
			average += cluster[i];
		}
		average /= this.clusterSize;		// TODO: check for type-specific division error
		assert(average <= 1 && average >= 0);
		return average;
	}
}
