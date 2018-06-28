import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used to represent the Hill Climbing Algorithm to solve an N-Queens
 * problem. The board is represented by an array of integers (similar to the Board
 * class). It also includes a cost for running the algorithm.
 */
public class HillClimbing {
    private int[] board;
    private int cost;

    /* HillClimbing constructor */
    public HillClimbing( int[] b ){
        board = b;
        cost = 0;
    }

    public int getCost(){
        return cost;
    }

    /**
     * This method uses the Hill-Climbing Algorithm to solve a given N-Queen problem:
     *    [1] Finds all possible successor boards formed by moving any queen.
     *    [2] Finds the number of pairs of attacking queens for the current board.
     *    [3] If none of the possible boards are better than the current, the
     *        algorithm quits.
     *    [4] If a better board is found, the best board is updated and the algorithm
     *        repeats.
     *  Note: It is possible for a solution not to be found.
     */
    public boolean solve(){
        ArrayList<int[]> allMoves = new ArrayList<>();
        ArrayList<Integer> allAttackingPairs = new ArrayList<>();
        int curAttackingPairs = findAttacking( board );

        while( true ){
            allMoves.clear();
            allAttackingPairs.clear();

            for( int i = 0; i < board.length; i++ ){
                for( int j = 0; j < board.length; j++ ){
                    if( board[i] != j ){
                        int[] nextMove = board.clone();
                        nextMove[i] = j;

                        allMoves.add( nextMove );
                        allAttackingPairs.add( findAttacking( nextMove ) );
                        cost++;
                    }
                }
            }

            int min = Collections.min( allAttackingPairs );
            if( min >= curAttackingPairs ){
                return false;
            }
            else{
                board = allMoves.get( allAttackingPairs.indexOf( min ) ).clone();
                curAttackingPairs = min;
                if( min == 0 ){
                    return true;
                }
            }
        }
    }

    /**
     * Returns the number of pairs of attacking queens in the specified board. It checks
     * the board column by column and sees if any queen can attack any queens on its
     * right horizontally or diagonally.
     */
    public int findAttacking( int[] b ){
        int pairs = 0;
        int num;

        for( int i = 0; i < b.length; i++ ){
            for( int j = i + 1; j < b.length; j++ ){
                num = j - i;
                int n1 = b[j] - num;
                int n2 = b[j] + num;
                if( b[i] == b[j] || n1 == b[i] || n2 == b[i] ){
                    pairs++;
                }
            }
        }

        return pairs;
    }

    /**
     * Returns a String containing the board:
     *   * = empty space
     *   Q = queen
     */
    public String toString(){
        String str = "";
        for( int i = 0; i < board.length; i++ ){
            for( int j = 0; j < board.length; j++ ){
                if( i == board[j] ){
                    str += ( "Q " );
                }else
                    str += ( "- " );
            }
            str += "\n";
        }
        return str;
    }
}
