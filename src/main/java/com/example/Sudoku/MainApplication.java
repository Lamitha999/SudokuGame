package com.example.Sudoku;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainApplication extends Application {
    private Stage primaryStage;
    private List<Question> questions;
    public int remainingChances = 3;
    public Label questionLabel = new Label();
    private VBox sudokuLayout;
    private int points = 0;
    public TextField answerTextField;
    public Button submitButton;
    private VBox quizLayout;
    private SudokuGame game;
    private SudokuGrid sudokuGrid;
    private int randQuestion = -1;
    private boolean allowGuessing = true;
    private Random random = new Random();

    @Override
    /**
     * Creates the GUI, where all the functions are being displayed
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showModeSelectionScreen();
        primaryStage.setTitle("Who will become the next millionaire");
        primaryStage.show();

    }

    /**
     * First UI to enter the game
     */
    private void showModeSelectionScreen() {

        Button quizModeButton = new Button("Play");
        quizModeButton.setStyle("-fx-background-color: linear-gradient(#01015e, #000000); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-font-family: 'Arial Black'; " +
                "-fx-font-size: 28px; " +
                "-fx-padding: 15px 40px; " +
                "-fx-border-color: silver; " +
                "-fx-border-width: 2px; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-radius: 10px;");

        quizModeButton.setOnAction(event -> showDifficultySelectionScreen(GameMode.PLAY));

        VBox layout = new VBox(40);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(#000000, #02025d);");

        Image image = new Image("Logo.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);

        layout.getChildren().addAll(imageView, quizModeButton);

        applyFadeInTransition(layout);

        primaryStage.setScene(new Scene(layout, 1100, 700));
    }

    /**
     *
     * @param startGame Second UI for GameMode
     */

    private void showDifficultySelectionScreen(GameMode startGame) {

        Button easyButton = new Button("Easy");
        easyButton.setStyle("-fx-background-color: linear-gradient(#01015e, #000000); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-font-family: 'Arial Black'; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-color: silver; " +
                "-fx-border-width: 2px; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-radius: 10px;");
        easyButton.setOnAction(event -> startGame(startGame, Difficulty.EASY));

        Button normalButton = new Button("Normal");
        normalButton.setStyle("-fx-background-color: linear-gradient(#01015e, #000000); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-font-family: 'Arial Black'; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-color: silver; " +
                "-fx-border-width: 2px; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-radius: 10px;");
        normalButton.setOnAction(event -> startGame(startGame, Difficulty.NORMAL));

        Button hardButton = new Button("Hard");
        hardButton.setStyle("-fx-background-color: linear-gradient(#01015e, #000000); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-font-family: 'Arial Black'; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-color: silver; " +
                "-fx-border-width: 2px; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-radius: 10px;");
        hardButton.setOnAction(event -> startGame(startGame, Difficulty.HARD));

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: linear-gradient(#01015e, #000000); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-font-family: 'Arial Black'; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-color: silver; " +
                "-fx-border-width: 2px; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-radius: 10px;");
        backButton.setOnAction(event -> showModeSelectionScreen());

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(#000000, #02025d);");

        Image image = new Image("Logo.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        layout.getChildren().addAll(imageView, easyButton, normalButton, hardButton);

        VBox.setMargin(backButton, new Insets(10, 0, 0, 0));
        layout.getChildren().add(backButton);
        applyFadeInTransition(layout);

        primaryStage.setScene(new Scene(layout, 1100, 700));
    }


    /**
     *
     * @param node Transition
     */
    private void applyFadeInTransition(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }



    /**
     *
     * @param startGame
     * @param difficulty Sudoku Game starts after selecting a difficulty
     */
    private void startGame(GameMode startGame, Difficulty difficulty) {
        if (startGame == GameMode.PLAY) {
            showQuizUI(difficulty);
        }
    }

    /**
     *
     * @param difficulty Third UI, depending on the difficulty different sudoku
     */


    private void showQuizUI(Difficulty difficulty) {
        sudokuLayout = createSudokuLayout(difficulty);
        questions = loadQuestionsFromJson();


        quizLayout = createQuizLayout();

        HBox rootLayout = new HBox(20);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.getChildren().addAll(sudokuLayout, quizLayout);

        // Set background image
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("background1.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        Background background = new Background(backgroundImage);
        rootLayout.setBackground(background);
        applyFadeInTransition(sudokuLayout);

        primaryStage.setScene(new Scene(rootLayout, 1100, 700));

        if (!questions.isEmpty()) {
            randQuestion = random.nextInt(questions.size());
            displayQuestion(questions.get(randQuestion), quizLayout);
        }
    }


/**
 * Questionbox
  */

    private VBox createQuizLayout() {
        VBox quizLayout = new VBox(20);
        quizLayout.setAlignment(Pos.CENTER);
        quizLayout.setPadding(new Insets(20));
        quizLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #00020e, #001469, #00041f); -fx-border-color: rgba(192, 192, 192, 0.5); -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");


        quizLayout.setMinHeight(300);
        quizLayout.setMaxHeight(300);
        quizLayout.setMinWidth(400);
        quizLayout.setMaxWidth(400);


        questionLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        questionLabel.setStyle("-fx-text-fill: white; -fx-padding: 10px; -fx-wrap-text: true;");

        answerTextField = new TextField();
        answerTextField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; ");

        HBox heartsBox = createHeartsBox();

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: linear-gradient(#bb6300, silver); -fx-text-fill: #050f2f; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-color: silver; -fx-border-width: 2px;");

        VBox.setMargin(questionLabel, new Insets(0, 0, 10, 0));

        quizLayout.getChildren().addAll(questionLabel, heartsBox, answerTextField, submitButton);
        return quizLayout;
    }



    private HBox getHeartsBox() {
        return (HBox) ((VBox) primaryStage.getScene().getRoot().getChildrenUnmodifiable().get(1)).getChildren().get(1);
    }


    private ImageView createHeartImageView() {
        ImageView heartImage = new ImageView(new Image(getClass().getResourceAsStream("/Money.png")));
        heartImage.setFitWidth(30);
        heartImage.setFitHeight(30);
        return heartImage;
    }

    public void updateHearts() {
        HBox heartsBox = getHeartsBox();
        heartsBox.getChildren().clear();
        for (int i = 0; i < remainingChances; i++) {
            heartsBox.getChildren().add(createHeartImageView());
        }
    }

    public void displayQuestion(Question question, VBox quizContentLayout) {
        questionLabel.setText(question.getQuestion());

        HBox heartsBox = createHeartsBox();

        answerTextField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: linear-gradient(#ab5700, #efad67, #ab5700); " +
                "-fx-text-fill: #07063a; " +
                "-fx-font-size: 16px; " +
                "-fx-border-color: silver; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-background-insets: 0, 1, 2; "+
                "-fx-font-family: 'Arial Black';");

        submitButton.setOnAction(event -> checkAnswer(answerTextField.getText(), questionLabel));
        answerTextField.setOnAction(event -> checkAnswer(answerTextField.getText(), questionLabel));
        VBox.setMargin(questionLabel, new Insets(0, 0, 10, 0));
        quizContentLayout.getChildren().clear();
        quizContentLayout.getChildren().addAll(questionLabel, heartsBox, answerTextField, submitButton);
        sudokuGrid.setDisable(true);
        answerTextField.requestFocus();
    }


    private HBox createHeartsBox() {
        HBox heartsBox = new HBox(5);
        heartsBox.setAlignment(Pos.CENTER);

        Image heartImage = new Image(getClass().getResourceAsStream("/Money.png"));
        double heartSize = 30; // Adjust the desired size of the hearts

        double aspectRatio = heartImage.getWidth() / heartImage.getHeight();

        for (int i = 0; i < remainingChances; i++) {
            ImageView heartImageView = new ImageView(heartImage);
            heartImageView.setPreserveRatio(true);
            heartImageView.setFitWidth(heartSize * aspectRatio);
            heartImageView.setFitHeight(heartSize);

            Pane heartPane = new Pane(heartImageView);
            heartPane.setPrefSize(heartSize * aspectRatio, heartSize);
            heartsBox.getChildren().add(heartPane);
        }

        return heartsBox;
    }

    /**
     *
     * @param difficulty
     * @return
     */

    private VBox createSudokuLayout(Difficulty difficulty) {
        game = new SudokuGame(this);
        game.generatePuzzle();
        game.removeNumbers(difficulty.getCellsToRemove());

        sudokuGrid = new SudokuGrid(game);
        sudokuGrid.setDisable(true);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: linear-gradient(#01015e, #000000); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-font-family: 'Arial Black'; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-color: silver; " +
                "-fx-border-width: 2px; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-radius: 10px;");backButton.setOnAction(event -> showDifficultySelectionScreen(GameMode.PLAY));

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: linear-gradient(#01015e, #000000); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-font-family: 'Arial Black'; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-color: silver; " +
                "-fx-border-width: 2px; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-radius: 10px;");exitButton.setOnAction(e -> Platform.exit());

        HBox buttonLayout = new HBox(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(backButton, exitButton);

        VBox layout = new VBox(sudokuGrid, buttonLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));
        return layout;
    }

    /**
     *
     * @param userAnswer input from user
     * @param questionLabel
     */


    public void checkAnswer(String userAnswer, Label questionLabel) {

        if (questions != null && !questions.isEmpty()) {
            Question currentQuestion = questions.get(randQuestion);

            if (isAnswerCorrect(currentQuestion, userAnswer)) {
                sudokuGrid.setDisable(false);
                answerTextField.setText("");
                questionLabel.setText("");
                answerTextField.setDisable(true);
                submitButton = new Button();
                submitButton.setDisable(true);

                if (remainingChances > 0) {
                    if (sudokuGrid != null) {
                        sudokuGrid.setDisable(false);
                        allowGuessing = false; // Disable guessing after a correct answer
                    }
                }


                // Focus on the Sudoku grid
                sudokuGrid.requestFocus();

                // Reset the hearts if lost during guessing
                if (remainingChances < 3) {
                    remainingChances = 3;
                    updateHearts();
                }
            } else {
                // Code for incorrect answer
                if (remainingChances > 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("And you wanna become a millionaire?");
                    alert.setHeaderText("Incorrect!");
                    alert.setContentText("The correct answer is: " + currentQuestion.getAnswer().get(0));
                    alert.showAndWait();
                    remainingChances--;
                    updateHearts();
                    getNextQuestion();
                } else {
                    // No more chances, show game over and close the application
                    Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
                    gameOverAlert.setHeaderText("Game Over");
                    gameOverAlert.setContentText("You have lost all your chances. Game Over!");
                    gameOverAlert.setOnHidden(event -> Platform.exit());
                    gameOverAlert.show();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error!");
            alert.setContentText("No questions found.");
            alert.showAndWait();
        }
    }

    /**
     *
     * @param point all alerts
     */

    public void transmit(int point){
        if(point==1){
            points = points + point;
            getNextQuestion();
        }

        if(point==-1){
            if(remainingChances>1){
                remainingChances = remainingChances -1;
                updateHearts();
            } else if(remainingChances<=1){
                Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
                gameOverAlert.setHeaderText("Game Over");
                gameOverAlert.setContentText("You have lost all your chances. Game Over!");
                gameOverAlert.setOnHidden(event -> Platform.exit());
                gameOverAlert.show();
            }
        }

    }

    public void getNextQuestion() {
        if (questions != null && !questions.isEmpty()) {
            int randomIndex = random.nextInt(questions.size());
            randQuestion = randomIndex;
            displayQuestion(questions.get(randQuestion), quizLayout);
        }
    }




    private boolean isAnswerCorrect(Question question, String userAnswer) {
        List<String> answers = question.getAnswer();
        userAnswer = answerTextField.getText().toString();
        if(answers.contains(userAnswer)){
            return true;
        } else return false;
    }

    private List<Question> loadQuestionsFromJson() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/questions.json"))) {
            Gson gson = new GsonBuilder().create();
            Type questionListType = new TypeToken<ArrayList<Question>>() {}.getType();
            return gson.fromJson(reader, questionListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        launch(args);


    }
}
