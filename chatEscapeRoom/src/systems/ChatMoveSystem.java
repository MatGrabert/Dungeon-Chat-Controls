package systems;

import core.Entity;
import core.System;
import core.components.PlayerComponent;
import core.components.PositionComponent;
import core.components.VelocityComponent;
import core.utils.Direction;
import core.utils.Point;
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
  private static final int speed = 5;
  private Point targetPosition;

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
    filteredEntityStream()
        .filter(entity -> entity.isPresent(PlayerComponent.class))
        .forEach(this::makeNextStep);
  }

  private void makeNextStep(Entity entity) {
    if (targetPosition != null) {
      VelocityComponent velocityComponent =
        entity
          .fetch(VelocityComponent.class)
          .orElseThrow(() -> MissingComponentException.build(entity, VelocityComponent.class));

      if (reachedTarget(entity)) {
        stop(entity);
        targetPosition = null;
      }
    } else {
      Direction direction = commandQueue.poll();
      if (direction != null) {
        targetPosition = calculateNextTarget(entity, direction);
        move(entity, direction);
      } else {
        stop(entity);
      }
    }
  }

  private boolean reachedTarget(Entity entity) {
    float maxDistance = 0.05f;

    PositionComponent positionComponent =
        entity
            .fetch(PositionComponent.class)
            .orElseThrow(() -> MissingComponentException.build(entity, PositionComponent.class));

    float dx = positionComponent.position().x() - targetPosition.x();
    float dy = positionComponent.position().y() - targetPosition.y();
    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

    return distance < maxDistance;
  }

  private Point calculateNextTarget(Entity entity, Direction direction) {
    PositionComponent positionComponent =
        entity
            .fetch(PositionComponent.class)
            .orElseThrow(() -> MissingComponentException.build(entity, PositionComponent.class));

    return switch (direction) {
      case UP -> new Point(positionComponent.position().x(), positionComponent.position().y() + 1);
      case RIGHT ->
          new Point(positionComponent.position().x() + 1, positionComponent.position().y());
      case DOWN ->
          new Point(positionComponent.position().x(), positionComponent.position().y() - 1);
      case LEFT ->
          new Point(positionComponent.position().x() - 1, positionComponent.position().y());
      case NONE -> null;
    };
  }

  private void stop(Entity entity) {
    VelocityComponent velocityComponent =
        entity
            .fetch(VelocityComponent.class)
            .orElseThrow(() -> MissingComponentException.build(entity, VelocityComponent.class));

    velocityComponent.currentVelocity(Vector2.of(0, 0));
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
