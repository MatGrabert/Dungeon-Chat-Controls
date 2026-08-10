package systems;

import core.System;
import core.game.PreRunConfiguration;
import quiz.Quiz;
import ui.ChatUI;
import ui.QuizUI;

/** System for time events. */
public class TimeSystem extends System {
  private static float gameTime = 3600;
  private static float quizTime = 9999;
  private static float modeTime = 3;
  private static boolean isServer;

  /**
   * Creates a TimeSystem.
   *
   * @param isServer If the server creates the TimeSystem
   */
  public TimeSystem(boolean isServer) {
    super(AuthoritativeSide.BOTH);
    TimeSystem.isServer = isServer;
  }

  @Override
  public void execute() {
    reduceTime();

    if (isServer) {
      checkQuizTime();
      checkModeTime();
      checkGameTime();
    } else {
      updateUI();
    }
  }

  private void reduceTime() {
    float deltaTime = 1f / PreRunConfiguration.frameRate();

    if (gameTime > 0) {
      gameTime -= deltaTime;
    } else {
      gameTime = 0;
    }
    if (quizTime > 0) {
      quizTime -= deltaTime;
    } else {
      quizTime = 0;
    }
    if (modeTime > 0) {
      modeTime -= deltaTime;
    } else {
      modeTime = 0;
    }
  }

  private void updateUI() {
    ChatUI.setModeTimeLabel((int) modeTime);
    ChatUI.setGameTimeLabel((int) gameTime);
    QuizUI.setTimerLabel((int) quizTime);
  }

  private void checkModeTime() {
    if (modeTime == 0) {
      return;
    }
  }

  private void checkQuizTime() {
    if (quizTime == 0) {
      Quiz.timeOver();
    }
  }

  private void checkGameTime() {
    if (gameTime == 0) {
      Quiz.closeGameUIs();
    }
  }

  /**
   * Sets the time for a timer
   *
   * @param timerName The name of the time
   * @param time The end-time of the timer
   */
  public static void setTimer(String timerName, int time) {
    switch (timerName) {
      case "game" -> gameTime = time;
      case "quiz" -> quizTime = time;
      case "mode" -> modeTime = time;
    }
  }

  /**
   * Formates the time in seconds (int) into hh:mm:ss (String).
   *
   * @param seconds Time in Seconds e.g. 3600 = 60 minutes
   * @return Seconds formated to a String
   */
  public static String formatTime(int seconds) {
    int hours = seconds / 3600;
    int minutes = (seconds % 3600) / 60;

    if (hours > 0) {
      return String.format("%02d:%02d:%02d", hours, minutes, (seconds % 60));
    } else if (minutes > 0) {
      return String.format("%02d:%02d", minutes, (seconds % 60));
    } else {
      return String.format("%02d", seconds);
    }
  }
}
