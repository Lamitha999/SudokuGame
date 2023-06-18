package com.example.Sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SudokuSolver {
    public static final int SIZE = 9;
    private int[][] board;
    private Random random;

    public SudokuSolver() {
        this.board = new int[SIZE][SIZE];
        this.random = new Random();
    }

    public void generatePuzzle() {
        do {
            this.board = new int[SIZE][SIZE];
            fillDiagonalRegions();
        } while (!solveSudoku(this.board));
    }

    private void fillDiagonalRegions() {
        for (int i = 0; i < SIZE; i += 3) {
            fillRegion(i, i);
        }
    }

    private void fillRegion(int row, int col) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[row + i][col + j] = numbers.remove(0);
            }
        }
    }

    public boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    ArrayList<Integer> numbers = new ArrayList<>();
                    for (int i = 1; i <= SIZE; i++) {
                        numbers.add(i);
                    }
                    Collections.shuffle(numbers);

                    while (!numbers.isEmpty()) {
                        int number = numbers.remove(0);
                        if (isValid(board, row, col, number)) {
                            board[row][col] = number;
                            if (solveSudoku(board)) {
                                return true;
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        // Check the row and column
        for (int i = 0; i < SIZE; i++) {
            if (board[i][col] == num || board[row][i] == num) {
                return false;
            }
        }
        // Check the region
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[][] getBoard() {
        return this.board;
    }

}
