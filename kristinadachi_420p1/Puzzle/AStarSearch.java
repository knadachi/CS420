import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class uses the A* search algorithm to solve and print the solution for any
 * solvable 8-puzzle. A priority queue represents the frontier which uses the Comparator
 * class to sort the BoardNode objects by f(n). A hash set represents the explored set
 * containing nodes we already visited. This class also keeps track of the number of
 * nodes generated in our tree (graph search is used). Two different heuristic functions
 * can be used for this A* search:
 *    (1) h1 = the number of misplaced tiles
 *    (2) h2 = the sum of the distances of the tiles from their goal positions (Manhattan
 *             Distance)
 */
public class AStarSearch {
    /* Contains the root node */
    private BoardNode root;
    /* Keeps track of the nodes generated */
    private int size;

    /* Used to compare nodes by f(n) */
    private Comparator<BoardNode> fCompare;
    /* Used for the frontier which contains nodes ordered by their f(n) value */
    private PriorityQueue<BoardNode> frontier;
    /* Used for the explored set which contains already visited nodes */
    private HashSet<String> explored;
    /* Contains the solution path in the tree */
    private ArrayList<BoardNode> solution;

    /* Goal key used to check if we reached the goal state */
    private final String GOAL_KEY = "012345678";
    /* Used for the calculation of H2 */
    private static ArrayList<int[]> goalH2;

    /* If this flag is false, we use H1; if it's true, we use H2 */
    private static boolean hFlag;

    /* Constructor that initializes the components of this search */
    public AStarSearch( int[][] node ){
        root = new BoardNode( node );
        size = 0;
        fCompare = new FCompare();
        frontier = new PriorityQueue<>( fCompare );
        explored = new HashSet<>();
        solution = new ArrayList<>();
        goalH2 = new ArrayList<>();
        hFlag = false;
    }

    /**
     * Returns the number of nodes generated in the search tree.
     */
    public int getSize(){
        return size;
    }

    /**
     * Returns goalH2 which is used by the BoardNode class to calculate the Manhattan
     * distance for each node when using h2.
     */
    public static ArrayList<int[]> getGoalH2(){
        return goalH2;
    }

    /**
     * Returns false if we are using h1 and true if we are using h2.
     */
    public static boolean getHFlag(){
        return hFlag;
    }

    /**
     * Used to switch between the two heuristics.
     */
    public void setHFlag( boolean b ){
        hFlag = b;
    }

    /**
     * Represents using the A* search algorithm to find a solution for any solvable
     * 8-puzzle. First, all fields are cleared to avoid any leftover information. Then,
     * we check which heuristic is going to be used. Finally, the A* search algorithm is
     * used to find and print the solution.
     */
    public void findSolution(){
        clearBoard();
        frontier.add( root );
        size++;

        if( hFlag ){
            setupH2();
        }

        while( !frontier.isEmpty() ){
            String key = createKey( frontier.peek().getBoard() );
            if( key.equals( GOAL_KEY ) ){
                BoardNode solved = frontier.peek();
                findPath( solved );
                frontier.clear();
            }
            else{
                BoardNode temp = frontier.remove();
                explored.add( createKey(temp.getBoard()) );
                findChildren( temp );
            }
        }
    }

    /**
     * Generates the possible children (next moves) of the current node. Nodes that are
     * in the explored set are not generated.
     */
    public void findChildren( BoardNode cur ){
        int row = 0;
        int col = 0;
        int[][] curBoard = cur.getBoard();
        /* A copy of the current node is created so that we can edit it */
        int[][] copy = createCopy( curBoard );

        /* Locates the space in the current node (board) */
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                if( copy[i][j] == 0 ){
                    row = i;
                    col = j;
                }
            }
        }

        /* The following four conditional statements use the space and generates the
         * next possible moves for the current node.
         */
        if( row < 2 ){
            down( copy, row, col );
            BoardNode temp = new BoardNode( copy, cur );
            if( !inExplored( createKey( temp.getBoard() ))){
                frontier.add( temp );
                size++;
            }
        }

        if( row > 0 ){
            copy = createCopy( curBoard );
            up( copy, row, col );
            BoardNode temp = new BoardNode( copy, cur );
            if( !inExplored( createKey( temp.getBoard() ))){
                frontier.add( temp );
                size++;
            }
        }

        if( col < 2 ){
            copy = createCopy( curBoard );
            right( copy, row, col );
            BoardNode temp = new BoardNode( copy, cur );
            if( !inExplored( createKey( temp.getBoard() ))){
                frontier.add( temp );
                size++;
            }
        }

        if( col > 0 ){
            copy = createCopy( curBoard );
            left( copy, row, col );
            BoardNode temp = new BoardNode( copy, cur );
            if( !inExplored( createKey( temp.getBoard() ))){
                frontier.add( temp );
                size++;
            }
        }
    }

    /**
     * Moves the empty space down on the given board.
     */
    private void down( int[][] arr, int row, int col ){
        arr[row][col] = arr[row+1][col];
        arr[row+1][col] = 0;
    }

    /**
     * Moves the empty space up on the given board.
     */
    private void up( int[][] arr, int row, int col ){
        arr[row][col] = arr[row-1][col];
        arr[row-1][col] = 0;
    }

    /**
     * Moves the empty space right on the given board.
     */
    private void right( int[][] arr, int row, int col ){
        arr[row][col] = arr[row][col+1];
        arr[row][col+1] = 0;
    }

    /**
     * Moves the empty space left on the given board.
     */
    private void left( int[][] arr, int row, int col ){
        arr[row][col] = arr[row][col-1];
        arr[row][col-1] = 0;
    }

    /**
     * When using h2, this method is used to find and initialize the goal positions for
     * each number.
     */
    private void setupH2(){
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                int[] arr = new int[2];
                arr[0] = i;
                arr[1] = j;
                goalH2.add( arr );
            }
        }
    }

    /**
     * Creates and returns a copy of the given board.
     */
    private int[][] createCopy( int[][] arr ){
        int[][] copy = new int[3][3];
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                copy[i][j] = arr[i][j];
            }
        }
        return copy;
    }

    /**
     * Checks if the given String is in the explored set (thus checking if the node has
     * already been explored. Returns true if it is and false otherwise.
     */
    private boolean inExplored( String key ){
        return explored.contains(key);
    }

    /**
     * Uses the given array and creates a String of its contents (stored as a key). This
     * created key is returned.
     */
    private String createKey( int[][] arr ){
        String key = "";
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                key += arr[i][j];
            }
        }
        return key;
    }

    /**
     * Clears all fields to avoid any leftover information from a previous puzzle.
     */
    private void clearBoard(){
        frontier.clear();
        explored.clear();
        solution.clear();
        size = 0;
    }

    /**
     * Traces the solution path from the given node to the root.
     */
    private void findPath( BoardNode node ){
        BoardNode step = node;
        while( step != null ){
            solution.add( step );
            step = step.getParent();
        }
    }

    /**
     * Saves the solution path to a String and returns it.
     */
    public String toString(){
        String str = "";
        Collections.reverse( solution );
        for( BoardNode n : solution ){
            str += n.toString();
        }
        return str;
    }
}
