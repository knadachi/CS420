import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;

/**
 * This class handles all the UI of this program. The user is able to enter their own
 * 8-puzzle to solve or choose to generate a random, solvable puzzle. This class also
 * checks if a given puzzle is solvable as well as if it is valid.
 */
public class UI {
    /* Scanner */
    private Scanner input;
    /* 2d array used when generating a random 8-puzzle */
    int[][] puzzle = {{-1, -1, -1},
                      {-1, -1, -1},
                      {-1, -1, -1}};

    /* Constructor that creates a Scanner and prints the menu */
    public UI(){
        input = new Scanner( System.in );
        System.out.println( "Welcome to 8-Puzzle Solver!" );
        printMenu();
    }

    /**
     * Prints the main menu.
     */
    public void printMenu(){
        System.out.println("------------------------------");
        System.out.println(" What would you like to do?");
        System.out.println("    [1] Enter your own puzzle");
        System.out.println("    [2] Generate a puzzle");
        System.out.println("    [3] Quit");
        System.out.println("------------------------------");
    }

    /**
     * Reads user input and performs an action based on that input. User has the choice of
     * entering their own puzzle, generating a random puzzle, or quitting the program.
     */
    public void start(){
        String choice = "0";
        try {
            while(!choice.equals( "3" ) ){
                System.out.print("> ");
                choice = input.nextLine();
                switch (choice) {
                    case "":
                        input.reset();
                        break;
                    case "1":
                        enterPuzzle();
                        break;
                    case "2":
                        generatePuzzle();
                        break;
                    case "3":
                        input.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid input.");
                        input.reset();
                }
            }
        }
        catch( InputMismatchException e ){
            System.out.println( "Invalid input." );
            input.nextLine();
            printMenu();
            start();
        }
    }

    /**
     * Prints the menu for entering a puzzle. If the puzzle is valid, A* search is used
     * with both heuristics h1 and h2. The step-by-step solution, time it takes to perform
     * the searches, and the number of nodes generated are printed for each heuristic.
     */
    public void enterPuzzle(){
        try {
            System.out.println("\nPlease use the following format...");
            System.out.println("# # #\n# # #\n# # #");
            System.out.println("Now enter your puzzle:");

            String line = input.nextLine();
            String[] args1 = line.split("\\s");

            line = input.nextLine();
            String[] args2 = line.split("\\s");

            line = input.nextLine();
            String[] args3 = line.split("\\s");

            int[][] puzzle = new int[3][3];
            int[] arr = new int[9];
            for (int i = 0; i < 3; i++) {
                puzzle[0][i] = Integer.parseInt(args1[i]);
                puzzle[1][i] = Integer.parseInt(args2[i]);
                puzzle[2][i] = Integer.parseInt(args3[i]);

                arr[i] = Integer.parseInt(args1[i]);
                arr[i + 3] = Integer.parseInt(args2[i]);
                arr[i + 6] = Integer.parseInt(args3[i]);
            }

            if (validate(arr)) {
                /* Running the search using h1 */
                AStarSearch search = new AStarSearch(puzzle);
                runSearch(search, false);

                /* Running the search using h2 */
                search.setHFlag(true);
                runSearch(search, true);
            } else {
                System.out.println("\nThe entered puzzle is not solvable.\n");
            }

            printMenu();
        }
        catch( Exception e ){
            System.out.println( "Invalid input." );
            input.reset();
            printMenu();
            start();
        }
    }

    /**
     * Generates a random, solvable puzzle. After generating a valid 8-puzzle, A* search is
     * performed using both heuristics h1 and h2. The step-by-step solution, time it takes to
     * perform the searches, and the number of nodes generated are printed for each heuristic.
     */
    public void generatePuzzle(){
        Random rand = new Random();
        int[] arr = new int[9];
        int num;

        while( !validate( arr ) ) {
            resetPuzzle();
            num = -1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    while (contains(num, puzzle)) {
                        num = rand.nextInt(9);
                    }
                    puzzle[i][j] = num;
                }
            }

            for (int i = 0; i < 3; i++) {
                arr[i] = puzzle[0][i];
                arr[i + 3] = puzzle[1][i];
                arr[i + 6] = puzzle[2][i];
            }
        }

        System.out.println( "\nGenerated Puzzle:" );
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                System.out.print( puzzle[i][j] + " " );
            }
            System.out.println();
        }

        /* Running the search using h1 */
        AStarSearch search = new AStarSearch(puzzle);
        runSearch(search, false);

        /* Running the search using h2 */
        search.setHFlag(true);
        runSearch(search, true);

        printMenu();
    }

    /**
     * Used to determine if a given 8-puzzle is valid. This means that it is solvable,
     * entered using the correct format, and contains valid numbers (with 0 being the
     * space). To check if it is solvable, the number of inverses is calculated. If it is
     * even, the puzzle is solvable and if it is odd, the puzzle is not solvable.
     */
    public boolean validate( int[] arr ){
        int invCount = 0;

        for( int i = 0; i < arr.length; i++ ){
            /* Counts the number of inverses in the puzzle */
            for( int j = i + 1; j < arr.length; j++ ){
                if( arr[i] > arr[j] ){
                    invCount++;
                }
                if( arr[i] == 0 && i % 2 == 1 ) {
                    invCount++;
                }
            }

            /* Ensures that there are no repeated numbers in the puzzle */
            for( int j = 0; j < arr.length; j++ ){
                if( i != j && arr[i] == arr[j] ){
                    return false;
                }
            }

            /* Checks for any invalid numbers (range is from 0-8) */
            if( arr[i] < 0 || arr[i] > 8 ){
                return false;
            }
        }

        return ( invCount % 2 == 0 );
    }

    /**
     * Checks if the given array contains a certain number. Returns true if it does and
     * false otherwise.
     */
    public boolean contains( int num, int[][] arr ){
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                if ( arr[i][j] == num ){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Resets the puzzle to values of -1.
     */
    public void resetPuzzle(){
        for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 3; j++ ){
                puzzle[i][j] = -1;
            }
        }
    }

    /**
     * Runs the A* search using the specified heuristic and prints the results. If h is
     * false, the h1 heuristic is used and if h2 is true, the h2 heuristic is used.
     */
    public void runSearch( AStarSearch search, boolean h ){
        long start = System.nanoTime();
        search.findSolution();
        long end = System.nanoTime();

        System.out.println( "\n-------------" );
        if( h ){
            System.out.println( " H2 Solution" );
        }
        else{
            System.out.println( " H1 Solution" );
        }
        System.out.println( "-------------" );
        System.out.println( search.toString() );
        System.out.println( "Time Elapsed: " + (end - start) );
        System.out.println( "Nodes Generated: " + search.getSize() + "\n" );
    }
}
