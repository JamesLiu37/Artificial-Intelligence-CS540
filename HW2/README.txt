Game Rules
------------------------------------------------------------
The game starts with n stones numbered 1, 2, 3, ..., n. Players take turns removing one of the remaining
numbered stones. At a given turn there are some restrictions on which numbers (i.e., stones) are legal
candidates to be taken. The restrictions are:
• At the first move, the first player must choose an odd-numbered stone that is strictly less than
n/2. For example, if n = 7 (n/2 = 3.5), the legal numbers for the first move are 1 and 3. If n = 6
(n/2 = 3), the only legal number for the first move is 1.
• At subsequent moves, players alternate turns. The stone number that a player can take must be
a multiple or factor of the last move (note: 1 is a factor of all other numbers). Also, this number
may not be one of those that has already been taken. After taking a stone, the number is saved
as the new last move. If a player cannot take a stone, she loses the game.
An example game is given below for n = 7:
Player 1: 3
Player 2: 6
Player 1: 2
Player 2: 4
Player 1: 1
Player 2: 7
Winner: Player 2


Program Specifications
------------------------------------------------------------
There are 2 players: player 1 (called Max) and player 2 (called Min). For a new game (i.e., no stones have
been taken yet), the Max player always plays first. Given a specific game board state, your program is to
compute the best move for the next turn of the next player. That is, only a single move is computed.


Input
------------------------------------------------------------
A sequence of positive integers given as command line arguments separated by spaces:
java Player <#stones> <#taken-stones> <list of taken stones> <depth>
• #stones: the total number of stones in the game
• #taken-stones: the number of stones that have already been taken in previous moves. If is
number is 0, it means this is the first move in a game, which will be played by Max. (Note: If
this number is even, then the current move is Max’s; if odd, the current move is Min’s)
• list of taken stones: a sequence of integers indicating the indexes of the already-taken stones,
ordered from first to last stone taken. Hence, the last stone in the list was the stone taken in
the last move. If #taken stones is 0, there will not arguments for this list.
• depth: the search depth. If depth is 0, search to end game states 
CS 540-2 Spring 2017
5
For example, with input: java Player 7 2 3 6 0, you have 7 stones while 2 stones, numbered 3
and 6, have already been taken, the next turn is the Max player (because 2 stones have been taken), and
you should search to end game states.
Output
You are required to print out to the console:
• A trace of the alpha-beta search tree that is generated. That is, at each node of the search tree
generated, you are to print the following right before returning from the node during your
recursive depth-first search of the tree:
o The number of the stone taken to reach the current node from its parent
o The list of currently available stones at the current node, output in increasing order and
separated by a space
o The final Alpha and Beta values computed at the current node, separated by a tab
Note: The above information is printed for you in the provided code.
• At the completion of your alpha-beta search, print the following three lines:
o “NEXT MOVE”
o The next move (i.e., stone number) taken by the current player (as computed by your
alpha-beta algorithm)
o A list of the stones remaining, in increasing order separated by spaces, after the
selected move is performed
For example, here is sample input and output when it is Min’s turn to move (because 3 stones have
previously been taken), there are 4 stones remaining (3 5 6 7), and the Alpha-Beta algorithm should
generate a search tree to maximum depth 3. Since the last move was 2 before starting the search for
the best move here, only one child is generated corresponding to removing stone 6 (since it is the only
multiplier of 2). That child node will itself have only one child corresponding to removing stone 3 (since
it is the only factor of 6 among the remaining stones). So, the search tree generated will have the root
node followed by taking 6, followed by taking 3, which leads to a terminal state. So, returning from
these nodes, from leaf to root, we get the output below.
Input:
$java TakeStones 7 3 1 4 2 3


Output:
------------------------------------------------------------
3
5 7
alpha: -1000 beta: 1000
6
3 5 7
alpha: 100 beta: 1000
2
3 5 6 7
alpha: -1000 beta: 100
NEXT MOVE
6
3 5 7
CS 540-2 Spring 2017
6


Static Board Evaluation
------------------------------------------------------------
The static board evaluation function should return values as follows:
• At an end game state where Player 1 (MAX) wins: 100
• At an end game state where Player 2 (MIN) wins: -100
• Otherwise,
o If stone 1 is not taken yet, return a value of 0 (because the current state is a relatively
neutral one for both players)
o If lastMove is 1, count the number of the possible successors (i.e., legal moves). If the
count is odd, return 5; otherwise, return -5.
o If lastMove is a prime, count the multiples of that prime in all possible successors. If
the count is odd, return 7; otherwise, return -7.
o If lastMove is a composite number (i.e., not prime), find the largest prime that can
divide lastMove, count the multipliers of that prime, including the prime number itself
if it hasn’t already been taken, in all the possible successors. If the count is odd, return
6; otherwise, return -6.


Other Important Information
------------------------------------------------------------
• Set the initial value of alpha to be -1000 and beta to be 1000
• To break ties (if any) between multiple child nodes that have equal values, pick the smaller
numbered stone. For example, if stones 3 and 7 have the same value, pick stone 3.
• Your program should run within roughly 10 seconds for each test case. 