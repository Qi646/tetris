package com.ics.tetwis;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class HelloApplication extends Application {
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private static final int TILE_SIZE = 30;

    private int[][] grid = new int[BOARD_HEIGHT][BOARD_WIDTH];
    private Tetromino currentTetromino;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Canvas canvas = new Canvas(BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);

        stage.setTitle("Tetris");
        stage.setScene(scene);
        stage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawGrid(gc);

        // Initialize game components
        currentTetromino = new Tetromino(0);

        // Start game loop
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render(gc);
            }
        };
        gameLoop.start();
    }

    private void update() {
        // Update game state (e.g., move tetromino down)
        currentTetromino.moveDown();
    }

    private void render(GraphicsContext gc) {
        // Clear the canvas
        gc.clearRect(0, 0, BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);

        // Draw the grid
        drawGrid(gc);

        // Render the current tetromino
        renderTetromino(gc, currentTetromino, Color.BLUE);
    }

    private void drawGrid(GraphicsContext gc) {
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                gc.strokeRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void colorCell(GraphicsContext gc, int x, int y, Color color) {
        gc.setFill(color);
        gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
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