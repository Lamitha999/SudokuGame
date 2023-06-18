package com.example.Sudoku;

/**
 * UI 1
 */
public enum Difficulty {
    EASY(35),
    NORMAL(45),
    HARD(55);

    private final int cellsToRemove;

    Difficulty(int cellsToRemove) {
        this.cellsToRemove = cellsToRemove;
    }

    public int getCellsToRemove() {
        return cellsToRemove;
    }
}
