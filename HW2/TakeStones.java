/***************************************************************************************
  CS540 - Section 2
  Homework Assignment 2: Game Playing

  TakeStones.java
  This is the main class that implements functions for Take Stones playing!
  ---------
 *Free to modify anything in this file, except the class name 
  	You are required:
  		- To keep the class name as TakeStones for testing
  		- Not to import any external libraries
  		- Not to include any packages 
 *Notice: To use this file, you should implement 4 methods below.

	@author: TA 
	@date: Feb 2017
 *****************************************************************************************/

import java.util.ArrayList;


public class TakeStones {

	final int WIN_SCORE = 100;	// score of max winning game
	final int LOSE_SCORE = -100;// score of max losing game
	final int INFINITY = 1000;	// infinity constant

	private static int bestMove = 420, fullDepth;
	/** 
	 * Class constructor.
	 */
	public TakeStones () {};
	
	private void printOnExit(GameState state, int alpha, int beta){
		state.log();
		Helper.log_alphabeta(alpha, beta);
	}


	/**
	 * This method is used to generate a list of successors 
	 * @param state This is the current game state
	 * @return ArrayList<Integer> This is the list of state's successors
	 */
	public ArrayList<Integer> generate_successors(GameState state) {
		int lastMove = state.get_last_move();	// the last move
		int size = state.get_size();			// game size
		ArrayList<Integer> successors = new ArrayList<Integer>();	// list of successors
		int limit = 0;
		if (lastMove == -1){
			limit = (size / 2) + (size % 2);
			for (int i = 1; i < limit; i += 2)
				successors.add(i); // all stones available
		}
		else {
			for (int i = 1; i < size + 1; i++)
				if (state.get_stone(i)) // Stone available
					if (lastMove % i == 0 || i % lastMove == 0) // factor or multiple
						successors.add(i);		
		}
		return successors;
	}


	/**
	 * This method is used to evaluate a game state based on 
	 * the given heuristic function 
	 * @param state This is the current game state
	 * @return int This is the static score of given state
	 */
	public int evaluate_state(GameState state, boolean maxPlayer) {
		ArrayList<Integer> successors = generate_successors(state);
		// if stone 1 is still available, score is 0
		if (successors.size() == 0){
			if (maxPlayer)
				return LOSE_SCORE;
			else
				return WIN_SCORE;
		}
		if (state.get_stone(1)) 
			return 0;

		int lastMove = state.get_last_move();
		if (1 == lastMove) {
			if (successors.size() % 2 == 1) // odd
				return 5;
			else
				return -5;

		} else if (Helper.is_prime(lastMove)){
			int count = 0; // include lastMove
			for (int i = 1; i < state.get_size() + 1; i++)
				if (state.get_stone(i) && i % lastMove == 0)
					count++;
			if (count %2 == 1) return 7;
			else return -7;
		} else {
			int bigPrime = Helper.get_largest_prime_factor(lastMove);

			int count = 0;
			for (int i = 1; i < state.get_size() + 1; i++)
				if (state.get_stone(i) && i % bigPrime == 0)
					count++;
			if (count % 2 == 1) return 6;
			else return -6;			
		}

	}


	/**
	 * This method is used to get the best next move from the current state
	 * @param state This is the current game state
	 * @param depth Current depth of search
	 * @param maxPlayer True if player is Max Player; Otherwise, false
	 * @return int This is the number indicating chosen stone
	 */
	public int get_next_move(GameState state, int depth, boolean maxPlayer) {
		int move = -1;			// the best next move 
		int alpha = -INFINITY;	// initial value of alpha
		int beta = INFINITY;	// initial value of alpha		

		// Getting successors of the given state 
		ArrayList<Integer> successors = generate_successors(state);
		// Check if depth is 0 or it is terminal state 
		if (0 == depth || 0 == successors.size()) {
			printOnExit(state, alpha, beta);
			return move;
		}

		// Prints state and alpha, beta before return
		alphabeta(state, depth, alpha, beta, maxPlayer);
		move = bestMove;
		
		return move;
	}


