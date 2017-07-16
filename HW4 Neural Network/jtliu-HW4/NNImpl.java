/**
 * The main class that handles the entire network
 * Has multiple attributes each with its own use
 * 
 */

import java.util.*;


public class NNImpl{
	public ArrayList<Node> inputNodes=null;//list of the input layer nodes.
	public ArrayList<Node> hiddenNodes=null;//list of the hidden layer nodes
	public ArrayList<Node> outputNodes=null;// list of the output layer nodes
	
	public int[][] confusionMatrix= new int[5][5];

	public ArrayList<Instance> trainingSet=null;//the training set

	Double learningRate=1.0; // variable to store the learning rate
	int maxEpoch=1; // variable to store the maximum number of epochs

	/**
	 * This constructor creates the nodes necessary for the neural network
	 * Also connects the nodes of different layers
	 * After calling the constructor the last node of both inputNodes and  
	 * hiddenNodes will be bias nodes. 
	 */

	public NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch, Double [][]hiddenWeights, Double[][] outputWeights)
	{
		this.trainingSet=trainingSet;
		this.learningRate=learningRate;
		this.maxEpoch=maxEpoch;

		//input layer nodes
		inputNodes=new ArrayList<Node>();
		int inputNodeCount=trainingSet.get(0).attributes.size();
		int outputNodeCount=trainingSet.get(0).classValues.size();
		for(int i=0;i<inputNodeCount;i++)
		{
			Node node=new Node(0);
			inputNodes.add(node);
		}

		//bias node from input layer to hidden
		Node biasToHidden=new Node(1);
		inputNodes.add(biasToHidden);

		//hidden layer nodes
		hiddenNodes=new ArrayList<Node> ();
		for(int i=0;i<hiddenNodeCount;i++)
		{
			Node node=new Node(2);
			//Connecting hidden layer nodes with input layer nodes
			for(int j=0;j<inputNodes.size();j++)
			{
				NodeWeightPair nwp=new NodeWeightPair(inputNodes.get(j),hiddenWeights[i][j]);
				node.parents.add(nwp);
			}
			hiddenNodes.add(node);
		}

		//bias node from hidden layer to output
		Node biasToOutput=new Node(3);
		hiddenNodes.add(biasToOutput);

		//Output node layer
		outputNodes=new ArrayList<Node> ();
		for(int i=0;i<outputNodeCount;i++)
		{
			Node node=new Node(4);
			//Connecting output layer nodes with hidden layer nodes
			for(int j=0;j<hiddenNodes.size();j++)
			{
				NodeWeightPair nwp=new NodeWeightPair(hiddenNodes.get(j), outputWeights[i][j]);
				node.parents.add(nwp);
			}	
			outputNodes.add(node);
		}	
	}

	/**
	 * Get the output from the neural network for a single instance
	 * Return the idx with highest output values. For example if the outputs
	 * of the outputNodes are [0.1, 0.5, 0.2, 0.1, 0.1], it should return 1. If outputs
	 * of the outputNodes are [0.1, 0.5, 0.1, 0.5, 0.2], it should return 3. 
	 * The parameter is a single instance. 
	 */

	public int calculateOutputForInstance(Instance inst)
	{		
		// TODO: add code here
		// Initialize input values into network's input layer, excluding bias
		for (int i = 0; i < inputNodes.size() - 1; i++) 
				inputNodes.get(i).setInput(inst.attributes.get(i));
		for (int h = 0; h < hiddenNodes.size(); h++) // Hidden layer pass
			hiddenNodes.get(h).calculateOutput();
		int maxIdx = 420; double maxVal = -1;
		for (int o = 0; o < outputNodes.size(); o++) {// Output layer pass
			outputNodes.get(o).calculateOutput();
			double output = outputNodes.get(o).getOutput();
			if (output >= maxVal){
				maxVal = output;
				maxIdx = o;				
			}
		}
		return maxIdx;		
	}

	/**
	 * Train the neural networks with the given parameters
	 * 
	 * The parameters are stored as attributes of this class
	 */

	public void train()
	{
		// TODO: add code here
		for (int t = 0; t < maxEpoch; t++) {
			for (int e = 0; e < trainingSet.size(); e++){
				// Forward Pass
				for (int i = 0; i < inputNodes.size() - 1; i++) // Set input layer input-values, excluding bias
					inputNodes.get(i).setInput(trainingSet.get(e).attributes.get(i));
				for (int h = 0; h < hiddenNodes.size(); h++) // Hidden layer pass
					hiddenNodes.get(h).calculateOutput();
				for (int o = 0; o < outputNodes.size(); o++) {// Output layer pass
					outputNodes.get(o).calculateOutput();
					//System.out.println(o + " > " + outputNodes.get(o).getOutput());
				}

				// Backwards Pass
				double[][] dw_jk = new double[hiddenNodes.size()][outputNodes.size()];
				for (int h = 0; h < hiddenNodes.size(); h++) // deltas for hidden > output
					for (int o = 0; o < outputNodes.size(); o++){
						double aj = hiddenNodes.get(h).getOutput();
						double Tk = trainingSet.get(e).classValues.get(o);
						double Ok = outputNodes.get(o).getOutput();
						dw_jk[h][o] = learningRate * aj * (Tk - Ok) * Ok * (1 - Ok);					
					}
				double[][] dw_ij = new double[inputNodes.size()][hiddenNodes.size() - 1];
				for (int i = 0; i < inputNodes.size(); i++) // deltas for input >  hidden
					for (int h = 0; h < hiddenNodes.size() - 1; h++){
						double ai = inputNodes.get(i).getOutput();
						double aj = hiddenNodes.get(h).getOutput();
						double sum = 0;
						for (int o = 0; o < outputNodes.size(); o++){
							double w_jk = outputNodes.get(o).parents.get(h).weight;
							double Tk = trainingSet.get(e).classValues.get(o);
							double Ok = outputNodes.get(o).getOutput();
							sum += w_jk * (Tk - Ok) * Ok * (1 - Ok);
						}
						dw_ij[i][h] = learningRate * ai * aj * (1 - aj) * sum;
					}

				// Update All Weights
				for (int h = 0; h < hiddenNodes.size(); h++) { // weights hidden > out
					for (int o = 0; o < outputNodes.size(); o++)
						outputNodes.get(o).parents.get(h).weight += dw_jk[h][o];
				}
				for (int i = 0; i < inputNodes.size(); i++) { // weights in > hidden, exclude bias
					for (int h = 0; h < hiddenNodes.size() - 1; h++)
						hiddenNodes.get(h).parents.get(i).weight += dw_ij[i][h];
				}
				//for (int x = 0; x < outputNodes.size(); x++) System.out.println(x + " > " + outputNodes.get(x).getOutput());
			} // end of example instance (16 x 16 image)
		} // end of epoch
	}
}
