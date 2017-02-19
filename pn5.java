// For Java 8
//
// This program searches for ways of perfectly filling a square that is ( N * (N+1)/2 ) units on a side with a 
// collection of tiles such that you have 1 tile that is 1 unit on a side, 2 tiles that are 2 units on a side, 
// three that are 3x3, four that are 4x4, and so on up to N tiles that are NxN. Each arrangement of tiles
// that uses all the tiles to exactly fill the board is considered a solution. The smallest value of N for 
// which a solution exists is 8. This kind of tiling puzzle was invented by mathematician Bob Wainwright and
// he gave a presentation in 1996 to the second Gathering for Gardner on what he dubbed "Partridge Puzzles".
//
// Here some links:
//
// http://www2.stetson.edu/~efriedma/papers/partridge.pdf
// http://www2.stetson.edu/~efriedma/mathmagic/0802.html
// http://www.mathpuzzle.com/partridge.html
//
// Algorithm Description:
//
// First, create a matrix whose width and height are N*(N+1)/2 that represents the board. Each position in
// the matrix contains a value representing the size of the tile that occupies that location. If the value is
// zero it means the location is not occupied.
//
// Second, initialize an array of tiles as follows. We will have a total of N*(N+1)/2 tiles and each tile
// has two properties, its size and a flag indiating whether it is being used (i.e., has been placed on the
// board).
//
// Define a recursive function which is called with four arguments: the board matrix, the tile array, a row
// position to start scanning from, and a column position to start scanning from
//
// 1. Traverse the board, left to right, bottom to top, starting at the position specified in the arguments
// 2. If the location is vacant, traverse the tile list starting with the first tile until we find a tile
// that is not being used and of a size that we haven't previously tried.
// 3. Check if the file will fit on the board if placed at the postion under consideration. 
// 3.a If it would not fit then bail. No point in trying any of the remaining tiles since the tile list 
// holds them in order of inreasing size.
// 3.b. If it would fit then add it to the board and mark it as used on the tile list. Check if we have 
// used up all the tiles.
// 3.b.i. If there are no unused tiles then that means we have found a solution. Display it.
// 3.b.ii. If there are still unused tiles then call this function (i.e., recurse).
// 3.b.iii. Back out of the last move so that we can continue trying more tiles. That is, remove the tile 
// from the board and mark the tile as unused on the tile list.
// 4. Once we tried all the available tiles, return to our caller.
// 5. Contiue traversing the board.
//
// Back-of-the-envelope complexity analysis: The three loops (row, column, and tile traversal) are all
// O(N^2) so without the recursion, that would give us O(N^6) complexity. With recursion I think that gives
// a complexity of a O(N^6^)*(N^6)) or O(N^12). 
//
// Note: There are several possible optimizations to the above algorithm and they all focus on reducing the
// number of board configurations to be examined since the fan-out is so extreme. The five optimizations 
// I've added so far help a lot. They reduced the running time for finding all 18,656 solutions for N=8 from
// about 12 hours to about 2 hours.
//

import java.io.Console;
import java.time.Instant;
import java.text.DecimalFormat;

class Tile {
    int size;
    boolean inuse;

    public Tile(int setSize, boolean setUse) {
        size = setSize;
        inuse = setUse;
    }
}

class TileStack {
    Tile[] tile_array;
    int tile_count;
    private int index;

    public TileStack(int BigN) {
        tile_count = BigN*(BigN+1)/2;
        tile_array = new Tile[tile_count];

        // create tile list
        index = 0;
        for (int outer = 1; outer < BigN+1; outer++) {
            for (int inner = 1; inner < outer+1; inner++) {
                Tile oneTile = new Tile(outer, false);
                tile_array[index] = oneTile;
                index++;
            }
        }
    }

    public void show() {
        for (index = 0; index < tile_count; index++) {
            System.out.println("Tile #"+index+" is ["+tile_array[index].size+", "+tile_array[index].inuse+"]");
        }
    }

    public boolean unused() {
        for (index = 0; index < tile_count; index++) {
            if (tile_array[index].inuse == false)
                return true;
        }
        return false;
    }

