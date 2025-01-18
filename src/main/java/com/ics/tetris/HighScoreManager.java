package com.ics.tetris;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class HighScoreManager {
  private static final TreeMap<Integer, String> highScores = new TreeMap<>(Collections.reverseOrder());
  private static final String HIGH_SCORES_FILE = "highscores.dat";

  public static void loadHighScores() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGH_SCORES_FILE))) {
      Map<Integer, String> loadedScores = (Map<Integer, String>) ois.readObject();
      highScores.putAll(loadedScores);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void saveHighScores() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORES_FILE))) {
      oos.writeObject(highScores);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void addHighScore(int linesCleared, String playerName) {
    highScores.put(linesCleared, playerName);
    while (highScores.size() > 5) {
      highScores.pollLastEntry();
    }
  }

  public static TreeMap<Integer, String> getHighScores() {
    return highScores;
  }
}