package com.ics.tetris;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Tetris extends Application {
  private final Grid grid = new Grid();
  private Tetromino currentTetromino;
  private long lastUpdateTime = 0;
  private long lockStartTime = 0;
  private boolean isLocking = false;
  private final TetrominoFactory tetrominoFactory = new TetrominoFactory();
  private Renderer renderer;

  @Override
  public void start(Stage stage) {
    Pane root = new Pane();
    Canvas canvas = new Canvas(Constants.BOARD_WIDTH * Constants.TILE_SIZE,
        Constants.BOARD_HEIGHT * Constants.TILE_SIZE);
    root.getChildren().add(canvas);
    Scene scene = new Scene(root);

    stage.setTitle("Tetris");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();

    GraphicsContext gc = canvas.getGraphicsContext2D();
    renderer = new Renderer(gc, grid);

    currentTetromino = tetrominoFactory.createTetromino();

    InputHandler inputHandler = new InputHandler(this);
    inputHandler.setupInput(scene);

    AnimationTimer gameLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        if (now - lastUpdateTime >= Constants.UPDATE_INTERVAL) {
          update(now);
          lastUpdateTime = now;
        }
        render();
      }
    };
    gameLoop.start();
  }

  private void update(long now) {
    if (grid.canMoveDown(currentTetromino)) {
      currentTetromino.moveDown();
      resetLock();
    } else {
      if (!isLocking) {
        isLocking = true;
        lockStartTime = now;
      } else if (now - lockStartTime >= Constants.LOCK_DELAY) {
        lockTetromino();
      }
    }
  }

  public void resetLock() {
    isLocking = false;
    lockStartTime = 0;
  }

  public void lockTetromino() {
    grid.placeTetromino(currentTetromino);
    currentTetromino = tetrominoFactory.createTetromino();
    resetLock();
  }

  private Tetromino getGhostPiece(Tetromino tetromino) {
    Tetromino ghost = new Tetromino(tetromino.getType());
    ghost.setPosition(tetromino.getX(), tetromino.getY());
    ghost.setRotationState(tetromino.getRotationState());
    while (grid.canMoveDown(ghost)) {
      ghost.moveDown();
    }
    return ghost;
  }

  public void render() {
    Tetromino ghostPiece = getGhostPiece(currentTetromino);
    renderer.render(currentTetromino, ghostPiece);
  }

  public Grid getGrid() {
    return grid;
  }

  public Tetromino getCurrentTetromino() {
    return currentTetromino;
  }

  public static void main(String[] args) {
    launch();
  }
}
