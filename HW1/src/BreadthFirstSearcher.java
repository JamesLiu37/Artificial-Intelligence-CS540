import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();		
		State init_State = new State(maze.getPlayerSquare(), null, 0, 0);
		explored[init_State.getX()][init_State.getY()] = true;
		ArrayList<State> successors = init_State.getSuccessors(explored, maze);
		noOfNodesExpanded++;
		queue.addAll(successors);
		if (queue.size() > maxSizeOfFrontier)
			maxSizeOfFrontier = queue.size();
		while (!queue.isEmpty()) {
			State expanded = queue.pop();
			noOfNodesExpanded++;
			explored[expanded.getX()][expanded.getY()] = true;
			if (expanded.isGoal(maze)){
				printSoln(expanded.getParent());				
				return true;
			}
			successors = expanded.getSuccessors(explored, maze);
			// Check if any states already in frontier queue
			for (int s = 0; s < successors.size(); s++){
				boolean duplicate = false;
				State added = successors.get(s);
				for (int q = 0; q < queue.size() && !duplicate; q++){
					State check = queue.get(q);
					if (added.getX() == check.getX() && added.getY() == check.getY())
						duplicate = true;
				}					
				if (!duplicate)
					queue.add(added);
			}
			if (queue.size() > maxSizeOfFrontier)
				maxSizeOfFrontier = queue.size();
		}
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
