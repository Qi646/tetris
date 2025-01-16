package com.ics.tetris;

public class Tetromino {
    private int[][][] rotations;
    private int type;
    private int x, y;
    private int rotationState = 0;

    // I-Tetromino (4 blocks in a straight line)
    public static final int[][] I_SHAPE = {
            { 1, 1, 1, 1 }
    };

    // O-Tetromino (2x2 square)
    public static final int[][] O_SHAPE = {
            { 1, 1 },
            { 1, 1 }
    };

    // T-Tetromino (T-shaped)
    public static final int[][] T_SHAPE = {
            { 0, 1, 0 },
            { 1, 1, 1 }
    };

    // S-Tetromino (S-shaped)
    public static final int[][] S_SHAPE = {
            { 0, 1, 1 },
            { 1, 1, 0 }
    };

    // Z-Tetromino (Z-shaped)
    public static final int[][] Z_SHAPE = {
            { 1, 1, 0 },
            { 0, 1, 1 }
    };

    // J-Tetromino (L-shaped, facing left)
    public static final int[][] J_SHAPE = {
            { 1, 0, 0 },
            { 1, 1, 1 }
    };

    // L-Tetromino (L-shaped, facing right)
    public static final int[][] L_SHAPE = {
            { 0, 0, 1 },
            { 1, 1, 1 }
    };

    // Combined SHAPES array containing all Tetromino shapes
    public static final int[][][] SHAPES = {
            I_SHAPE, // I-Tetromino
            O_SHAPE, // O-Tetromino
            T_SHAPE, // T-Tetromino
            S_SHAPE, // S-Tetromino
            Z_SHAPE, // Z-Tetromino
            J_SHAPE, // J-Tetromino
            L_SHAPE // L-Tetromino
    };

    public Tetromino(int type) {
        this.type = type;
        this.rotations = precomputeRotations(SHAPES[type]);
        this.x = 3;
        this.y = 0;
    }

    private int[][][] precomputeRotations(int[][] shape) {
        int[][][] rotations = new int[4][][];
        rotations[0] = shape;
        for (int i = 1; i < 4; i++) {
            rotations[i] = rotateShape(rotations[i - 1]);
        }
        return rotations;
    }

    private int[][] rotateShape(int[][] shape) {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotatedShape = new int[cols][rows];

        // Rotate the shape by 90 degrees clockwise
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rotatedShape[col][rows - row - 1] = shape[row][col];
            }
        }

        return rotatedShape;
    }

    public void rotate() {
        rotationState = (rotationState + 1) % 4;
    }

    public void moveLeft() {
        if (isOTetromino()) {
            if (x > -1) {
                x--;
            }
        } else {
            if (x > 0) {
                x--;
            }
        }
    }

    private boolean isOTetromino() {
        return this.type == 1;
    }

    public void moveRight() {
        int width = 0;
        for (int i = 0; i < getShape().length; i++) {
            for (int j = getShape()[i].length - 1; j >= 0; j--) {
                if (getShape()[i][j] != 0) {
                    width = Math.max(width, j + 1);
                    break;
                }
            }
        }
        if (x + width < Constants.BOARD_WIDTH) {
            x++;
        }
    }

    public void moveDown() {
        int height = 0;
        for (int i = getShape().length - 1; i >= 0; i--) {
            for (int j = 0; j < getShape()[i].length; j++) {
                if (getShape()[i][j] != 0) {
                    height = Math.max(height, i + 1);
                    break;
                }
            }
        }
        if (y + height < Constants.BOARD_HEIGHT) {
            y++;
        }
    }

    public int[][] getShape() {
        return rotations[rotationState];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public int getRotationState() {
        return rotationState;
    }
}