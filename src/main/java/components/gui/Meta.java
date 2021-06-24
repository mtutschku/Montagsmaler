package components.gui;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class holds a hardcoded array consisting of single instances of all training objects
 * the neural network is getting trained with.
 * 
 * It also provides a dynamic array holding the same names. A single random
 * name can be requested, in order to e.g. provide necessary info to the GUI.
 * 
 * @author Jakob Hiestermann
 * @version June 13 2021
 */
public class Meta {
	/** Random Object to provide random integer used in getRandomNext. */
	private final Random rand = new Random();
	
	/** Hardcoded array holding training data objects, hence all objects that can potentially be drawn in general. */
	private final static String[] META = {"Banana", "Car", "Human", "Smiley", "Flower", "Crown", "Pants"};
	
	/**
	 * Dynamic (i.e. alterable) array.
	 * Is initialized with objects provided in META.
	 */
	private ArrayList <String> Meta;

	/**
	 * Constructor.
	 * 
	 * Initializes Meta with Strings specified in META.
	 * 
	 * @author Jakob Hiestermann
	 */
	public Meta() {
		Meta = new ArrayList<String>(META.length);
		for (String i : META) {
			Meta.add(i);
		}
	}

	/**
	 * Randomly selects an element inside Meta.
	 * 
	 * The parameter remove determines, whether the returned object stays in the list or is removed.
	 * 
	 * @author	Jakob Hiestermann
	 * @param	remove	if true, the randomly selected element gets removed from the list
	 * @return 	a randomly selected element/name
	 */
	public String getRandomNext(Boolean remove) {
		String randomNext = "";
		if(!Meta.isEmpty()){
			int randomIndex = rand.nextInt(this.Meta.size());
			randomNext = String.valueOf(this.Meta.get(randomIndex));
			if (remove) {
			this.Meta.remove(randomIndex);
			}
			return randomNext;
		} else {
			randomNext = "";
		}
		return randomNext;
	}
	
	/** 
	 * get-method for private var Meta.
	 * 
	 * @return alterable
	 */
	public ArrayList <String> getMeta() {
		return this.Meta;
	}

	/** 
	 * get-method for private constant META.
	 * 
	 * @return unalterable
	 */
	public static String[] getMETA() {
		return META;
	}

	/** get-method for specified element of private constant META. */
	public static String getMETAIndex(int i) {
		return META[i];
	}
	
	@Override
	public String toString() {
		String stringRep = "";
		Object[] metaArray = this.Meta.toArray();
		
		for (int i = 0; i < metaArray.length; i++) {
			stringRep += "[" + (String) metaArray[i] + "]";
		}
		return stringRep;
		
	}
}
