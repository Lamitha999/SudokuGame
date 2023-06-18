package com.example.Sudoku;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.util.Random;

public class SudokuGame {
    private Alert alert;


    public static final int SIZE = 9;
    public static final int BOX_SIZE = 3;
    private int[][] board;
    private int[][] solvedPuzzle;
    public Random randoms = new Random();
    public MainApplication application;

    public SudokuGame(MainApplication application) {
        this.application = application;
        this.board = new int[SIZE][SIZE];
        this.solvedPuzzle = new int[SIZE][SIZE];
    }

    public void generatePuzzle() {
        SudokuSolver solver = new SudokuSolver();
        solver.generatePuzzle();
        this.board = solver.getBoard();
        for (int r = 0; r < SIZE; r++) {
            this.solvedPuzzle[r] = this.board[r].clone();
        }
    }

    public int[][] getSolvedPuzzle() {
        return this.solvedPuzzle;
    }

    public int[][] getUserPuzzle() {
        return this.board;
    }

    public void removeNumbers(int cellsToRemove) {
        Random random = new Random();
        while (cellsToRemove > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (this.board[row][col] != 0) {
                this.board[row][col] = 0;
                cellsToRemove--;
            }
        }
    }

    public void printPuzzle(int[][] puzzle) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                System.out.print(puzzle[r][c] + " ");
            }
            System.out.println();
        }
    }

    public boolean isValidMove(int row, int col, int num) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            application.transmit(-1);
            return false;
        }

        // Check row and column
        for (int i = 0; i < SIZE; i++) {
            if (this.board[row][i] == num || this.board[i][col] == num) {
                application.transmit(-1);
                return false;
            }
        }

        // Check 3x3 box
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (this.board[r][c] == num) {
                    application.transmit(-1);
                    return false;
                }
            }
        }
        return true;
    }

    public void fillCell(int row, int col, int num) {
        if (isValidMove(row, col, num)) {
            this.board[row][col] = num;
            application.transmit(1);
        } else {
            this.board[row][col] = 0;
        }
    }

    public boolean isBoardFilled() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isBoardValid() {
        // Check rows
        for (int r = 0; r < SIZE; r++) {
            if (!isSetValid(board[r])) {
                return false;
            }
        }

        // Check columns
        for (int c = 0; c < SIZE; c++) {
            int[] column = new int[SIZE];
            for (int r = 0; r < SIZE; r++) {
                column[r] = board[r][c];
            }
            if (!isSetValid(column)) {
                return false;
            }
        }

        // Check 3x3 boxes
        for (int boxRow = 0; boxRow < SIZE; boxRow += BOX_SIZE) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += BOX_SIZE) {
                int[] box = new int[SIZE];
                int index = 0;
                for (int r = boxRow; r < boxRow + BOX_SIZE; r++) {
                    for (int c = boxCol; c < boxCol + BOX_SIZE; c++) {
                        box[index++] = board[r][c];
                    }
                }
                if (!isSetValid(box)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isSetValid(int[] set) {
        boolean[] seen = new boolean[SIZE];
        for (int num : set) {
            if (num != 0) {
                if (seen[num - 1]) {
                    return false; // Duplicate number found
                }
                seen[num - 1] = true;
            }
        }
        return true;
    }

    public boolean isSolved() {
        if (isBoardFilled() && isBoardValid()) {
            if (alert == null) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sudoku Game");
                alert.setHeaderText("Congratulations!");
                alert.setContentText("You have successfully solved the Sudoku puzzle!");

                // Set the alert to close when the OK button is clicked
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getButtonTypes().clear();
                dialogPane.getButtonTypes().add(javafx.scene.control.ButtonType.OK);

                // Show the alert
                alert.showAndWait();
            }
            return true;
        }
        return false;
    }
}
