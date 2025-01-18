package com.ics.tetris;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Random;

public class Tetris extends Application {
  private static final int BOARD_WIDTH = 10;
  private static final int BOARD_HEIGHT = 20;
  private static final int TILE_SIZE = 30;

  private int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];
  private Shape currentShape;
  private int currentRow = 0;
  private int currentCol = BOARD_WIDTH / 2;
  private Group root;
  private Timeline timeline = new Timeline();

  private static final int[][][] SHAPES = {
      // I
      { { 1, 1, 1, 1 } },
      // O
      { { 2, 2 }, { 2, 2 } },
      // T
      { { 0, 3, 0 }, { 3, 3, 3 } },
      // S
      { { 0, 4, 4 }, { 4, 4, 0 } },
      // Z
      { { 5, 5, 0 }, { 0, 5, 5 } },
      // J
      { { 6, 0, 0 }, { 6, 6, 6 } },
      // L
      { { 0, 0, 7 }, { 7, 7, 7 } }
  };

  @Override
  public void start(Stage primaryStage) {
    root = new Group();
    Scene scene = new Scene(root, BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Tetris");
    primaryStage.show();

    newShape();
    drawBoard();

    scene.setOnKeyPressed(e -> {
      switch (e.getCode()) {
        case LEFT -> move(-1);
        case RIGHT -> move(1);
        case UP -> rotate();
        case DOWN -> drop();
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

  private void newShape() {
    Random r = new Random();
    int[][] shapeData = SHAPES[r.nextInt(SHAPES.length)];
    currentShape = new Shape(shapeData);
    currentRow = 0;
    currentCol = BOARD_WIDTH / 2 - currentShape.width() / 2;
    if (!validMove(currentRow, currentCol, currentShape)) {
      timeline.stop();
    }
  }

  private void drawBoard() {
    root.getChildren().clear();
    // Draw placed tiles
    for (int r = 0; r < BOARD_HEIGHT; r++) {
      for (int c = 0; c < BOARD_WIDTH; c++) {
        if (board[r][c] != 0) {
          drawTile(r, c, board[r][c]);
        }
      }
    }
    // Draw current piece
    for (int r = 0; r < currentShape.height(); r++) {
      for (int c = 0; c < currentShape.width(); c++) {
        int val = currentShape.data[r][c];
        if (val != 0) {
          drawTile(currentRow + r, currentCol + c, val);
        }
      }
    }
  }

  private Color tileColor(int val) {
    return switch (val) {
      case 1 -> Color.CYAN; // I
      case 2 -> Color.YELLOW; // O
      case 3 -> Color.PURPLE; // T
      case 4 -> Color.GREEN; // S
      case 5 -> Color.RED; // Z
      case 6 -> Color.BLUE; // J
      case 7 -> Color.ORANGE; // L
      default -> Color.GRAY;
    };
  }

  private void drawTile(int row, int col, int val) {
    Rectangle rect = new Rectangle(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    rect.setFill(tileColor(val));
    root.getChildren().add(rect);
  }

  private void drop() {
    if (validMove(currentRow + 1, currentCol, currentShape)) {
      currentRow++;
    } else {
      placeShape();
      clearLines();
      newShape();
    }
  }

  private void move(int delta) {
    if (validMove(currentRow, currentCol + delta, currentShape)) {
      currentCol += delta;
    }
  }

  private void rotate() {
    Shape rotated = currentShape.rotate();
    if (validMove(currentRow, currentCol, rotated)) {
      currentShape = rotated;
    }
  }

  private boolean validMove(int row, int col, Shape shape) {
    for (int r = 0; r < shape.height(); r++) {
      for (int c = 0; c < shape.width(); c++) {
        if (shape.data[r][c] != 0) {
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
        int val = currentShape.data[r][c];
        if (val != 0) {
          board[currentRow + r][currentCol + c] = val;
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

  class Shape {
    int[][] data;

    Shape(int[][] data) {
      this.data = data;
    }

    int width() {
      return data[0].length;
    }

    int height() {
      return data.length;
    }

    Shape rotate() {
      int h = height();
      int w = width();
      int[][] rotated = new int[w][h];
      for (int r = 0; r < h; r++) {
        for (int c = 0; c < w; c++) {
          rotated[c][h - 1 - r] = data[r][c];
        }
      }
      return new Shape(rotated);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}