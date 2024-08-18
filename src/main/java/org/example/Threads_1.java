package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.




public class Threads_1 {
    public static void main(String[] args) {
    }


import java.util.ArrayList;
import java.util.List;

public class NQueensThreaded {
    private int n;
    private List<int[]> solutions = new ArrayList<>();

    public NQueensThreaded(int n) {
        this.n = n;
    }

    public void solve() {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            final int col = i;
            Thread t = new Thread(() -> solveNQueens(new int[0], col));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printSolutions();
    }

    private void solveNQueens(int[] prefix, int initialCol) {
        int row = prefix.length;
        int[] newPrefix = new int[row + 1];
        System.arraycopy(prefix, 0, newPrefix, 0, row);
        newPrefix[row] = initialCol;

        if (row + 1 == n) {
            synchronized (this) {
                solutions.add(newPrefix);
            }
            return;
        }

        for (int col = 0; col < n; col++) {
            if (isSafe(newPrefix, col)) {
                solveNQueens(newPrefix, col);
            }
        }
    }

    private boolean isSafe(int[] board, int col) {
        int row = board.length;
        for (int i = 0; i < row; i++) {
            if (board[i] == col || Math.abs(board[i] - col) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }

    private void printSolutions() {
        for (int[] solution : solutions) {
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    if (solution[row] == col) {
                        System.out.print("Q ");
                    } else {
                        System.out.print(". ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int n = 8;
        NQueensThreaded nQueens = new NQueensThreaded(n);
        nQueens.solve();
    }
}