    public int almost_smallest(int tsize) {
        boolean seen_one = false;

        for (index = 0; index < tile_count; index++) {
            if (tile_array[index].inuse == false) {
                if (tile_array[index].size != tsize)
                    return tile_array[index].size;
                else {
                    if (seen_one = true)
                        return tile_array[index].size;
                    else
                        seen_one = true;
                }
            }
        }
        return 0; // this should never happen because the caller has ensured that there is more than one tile left
    }
}

class BoardSquare {
    private int row, col;
    static int[][] matrix;
    static int board_size;
    static long attempts;
    static long solutions;
    static long startTime;

    static String CSI_BEG="\u001B[";
    static String CSI_END="m";
    static String CSI_OFF=CSI_BEG+"0m";
    static String CSI_NONE="";
    static String CSI_black=CSI_BEG+"0;30;40"+CSI_END;
    static String CSI_red=CSI_BEG+"0;30;41"+CSI_END;
    static String CSI_green=CSI_BEG+"0;30;42"+CSI_END;
    static String CSI_blue=CSI_BEG+"0;30;44"+CSI_END;
    static String CSI_yellow=CSI_BEG+"0;30;43"+CSI_END;
    static String CSI_purple=CSI_BEG+"0;30;45"+CSI_END;
    static String CSI_teal=CSI_BEG+"0;30;46"+CSI_END;
    static String CSI_grey=CSI_BEG+"0;30;47"+CSI_END;
    static String[] CSI_COLORS={CSI_NONE, CSI_black, CSI_red, CSI_green, CSI_blue, CSI_yellow, CSI_purple, CSI_teal, CSI_grey};
    static String[] TILE_SYMBOLS={".","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"}; // Ought to be enough for now

    public BoardSquare(int BigN) {
        board_size = BigN*(BigN+1)/2;
        matrix = new int[board_size][board_size]; 
        attempts = 0;
        solutions = 0;
        startTime = Instant.now().toEpochMilli();
    }

    public void show() {
        for (row = board_size-1; row >= 0; row--) {
            for (col = 0; col < board_size; col ++) {
                int tsize=matrix[row][col];

                // if N is 8 or less and you want to print in color, this will work:
                // System.out.print(CSI_COLORS[tsize]+tsize+" "+CSI_OFF);

                // if N is 25 or less and you want to use letters for values greater than 9, this will work:
                if (tsize < TILE_SYMBOLS.length) // use symbols if we have enough symbols
                    System.out.print(TILE_SYMBOLS[tsize]+" ");
                else
                    System.out.print(tsize+" ");
            }
            System.out.println();
        }
    System.out.println();
    }