	/**
	 * This method is used to implement alpha-beta pruning for both 2 players
	 * @param state This is the current game state
	 * @param depth Current depth of search
	 * @param alpha Current Alpha value
	 * @param beta Current Beta value
	 * @param maxPlayer True if player is Max Player; Otherwise, false
	 * @return int This is the number indicating score of the best next move
	 */
	public int alphabeta(GameState state, int depth, int alpha, int beta, boolean maxPlayer) {
		int v; ArrayList<Integer> successors = generate_successors(state);
		if (depth == 0 || successors.size() == 0){ // terminal node or search depth reached
			printOnExit(state, alpha, beta);
			return evaluate_state(state, maxPlayer);
		}
		if (maxPlayer){
			v = -INFINITY; // @ max node: max value found from min-node children
			for (int s = 0; s < successors.size(); s++){
				int lastMove = state.get_last_move(); // save current last stone
				int move = successors.get(s);
				state.remove_stone(move); // avail stones[move] <- false, lastMove <- move
				int minChildVal = alphabeta(state, depth - 1, alpha, beta, !maxPlayer);
				if (minChildVal > v) {// on tie nodeBestMove unchanged
					v = minChildVal;
					if (depth == fullDepth)
						bestMove = move;
				}
				// revert to original max node state after returning from recursive call
				state.set_stone(move); // avail stones[move] <= true
				state.set_last_move(lastMove); // undo last move
				if (v >= beta) { // parent min node prunes this max node's children
					printOnExit(state, alpha, beta);
					return v;
				}				
				// min node ancestor of current max node will ignore this call
				alpha = Integer.max(alpha, v);
			}			
		}
		else { // @ min node: min value from max-node children
			v = INFINITY;
			for (int s = 0; s < successors.size(); s++){
				int lastMove = state.get_last_move();
				int move = successors.get(s);
				state.remove_stone(move);
				int maxChildVal = alphabeta(state, depth - 1, alpha, beta, !maxPlayer);
				if (maxChildVal < v){
					v = maxChildVal;
					if (depth == fullDepth)
						bestMove = move;
				}
				state.set_stone(move);
				state.set_last_move(lastMove);
				if (v <= alpha) { // parent max node prunes this min node's children
					printOnExit(state, alpha, beta);
					return v;
				}
				beta  = Integer.min(beta,  v);
			}
		}

		// Print state and alpha, beta before return 
		printOnExit(state, alpha, beta);
		return v;	
	}


	/**
	 * This is the main method which makes use of addNum method.
	 * @param args A sequence of integer numbers, including the number of stones,
	 * the number of taken stones, a list of taken stone and search depth
	 * @return Nothing.
	 * @exception IOException On input error.
	 * @see IOException
	 */
	public static void main (String[] args) {
		//try {
		// Read input from command line
		int n = Integer.parseInt(args[0]);		// the number of stones
		int nTaken = Integer.parseInt(args[1]);	// the number of taken stones

		// Initialize the game state
		GameState state = new GameState(n);		// game state
		int stone;
		for (int i = 0; i < nTaken; i++) {
			stone = Integer.parseInt(args[i + 2]);
			state.remove_stone(stone);
		}

		int depth = Integer.parseInt(args[nTaken + 2]);	// search depth
		// Process for depth being 0
		if (0 == depth)
			depth = n + 1;
		fullDepth = depth;

		TakeStones player = new TakeStones();	// TakeStones Object
		boolean maxPlayer = (0 == (nTaken % 2));// Detect current player

		// Get next move
		int move = player.get_next_move(state, depth, maxPlayer);	
		// Remove the chosen stone out of the board
		state.remove_stone(move);

		// Print Solution 
		System.out.println("NEXT MOVE");
		state.log();

		/*} catch (Exception e) {
			System.out.println("Invalid input");
		}*/
	}
}