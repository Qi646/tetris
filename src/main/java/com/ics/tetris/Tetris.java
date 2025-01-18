package com.ics.tetris;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tetris extends Application {
  private static final int BOARD_WIDTH = 10;
  private static final int BOARD_HEIGHT = 22;
  private static final int TILE_SIZE = 30;

  private int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];
  private Shape currentShape;
  private int currentRow = 0;
  private int currentCol = BOARD_WIDTH / 2;
  private Group root;
  private Timeline timeline = new Timeline();
  private List<Integer> shapeBag = new ArrayList<>();
  private int bagIndex = 0;
  private int ghostRow;
  private Scene gameScene;
  private Stage primaryStageRef;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStageRef = primaryStage;
    root = new Group();
    gameScene = new Scene(root, BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
    primaryStageRef.setScene(gameScene);
    primaryStageRef.setTitle("Tetris");
    primaryStageRef.show();

    setupGrid();
    newShape();
    drawBoard();

    gameScene.setOnKeyPressed(e -> {
      switch (e.getCode()) {
        case LEFT -> move(-1);
        case RIGHT -> move(1);
        case Z -> rotateCounterClockwise();
        case X -> rotate();
        case DOWN -> drop();
        case SPACE -> hardDrop();
        default -> {
        }
      }
      drawBoard();
    });

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), e -> {
      drop();
      drawBoard();
    }));
    timeline.play();
  }

  private void setupGrid() {
    for (int r = 0; r < BOARD_HEIGHT; r++) {
      Line horizontalLine = new Line(0, r * TILE_SIZE, BOARD_WIDTH * TILE_SIZE, r * TILE_SIZE);
      if (r == 2) {
        horizontalLine.setStroke(Color.RED);
      } else {
        horizontalLine.setStroke(Color.LIGHTGRAY);
      }
      root.getChildren().add(horizontalLine);
    }
    for (int c = 0; c < BOARD_WIDTH; c++) {
      Line verticalLine = new Line(c * TILE_SIZE, 0, c * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
      verticalLine.setStroke(Color.LIGHTGRAY);
      root.getChildren().add(verticalLine);
    }
  }

  private void loseGame() {
    timeline.stop();
    Label loseLabel = new Label("Thanks for playing!");
    loseLabel.setStyle("-fx-font-size: 22; -fx-text-fill: blue;");
    Button replayButton = new Button("Replay");
    replayButton.setOnAction(e -> restartGame());

    VBox losePane = new VBox(20, loseLabel, replayButton);
    losePane.setAlignment(Pos.CENTER);
    losePane.setPadding(new Insets(20));
    losePane.setStyle("-fx-background-color: white;");

    Scene loseScene = new Scene(losePane, BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
    primaryStageRef.setScene(loseScene);
  }

  private void restartGame() {
    board = new int[BOARD_HEIGHT][BOARD_WIDTH];
    currentRow = 0;
    currentCol = BOARD_WIDTH / 2;
    shapeBag.clear();
    fillBag();
    newShape();
    timeline.play();
    drawBoard();
    primaryStageRef.setScene(gameScene);
  }

  private void fillBag() {
    shapeBag = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6));
    Collections.shuffle(shapeBag);
    bagIndex = 0;
  }

  private void newShape() {
    if (shapeBag.isEmpty() || bagIndex >= shapeBag.size()) {
      fillBag();
    }
    int shapeId = shapeBag.get(bagIndex++);
    int[][][] shapeRotations = SHAPES_ROTATIONS[shapeId];
    currentShape = new Shape(shapeRotations);
    currentRow = 0;
    if (shapeId == 0 || shapeId == 1) {
      currentCol = BOARD_WIDTH / 2 - currentShape.width() / 2;
    } else {
      currentCol = BOARD_WIDTH / 2 - currentShape.width() / 2 - 1;
    }
    if (!validMove(currentRow, currentCol, currentShape.getCurrentData())) {
      timeline.stop();
    }
  }

  private void hardDrop() {
    while (validMove(currentRow + 1, currentCol, currentShape.getCurrentData())) {
      currentRow++;
    }
    placeShape();
    clearLines();
    newShape();
  }

  private void calculateGhostPiece() {
    ghostRow = currentRow;
    while (validMove(ghostRow + 1, currentCol, currentShape.getCurrentData())) {
      ghostRow++;
    }
  }

  private void drawGhostTile(int row, int col, int val) {
    Rectangle rect = new Rectangle(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    rect.setFill(tileColor(val));
    rect.setOpacity(0.3);
    root.getChildren().add(rect);
  }

  private void drawBoard() {
    root.getChildren().clear();
    for (int r = 0; r < BOARD_HEIGHT; r++) {
      for (int c = 0; c < BOARD_WIDTH; c++) {
        if (board[r][c] != 0) {
          drawTile(r, c, board[r][c]);
        }
      }
    }

    calculateGhostPiece();
    for (int r = 0; r < currentShape.height(); r++) {
      for (int c = 0; c < currentShape.width(); c++) {
        int val = currentShape.getCurrentData()[r][c];
        if (val != 0) {
          drawGhostTile(ghostRow + r, currentCol + c, val);
        }
      }
    }

    for (int r = 0; r < currentShape.height(); r++) {
      for (int c = 0; c < currentShape.width(); c++) {
        int val = currentShape.getCurrentData()[r][c];
        if (val != 0) {
          drawTile(currentRow + r, currentCol + c, val);
        }
      }
    }

    for (int r = 0; r < BOARD_HEIGHT; r++) {
      Line horizontalLine = new Line(0, r * TILE_SIZE, BOARD_WIDTH * TILE_SIZE, r * TILE_SIZE);
      if (r == 2) {
        horizontalLine.setStroke(Color.RED);
      } else {
        horizontalLine.setStroke(Color.LIGHTGRAY);
      }
      root.getChildren().add(horizontalLine);
    }
    for (int c = 0; c < BOARD_WIDTH; c++) {
      Line verticalLine = new Line(c * TILE_SIZE, 0, c * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
      verticalLine.setStroke(Color.LIGHTGRAY);
      root.getChildren().add(verticalLine);
    }
  }

  private Color tileColor(int val) {
    return switch (val) {
      case 1 -> Color.CYAN;
      case 2 -> Color.YELLOW;
      case 3 -> Color.PURPLE;
      case 4 -> Color.GREEN;
      case 5 -> Color.RED;
      case 6 -> Color.BLUE;
      case 7 -> Color.ORANGE;
      default -> Color.GRAY;
    };
  }

  private void drawTile(int row, int col, int val) {
    Rectangle rect = new Rectangle(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    rect.setFill(tileColor(val));
    root.getChildren().add(rect);
  }

  private void drop() {
    if (validMove(currentRow + 1, currentCol, currentShape.getCurrentData())) {
      currentRow++;
    } else {
      placeShape();
      clearLines();
      newShape();
    }
  }

  private void move(int delta) {
    if (validMove(currentRow, currentCol + delta, currentShape.getCurrentData())) {
      currentCol += delta;
    }
  }

  private void rotate() {
    currentShape.rotate(true);
    if (validMove(currentRow, currentCol, currentShape.getCurrentData())) {
    } else {
      currentShape.rotate(false);
    }
  }

  private void rotateCounterClockwise() {
    currentShape.rotate(false);
    if (validMove(currentRow, currentCol, currentShape.getCurrentData())) {
    } else {
      currentShape.rotate(true);
    }
  }

  private boolean validMove(int row, int col, int[][] shapeData) {
    for (int r = 0; r < shapeData.length; r++) {
      for (int c = 0; c < shapeData[0].length; c++) {
        if (shapeData[r][c] != 0) {
          int newR = row + r, newC = col + c;
          if (newC < 0 || newC >= BOARD_WIDTH ||
              newR < 0 || newR >= BOARD_HEIGHT ||
              board[newR][newC] != 0) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private void placeShape() {
    for (int r = 0; r < currentShape.height(); r++) {
      for (int c = 0; c < currentShape.width(); c++) {
        int val = currentShape.getCurrentData()[r][c];
        if (val != 0) {
          board[currentRow + r][currentCol + c] = val;
          if (currentRow + r < 2) {
            loseGame();
            return;
          }
        }
      }
    }
  }

  private void clearLines() {
    for (int r = BOARD_HEIGHT - 1; r >= 0; r--) {
      boolean full = true;
      for (int c = 0; c < BOARD_WIDTH; c++) {
        if (board[r][c] == 0) {
          full = false;
          break;
        }
      }
      if (full) {
        for (int rr = r; rr > 0; rr--) {
          board[rr] = board[rr - 1].clone();
        }
        board[0] = new int[BOARD_WIDTH];
        r++;
      }
    }
  }

  private static final int[][][][] SHAPES_ROTATIONS = {
      {
          { { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
          { { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 } },
          { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 } },
          { { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 } }
      },
      {
          { { 2, 2 }, { 2, 2 } }
      },
      {
          { { 0, 3, 0 }, { 3, 3, 3 }, { 0, 0, 0 } },
          { { 0, 3, 0 }, { 0, 3, 3 }, { 0, 3, 0 } },
          { { 0, 0, 0 }, { 3, 3, 3 }, { 0, 3, 0 } },
          { { 0, 3, 0 }, { 3, 3, 0 }, { 0, 3, 0 } }
      },
      {
          { { 0, 4, 4 }, { 4, 4, 0 }, { 0, 0, 0 } },
          { { 0, 4, 0 }, { 0, 4, 4 }, { 0, 0, 4 } },
          { { 0, 0, 0 }, { 0, 4, 4 }, { 4, 4, 0 } },
          { { 4, 0, 0 }, { 4, 4, 0 }, { 0, 4, 0 } }
      },
      {
          { { 5, 5, 0 }, { 0, 5, 5 }, { 0, 0, 0 } },
          { { 0, 0, 5 }, { 0, 5, 5 }, { 0, 5, 0 } },
          { { 0, 0, 0 }, { 5, 5, 0 }, { 0, 5, 5 } },
          { { 0, 5, 0 }, { 5, 5, 0 }, { 5, 0, 0 } }
      },
      {
          { { 6, 0, 0 }, { 6, 6, 6 }, { 0, 0, 0 } },
          { { 0, 6, 6 }, { 0, 6, 0 }, { 0, 6, 0 } },
          { { 0, 0, 0 }, { 6, 6, 6 }, { 0, 0, 6 } },
          { { 0, 6, 0 }, { 0, 6, 0 }, { 6, 6, 0 } }
      },
      {
          { { 0, 0, 7 }, { 7, 7, 7 }, { 0, 0, 0 } },
          { { 0, 7, 0 }, { 0, 7, 0 }, { 0, 7, 7 } },
          { { 0, 0, 0 }, { 7, 7, 7 }, { 7, 0, 0 } },
          { { 7, 7, 0 }, { 0, 7, 0 }, { 0, 7, 0 } }
      }
  };

  class Shape {
    int[][][] rotations;
    int currentRotation;

    Shape(int[][][] rotations) {
      this.rotations = rotations;
      this.currentRotation = 0;
    }

    int[][] getCurrentData() {
      return rotations[currentRotation];
    }

    void rotate(boolean clockwise) {
      if (clockwise) {
        currentRotation = (currentRotation + 1) % rotations.length;
      } else {
        currentRotation = (currentRotation + rotations.length - 1) % rotations.length;
      }
    }

    int width() {
      return getCurrentData()[0].length;
    }

    int height() {
      return getCurrentData().length;
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}