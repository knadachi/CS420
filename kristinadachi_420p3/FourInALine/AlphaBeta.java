/**
 * This class performs alpha beta pruning with iterative deepening search and represents
 * the AI decisions. The variables alpha and beta represent the current worst and best
 * value respectively. Depth is used for the iterative deepening search. The start time
 * of the algorithm is represented by startTime and limit represents how long the algorithm
 * should run. Finally, aiFirst is true when the AI moves first.
 */
public class AlphaBeta {
    private int alpha;
    private int beta;
    private int depth;
    private long startTime;
    private long limit;
    private boolean aiFirst;

    /* AlphaBeta constructor */
    public AlphaBeta( long lim, boolean b ){
        depth = Integer.MAX_VALUE;
        limit = lim;
        aiFirst = b;
    }

    /**
     * Performs the alpha beta pruning algorithm. Finds and returns the AI's  next
     * move.
     */
    public Action abSearch( Board b ){
        int score;
        int curRow = 0;
        int curCol = 0;
        alpha = Integer.MIN_VALUE;
        beta = Integer.MAX_VALUE;
        int best = alpha;
        startTime = System.currentTimeMillis();

        for( int i = 0; i < b.getBoardLength(); i++ ){
            for( int j = 0; j < b.getBoardLength(); j++ ){
                if( b.getBoard()[i][j] == 0 ){
                    b.move( i, j, false );
                    score = minVal( b, depth-1 );
                    b.undoMove( i, j );
                    if( score > best ){
                        curRow = i;
                        curCol = j;
                        best = score;
                    }
                }
            }
        }
        return new Action( curRow, curCol );
    }

    /**
     * Finds and returns the minimum value on the board.
     */
    private int minVal( Board b, int d ){
        if( cutoff() ){
            return calcScore( b );
        }
        if( b.getEmptySpaces() == 0 ){
            return 0;
        }
        if( b.checkWin() == -1 ){
            return Integer.MIN_VALUE / 2;
        }
        if( b.checkWin() == 1 ){
            return Integer.MAX_VALUE / 2;
        }

        int best = beta;
        for( int i = 0; i < b.getBoardLength(); i++ ){
            for( int j = 0; j < b.getBoardLength(); j++ ){
                if( b.getBoard()[i][j] == 0 ){
                    b.move( i, j, false );
                    best = Integer.min( best, maxVal( b, d-1 ) );
                    b.undoMove( i, j );
                }
            }
        }
        return best;
    }

    /**
     * Finds and returns the maximum value on the board.
     */
    private int maxVal( Board b, int d ){
        if( cutoff() ){
            return calcScore( b );
        }
        if( b.getEmptySpaces() == 0 ){
            return 0;
        }
        if( b.checkWin() == -1 ){
            return Integer.MIN_VALUE / 2;
        }
        if( b.checkWin() == 1 ){
            return Integer.MAX_VALUE / 2;
        }

        int best = alpha;
        for( int i = 0; i < b.getBoardLength(); i++ ){
            for( int j = 0; j < b.getBoardLength(); j++ ){
                if( b.getBoard()[i][j] == 0 ){
                    b.move( i, j, true );
                    best = Integer.max( best, minVal( b,d-1 ) );
                    b.undoMove( i, j );
                }
            }
        }
        return best;
    }

    /**
     * Calculates and returns the score of a board state.
     */
    private int calcScore( Board b ){
        int score = 0;

        for( int i = 0; i < b.getBoardLength(); i++ ){
            for( int j = 0; j < b.getBoardLength(); j++ ){
                score += potential( i, j, b );
            }
        }
        return score;
    }

