import java.util.Comparator;

/**
 * This class was created to allow the priority queue (frontier) in the AStarSearch class
 * to compare the nodes by their f(n) values.
 */
public class FCompare implements Comparator<BoardNode>{

    /**
     * Compares two given nodes by their f(n) value.
     */
    public int compare( BoardNode n1, BoardNode n2 ){
        int f1 = n1.getF();
        int f2 = n2.getF();
        if( f1 < f2 ){
            return -1;
        }
        else if( f1 > f2 ){
            return 1;
        }
        else{
            return 0;
        }
    }
}
