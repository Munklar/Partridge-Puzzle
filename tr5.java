//
// This program is derived from pn5.java (q.v.) but is fundamentally different. pn5 tries to fill
// a square with a collection of tiles composed of 1 1x1 tiles, 2 2x2 tiles, 3 3x3 tiles, and so forth. 
// tr5, on the other hand, uses a collection of tiles composed of N 1x1 tiles, N-1 2x2 tiles, N-2 3x3 
// tiles, ... and 1 NxN tile. The "tr" in "tr5" comes from "Trapridge" (see the README file) and the "5" 
// comes from the fact that it is derived from pn5.java.
//
// Needless to say, tr5 doesn't work for all values of N. The first half dozen values of N for which 
// a square tile can be filled with the corresponding tile set are:
//      N       Square Size
//      1           1
//      6           14
//      25          195
//      96          2716
//      361         37829
//      1350        526890
//      5041        7338631
//
// It finds all 60,568 solutions for N=6 in less than a minute. 
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
        for (int outer = BigN; outer > 0; outer--) {
            for (int inner = 1; inner < outer+1; inner++) {
                Tile oneTile = new Tile(BigN+1-outer, false);
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
        int Area = 0;
        double Side;
        
        for (int i = 1; i <= BigN; i++) 
            Area = Area + (BigN + 1 - i)*i*i;

        Side = Math.sqrt(Area);
        board_size = (int)Side;

        if (Side != board_size) {
            System.out.println("Total area of tiles isn't a perfect square");
            System.exit(1);
        }

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


public class tr5 {
    static DecimalFormat cform = new DecimalFormat("###,###,###,###,##0");
    static DecimalFormat dform = new DecimalFormat("###,##0.00");

    static int prints = 10;
    static double optc1=0;
    static double optc2=0;
    static double optc3=0;
    static double optc4=0;
    static double optc5=0;

    public static void main(String[] args) {
        int index, BigN, tile_count, board_size;
        TileStack tilesTop;
        BoardSquare boardTop;

        BigN = 6;
        tilesTop = new TileStack(BigN);
        boardTop = new BoardSquare(BigN);
        
        // tilesTop.show();

        System.out.println("Solution for N = "+cform.format(BigN));
        System.out.println();

        go_deep(boardTop, tilesTop, 0, 0);

        System.out.println("Tried "+cform.format(boardTop.attempts)+" configurations in "+dform.format((Instant.now().toEpochMilli()-boardTop.startTime)/(float)(1000*60))+ " minutes");

        System.out.println("Optimization 1: "+cform.format(optc1));
        System.out.println("Optimization 2: "+cform.format(optc2));
        System.out.println("Optimization 3: "+cform.format(optc3));
        System.out.println("Optimization 4: "+cform.format(optc4));
        System.out.println("Optimization 5: "+cform.format(optc5));
    }

    public static void go_deep(BoardSquare board, TileStack tiles, int start_row, int start_col) {
        board.attempts++;

	    if ((board.attempts % (1L << 33)) == 0)  // 31
            prints = 10;

        if (prints > 0) {
            System.out.println("Attempt #"+cform.format(board.attempts)+" after "+dform.format((Instant.now().toEpochMilli()-board.startTime)/(float)(1000*60))+" minutes");
            board.show();
            prints -=1;
        }

        boolean init_loop = false;

        for (int row = 0; row < board.board_size; row++) {
            if (!init_loop)
                row = start_row;

            for (int col = 0; col < board.board_size; col++) {
                if (!init_loop)
                    col = start_col;

                init_loop = true;

                if (board.matrix[row][col] == 0) { // found an empty board position
                    int last_tile_used = 0;
                    for (int index = 0; index < tiles.tile_count; index++) {

                        if (tiles.tile_array[index].inuse == false  && tiles.tile_array[index].size != last_tile_used) { // unused tile and a size we haven't already tried
                            last_tile_used = tiles.tile_array[index].size;

                            if (!board.canfit(row, col, last_tile_used)) {
                                return; // this tile is too big and the rest of the tiles are even bigger
                            } else {
                                int top_gap = board.board_size - (row + last_tile_used);
                                int smallest_tile = tiles.almost_smallest(last_tile_used);

                                if (top_gap > 0 && smallest_tile > top_gap) {
                                    optc4++;
                                    // System.out.println("Optimization 4");
                                    continue; // skip this tile since it leaves too small a gap at the top
                                }

                                int right_gap = board.board_size - (col + last_tile_used);
                                if (right_gap > 0 && smallest_tile > right_gap) {
                                    optc5++;
                                    // System.out.println("Optimization 5");
                                    continue; // skip this tile since it leaves too small a gap on the right
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
