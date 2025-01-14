package com.ics.tetris;

public class Tetromino {
    private int[][] shape;
    private int type;
    private int x, y;

    private static final int[][][] SHAPES = {
        {
            {1, 1, 1, 1}, 
            {0, 0, 0, 0}, 
            {0, 0, 0, 0}, 
            {0, 0, 0, 0}
        },
        {
            {0, 1, 1, 0}, 
            {0, 1, 1, 0}, 
            {0, 0, 0, 0}, 
            {0, 0, 0, 0}
        },
        {
            {0, 1, 0, 0}, 
            {1, 1, 1, 0}, 
            {0, 0, 0, 0}, 
            {0, 0, 0, 0}
        },
        {
            {0, 0, 1, 0}, 
            {1, 1, 1, 0}, 
            {0, 0, 0, 0}, 
            {0, 0, 0, 0}
        },
        {
            {1, 0, 0, 0}, 
            {1, 1, 1, 0}, 
            {0, 0, 0, 0}, 
            {0, 0, 0, 0}
        },
        {
            {0, 1, 1, 0}, 
            {1, 1, 0, 0}, 
            {0, 0, 0, 0}, 
            {0, 0, 0, 0}
        },
        {
            {1, 1, 0, 0}, 
            {0, 1, 1, 0}, 
            {0, 0, 0, 0}, 
            {0, 0, 0, 0}
        }
    };

    public Tetromino(int type) {
        this.type = type;
        this.shape = SHAPES[type];
        this.x = 3;
        this.y = 0;
    }

    public void rotate() {
        int n = shape.length;
        int[][] newShape = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newShape[j][n - 1 - i] = shape[i][j];
            }
        }

        shape = newShape;
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
        for (int i = 0; i < shape.length; i++) {
            for (int j = shape[i].length - 1; j >= 0; j--) {
                if (shape[i][j] != 0) {
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
        for (int i = shape.length - 1; i >= 0; i--) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
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
        return shape;
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
}

