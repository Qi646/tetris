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

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    currentTetromino.moveLeft();
                    break;
                case RIGHT:
                    currentTetromino.moveRight();
                    break;
                default:
                    break;
            }
            render(gc);
        });

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
        if (canMoveDown(currentTetromino)) {
            currentTetromino.moveDown();
        } else {
            placeTetromino(currentTetromino);
            currentTetromino = new Tetromino(new Random().nextInt(7));
        }
    }

    private boolean canMoveDown(Tetromino tetromino) {
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

    private void placeTetromino(Tetromino tetromino) {
        int[][] shape = tetromino.getShape();
        int x = tetromino.getX();
        int y = tetromino.getY();
        int type = tetromino.getType() + 1;

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    grid[y + row][x + col] = type;
                }
            }
        }

        clearFullLines();
    }

    private void clearFullLines() {
        for (int row = 0; row < Constants.BOARD_HEIGHT; row++) {
            boolean isFullLine = true;
            for (int col = 0; col < Constants.BOARD_WIDTH; col++) {
                if (grid[row][col] == 0) {
                    isFullLine = false;
                    break;
                }
            }
            if (isFullLine) {
                clearLine(row);
            }
        }
    }

    private void clearLine(int line) {
        for (int row = line; row > 0; row--) {
            for (int col = 0; col < Constants.BOARD_WIDTH; col++) {
                grid[row][col] = grid[row - 1][col];
            }
        }
        for (int col = 0; col < Constants.BOARD_WIDTH; col++) {
            grid[0][col] = 0;
        }
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, Constants.BOARD_WIDTH * Constants.TILE_SIZE, Constants.BOARD_HEIGHT * Constants.TILE_SIZE);

        drawGrid(gc);
        renderTetromino(gc, currentTetromino);
        renderGrid(gc);
    }

    private void renderTetromino(GraphicsContext gc, Tetromino tetromino) {
        int[][] shape = tetromino.getShape();
        int tetrominoX = tetromino.getX();
        int tetrominoY = tetromino.getY();
        Color tetrominoColor = getColorForTetromino(tetromino.getType());

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int gridX = tetrominoX + col;
                    int gridY = tetrominoY + row;
                    colorCell(gc, gridX, gridY, tetrominoColor);
                }
            }
        }
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.LIGHTGREY);
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                gc.strokeRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE,
                        Constants.TILE_SIZE);
            }
        }
    }

    private void renderGrid(GraphicsContext gc) {
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                if (grid[y][x] != 0) {
                    colorCell(gc, x, y, getColorForTetromino(grid[y][x] - 1));
                }
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
                throw new IllegalArgumentException("Invalid type: " + type);
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