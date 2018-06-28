import java.util.ArrayList;
import java.util.Random;
import java.util.Comparator;
import java.util.Collections;

/**
 * This class is used to represent using the Genetic Algorithm to solve an N-Queens
 * problem. An ArrayList is used to represent the population. It also contains fields
 * including popSize (population size), totalFit (total fitness), gens (used for the
 * random selection of parents), and dimension (used to determine the ideal fitness
 * value). A random number generator and the GenCompare class are used as well.
 */
public class Genetic {
    private ArrayList<Board> population = new ArrayList<>();
    private int popSize;
    private int totalFit;
    private int gens;
    private int dimension;
    private Random rand;
    private Comparator<Board> genCompare;

    /* Genetic constructor */
    public Genetic( ArrayList<Board> pop ){
        population = pop;
        popSize = pop.size();
        totalFit = 0;
        gens = 0;
        dimension = pop.get(0).getBoard().length;
        rand = new Random();
        genCompare = new GenCompare();
    }

    /**
     * Returns the generation count.
     */
    public int getGens(){
        return gens;
    }

    /**
     * Returns the ideally fit board (only used when printing the solution).
     */
    public int[] getBoard(){
        return population.get( popSize - 1 ).getBoard();
    }

    /**
     * This method uses the Genetic Algorithm to solve a given N-Queen problem:
     *    [1] Finds the fitness value for all members in the population.
     *    [2] Selects two random members to be parents for the next generation.
     *    [3] Finds and removes the 25% of the population considered to be
     *        "least fit."
     *    [4] Populates the successor generation by crossing parents with
     *        remaining members (reproducing).
     *    [5] Applies random mutations to the 25% of the successor generation
     *        considered to be "least fit."
     *    [6] Repeats algorithm until the ideally fit member is found.
     */
    public boolean solve(){
        ArrayList<Board> successors = new ArrayList<>();
        int[] parent1;
        int[] parent2;

        while( true ){
            findFitness();

            int fitness = population.get(popSize - 1).getFitness();
            if( fitness == (int)( dimension * ((dimension - 1 ) / 2.0)) ){
                return true;
            }

            parent1 = findParent().clone();
            parent2 = findParent().clone();

            successors.add( new Board( crossover( parent1, parent2 ) ));

            for( int i = 0; i <= population.size() / 4; i++ ){
                population.remove(0 );
            }

            reproduce( successors, parent1, parent2 );

            Collections.sort( successors, genCompare );

            mutate( successors );

            population.clear();
            population.addAll( successors );
            successors.clear();

            gens++;
        }
    }

    /**
     * Calculates the fitness value for every member in the population. The weight
     * is changed because it represents the member's probability of being chosen
     * as a parent for the next generation.
     */
    public void findFitness(){
        totalFit = 0;
        Collections.sort( population, genCompare );
        for( int i = 0; i < population.size(); i++ ){
            population.get(i).setWeight(totalFit);
            totalFit += population.get(i).getFitness();
        }
    }

    /**
     * Used to select the parents of the next generation. Fitter members (determined
     * by their weight) are more likely to be chosen. Decrements are used to ensure
     * the probability's consistency.
     */
    public int[] findParent(){
        int count = 0;
        int decrement;
        int num = rand.nextInt( totalFit );
        while( count < population.size() - 1 && num > population.get(count).getWeight() ){
            count++;
        }

        int[] parent = population.get(count).getBoard();
        decrement = population.get(count).getFitness();
        totalFit -= decrement;
        population.remove( count );

        for( int i = count + 1; i < population.size(); i++ ){
            population.get(i).decrementWeight( decrement );
        }

        return parent;
    }

    /**
     * Used to calculate the combination of two boards to produce the next generation. The
     * crossover point, represented by the variable crossPoint, is randomly chosen. The
     * indices before the point are changed to values of board1 and the indices after the
     * point are changed to values of board2.
     */
    public int[] crossover( int[] board1, int[] board2 ){
        int crossPoint = rand.nextInt( board1.length );
        int[] child = new int[board1.length];
        for( int i = 0; i < child.length; i++ ){
            if( i < crossPoint ){
                child[i] = board1[i];
            }
            else{
                child[i] = board2[i];
            }
        }
        return child;
    }

    /**
     * Used for the reproduction of the parent boards with other random members
     * in the population and populates the successor generation.
     */
    public void reproduce( ArrayList<Board> successors, int[] parent1, int[] parent2 ){
        while( successors.size() < popSize){
            int index = rand.nextInt( population.size() );
            int[] child;
            int[] clone = population.get(index).getBoard().clone();
            if( rand.nextBoolean() ){
                child = crossover( parent1, clone );
            }
            else{
                child = crossover( parent2, clone );
            }
            successors.add( new Board(child) );
        }
    }

    /**
     * Used to apply mutations on the lower 25% of the population considered to be
     * "least fit."
     */
    public void mutate( ArrayList<Board> successors ){
        for( int i = 0; i < successors.size() / 4; i++ ){
            successors.get(i).moveQueen();
        }
    }

    /**
     * Returns a String containing the board:
     *   - = empty space
     *   Q = queen
     */
    public String toString(){
        String str = "";
        for( int i = 0; i < getBoard().length; i++ ){
            for( int j = 0; j < getBoard().length; j++ ){
                if( i == getBoard()[j] ){
                    str += ( "Q " );
                }else
                    str += ( "- " );
            }
            str += "\n";
        }
        return str;
    }
}