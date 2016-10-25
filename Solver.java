import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by zhanlgu on 16/10/2.
 */
public class Solver {
    private Move lastMove;

    private class Move implements Comparable<Move> {
        private Move previousM = null;
        private Board currentB;
        private int countMoves = 0;

        public Move(Board board) {
            currentB = board;
        }

        public Move(Move previous, Board current) {
            previousM = previous;
            currentB = current;
            countMoves = previous.countMoves + 1;
        }

        public int compareTo(Move that) {
            return this.currentB.manhattan() - that.currentB.manhattan()
                    + this.countMoves - that.countMoves;
        }
    }

    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.NullPointerException();
        MinPQ<Move> moves = new MinPQ<Move>();
        MinPQ<Move> twinMoves = new MinPQ<Move>();
        moves.insert(new Move(initial));
        twinMoves.insert(new Move(initial.twin()));

        while (true) {
            lastMove = chooseMove(moves);
            Move twinLastMove = chooseMove(twinMoves);
            if (lastMove != null || twinLastMove != null)
                return;
        }
    }

    private Move chooseMove(MinPQ<Move> moves) {
        if (moves.isEmpty()) return null;
        Move bestMove = moves.delMin();
        if (bestMove.currentB.isGoal()) return bestMove;
        for (Board neighbor : bestMove.currentB.neighbors()) {
            if (bestMove.previousM == null || !neighbor.equals(bestMove.previousM.currentB)) {
                moves.insert(new Move(bestMove, neighbor));
            }
        }
        return null;
    }

    public boolean isSolvable() {
        return lastMove != null;
    }

    public int moves() {
        return isSolvable() ? lastMove.countMoves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> moves = new Stack<Board>();
        Move tmpLastMove = lastMove;
        while (lastMove != null) {
            moves.push(lastMove.currentB);
            lastMove = lastMove.previousM;
        }
        lastMove = tmpLastMove;
        return moves;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}