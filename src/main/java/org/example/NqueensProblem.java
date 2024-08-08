package org.example;

import java.util.*;

public abstract class NqueensProblem implements java.lang.Runnable {
    List<List<String>> res;

    public List<List<String>> nQueensSolutions(int n) {
        res = Collections.synchronizedList(new ArrayList<>());//Stores all possible solutions
        List<Thread> threads = new ArrayList<>(n);//Creates 'n' threads, one for each possible solution of the problem
        for (int i = 0; i < n; i++) {
            threads.add(new QueensUsingMultiThread(i, n)); //Uses parallelism to run the threads
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) { //Stops until all the threads are executed
            try {
                t.join();
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
        return res;
    }


    //This represents an instance of every thread that is going to try to solve the problem
    class QueensUsingMultiThread extends Thread {
        Set<Integer> vertical;
        Set<Integer> positiveDiag;//Row + col
        Set<Integer> negativeDiag;//Row - col
        List<Integer> solving;
        int col;
        int n;

        QueensUsingMultiThread(int col, int n) {
            vertical = new HashSet<>();
            positiveDiag = new HashSet<>();
            negativeDiag = new HashSet<>();
            solving = new LinkedList<>();
            //solving = new ArrayList<>();
            this.col = col;
            this.n = n;
        }

        //Executed when thread is initialized
        public void run() {
            //Places queen on the first column
            //Call the method to continue searching
            try {
                vertical.add(col);
                negativeDiag.add(col);
                positiveDiag.add(col);
                solving.add(col);
                nQueensSolutions(n, vertical, positiveDiag, negativeDiag, 1, res, solving);

            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }


    private void nQueensSolutions(int n, Set<Integer> vertical, Set<Integer> positiveDiag, Set<Integer> negativeDiag,
                                  int row, List<List<String>> res, List<Integer> solving) {
        if (row == n) {
            res.add(stringifySolution(solving, n));
        } else { //Checks if column or diagonal is occupied
            for (int col = 0; col < n; col++) {
                if (vertical.contains(col) || positiveDiag.contains(col + row) || negativeDiag.contains(col - row)) {
                    continue;
                }
                //Backtracking
                //Adds first
                vertical.add(col);
                negativeDiag.add(col - row);
                positiveDiag.add(col + row);
                solving.add(col);
                nQueensSolutions(n, vertical, positiveDiag, negativeDiag, row + 1, res, solving);//Calls method to remove
                vertical.remove(col);
                negativeDiag.remove(col - row);
                positiveDiag.remove(col + row);

                solving.remove(solving.size() - 1);
            }
        }
    }

    //Turns the solution into a string to form a chessBoard, placing the queens where they are supposed to be.
    private List<String> stringifySolution(List<Integer> solving, int n) {
        List<String> res = new ArrayList<>();
        for (int solvingColumn : solving) {
            StringBuilder stringify = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if (i == solvingColumn) {
                    stringify.append('Q');
                } else {
                    stringify.append('.');
                }

            }
            res.add(stringify.toString());
        }
        return res;
    }

    public static void main(String[] args) {
        NqueensProblem problem = new NqueensProblemImpl();
        int n = 4;
        List<List<String>> solutions = problem.nQueensSolutions(n);
        for (List<String> solution : solutions) {
            for (String row : solution) {
                System.out.println(row);
            }
            System.out.println();
        }
    }
    static class NqueensProblemImpl extends NqueensProblem {
        @Override
        public void run() {
            // This method can be used to run the algorithm in a separate thread if needed
        }
    }
}