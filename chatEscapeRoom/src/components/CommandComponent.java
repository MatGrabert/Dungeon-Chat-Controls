package components;

import core.Component;
import core.utils.Direction;
import java.util.ArrayDeque;
import java.util.Queue;

/** Stores commands as direction of an entity. */
public final class CommandComponent implements Component {
  private final Queue<Direction> commandQueue = new ArrayDeque<>();

  /**
   * Adds a movement command to the queue-
   *
   * @param direction The direction of the movement to execute.
   */
  public void addCommand(Direction direction) {
    commandQueue.add(direction);
  }

  /**
   * Polls a direction.
   *
   * @return The next direction
   */
  public Direction poll() {
    return commandQueue.poll();
  }
}
