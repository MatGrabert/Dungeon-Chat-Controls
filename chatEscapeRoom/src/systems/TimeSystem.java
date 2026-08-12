package systems;

import core.System;
import core.game.PreRunConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/** System for time events. */
public class TimeSystem extends System {
  private static boolean isServer;
  private static final float deltaTime = 1f / PreRunConfiguration.frameRate();
  private static Map<String, Float> timer = new HashMap<>();
  private static Map<String, Runnable> serverCheckFuntion = new HashMap<>();
  private static Map<String, Consumer<Integer>> clientUpdateFunction = new HashMap<>();
  private static Map<String, Consumer<Integer>> serverUpdateFuntion = new HashMap<>();
  private static Map<String, Runnable> clientCheckFunction = new HashMap<>();
  private static Map<String, Boolean> timeOver = new HashMap<>();
  private static Consumer<String> synchronizeTimer;
  private static final int MAX_TIME = 9000;

  /**
   * Creates a TimeSystem.
   *
   * @param isServer If the server creates the TimeSystem
   */
  public TimeSystem(boolean isServer) {
    super(AuthoritativeSide.BOTH);
    TimeSystem.isServer = isServer;
  }

  /**
   * Gets the time of the timer.
   *
   * @param timerName The name of the timer
   * @return Time of the timer in seconds
   */
  public static int getTime(String timerName) {
    if(timer.containsKey(timerName)) {
      return timer.get(timerName).intValue();
    } else {
      return -1;
    }
  }

  /**
   * Sets a function to synchronize the timer of the server and a client.
   *
   * @param synchronizeTimer The function to synchronize the timer
   */
  public static void setSynchronizeTimer(Consumer<String> synchronizeTimer) {
    TimeSystem.synchronizeTimer = synchronizeTimer;
  }

  @Override
  public void execute() {
    reduceTime();

    if (isServer) {
      checkServerTime();
      updateServerTime();
    } else {
      checkClientTime();
      updateClientTime();
    }
  }

  private void reduceTime() {
    for(String timerName : timer.keySet()) {
      float time = timer.get(timerName);

      if(time > 0) {
        timer.put(timerName, (time - deltaTime));
      }
    }
  }

  private void updateClientTime() {
    for(String timerName : timer.keySet()) {
      if(clientUpdateFunction.containsKey(timerName)) {
        clientUpdateFunction.get(timerName).accept(timer.get(timerName).intValue());
      }
      if(timer.get(timerName) > MAX_TIME) {
        synchronizeTimer.accept(timerName);
      }
    }
  }

  private void checkServerTime() {
    for(String timerName : timer.keySet()) {
      if(!timeOver.get(timerName) && timer.get(timerName) <= 0) {
        timeOver.put(timerName, true);

        if(serverCheckFuntion.containsKey(timerName)) {
          serverCheckFuntion.get(timerName).run();
        }
      }
    }
  }

  private void updateServerTime() {
    for(String timerName : timer.keySet()) {
      if(serverUpdateFuntion.containsKey(timerName)) {
        serverUpdateFuntion.get(timerName).accept(timer.get(timerName).intValue());
      }
    }
  }

  private void checkClientTime() {
    for(String timerName : timer.keySet()) {
      if(!timeOver.get(timerName) && timer.get(timerName) <= 0) {
        timeOver.put(timerName, true);
        if(clientCheckFunction.containsKey(timerName)) {
          clientCheckFunction.get(timerName).run();
        }
      }
    }
  }

  /**
   * Sets the time for a timer.
   *
   * @param timerName The name of the timer
   * @param time The end-time of the timer
   */
  public static void setTimer(String timerName, float time) {
    timer.put(timerName, time);
    timeOver.put(timerName, time <= 0);
  }

  /**
   * Sets the server check function for a timer.
   *
   * <p> The system checks if the timer for the function has expired. If the timer has expired, the function is triggered.
   *
   * @param timerName The name of the timer
   * @param function Function of a server to be executed when the timer ends
   * @return True, if TimerSystem contains timer
   */
  public static boolean addServerCheckFunction(String timerName, Runnable function) {
    if(timer.containsKey(timerName)) {
      serverCheckFuntion.put(timerName, function);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Sets the client update function for a timer.
   *
   * <p> The function will be triggered every tick.
   *
   * @param timerName The name of the timer
   * @param function Function of a server to be executed when the timer ends
   * @return True, if TimerSystem contains timer
   */
  public static boolean addClientUpdateFunction(String timerName, Consumer<Integer> function) {
    if(timer.containsKey(timerName)) {
      clientUpdateFunction.put(timerName, function);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Sets the client check function for a timer.
   *
   * <p> The system checks if the timer for the function has expired. If the timer has expired, the function is triggered.
   *
   * @param timerName The name of the timer
   * @param function Function of a server to be executed when the timer ends
   * @return True, if TimerSystem contains timer
   */
  public static boolean addClientCheckFunction(String timerName, Runnable function) {
    if(timer.containsKey(timerName)) {
      clientCheckFunction.put(timerName, function);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Sets the server update function for a timer.
   *
   * <p> The function will be triggered every tick.
   *
   * @param timerName The name of the timer
   * @param function Function of a server to be executed when the timer ends
   * @return True, if TimerSystem contains timer
   */
  public static boolean addServerUpdateFunction(String timerName, Consumer<Integer> function) {
    if(timer.containsKey(timerName)) {
      serverUpdateFuntion.put(timerName, function);
      return true;
    } else {
      return false;
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
