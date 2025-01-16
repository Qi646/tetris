package com.ics.tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {
  private final GraphicsContext gc;
  private final Grid grid;

  public Renderer(GraphicsContext gc, Grid grid) {
    this.gc = gc;
    this.grid = grid;
  }

  public void render(Tetromino currentTetromino, Tetromino ghostPiece) {
    gc.clearRect(0, 0, Constants.BOARD_WIDTH * Constants.TILE_SIZE, Constants.BOARD_HEIGHT * Constants.TILE_SIZE);

    drawGrid();
    renderTetromino(ghostPiece, Color.GRAY);
    renderTetromino(currentTetromino);
    renderGrid();
  }

  private void drawGrid() {
    gc.setStroke(Color.LIGHTGREY);
    for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
      for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
        gc.strokeRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE,
            Constants.TILE_SIZE);
      }
    }
  }

  private void renderGrid() {
    int[][] gridArray = grid.getGrid();
    for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
      for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
        if (gridArray[y][x] != 0) {
          colorCell(x, y, getColorForTetromino(gridArray[y][x] - 1));
        }
      }
    }
  }

  private void renderTetromino(Tetromino tetromino) {
    renderTetromino(tetromino, getColorForTetromino(tetromino.getType()));
  }

  private void renderTetromino(Tetromino tetromino, Color color) {
    int[][] shape = tetromino.getShape();
    int tetrominoX = tetromino.getX();
    int tetrominoY = tetromino.getY();

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] != 0) {
          int gridX = tetrominoX + col;
          int gridY = tetrominoY + row;
          colorCell(gridX, gridY, color);
        }
      }
    }
  }

  private void colorCell(int x, int y, Color color) {
    gc.setFill(color);
    gc.fillRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
  }

  private Color getColorForTetromino(int type) {
    switch (type) {
      case 0:
        return Color.CYAN;
      case 1:
        return Color.YELLOW;
      case 2:
        return Color.PURPLE;
      case 3:
        return Color.GREEN;
      case 4:
        return Color.RED;
      case 5:
        return Color.BLUE;
      case 6:
        return Color.ORANGE;
      default:
        throw new IllegalArgumentException("Invalid type: " + type);
    }
  }
}