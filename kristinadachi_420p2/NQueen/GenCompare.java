import java.util.Comparator;

/**
 * This class is used to compare boards by their fitness value which is represented by
 * the number of non-attacking queens on the board. It is used for the Genetic Algorithm
 * and orders boards in order of increasing fitness values.
 */
public class GenCompare implements Comparator<Board>{
    public int compare( Board b1, Board b2 ){
        if( b1.getFitness() < b2.getFitness() ){
            return -1;
        }
        else if( b1.getFitness() > b2.getFitness() ){
            return 1;
        }
        else{
            return 0;
        }
    }
}
