package com.ics.tetwis;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Tetris extends Application {
    private int[][] grid = new int[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
    private Tetromino currentTetromino;
    private long lastUpdateTime = 0;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Canvas canvas = new Canvas(Constants.BOARD_WIDTH * Constants.TILE_SIZE,
                Constants.BOARD_HEIGHT * Constants.TILE_SIZE);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);

        stage.setTitle("Tetris");
        stage.setScene(scene);
        stage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawGrid(gc);

        currentTetromino = new Tetromino(0);

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdateTime >= Constants.UPDATE_INTERVAL) {
                    update();
                    lastUpdateTime = now;
                }
                render(gc);
            }
        };
        gameLoop.start();
    }

    private void update() {
        currentTetromino.moveDown();
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, Constants.BOARD_WIDTH * Constants.TILE_SIZE, Constants.BOARD_HEIGHT * Constants.TILE_SIZE);

        drawGrid(gc);
        renderTetromino(gc, currentTetromino, Color.BLUE);
    }

    private void drawGrid(GraphicsContext gc) {
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                gc.strokeRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE,
                        Constants.TILE_SIZE);
            }
        }
    }

    private void colorCell(GraphicsContext gc, int x, int y, Color color) {
        gc.setFill(color);
        gc.fillRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public void renderTetromino(GraphicsContext gc, Tetromino tetromino, Color color) {
        int[][] shape = tetromino.getShape();
        int tetrominoX = tetromino.getX();
        int tetrominoY = tetromino.getY();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    int gridX = tetrominoX + col;
                    int gridY = tetrominoY + row;
                    colorCell(gc, gridX, gridY, color);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}