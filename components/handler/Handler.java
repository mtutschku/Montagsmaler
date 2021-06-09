package components.handler;

import components.neuralnetwork.Matrix;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

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
	// /** holds the value used for width and height of a cluster */
	// private final int CLUSTER_SIZE = 28;
	
	/** matrix holding grayscale info about a drawing on a canvas */
    private Matrix mat;
	
	// /** holds the value used for width and height of a cluster */
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
	public Handler(int matrixSideLength, int clusterSize_) {
		if (matrixSideLength <= 0) {
			System.out.println("matrixSideLength needs to be greater than zero.");
		} else {
			this.mat = new Matrix(matrixSideLength, matrixSideLength);
		}
		if (clusterSize_ <= 0) {
			System.out.println("clusterSize_ needs to be greater than zero.");
		} else {
			this.clusterSize = clusterSize_;
		}
	}
	
	/**
	 * 
	 */
	public Matrix translateCanvas(Canvas canvas) {
		// TODO
		
		image = canvas.snapshot(null, null);
		return mat;
	}

	/**
	 * 
	 */
	private int[] makeCluster(int index) {
		// TODO
	}

	/**
	 * 
	 */
	private double averageArray(int[] cluster) {
		double average = 0.0;
		for(int i : cluster) {
			average += cluster[i];
		}
		average /= this.clusterSize * this.clusterSize;		// TODO: check for type-specific division error
		return average;
	}
}
