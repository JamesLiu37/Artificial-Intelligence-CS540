/***************************************************************************************
  CS540 - Section 2
  Homework Assignment 5: Naive Bayes

  NBClassifierImpl.java
  This is the main class that implements functions for Naive Bayes Algorithm!
  ---------
  	*Free to modify anything in this file, except the class name 
  	You are required:
  		- To keep the class name as NBClassifierImpl for testing
  		- Not to import any external libraries
  		- Not to include any packages 
	*Notice: To use this file, you should implement 2 methods below.

	@author: TA 
	@date: April 2017
*****************************************************************************************/

import java.util.ArrayList;
import java.util.List;


public class NBClassifierImpl implements NBClassifier {
	
	private int nFeatures; 		// The number of features including the class 
	private int[] featureSize;	// Size of each features
	//private List<List<Double[]>> logPosProbs;	// parameters of Naive Bayes
	
	private ArrayList<int[][]> condProbTable;
	private int numPosInstances;
	private int numInstances;
	
	/**
     * Constructs a new classifier without any trained knowledge.
     */
	public NBClassifierImpl() {

	}

	/**
	 * Construct a new classifier 
	 * 
	 * @param int[] sizes of all attributes
	 */
	public NBClassifierImpl(int[] features) {
		this.nFeatures = features.length;
		
		// initialize feature size
		this.featureSize = features.clone();
		numPosInstances = 0;

		//this.logPosProbs = new ArrayList<List<Double[]>>(this.nFeatures);
	}


	/**
	 * Read training data and learn parameters
	 * 
	 * @param int[][] training data
	 */
	@Override
	public void fit(int[][] data) {

		//	TO DO
		numInstances = data.length;
		condProbTable = new ArrayList<int[][]>();
		
		// Initialize conditional probability table
		for (int featSizeIdx = 0; featSizeIdx < nFeatures - 1; featSizeIdx++){
			int attributeSize = featureSize[featSizeIdx];
			int[][] attributeTable = new int[2][attributeSize]; // assumes only 2 class values
			condProbTable.add(attributeTable);
		}
		
		// Iterate through all instances (data rows)
		for (int instIdx = 0; instIdx < numInstances; instIdx++){
			int[] instance = data[instIdx];
			int classVal = instance[nFeatures - 1];
			if (classVal == 1)
				numPosInstances++;
			// Iterate through all features of the current instance
			for (int featIdx = 0; featIdx < nFeatures - 1; featIdx++){
				int featVal = instance[featIdx];
				condProbTable.get(featIdx)[classVal][featVal]++;
			}
		}
		
		
	}

	/**
	 * Classify new dataset
	 * 
	 * @param int[][] test data
	 * @return Label[] classified labels
	 */
	@Override
	public Label[] classify(int[][] instances) {
		
		int nrows = instances.length;
		Label[] yPred = new Label[nrows]; // predicted data
		
		// Classify each instance using conditional probability table
		for (int instIdx = 0; instIdx < nrows; instIdx++){
			int numNegInstances = numInstances -  numPosInstances;
			double sumPos = Math.log((numPosInstances + 1.0)/(numInstances + 2.0)); 
			double sumNeg = Math.log((numNegInstances + 1.0)/(numInstances + 2.0));
			int[] instance = instances[instIdx];
			// Iterate through all features of the current instance
			for (int featIdx = 0; featIdx < nFeatures - 1; featIdx++){
				int featVal = instance[featIdx];
				int attributeSize = featureSize[featIdx];
				sumPos += Math.log( (condProbTable.get(featIdx)[1][featVal] + 1.0)/
									(numPosInstances + attributeSize) );
				sumNeg += Math.log( (condProbTable.get(featIdx)[0][featVal] + 1.0)/
									(numNegInstances + attributeSize) );
			}
			if (sumPos >= sumNeg)
				yPred[instIdx] = Label.Positive;
			else
				yPred[instIdx] = Label.Negative;
			
		}
		

		return yPred;
	}
}