    /**
     * Checks three spaces in every direction from a space and calculates the potential.
     */
    private int potential( int row, int col, Board b ){
        int check = b.getBoard()[row][col];
        int score = 0;
        int temp = 0;

        /**
         * AI pretends to be the player (O) and checks for any moves that enables the player
         * to win.
         */
        if( !aiFirst && check > 0 ){
            check = -1;
            b.getBoard()[row][col] = -1;
            if( b.checkWin() == -1 ){
                score += 10000;
            }
            b.getBoard()[row][col] = 1;
        }

        /**
         * When the AI does NOT start first, this conditional tricks the AI into thinking it
         * wants the player to win. As a result, it makes it difficult for the player by blocking
         * potential win moves.
         */
        if( !aiFirst ){
            check = -1;
        }

        /**
         * The following conditional blocks check three spaces in each direction (up, down, left,
         * and right). They each check special cases as well.
         */
        if( row >= 3 ){
            for( int i = 1; i < 4; i++ ){
                if( b.getBoard()[row-i][col] == check ){
                    temp += 5 - i;
                }
                else if( b.getBoard()[row-i][col] != 0 ){
                    temp = -1;
                    i = 4;
                }
            }
            if( row < b.getBoardLength() - 1 ){
                if( temp == 7 && b.getBoard()[row+1][col] == 0 ){
                    temp = 10000;
                }
            }
            if( temp == 9 && !aiFirst ){
                temp = 9990;
            }
            score += temp;
            temp = 0;
        }
        else{
            score--;
        }

        if( row < b.getBoardLength() - 3 ){
            for( int i = 1; i < 4; i++ ){
                if( b.getBoard()[row+i][col] == check ){
                    temp += 5 - i;
                }
                else if( b.getBoard()[row+i][col] != 0 ){
                    temp = -1;
                    i = 4;
                }
            }
            if( row > 0 ){
                if( temp == 7 && b.getBoard()[row-1][col] == 0 ){
                    temp = 10000;
                }
            }
            if( temp == 9 && !aiFirst ){
                temp = 9990;
            }
            score += temp;
            temp = 0;
        }
        else{
            score--;
        }

        if( col >= 3 ){
            for( int i = 1; i < 4; i++ ){
                if( b.getBoard()[row][col-i] == check ){
                    temp += 5 - i;
                }
                else if( b.getBoard()[row][col-i] != 0 ){
                    temp = -1;
                    i = 4;
                }
            }
            if( col < b.getBoardLength() - 1 ){
                if( temp == 7 && b.getBoard()[row][col+1] == 0 ){
                    temp = 10000;
                }
            }
            if( temp == 9 && !aiFirst ){
                temp = 9990;
            }
            score += temp;
            temp = 0;
        }
        else{
            score--;
        }

        if( col < b.getBoardLength() - 3 ){
            for( int i = 1; i < 4; i++ ){
                if( b.getBoard()[row][col+i] == check ){
                    temp += 5 - i;
                }
                else if( b.getBoard()[row][col+i] != 0 ){
                    temp = -1;
                    i = 4;
                }
            }
            if( col > 0 ){
                if( temp == 7 && b.getBoard()[row][col-1] == 0 ){
                    temp = 10000;
                }
            }
            if( temp == 9 && !aiFirst ){
                temp = 9990;
            }
            score += temp;
        }
        else{
            score--;
        }

        /**
         * Checks immediate diagonals and adds them to the score (mostly intended to break
         * any possible ties.
         */
        if( !aiFirst ){
            if( row >= 1 && row < b.getBoardLength() - 1 && col >= 1 && col < b.getBoardLength() - 1 ){
                if( b.getBoard()[row+1][col+1] == check || b.getBoard()[row+1][col-1] == check || b.getBoard()[row-1][col+1] == check || b.getBoard()[row-1][col-1] == check ){
                    score++;
                }
            }
            check = b.getBoard()[row][col];
        }

        return score * check;
    }

    /**
     * Limits the program by forcing it to return the best solution found so far in the specified time.
     * Returns true if its time is up and false otherwise.
     */
    private boolean cutoff(){
        return ( System.currentTimeMillis() - startTime > limit );
    }
}
