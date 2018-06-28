import java.util.ArrayList;

/**
 * This class represents one step or state of the 8-puzzle board. It references its
 * parent node and contains the current board. It also keeps track of f(n), g(n), and
 * h(n).
 */
public class BoardNode {
    private BoardNode parent;
    private int[][] board = new int[3][3];
    private int f = 0;
    private int h = 0;
    private int g;
    /* Represents the goal state of the board */
    private final int[][] GOAL_STATE = {{0 ,1, 2},
                                        {3, 4, 5},
                                        {6, 7, 8}};
    /* Uses the goalH2 calculated in AStarSearch */
    private ArrayList<int[]> goalH2 = new ArrayList<>();

    /* Constructor for the root node (initial puzzle) */
    public BoardNode( int[][] node ){
        parent = null;
        board = node;
        g = 0;

        /* Determines which heuristic to use */
        if( AStarSearch.getHFlag() ){
            goalH2 = AStarSearch.getGoalH2();
            findH2();
        }
        else{
            findH1();
        }

        /* Calculates f(n) = g(n) + h(n) */
        f = g + h;
    }

    /* Constructor for any non-root node */
    public BoardNode( int[][] node, BoardNode p ){
        parent = p;
        board = node;
        g = parent.g + 1;

        /* Determines which heuristic to use */
        if( AStarSearch.getHFlag() ){
            goalH2 = AStarSearch.getGoalH2();
            findH2();
        }
        else{
            findH1();
        }

        /* Calculates f(n) = g(n) + h(n) */
        f = g + h;
    }

    /**
     * Returns the current node (board).
     */
    public int[][] getBoard(){
        return board;
    }

    /**
     * Returns the parent of the current node.
     */
    public BoardNode getParent(){
        return parent;
    }

    /**
     * Returns the f(n) value of the current node.
     */
    public int getF(){
        return f;
    }

    /**
     * Calculates h(n) for the first heuristic function (the number of misplaced tiles.
     */
    public void findH1(){
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                if( board[i][j] != 0 && board[i][j] != GOAL_STATE[i][j] ){
                    h++;
                }
            }
        }
    }

    /**
     * Calculates h(n) for the second heuristic function (the sum of the distances of the
     * tiles from their goal positions.
     */
    public void findH2(){
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                if( board[i][j] != 0 ){
                    h += Math.abs( goalH2.get( board[i][j] )[0] - i ) + Math.abs( goalH2.get( board[i][j] )[1] - j );
                }
            }
        }
    }


    /**
     * Saves the current board to a String and returns it.
     */
    public String toString(){
        String str = "\n";
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                str += ( board[i][j] + " "  );
            }
            str += "\n";
        }
        return str;
    }
}
