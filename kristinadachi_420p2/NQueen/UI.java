import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;
import java.util.ArrayList;

/**
 * This class handles all the UI of this program. The user is able to choose which
 * algorithm they would like to use to solve an n-queen problem (steepest-ascent hill
 * climbing or genetic). The user decides on the n value as well as the population
 * size for the genetic algorithm. The time and cost to run either algorithm is displayed
 * as output as well as the solution.
 */
public class UI {
    private Scanner input;
    private Random rand;

    /* UI constructor */
    public UI(){
        input = new Scanner( System.in );
        rand = new Random();
        System.out.println("Welcome to N-Queen Problem Generator!");
        printMenu();
    }

    /**
     * Prints the main menu.
     */
    public void printMenu(){
        System.out.println("\n---------------------------------------");
        System.out.println(" What algorithm would you like to use?");
        System.out.println("    [1] Steepest-Ascent Hill Climbing");
        System.out.println("    [2] Genetic");
        System.out.println("    [3] Quit");
        System.out.println("---------------------------------------");
    }

    /**
     * Prints the menu for the hill climbing algorithm.
     */
    public void printHCMenu(){
        System.out.println("\n----------------------------");
        System.out.println(" What would you like to do?");
        System.out.println("    [1] Generate 1 problem");
        System.out.println("    [2] Test 200 problems");
        System.out.println("    [3] Go back");
        System.out.println("----------------------------");
    }

    /**
     * Reads the user input and performs a certain action based on that input. User has
     * the choice of using the steepest-ascent hill climbing algorithm or the genetic
     * algorithm. They can also quit the program.
     */
    public void start(){
        String choice = "0";
        try{
            while( !choice.equals( "3" ) ){
                System.out.print( "> " );
                choice = input.nextLine();
                switch( choice ){
                    case "":
                        break;
                    case "1":
                        printHCMenu();
                        hillClimbing();
                        printMenu();
                        break;
                    case "2":
                        int size = setupN();
                        solveGenetic(size);
                        printMenu();
                        input.nextLine();
                        break;
                    case "3":
                        input.close();
                        System.exit(0 );
                    default:
                        System.out.println( "Invalid input." );
                }
            }
        } catch( InputMismatchException e ){
            System.out.println( "Invalid input." );
        }
    }

    /**
     * If the user chose to use the hill climbing algorithm, they have the choice of
     * attempting to solve one n-queen progblem or 200 instances of the n-queen problem.
     * They may also go back to the main menu.
     */
    public void hillClimbing(){
        try {
            while( true ) {
                System.out.print("> ");
                String choice = input.nextLine();
                switch (choice) {
                    case "":
                        break;
                    case "1":
                        int size1 = setupN();
                        solveHC(size1);
                        input.nextLine();
                        return;
                    case "2":
                        int size2 = setupN();
                        printResults(test(size2));
                        input.nextLine();
                        return;
                    case "3":
                        return;
                    default:
                        System.out.println("Invalid input.");
                }
            }
        }
        catch( InputMismatchException e ){
            System.out.println( "Invalid input." );
        }
    }

    /**
     * Attempts to solve an n-queen problem by using the hill climbing algorithm. It
     * prints the original board as well as a solution if it can find one. The time and
     * cost of running the algorithm is also displayed.
     */
    public void solveHC( int size ){
        int[] board = generateBoard(size);
        HillClimbing hc = new HillClimbing(board);

        System.out.println("\n----------------");
        System.out.println(" Original Board");
        System.out.println("----------------");
        System.out.println(hc.toString());

        long start = System.currentTimeMillis();
        boolean solved = hc.solve();
        long end = System.currentTimeMillis();

        if (solved) {
            System.out.println("----------");
            System.out.println(" Solution");
            System.out.println("----------");
            System.out.println(hc.toString());
            System.out.println( "Time: " + (end - start) + " ms");
            System.out.println( "Cost (boards generated): " + hc.getCost() );
        } else {
            System.out.println("Solution could not be found.");
        }
    }

    /**
     * Attempts to solve an n-queen problem by using the genetic algorithm. The user
     * must enter the population size (k). It prints the solution as well as the time
     * and cost of running the algorithm.
     */
    public void solveGenetic( int size ){
        int k = setupK();
        ArrayList<Board> population = new ArrayList<>();
        for( int i = 0; i < k; i++ ){
            population.add( new Board(generateBoard( size )) );
        }
        Genetic g = new Genetic( population );

        long start = System.currentTimeMillis();
        g.solve();
        long end = System.currentTimeMillis();

        System.out.println("\n----------");
        System.out.println(" Solution");
        System.out.println("----------");
        System.out.println( g.toString() );
        System.out.println( "Time: " + (end - start) + " ms");
        System.out.println( "Cost (generation count): " + g.getGens() );
    }

    /**
     * Used when testing 200 instances of the n-queen problem. It returns the number of
     * solved instances, the total time to run all 200 instances, and the cost of running
     * all 200 instances.
     */
    public int[] test( int size ){
        int solved = 0;
        int cost = 0;
        int time = 0;
        int[] results = new int[3];

        for( int i = 0; i < 200; i++ ) {
            int[] board = generateBoard(size);
            HillClimbing hc = new HillClimbing(board);

            long start = System.currentTimeMillis();
            if (hc.solve()) {
                solved++;
            }
            long end = System.currentTimeMillis();

            time += (end - start);
            cost += hc.getCost();
        }

        results[0] = solved;
        results[1] = time;
        results[2] = cost;
        return results;
    }

    /**
     * Requests the user to enter the number of queens (n).
     */
    public int setupN(){
        int num = 0;
        while( num == 0 ) {
            try {
                while (num < 4) {
                    System.out.print("Enter the number of queens (higher than 3): ");
                    num = input.nextInt();
                }
            } catch (InputMismatchException e) {
                input.nextLine();
                num = 0;
            }
        }
        return num;
    }

    /**
     * Requests the user to enter the size of the population (k) for the genetic algorithm.
     */
    public int setupK(){
        int num = 0;
        while( num == 0 ) {
            try {
                while (num < 4) {
                    System.out.print("Enter the population size (higher than 3): ");
                    num = input.nextInt();
                }
            } catch (InputMismatchException e) {
                input.nextLine();
                num = 0;
            }
        }
        return num;
    }

    /**
     * Prints the results of testing 200 instances of the n-queen problem and using the hill
     * climbing algorithm. The percentage of instances solved, the average time to solve each
     * board, and the average cost to solve each board are calculated and printed.
     */
    public void printResults( int[] results ){
        System.out.println( "\n---------"  );
        System.out.println( " Results" );
        System.out.println( "---------"  );
        System.out.println("Percent solved: " + results[0] / 2 + "%");
        System.out.println("Average time to solve a board: " + results[1] / 200.0 + " ms");
        System.out.println("Average cost to solve a board: " + results[2] / 200 + " boards generated");
    }

    /**
     * Generates a random n-queen board (array) of a given size.
     */
    public int[] generateBoard( int size ){
        int[] board = new int[size];
        for( int i = 0; i < size; i++ ){
            board[i] = rand.nextInt( size );
        }
        return board;
    }
}
