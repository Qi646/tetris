package com.ics.tetris;

public class Shape {
  int[][][] rotations;
  int currentRotation;

  Shape(int[][][] rotations) {
    this.rotations = rotations;
    this.currentRotation = 0;
  }

  int[][] getCurrentData() {
    return rotations[currentRotation];
  }

  void rotate(boolean clockwise) {
    if (clockwise) {
      currentRotation = (currentRotation + 1) % rotations.length;
    } else {
      currentRotation = (currentRotation + rotations.length - 1) % rotations.length;
    }
  }

  int width() {
    return getCurrentData()[0].length;
  }

  int height() {
    return getCurrentData().length;
  }
}