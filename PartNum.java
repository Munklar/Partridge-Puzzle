// For Java 8
//
// N=3: 40 configurations
// N=4: 454 configurations
// N=5: 13,098 configurations (0.56 seconds Python v2.7.5 vs. 0.03 seconds Java 8))
// N=6: 1,214,975 configurations (89.5 seconds Python v2.7.5 vs. 0.61 seconds Java 8)
// N=7: 110,483,315 configurations (3.60 hours Python v2.7.5 vs. 55.95 seconds Java 8)
// N=8: 61,636,000,054 configurations (746.36 minutes Java 8)
//
// This program searches for ways of perfectly filling a square that is ( N * (N+1)/2 ) units on a side with a 
// collection of tiles such that you have 1 tile that is 1 unit on a side, 2 tiles that are 2 units on a side, 
// three that are 3x3, four that are 4x4, and so on up to N tiles that are NxN. Each configuration of tiles
// that uses all the tiles to exactly fill the board is considered a solution. The smallest value of N for
// which a solution exists is called the "Partridge Number" and it turns out that for a square board, the
// Partridge Number is 8. This program proves that.
//
// Here some links about this kind of problem:
//
// http://www2.stetson.edu/~efriedma/papers/partridge.pdf
// http://www2.stetson.edu/~efriedma/mathmagic/0802.html
// http://www.mathpuzzle.com/partridge.html
//
// Algorithm Description:
//
// First, initialize the board by creating a matrix that represents the board. The width and height of the
// matrix equals N*(N+1)/2. Each position in the matrix contains a value representing the size of the tile
// that is located there. If the value is zero it means the location is not occupied.
//
// Second, initialize the array of tiles as follows. We will have a total of N*(N+1)/2 tiles and each tile
// has two properties, its size and a flag indiating whether it is being used (i.e., has been placed on the
// board.
//
// Define a recursive function which when called, performs the following steps on the board and the tile 
// list // in whatever state they were in when the function was called.
// 1. Traverse the board, one position at a time starting at [R][C] = [0][0] 
// 2. If the location is vacant, traverse the tile list starting with the first tile until we find a tile
// that is not being used and of a size that we haven't previously tried.
// 3. Check if the file will fit on the board if placed at the postion under consideration. 
// 3.a. If it would fit then add it to the board and mark it as used on the tile list. Check if we have 
// used up all the tiles.
// 3.a.i. If there are no unused tiles then that means we have found a solution. Display it.
// 3.a.ii. If there are still unused tiles then call this function (i.e., recurse).
// 3.a.iii. Back out of the last move so that we can continue trying more tiles. That is, remove the tile 
// from the board and mark the tile as unused on the tile list.
// 3.b. Continue traversing the tile list. (BTW: There's an optimization opportunity here: since the tile
// list is ordered by increasing tile size, once you hit a tile that doesn't fit, you can bail on the loop
// that is traversing the list).
// 4. Once we tried all the available tiles, return to our caller.
// 5. Contiue traversing the board.
//
// Back-of-the-envelope complexity analysis: The three loops (row, column, and tile traversal) are all
// O(N^2) so without the recursion, that would give us O(N^6) complexity. With recursion I think that gives
// a complexity of a O(N^6^)*(N^6)) or O(N^12). 
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
    static String CSI_black=CSI_BEG+"0;30;40"+CSI_END;
    static String CSI_red=CSI_BEG+"0;30;41"+CSI_END;
    static String CSI_green=CSI_BEG+"0;30;42"+CSI_END;
    static String CSI_blue=CSI_BEG+"0;30;44"+CSI_END;
    static String CSI_yellow=CSI_BEG+"0;30;43"+CSI_END;
    static String CSI_purple=CSI_BEG+"0;30;45"+CSI_END;
    static String CSI_teal=CSI_BEG+"0;30;46"+CSI_END;
    static String CSI_grey=CSI_BEG+"0;30;47"+CSI_END;
    static String[] CSI_COLORS={CSI_black, CSI_red, CSI_green, CSI_blue, CSI_yellow, CSI_purple, CSI_teal, CSI_grey};

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
                // If you don't want color, use this: System.out.print(matrix[row][col]+" ");
                System.out.print(CSI_COLORS[matrix[row][col]-1]+matrix[row][col]+" "+CSI_OFF);
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


public class PartNum {
    static DecimalFormat cform = new DecimalFormat("###,###,###,##0");
    static DecimalFormat dform = new DecimalFormat("###,##0.00");

    public static void main(String[] args) {
        int index, BigN, tile_count, board_size;
        TileStack tilesTop;
        BoardSquare boardTop;
        String answer;
        
        Console c = System.console();
        if (c == null) {
            System.err.println("Weird. No console.");
            System.exit(1);
        }

        answer = c.readLine("What is N? ");
        BigN = Integer.parseInt(answer);
        tilesTop = new TileStack(BigN);
        boardTop = new BoardSquare(BigN);

        go_deep(boardTop, tilesTop);

        System.out.println("Tried "+cform.format(boardTop.attempts)+" configurations in "+dform.format((Instant.now().toEpochMilli()-boardTop.startTime)/(float)(1000*60))+ " minutes");
    }

    public static void go_deep(BoardSquare board, TileStack tiles) {
        board.attempts += 1;

        for (int row = 0; row < board.board_size; row++) {
            for (int col = 0; col < board.board_size; col++) {
                if (board.matrix[row][col] == 0) { // found an empty board position
                    int last_tile_used = 0;
                    for (int index = 0; index < tiles.tile_count; index++) {
                        if (tiles.tile_array[index].inuse == false  && tiles.tile_array[index].size != last_tile_used) { // unused tile and a size we haven't already tried
                            last_tile_used = tiles.tile_array[index].size;
                            if (board.canfit(row, col, last_tile_used)) {
                                tiles.tile_array[index].inuse = true;
                                board.addtile(row, col, last_tile_used);

                                if (tiles.unused()) {
                                    go_deep(board, tiles);
                                } else { // we used all the tiles so we found a solution
                                    board.solutions += 1;
                                    System.out.println("Solution #"+board.solutions+" is Configuration #"+cform.format(board.attempts)+" found in "+dform.format((Instant.now().toEpochMilli()-board.startTime)/(float)(1000*60))+" minutes");
                                    board.show(); // don't return; keep looking for more solutions
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
