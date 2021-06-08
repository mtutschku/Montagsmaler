package components.handler;

import components.neuralnetwork.Matrix;
import javafx.scene.canvas.Canvas;

/**
 * This class handles transmission of data between GUI and Network
 * There's two main tasks:
 * 1. 	Feeding the canvas data from GUI into Network which means transforming the data into
 * 		a 28x28 matrix holding grayscale values: Q E [0,1]
 * 2. 	Returning the Networks guess to the GUI so that it can be displayed to the user.
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
	/** matrix holding grayscale info about finished drawing */
    private static Matrix mat = new Matrix(28, 28);

	// might not receive a canvas
	public static Matrix translateCanvas(Canvas canvas) {
		// TODO
		return mat;
	}
}
