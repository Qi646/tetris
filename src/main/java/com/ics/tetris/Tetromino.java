package com.ics.tetris;

public class Tetromino {
  private int[][][] rotations;
  private int type;
  private int x, y;
  private int rotationState = 0;

  public static final int[][][] I_SHAPE = {
    {
      { 0, 0, 0, 0 },
      { 1, 1, 1, 1 },
      { 0, 0, 0, 0 },
      { 0, 0, 0, 0 }
    },
    {
      { 0, 0, 1, 0 },
      { 0, 0, 1, 0 },
      { 0, 0, 1, 0 },
      { 0, 0, 1, 0 }
    },
    {
      { 0, 0, 0, 0 },
      { 0, 0, 0, 0 },
      { 1, 1, 1, 1 },
      { 0, 0, 0, 0 }
    },
    {
      { 0, 1, 0, 0 },
      { 0, 1, 0, 0 },
      { 0, 1, 0, 0 },
      { 0, 1, 0, 0 }
    }
  };

  public static final int[][][] O_SHAPE = {
    {
      { 1, 1 },
      { 1, 1 }
    },
    {
      { 1, 1 },
      { 1, 1 }
    },
    {
      { 1, 1 },
      { 1, 1 }
    },
    {
      { 1, 1 },
      { 1, 1 }
    }
  };

  public static final int[][][] T_SHAPE = {
    {
      { 0, 1, 0 },
      { 1, 1, 1 },
      { 0, 0, 0 }
    },
    {
      { 0, 1, 0 },
      { 0, 1, 1 },
      { 0, 1, 0 }
    },
    {
      { 0, 0, 0 },
      { 1, 1, 1 },
      { 0, 1, 0 }
    },
    {
      { 0, 1, 0 },
      { 1, 1, 0 },
      { 0, 1, 0 }
    }
  };

  public static final int[][][] S_SHAPE = {
    {
      { 0, 1, 1 },
      { 1, 1, 0 },
      { 0, 0, 0 }
    },
    {
      { 0, 1, 0 },
      { 0, 1, 1 },
      { 0, 0, 1 }
    },
    {
      { 0, 0, 0 },
      { 0, 1, 1 },
      { 1, 1, 0 }
    },
    {
      { 1, 0, 0 },
      { 1, 1, 0 },
      { 0, 1, 0 }
    }
  };

  public static final int[][][] Z_SHAPE = {
    {
      { 1, 1, 0 },
      { 0, 1, 1 },
      { 0, 0, 0 }
    },
    {
      { 0, 0, 1 },
      { 0, 1, 1 },
      { 0, 1, 0 }
    },
    {
      { 0, 0, 0 },
      { 1, 1, 0 },
      { 0, 1, 1 }
    },
    {
      { 0, 1, 0 },
      { 1, 1, 0 },
      { 1, 0, 0 }
    }
  };

  public static final int[][][] J_SHAPE = {
    {
      { 1, 0, 0 },
      { 1, 1, 1 },
      { 0, 0, 0 }
    },
    {
      { 0, 1, 1 },
      { 0, 1, 0 },
      { 0, 1, 0 }
    },
    {
      { 0, 0, 0 },
      { 1, 1, 1 },
      { 0, 0, 1 }
    },
    {
      { 0, 1, 0 },
      { 0, 1, 0 },
      { 1, 1, 0 }
    }
  };

  public static final int[][][] L_SHAPE = {
    {
      { 0, 0, 1 },
      { 1, 1, 1 },
      { 0, 0, 0 }
    },
    {
      { 0, 1, 0 },
      { 0, 1, 0 },
      { 0, 1, 1 }
    },
    {
      { 0, 0, 0 },
      { 1, 1, 1 },
      { 1, 0, 0 }
    },
    {
      { 1, 1, 0 },
      { 0, 1, 0 },
      { 0, 1, 0 }
    }
  };

  public static final int[][][][] SHAPES = {
    I_SHAPE,
    O_SHAPE,
    T_SHAPE,
    S_SHAPE,
    Z_SHAPE,
    J_SHAPE,
    L_SHAPE
  };

  public Tetromino(int type) {
    this.type = type;
    this.rotations = SHAPES[type];
    this.x = 3;
    this.y = 0;
  }

  public void rotateClockwise() {
    rotationState = (rotationState + 1) % 4;
  }

  public void rotateCounterclockwise() {
    rotationState = (rotationState + 3) % 4;
  }

  public void moveLeft() {
    if (x > 0) {
      x--;
    }
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