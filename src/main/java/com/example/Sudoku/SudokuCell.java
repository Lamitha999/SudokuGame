package com.example.Sudoku;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Sudoku cells
 */
public class SudokuCell extends Button {
    private SudokuGame game;
    private SudokuGrid sudokuGrid;
    private int row;
    private int col;
    private boolean filled;

    /**
     *
     * @param game has access to the game logic and functionality within SudokuCell. Allows Sudokucell to interact with the game
     * @param sudokuGrid put the cells together
     * @param row row of sudoku
     * @param col column of sudoku
     */

    public SudokuCell(SudokuGame game, SudokuGrid sudokuGrid, int row, int col) {
        this.game = game;
        this.sudokuGrid = sudokuGrid;
        this.row = row;
        this.col = col;
        this.filled = false;

        setPrefSize(60, 60);
        setAlignment(Pos.CENTER);
        setFocusTraversable(true);

        setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Set the font and size


        setStyle("-fx-background-color: #333333, #444444;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 5;" +
                "-fx-border-color: #C0C0C0;" + // Set the border color to silver
                "-fx-border-width: 1;" +
                "-fx-text-fill: white;"); // Set the text color to silver

        int initialCellValue = game.getUserPuzzle()[row][col];
        if (initialCellValue != 0) {
            setText(String.valueOf(initialCellValue));
            filled = true;
        }

        setOnKeyPressed(this::handleKeyPress);
        setOnMouseClicked(this::handleMouseClicked);
    }

    /**
     *
     * @param event retrives the key
     */

    private void handleKeyPress(KeyEvent event) {
        String input = event.getText();
        int number = 0;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return;
        }
        if (!filled && game.isValidMove(row, col, number)) {
            game.fillCell(row, col, number);
            setText(input);
            filled = true;
            setDisable(true); // Disable the cell after it is filled
        }

        requestFocusNextCell();
    }

    private void handleMouseClicked(MouseEvent event) {
        if (!filled) {
            sudokuGrid.setCurrentCell(this);
        }
    }

    private void requestFocusNextCell() {
        SudokuCell nextCell = sudokuGrid.findNextEditableCell(row, col);
        if ((nextCell != null && !sudokuGrid.getCurrentCell().isFilled()) || sudokuGrid.getCurrentCell().equals(0)) {
            nextCell.requestFocus();
        } else {
            sudokuGrid.requestFocusQuiz();
        }
    }

    public void update() {
        int value = game.getUserPuzzle()[row][col];
        if (value != 0) {
            setText(String.valueOf(value));
            filled = true;
            setDisable(true); // Disable the cell if it is filled
        } else {
            setText("");
            filled = false;
            setDisable(false); // Enable the cell if it is empty
        }
    }

    public boolean isFilled() {
        return filled;
    }
}
