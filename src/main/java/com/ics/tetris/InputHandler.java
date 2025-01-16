package com.ics.tetris;

import javafx.scene.Scene;

public class InputHandler {
  private final Tetris tetris;

  public InputHandler(Tetris tetris) {
    this.tetris = tetris;
  }

  public void setupInput(Scene scene) {
    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case LEFT:
          if (tetris.getGrid().canMoveLeft(tetris.getCurrentTetromino())) {
            tetris.getCurrentTetromino().moveLeft();
            tetris.resetLock();
          }
          break;
        case RIGHT:
          if (tetris.getGrid().canMoveRight(tetris.getCurrentTetromino())) {
            tetris.getCurrentTetromino().moveRight();
            tetris.resetLock();
          }
          break;
        case DOWN:
          if (tetris.getGrid().canMoveDown(tetris.getCurrentTetromino())) {
            tetris.getCurrentTetromino().moveDown();
            tetris.resetLock();
          }
          break;
        case X:
          tetris.getCurrentTetromino().rotateClockwise();
          tetris.resetLock();
          break;
        case Z:
          tetris.getCurrentTetromino().rotateCounterclockwise();
          tetris.resetLock();
          break;
        case SPACE:
          while (tetris.getGrid().canMoveDown(tetris.getCurrentTetromino())) {
            tetris.getCurrentTetromino().moveDown();
          }
          tetris.lockTetromino();
          break;
        default:
          break;
      }
      tetris.render();
    });
  }
}