// 2c) Yes after running this algorithm below, it can find the global min by using greedy hill-climbing on its neighbors.

import java.util.ArrayList;

public class HillClimbing {
	static double d[][] = new double[][]{ // distance Matrix
		{0.0, 0.9, 0.6, 0.8, 0.7},
		{0.9, 0.0, 1.3, 1.5, 1.3},
		{0.6, 1.3, 0.0, 0.2, 0.3},
		{0.8, 1.5, 0.2, 0.0, 0.2},
		{0.7, 1.3, 0.3, 0.2, 0.0}		
	};
	static char c[] = {'M', 'C', 'W', 'E', 'S'};
	static double minPathCost = Double.MAX_VALUE; // positive infinity
	static String path = "";
	
	// TODO testing shit
	

	public static String findGlobalMinFromM(){
		for (int i1 = 0; i1 < 4; i1++){
			ArrayList<Integer> avail = new ArrayList<Integer>();
			// available locations at any point in making path
			for (int i = 1; i < 5; i++)
				avail.add(i);
			int p1 = avail.remove(i1); // point 1
			for (int i2 = 0; i2 < 3; i2++){
				ArrayList<Integer> avail2 = new ArrayList<Integer>(avail);
				int p2 = avail2.remove(i2); // point 2
				for (int i3 = 0; i3 < 2; i3++){
					ArrayList<Integer> avail3 = new ArrayList<Integer>(avail2);
					int p3 = avail3.remove(i3); // point 3
					int p4 = avail3.remove(0); // only point left
					double pathCost = d[0][p1]+d[p1][p2]+
							d[p2][p3]+d[p3][p4]+d[p4][0];
					if (pathCost < minPathCost){
						minPathCost = pathCost;
						path = "" + c[0] + c[p1] + c[p2] + c[p3] + c[p4] + c[0];						
					}

				}
			}
			avail = null;
		}
		return path;
	}

	public static String neighboringMinFromM(){
		int[] currPath = {3,1,4,2}; // M-ECSW-M
		path = "";
		double currPathCost, neighborPathCost, minNearbyCost;
		boolean foundCheaper = false;
		int minNeighbor[] = currPath;
		do {
			foundCheaper = false; minNearbyCost = Double.MAX_VALUE;
			currPathCost = d[0][currPath[0]] + d[currPath[0]][currPath[1]] + 
					d[currPath[1]][currPath[2]] + d[currPath[2]][currPath[3]] + d[currPath[3]][0];
			for (int i = 0; i < 3; i++){ // find minimum neighbor				
				for (int j = i + 1; j < 4; j++){
					int[] neighborTestPath = currPath.clone();
					// swap locations (i, j)
					int temp = neighborTestPath[i]; 
					neighborTestPath[i] = neighborTestPath[j];
					neighborTestPath[j] = temp;
					// done swapping
					neighborPathCost = d[0][neighborTestPath[0]] + d[neighborTestPath[0]][neighborTestPath[1]] + 
							d[neighborTestPath[1]][neighborTestPath[2]] + d[neighborTestPath[2]][neighborTestPath[3]] + d[neighborTestPath[3]][0];
					if (neighborPathCost < currPathCost && neighborPathCost < minNearbyCost){
						minNeighbor = neighborTestPath;
						minNearbyCost = neighborPathCost;
						foundCheaper = true;
					}
				}
			}
			if (foundCheaper)
				currPath = minNeighbor;
		}
		while (foundCheaper);
		path = "" + c[0] + c[currPath[0]] + c[currPath[1]] + c[currPath[2]] + c[currPath[3]] + c[0];
		return path;
	}

	public static void main(String[] args) {
		System.out.println("Global Min: " + findGlobalMinFromM());
		System.out.println("Neighbor Min: " + findGlobalMinFromM());
	}
}
