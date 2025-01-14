package com.ics.tetwis;

import java.util.Random;

public class TetrominoFactory {
  private final Random random = new Random();

  public Tetromino createTetromino() {
    return new Tetromino(random.nextInt(7));
  }
}