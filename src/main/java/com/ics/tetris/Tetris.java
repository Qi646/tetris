package com.ics.tetris;

import java.util.Arrays;

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
    private long lockStartTime = 0;
    private boolean isLocking = false;
    private final TetrominoFactory tetrominoFactory = new TetrominoFactory();

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
        drawGrid(gc);

        currentTetromino = tetrominoFactory.createTetromino();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    if (canMoveLeft(currentTetromino)) {
                        currentTetromino.moveLeft();
                        resetLock();
                    }
                    break;
                case RIGHT:
                    if (canMoveRight(currentTetromino)) {
                        currentTetromino.moveRight();
                        resetLock();
                    }
                    break;
                case DOWN:
                    if (canMoveDown(currentTetromino)) {
                        currentTetromino.moveDown();
                        resetLock();
                    }
                    break;
                case X:
                    currentTetromino.rotateClockwise();
                    resetLock();
                    break;
                case Z:
                    currentTetromino.rotateCounterclockwise();
                    resetLock();
                    break;
                case SPACE:
                    while (canMoveDown(currentTetromino)) {
                        currentTetromino.moveDown();
                    }
                    lockTetromino();
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
                    update(now);
                    lastUpdateTime = now;
                }
                render(gc);
            }
        };
        gameLoop.start();
    }

    private void update(long now) {
        if (canMoveDown(currentTetromino)) {
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

    private void resetLock() {
        isLocking = false;
        lockStartTime = 0;
    }

    private void lockTetromino() {
        placeTetromino(currentTetromino);
        currentTetromino = tetrominoFactory.createTetromino();
        resetLock();
    }

    private Tetromino getGhostPiece(Tetromino tetromino) {
        Tetromino ghost = new Tetromino(tetromino.getType());
        ghost.setPosition(tetromino.getX(), tetromino.getY());
        for (int i = 0; i < tetromino.getRotationState(); i++) {
            ghost.rotateClockwise();
        }
        while (canMoveDown(ghost)) {
            ghost.moveDown();
        }
        return ghost;
    }

    private boolean canMoveLeft(Tetromino tetromino) {
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

    private boolean canMoveRight(Tetromino tetromino) {
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

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, Constants.BOARD_WIDTH * Constants.TILE_SIZE, Constants.BOARD_HEIGHT * Constants.TILE_SIZE);

        drawGrid(gc);
        Tetromino ghostPiece = getGhostPiece(currentTetromino);
        renderTetromino(gc, ghostPiece, Color.GRAY);
        renderTetromino(gc, currentTetromino);
        renderGrid(gc);
    }

    private void renderTetromino(GraphicsContext gc, Tetromino tetromino) {
        renderTetromino(gc, tetromino, getColorForTetromino(tetromino.getType()));
    }

    private void renderTetromino(GraphicsContext gc, Tetromino tetromino, Color color) {
        int[][] shape = tetromino.getShape();
        int tetrominoX = tetromino.getX();
        int tetrominoY = tetromino.getY();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int gridX = tetrominoX + col;
                    int gridY = tetrominoY + row;
                    colorCell(gc, gridX, gridY, color);
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

    public static void main(String[] args) {
        launch();
    }
}