package com.example.Sudoku;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class SudokuGrid extends GridPane {
    private SudokuGame game;
    private SudokuCell[][] cells;
    private SudokuCell currentCell;

    /**
     *
     * @param game Grid for Sudoku
     */
    public SudokuGrid(SudokuGame game) {
        this.game = game;
        this.cells = new SudokuCell[SudokuGame.SIZE][SudokuGame.SIZE];
        this.currentCell = null;

        // Create 3x3 boxes
        GridPane[][] boxes = new GridPane[SudokuGame.BOX_SIZE][SudokuGame.BOX_SIZE];
        for (int row = 0; row < SudokuGame.BOX_SIZE; row++) {
            for (int col = 0; col < SudokuGame.BOX_SIZE; col++) {
                GridPane box = new GridPane();
                box.getStyleClass().add("sudoku-box");
                boxes[row][col] = box;
            }
        }

        // Add cells to the corresponding boxes
        for (int boxRow = 0; boxRow < SudokuGame.BOX_SIZE; boxRow++) {
            for (int boxCol = 0; boxCol < SudokuGame.BOX_SIZE; boxCol++) {
                for (int cellRow = 0; cellRow < SudokuGame.BOX_SIZE; cellRow++) {
                    for (int cellCol = 0; cellCol < SudokuGame.BOX_SIZE; cellCol++) {
                        int row = boxRow * SudokuGame.BOX_SIZE + cellRow;
                        int col = boxCol * SudokuGame.BOX_SIZE + cellCol;

                        SudokuCell cell = new SudokuCell(game, this, row, col);
                        cell.update();
                        cells[row][col] = cell;

                        GridPane box = boxes[boxRow][boxCol];
                        box.add(cell, cellCol, cellRow);
                    }
                }
            }
        }

        /**
         * distance between the 3x3 boxes
         */
        for (int row = 0; row < SudokuGame.BOX_SIZE; row++) {
            for (int col = 0; col < SudokuGame.BOX_SIZE; col++) {
                GridPane box = boxes[row][col];
                this.add(box, col, row);
                setMargin(box, new Insets(1.5));
            }
        }

        // Add box constraints to create 3x3 boxes
        for (int i = 0; i < SudokuGame.BOX_SIZE; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / SudokuGame.BOX_SIZE);
            getColumnConstraints().add(columnConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / SudokuGame.BOX_SIZE);
            getRowConstraints().add(rowConstraints);
        }
    }

    public void setCurrentCell(SudokuCell cell) {
        this.currentCell = cell;
    }

    public SudokuCell getCurrentCell() {
        return currentCell;
    }

    public SudokuCell findNextEditableCell(int currentRow, int currentCol) {
        int row = currentRow;
        int col = currentCol;
        while (row < SudokuGame.SIZE - 1 || col < SudokuGame.SIZE - 1) {
            if (col < SudokuGame.SIZE - 1) {
                col++;
            } else {
                col = 0;
                row++;
            }
            SudokuCell nextCell = cells[row][col];
            if (!nextCell.isFilled()) {
                return nextCell;
            }
        }
        return null;
    }

    public void requestFocusQuiz() {
        getCurrentCell().requestFocus();
    }
}
