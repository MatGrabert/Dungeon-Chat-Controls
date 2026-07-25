package systems;

import core.Entity;
import core.System;
import core.components.PlayerComponent;
import core.components.PositionComponent;
import core.components.VelocityComponent;
import core.utils.Direction;
import core.utils.Vector2;
import core.utils.components.MissingComponentException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * System for processing movement commands from the chat.
 *
 * <p>Movement commands are buffered in a queue and processed during the next system cycle.
 */
public class ChatMoveSystem extends System {
  private static final Queue<Direction> commandQueue = new ArrayDeque<>();
  private static final int speed = 400;

  /** Creates a new ChatMoveSystem. */
  public ChatMoveSystem() {
    super(PlayerComponent.class, VelocityComponent.class, PositionComponent.class);
  }

  /**
   * Adds a movement command to the queue-
   *
   * @param direction The direction of the movement to execute.
   */
  public static void addCommand(Direction direction) {
    commandQueue.add(direction);
  }

  @Override
  public void execute() {
    Direction direction = commandQueue.poll();

    if (direction != null) {
      filteredEntityStream()
          .filter(entity -> entity.isPresent(PlayerComponent.class))
          .forEach(entity -> move(entity, direction));
    }
  }

  private void move(Entity entity, Direction direction) {
    VelocityComponent velocityComponent =
        entity
            .fetch(VelocityComponent.class)
            .orElseThrow(() -> MissingComponentException.build(entity, VelocityComponent.class));

    Vector2 velocity =
        switch (direction) {
          case Direction.UP -> Vector2.of(0, speed);
          case Direction.DOWN -> Vector2.of(0, -speed);
          case Direction.LEFT -> Vector2.of(-speed, 0);
          case Direction.RIGHT -> Vector2.of(speed, 0);
          case NONE -> null;
        };

    velocityComponent.currentVelocity(velocity);
  }
}
