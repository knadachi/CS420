/**
 * This class represents one board state of the Four In A Line game. A board is
 * an 8x8 2D array. The number of empty spaces on the board is also kept track
 * of.
 */
public class Board {
    private int[][] board;
    private int emptySpaces;

    /* Board constructor */
    public Board( int[][] b, int s ){
        board = b;
        emptySpaces = s;
    }

    /**
     * Returns the current board.
     */
    public int[][] getBoard(){
        return board;
    }

    /**
     * Returns the current number of empty spaces.
     */
    public int getEmptySpaces(){
        return emptySpaces;
    }

    /**
     * Returns the length of the board.
     */
    public int getBoardLength(){
        return board.length;
    }

    /**
     * Checks the board for any four O's in a row or any four X's in a row by
     * traversing the entire array. Returns 1 if the player wins, -1 if the AI wins,
     * and 0 if there isn't a winner yet.
     */
    public int checkWin(){
        for( int i = 0; i < board.length; i++ ){
            for( int j = 0; j < board.length; j++ ){
                if( board[i][j] == -1 || board[i][j] == 1 ){
                    if( checkFour( i, j, board[i][j] ) ){
                        return board[i][j];
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Helper method for the checkWin method. Checks each row and column and
     * returns true if four O's in a row or four X's in a row are found. Returns
     * false otherwise.
     */
    private boolean checkFour( int row, int col, int player ){
        if( row + 4 <= board.length && checkCol( row, col, player ) ){
            return true;
        }
        if( col + 4 <= board.length && checkRow( row, col, player ) ){
            return true;
        }
        return false;
    }

    /**
     * Helper method for the checkFour method. Checks each row and returns true if
     * four O's in a row or four X's in a row are found. Returns false otherwise.
     */
    private boolean checkRow( int row, int col, int player ){
        for( int i = col; i < col + 4; i++ ){
            if( board[row][i] != player ){
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method for the checkFour method. Checks each column and returns true if
     * four O's in a row or four X's in a row are found. Returns false otherwise.
     */
    private boolean checkCol( int row, int col, int player ){
        for( int i = row; i < row + 4; i++ ){
            if( board[i][col] != player ){
                return false;
            }
        }
        return true;
    }

    /**
     * Converts the given String to a row index and a column index for the board (2D
     * array). Returns true if the move is valid and false otherwise.
     */
    public boolean move( String input, boolean playerTurn ){
        int row = input.charAt( 0 ) - 97;
        if( row < 0 ){
            row = input.charAt( 0 ) - 65;
        }
        int col = input.charAt( 1 ) - 49;
        return move( row, col, playerTurn );
    }

    /**
     * Helper method for the move method. Returns true if the move is valid and if the
     * board is successfully updated. Returns false otherwise.
     */
    public boolean move( int row, int col, boolean playerTurn ){
        if( row < 0 || col < 0 || row >= board.length || col >= board.length || board[row][col] != 0 ){
            return false;
        }
        emptySpaces--;
        if( playerTurn ){
            board[row][col] = -1;
        }
        else{
            board[row][col] = 1;
        }
        return true;
    }

    /**
     * Resets the space at the given row and column. Returns true if it is successful
     * and false otherwise.
     */
    public boolean undoMove( int row, int col ){
        if( board[row][col] == 0 ){
            return false;
        }
        else{
            emptySpaces++;
            board[row][col] = 0;
            return true;
        }
    }

    /**
     * Returns a String of the current board state.
     *    "-" = empty space
     *    "O" = player move
     *    "X" = AI move
     */
    public String toString(){
        String str = "  1 2 3 4 5 6 7 8\n";
        char ch = 'A';
        for( int i = 0; i < board.length; i++ ){
            str += ( ( ch++ ) + " " );
            for( int j = 0; j < board.length; j++ ){
                if( board[i][j] == 0 ){
                    str += "- ";
                }
                else if( board[i][j] == 1 ){
                    str += "X ";
                }
                else{
                    str += "O ";
                }
            }
            str += "\n";
        }
        return str;
    }
}
