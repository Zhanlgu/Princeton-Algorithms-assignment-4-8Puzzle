import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by zhanlgu on 16/10/2.
 */
public class Board {
    private final int[][] blocks;
    private final int n;

    public Board(int[][] blocks) {
        n = blocks.length;
        this.blocks = new int[n][];
        for (int i = 0; i < n; i++)
            this.blocks[i] = Arrays.copyOf(blocks[i], n);
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int counterNotInSpace = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n && i + j < 2 * (n - 1); j++)
                if (blocks[i][j] != i * n + j + 1)
                    counterNotInSpace++;
        return counterNotInSpace;
    }

    public int manhattan() {
        int sumDistance = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != 0) {
                    sumDistance += (Math.abs(j - (blocks[i][j] - 1) % n) +
                            Math.abs(i - (blocks[i][j] - 1) / n));
                }
        return sumDistance;
    }

    public boolean isGoal() {
        return this.hamming() == 0;
    }

    public Board twin() {
        int[][] twinBoard;
//        for (int i = 0; i < n; i++)
//            for (int j = 0; j < n; j++)
//                twinBoard[i][j] = blocks[i][j];
        if (blocks[0][0] != 0 && blocks[0][1] != 0)
            twinBoard = swap(0, 0, 0, 1);
        else
            twinBoard = swap(1, 0, 1, 1);
        return new Board(twinBoard);
    }

    private int[][] swap(int x1, int y1, int x2, int y2) {
        int[][] copyBlocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                copyBlocks[i][j] = blocks[i][j];
        int swap = copyBlocks[x1][y1];
        copyBlocks[x1][y1] = copyBlocks[x2][y2];
        copyBlocks[x2][y2] = swap;
        return copyBlocks;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        int dim = this.dimension();
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                if (this.blocks[i][j] != that.blocks[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        int[] location = spaceLocation();
        int spaceX = location[0];
        int spaceY = location[1];

        if (spaceX > 0) neighbors.add(new Board(swap(spaceX, spaceY, spaceX - 1, spaceY)));
        if (spaceX < n - 1) neighbors.add(new Board(swap(spaceX, spaceY, spaceX + 1, spaceY)));
        if (spaceY > 0) neighbors.add(new Board(swap(spaceX, spaceY, spaceX, spaceY - 1)));
        if (spaceY < n - 1) neighbors.add(new Board(swap(spaceX, spaceY, spaceX, spaceY + 1)));

        return neighbors;

    }

    private int[] spaceLocation() {
        int[] location = new int[2];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] == 0) {
                    location[0] = i;
                    location[1] = j;
                }
        return location;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                s.append(String.format("%2d ", blocks[i][j]));
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
        StdOut.println(initial.hamming());
    }
}