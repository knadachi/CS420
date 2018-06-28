import java.util.Random;

/**
 * This class represents an N-Queen board. A board is represented by an array
 * of integers, its fitness value, and its weight. Each number in the array
 * represents the row the queen is in (only one queen per column). The fitness
 * represents the number of pairs of queens that are not attacking. The weight
 * is used for probability in the Genetic Algorithm.
 */
public class Board {
    private int[] board;
    private int fitness;
    private int weight;

    /* Board constructor*/
    public Board( int[] b ){
        board = b;
        fitness = findNonAttacking();
        weight = 0;
    }

    /**
     * Returns the current board.
     */
    public int[] getBoard(){
        return board;
    }

    /**
     * Returns the fitness value.
     */
    public int getFitness(){
        return fitness;
    }

    /**
     * Returns the weight.
     */
    public int getWeight(){
        return weight;
    }

    /**
     * Assigns the weight a specified value.
     */
    public void setWeight( int w ){
        weight = w;
    }

    /**
     * Decrements the weight by a specified value.
     */
    public void decrementWeight( int d ){
        weight -= d;
    }

    /**
     * Returns the number of non-attacking queen pairs on the current board. It
     * checks the board column by column and sees if any queen can attack any queens
     * on its right horizontally or diagonally.
     */
    public int findNonAttacking(){
        int pairs = 0;
        int num;
        for( int i = 0; i < board.length; i++ ){
            for( int j = i + 1; j < board.length; j++ ){
                num = j - i;
                int n1 = board[j] - num;
                int n2 = board[j] + num;
                if( board[i] != board[j] && n1 != board[i] && n2 != board[i] ){
                    pairs++;
                }
            }
        }
        return pairs;
    }

    /**
     * Alters the board by randomly selecting a queen and moving it to another random
     * position in the same column.
     */
    public void moveQueen(){
        Random rand = new Random();
        board[rand.nextInt( board.length )] = rand.nextInt( board.length );
        fitness = findNonAttacking();
    }
}
