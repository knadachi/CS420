import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class handles all the UI of the program. The player is asked if they want to
 * move first and also how long they want the AI to think about its move. The player
 * and the AI take turns placing their symbol on the board ("O" for the player and "X"
 * for the AI). The game ends if there are four O's or four X's in a row. It ends in a
 * tie if all spaces on the board are taken.
 */
public class UI {
    private Scanner input;
    Board board;
    boolean playerTurn;
    int seconds;

    /* UI constructor */
    public UI(){
        input = new Scanner( System.in );
        board = new Board( new int[8][8], 8*8 );
        System.out.println( "Welcome to the Four in a Line Game!\n" );
    }

    /**
     * This method essentially starts the game. It handles printing the board, asking
     * for the player's move, running the alpha beta pruning algorithm for the AI, and
     * determining when a winner is found.
     */
    public void start(){
        setPlayerTurn();
        setSeconds();
        System.out.println( "\n" + board.toString() );

        AlphaBeta ai = new AlphaBeta( seconds, !playerTurn );
        Action action;

        while( board.getEmptySpaces() > 0 ){
            if( playerTurn ){
                System.out.print( "Your move: " );
                String choice = input.nextLine();
                while( !board.move( choice, playerTurn ) ){
                    System.out.println( "Invalid input." );
                    System.out.print( "Your move: " );
                    choice = input.nextLine();
                }
            }
            else{
                action = ai.abSearch( board );
                board.move( action.getRow(), action.getCol(), playerTurn );
                System.out.println( "Computer move: " + action.toString() );
            }
            if( playerTurn ){
                playerTurn = false;
            }
            else{
                playerTurn = true;
            }
            System.out.println( "\n" + board.toString() );
            if( board.checkWin() != 0 ){
                break;
            }
        }
        switch (board.checkWin()) {
            case 0:
                System.out.println("It's a tie!");
                break;
            case 1:
                System.out.println("Computer wins!");
                break;
            case -1:
                System.out.println("You win!");
        }
        input.close();
    }

    /**
     * Allows the player to determine who goes first (player or AI).
     */
    private void setPlayerTurn(){
        boolean flag = true;
        while( flag ){
            System.out.print( "Would you like to go first? [Y/N]: " );
            String choice = input.nextLine().toUpperCase();
            char ch = choice.charAt(0);
            if( choice.length() == 1 ) {
                if (ch == 'Y') {
                    playerTurn = true;
                    flag = false;
                } else if (ch == 'N') {
                    playerTurn = false;
                    flag = false;
                }
            }
        }
    }

    /**
     * Asks the player how long they would like the AI to consider their move. The time
     * is in seconds and must be less than 30.
     */
    private void setSeconds(){
        boolean flag = true;
        System.out.println( "\nHow much time (in seconds) will you allow the computer to think?" );
        while( flag ){
            try {
                System.out.print("Time: ");
                seconds = input.nextInt();
                input.nextLine();
                if (seconds > 30) {
                    System.out.println("Please enter a time that is less than 30 seconds.");
                } else {
                    flag = false;
                }
            }
            catch( InputMismatchException e ){
                System.out.println( "Invalid input." );
                input.nextLine();
            }
        }
        seconds *= 1000;
    }
}