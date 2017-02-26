import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		
		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();
		State init_State = new State(maze.getPlayerSquare(), null, 0, 0);
		explored[init_State.getX()][init_State.getY()] = true;
		ArrayList<State> successors = init_State.getSuccessors(explored, maze);
		noOfNodesExpanded++;
		int goalX = maze.getGoalSquare().X, goalY = maze.getGoalSquare().Y;
		for (int s = 0; s < successors.size(); s++){
			State added = successors.get(s);
			int currX = added.getX(), currY = added.getY();
			double heuristic = Math.sqrt((currX - goalX)*(currX - goalX)+(currY - goalY)*(currY - goalY));
			double f = added.getGValue() + heuristic;			
			StateFValuePair addedPair = new StateFValuePair(added, f);
			frontier.add(addedPair);			
		}
		// TODO initialize the root state and add
		// to frontier list
		// ...

		while (!frontier.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
			State expanded = frontier.poll().getState();
			noOfNodesExpanded++;
			explored[expanded.getX()][expanded.getY()] = true;
			if (expanded.isGoal(maze)){
				printSoln(expanded.getParent());				
				return true;
			}			
			successors = expanded.getSuccessors(explored, maze);
			ArrayList<StateFValuePair> clone = new ArrayList<StateFValuePair>();
			clone.addAll(frontier);
			// Check if any states already in frontier queue
			for (int s = 0; s < successors.size(); s++){ // loop through new expanded nodes
				boolean found = false, better = false;
				State newNode = successors.get(s); // new state node
				int newX = newNode.getX(), newY = newNode.getY();
				double heuristic = Math.sqrt((newX - goalX)*(newX - goalX)+(newY - goalY)*(newY - goalY));
				double f = newNode.getGValue() + heuristic;
				StateFValuePair addedPair = new StateFValuePair(newNode, f);
				for (int fr = 0; fr < clone.size() && !found; fr++){ // search frontier for duplicate
					State inFrontier = clone.get(fr).getState();
					if (newX == inFrontier.getX() && newY == inFrontier.getY()){
						found = true;
						if (newNode.getGValue() < inFrontier.getGValue())
							better = true;
					}
				}
				if (!found || better)
					frontier.add(addedPair);
			}
			if (frontier.size() > maxSizeOfFrontier)
				maxSizeOfFrontier = frontier.size();
			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
		}

		// TODO return false if no solution
		return false;
	}

	private void printSoln(State goalParent){
		cost++; // from goal to goalParent
		State curr = goalParent;
		while (maze.getSquareValue(curr.getX(), curr.getY()) != 'S'){
			maze.setOneSquare(curr.getSquare(), '.');
			curr = curr.getParent();
			cost++;
		}
		maxDepthSearched=cost;
	}

}
