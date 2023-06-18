module com.example.Sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.Sudoku to javafx.fxml, com.google.gson;

    exports com.example.Sudoku;
}