    public boolean canfit(int pos_row, int pos_col, int tsize) {
        if (pos_row+tsize > board_size || pos_col+tsize > board_size) {
            return false;
        }

        for (row = pos_row; row < pos_row+tsize; row++) {
            for (col = pos_col; col < pos_col+tsize; col++) {
                if (matrix[row][col] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addtile(int pos_row, int pos_col, int tsize) {
        for (row = pos_row; row < pos_row+tsize; row++) {
            for (col = pos_col; col < pos_col+tsize; col++) {
                matrix[row][col]=tsize;
            }
        }
    }

    public void deltile(int pos_row, int pos_col, int tsize) {
        for (row = pos_row; row < pos_row+tsize; row++) {
            for (col = pos_col; col < pos_col+tsize; col++) {
                matrix[row][col]=0;
            }
        }
    }
}


public class pn5 {
    static DecimalFormat cform = new DecimalFormat("###,###,###,###,##0");
    static DecimalFormat dform = new DecimalFormat("###,##0.00");

    static int prints = 10;

    /*
    static double optc1=0;
    static double optc2=0;
    static double optc3=0;
    static double optc4=0;
    static double optc5=0;
    */

    public static void main(String[] args) {
        int index, BigN, tile_count, board_size;
        TileStack tilesTop;
        BoardSquare boardTop;

        BigN = 8;
        tilesTop = new TileStack(BigN);
        boardTop = new BoardSquare(BigN);
        // tilesTop.show();

        System.out.println("Solution for N = "+cform.format(BigN));
        System.out.println();

        go_deep(boardTop, tilesTop, 0, 0);

        System.out.println("Tried "+cform.format(boardTop.attempts)+" configurations in "+dform.format((Instant.now().toEpochMilli()-boardTop.startTime)/(float)(1000*60))+ " minutes");

        /*
        System.out.println("Optimization 1: "+cform.format(optc1));
        System.out.println("Optimization 2: "+cform.format(optc2));
        System.out.println("Optimization 3: "+cform.format(optc3));
        System.out.println("Optimization 4: "+cform.format(optc4));
        System.out.println("Optimization 5: "+cform.format(optc5));
        */
    }

    public static void go_deep(BoardSquare board, TileStack tiles, int start_row, int start_col) {
        board.attempts++;

        /*
	    if ((board.attempts % (1L << 34)) == 0) 
            prints = 10;

        if (prints > 0) {
            System.out.println("Attempt #"+cform.format(board.attempts)+" after "+dform.format((Instant.now().toEpochMilli()-board.startTime)/(float)(1000*60))+" minutes");
            board.show();
            prints -=1;
        }
        */

        boolean init_loop = true;

        for (int row = start_row; row < board.board_size; row++) {
            for (int col = 0; col < board.board_size; col++) {
                if (init_loop)
                    col = start_col;
                init_loop = false;

                if (board.matrix[row][col] == 0) { // found an empty board position
                    int last_tile_used = 0;
                    for (int index = 0; index < tiles.tile_count; index++) {
                        if (index == 0 && (row < 2 || row > board.board_size-3 || col < 2 || col > board.board_size-3)) {
                            // optc1++;
                            // System.out.println("Optimization 1");
                            continue; 
                        }

                        if (tiles.tile_array[index].inuse == false  && tiles.tile_array[index].size != last_tile_used) { // unused tile and a size we haven't already tried
                            last_tile_used = tiles.tile_array[index].size;

                            if (last_tile_used == 1) { // special considerations if we're trying to fit the 1x1 
                                if (board.matrix[row+1][col-1] != 0) { 
                                    if (board.matrix[row][col+1] == 0 || board.matrix[row+1][col+1] !=0) { 
                                        // optc2++;
                                        // System.out.println("Optimization 2");
                                        continue;
                                    }
                                } else {
                                    if (board.matrix[row][col+1] == 0 && board.matrix[row][col+2] !=0) {
                                        // optc3++;
                                        // System.out.println("Optimization 3");
                                        continue;
                                    }
                                }
                            }

                            if (!board.canfit(row, col, last_tile_used)) {
                                return; // this tile is too big and the rest of the tiles are even bigger
                            } else {
                                int top_gap = board.board_size - (row + last_tile_used);
                                int smallest_tile = tiles.almost_smallest(last_tile_used);
                                if (top_gap > 0) {
                                    if (top_gap == 1 || smallest_tile > top_gap) {
                                        // optc4++;
                                        // System.out.println("Optimization 4");
                                        continue; // skip this tile since it leaves too small a gap at the top
                                    }
                                }

                                int right_gap = board.board_size - (col + last_tile_used);
                                if (right_gap > 0) {
                                    if (right_gap == 1 || smallest_tile > right_gap) {
                                        // optc5++;
                                        // System.out.println("Optimization 5");
                                        continue; // skip this tile since it leaves too small a gap on the right
                                    }
                                }

                                tiles.tile_array[index].inuse = true;
                                board.addtile(row, col, last_tile_used);

                                if (tiles.unused()) {
                                    go_deep(board, tiles, row, col);
                                } else { // we used all the tiles so we found a solution
                                    board.solutions++;
                                    System.out.println("Solution #"+board.solutions+" is Configuration #"+cform.format(board.attempts)+" found in "+dform.format((Instant.now().toEpochMilli()-board.startTime)/(float)(1000*60))+" minutes");
                                    board.show(); 
                                }

                                board.deltile(row, col, last_tile_used);
                                tiles.tile_array[index].inuse = false;
                            } 
                        }
                    }
                    return; // we tried all the tiles so it's time to leave
                }
            }
        }
    }
}
