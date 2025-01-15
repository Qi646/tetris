package com.ics.tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TetrominoFactory {

  private List<Integer> bag;
  private int index;

  public TetrominoFactory() {
    bag = new ArrayList<>();
    index = 0;
    refillBag();
  }

  private void refillBag() {
    bag.clear();
    for (int i = 0; i < 7; i++) {
      bag.add(i);
    }
    Collections.shuffle(bag);
  }

  public Tetromino createTetromino() {
    if (index >= bag.size()) {
      refillBag();
      index = 0;
    }
    return new Tetromino(bag.get(index++));
  }
}