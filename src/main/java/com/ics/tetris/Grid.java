package com.ics.tetris;

import java.util.Arrays;

public class Grid {
  private int[][] grid;

  public Grid() {
    grid = new int[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
  }

  public boolean canMoveLeft(Tetromino tetromino) {
    int[][] shape = tetromino.getShape();
    int x = tetromino.getX();
    int y = tetromino.getY();

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] != 0) {
          int newX = x + col - 1;
          if (newX < 0 || grid[y + row][newX] != 0) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public boolean canMoveRight(Tetromino tetromino) {
    int[][] shape = tetromino.getShape();
    int x = tetromino.getX();
    int y = tetromino.getY();

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] != 0) {
          int newX = x + col + 1;
          if (newX >= Constants.BOARD_WIDTH || grid[y + row][newX] != 0) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public boolean canMoveDown(Tetromino tetromino) {
    int[][] shape = tetromino.getShape();
    int x = tetromino.getX();
    int y = tetromino.getY();

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] != 0) {
          int newY = y + row + 1;
          if (newY >= Constants.BOARD_HEIGHT || grid[newY][x + col] != 0) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public void placeTetromino(Tetromino tetromino) {
    int[][] shape = tetromino.getShape();
    int x = tetromino.getX();
    int y = tetromino.getY();
    int type = tetromino.getType() + 1;

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
          if (shape[row][col] != 0) {
              int gridX = x + col;
              int gridY = y + row;

              if (gridY >= 0 && gridY < Constants.BOARD_HEIGHT && gridX >= 0 && gridX < Constants.BOARD_WIDTH) {
                  grid[gridY][gridX] = type;
              }
          }
      }
    }

    clearFullLines();
  }

  private void clearFullLines() {
    int writeRow = Constants.BOARD_HEIGHT - 1;
    for (int row = Constants.BOARD_HEIGHT - 1; row >= 0; row--) {
      boolean isFullLine = true;
      for (int col = 0; col < Constants.BOARD_WIDTH; col++) {
        if (grid[row][col] == 0) {
          isFullLine = false;
          break;
        }
      }
      if (!isFullLine) {
        if (writeRow != row) {
          grid[writeRow] = grid[row];
        }
        writeRow--;
      }
    }
    while (writeRow >= 0) {
      Arrays.fill(grid[writeRow--], 0);
    }
  }

  public int[][] getGrid() {
    return grid;
  }
}
