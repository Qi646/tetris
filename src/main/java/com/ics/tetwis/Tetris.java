package com.ics.tetwis;

import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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

        currentTetromino = new Tetromino(new Random().nextInt(7));

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

    private Color getColorForTetromino(int type) {
        switch (type) {
            case 0:
                return Color.CYAN;
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.PURPLE;
            case 3:
                return Color.ORANGE;
            case 4:
                return Color.BLUE;
            case 5:
                return Color.GREEN;
            case 6:
                return Color.RED;
            default:
                return Color.BLACK; // Default color, should never activate
        }
    }

    public void renderTetromino(GraphicsContext gc, Tetromino tetromino, Color color) {
        int[][] shape = tetromino.getShape();
        int tetrominoX = tetromino.getX();
        int tetrominoY = tetromino.getY();
        Color tetrominoColor = getColorForTetromino(tetromino.getType());

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    int gridX = tetrominoX + col;
                    int gridY = tetrominoY + row;
                    colorCell(gc, gridX, gridY, tetrominoColor);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}