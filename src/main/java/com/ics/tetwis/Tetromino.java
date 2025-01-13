package com.ics.tetwis;

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
            {1, 1, 0, 0}, 
            {1, 1, 0, 0}, 
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
        this.x = 5;
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
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void moveDown() {
        y++;
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
}

