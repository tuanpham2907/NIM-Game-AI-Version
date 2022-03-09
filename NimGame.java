import java.io.*;

/**
 * Alpha-Beta version of NIM
 * @author Tuan Pham
 * 
 */

public class NimGame implements Cloneable
{
    /**
     * The number of sticks left and the maximum number of sticks to choose
     */

    private int sticksLeft;
	private int maxSticks;
    private boolean isPlayerTurn;

    /**
     * Creates a new Nin game with the initial number of sticks.  
     * A boolean to indicate who should go first
     *
     * @param x is the number of sticks left
	 * @param y is the maximum number of sticks to choose
     * @param z true to indicate that that the human starts
     */

    public  NimGame(int x, int y, boolean z)
    {
	sticksLeft = x;
	maxSticks = y;
	isPlayerTurn = z;

    }
    /**
     * Returns a clone of this game.
     */

    public Object clone()
    {
	try
	    {
		return super.clone();
	    }
	catch (CloneNotSupportedException cantHappen)
	    {
		System.err.println("NimGamel.clone: an unexpected exception");
		cantHappen.printStackTrace(System.err);
		System.exit(1);
		return null;
	    }
    }

	 /**
     * Identify if it is player's turn
     */

	public boolean isPlayerTurn()
    {
	return isPlayerTurn;
    }

    public void startMoving(int sticksToSelect)
    {
	sticksLeft = sticksLeft - sticksToSelect;
	isPlayerTurn = !isPlayerTurn;
    }


    /**
     * Returns the number of sticks left in this game.
     */

    public int countSticks()
    {
	return sticksLeft;
    }

    /**
     * Determines if this game is over.
     * When there is no sticks left, 
     * game is over.
     */

    public boolean isOver()
    {
	return (sticksLeft == 0);
    }

    /**
     * Determines if the player enter a right move.
     * Only numbers from 0 to the maximum that player select are allowed.
     */

    public boolean isRightChoice(int sticksToSelect)
    {
	return (sticksToSelect > 0 && sticksToSelect <= maxSticks && sticksToSelect <= sticksLeft);
    }

    /**
     * Determines if the player won.
     * The player won if it is the computers turn and there are no sticks
	 * to take
     * 
     */

    public boolean playerWon()
    {
	return (sticksLeft == 0 && !isPlayerTurn);
    }

    /**
     * In Nim, it would make sense to have MIN-VALUE and MAX-VALUE to return
     * +1 and -1. 
     * 
     */

    public int minimax()
    {
	if (isOver())
	    {
		// Indentify the winner based on what turn
		// when the game is over

		if (isPlayerTurn)
		    return -1;
		else
		    return 1;
	    }
	else
	    {
		if (isPlayerTurn)
		    {
			// maximize value over all the moves the
			// player could make

			int max = -1;
			for (int s = 0; s <= sticksLeft; s++)
			    if (isRightChoice(s))
				{
					NimGame next = ( NimGame)clone();
				    next.startMoving(s);
				    max = Math.max(max, next.minimax());
				}
			return max;
		    }
		else
		    {
			// minimize value over all the moves the
			// computer could make

			int min = 1;
			for (int s = 0; s <= sticksLeft; s++)
			    if (isRightChoice(s))
				{
					NimGame next = ( NimGame)clone();
				    next.startMoving(s);
				    min = Math.min(min, next.minimax());
				}
			return min;
		    }
	    }
    }

    /**
     * Return the number of sticks that a computer should
     * take to make sure that it win.
     * 
     */

    public int findComputerMove()
    {
	int min = 1;

	// find first winning move

	for (int s = 0; s <= sticksLeft; s++)
	    if (isRightChoice(s))
		{
			NimGame next = ( NimGame)clone();
		    next.startMoving(s);
		    if (next.minimax() == -1)
			return s;
		}

	// When there are no good moves
	// try to take the least stick as possible

	int s = 0;
	while (!isRightChoice(s))
	    s++;
	return s;
    }

    /**
     * Display the sticks on the screen
     */

    public String toString()
    {
	StringBuffer result = new StringBuffer();

	for (int i = 0; i < sticksLeft; i++)
	    result.append('|');

	return result.toString();
    }	

    public static void main(String[] args) throws IOException
    {
	// ask the player to select the initial number of sticks 

	BufferedReader reader 
	    = new BufferedReader(new InputStreamReader(System.in));

	System.out.println("Select the intial number of sticks to start: ?");
	int sticksToStart = Integer.parseInt(reader.readLine());

	System.out.println("Select the maximum number of sticks to choose: ? ");
	int maxNumberSticks = Integer.parseInt(reader.readLine());

	// Create a game that computer should go first
	NimGame game = new  NimGame(sticksToStart, maxNumberSticks,  false);
	
	while (!game.isOver())
	    {
		System.out.println(game);

		if (game.isPlayerTurn())
		    {
			System.out.println("Choose how many sticks to take");
			int humansMove = Integer.parseInt(reader.readLine());
			while (!game.isRightChoice(humansMove))
			    {
				System.out.println("Please choose from 0 to " + maxNumberSticks + " only!");
				humansMove = Integer.parseInt(reader.readLine());
			    }
			game.startMoving(humansMove);
		    }
		else
		    {
			int computersMove = game.findComputerMove();
			game.startMoving(computersMove);
			System.out.println("Computer takes "
					   + computersMove + " stick(s)");
		    }
	    }

	if (game.playerWon())
	    System.out.println("Congratulation! You win the game!");
	else
		
		System.out.println("Computer wins the game!");
    }
}