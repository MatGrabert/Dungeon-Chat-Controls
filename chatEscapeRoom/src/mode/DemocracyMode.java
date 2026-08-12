package mode;

import chat.CommandExecutor;
import core.Game;
import core.network.messages.TimeMessage;
import systems.TimeSystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** This mode collects all commands and executes the one with the most votes. */
public class DemocracyMode {
  private static Map<String, Integer> commandCounter = new HashMap<>();
  private static boolean modeIsActive = false;
  private static final int modeTime = 4;

  /**
   * Gets the time of this mode.
   *
   * @return the time of this mode
   */
  public static int getModeTime() {
    return modeTime;
  }

  /**
   * If this mode is currently active.
   *
   * @return True if the mode is active
   */
  public static boolean isModeActive() {
    return modeIsActive;
  }

  /**
   * Sets this mode as active.
   *
   * @param modeIsActive True if the mode should be activated
   */
  public static void setModeIsActive(boolean modeIsActive) {
    DemocracyMode.modeIsActive = modeIsActive;
  }

  /** Resets the mode time. */
  public static void resetTimer() {
    if (modeIsActive) {
      TimeSystem.setTimer("modeTime", modeTime);
    }
  }

  /**
   * Adds commands to the command map and counts them.
   *
   * @param command Command that should be added
   */
  public static void addCommand(String command) {
    commandCounter.merge(command, 1, Integer::sum);
  }

  /** Executes the command with the most votes. */
  public static void evaluate() {
    if (!commandCounter.isEmpty()) {
      String command =
          Collections.max(commandCounter.entrySet(), Map.Entry.comparingByValue()).getKey();
      CommandExecutor.execute(command.split(",")[0], command.split(",")[1]);
      commandCounter.clear();
    }
  }
}
