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
  private Point lastPosition;
  private int stuckCounter = 0;
  private int stuckLimit = 5;

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
      if (reachedTarget(entity)) {
        stop(entity);
        stuckCounter = 0;
        lastPosition = null;
        targetPosition = null;
      } else if (stucks(entity)) {
        stop(entity);
        stuckCounter = 0;
        lastPosition = null;
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

  private boolean stucks(Entity entity) {
    PositionComponent positionComponent =
        entity
            .fetch(PositionComponent.class)
            .orElseThrow(() -> MissingComponentException.build(entity, PositionComponent.class));

    if (lastPosition == null) {
      lastPosition = positionComponent.position();
      return false;
    }

    float dx = positionComponent.position().x() - lastPosition.x();
    float dy = positionComponent.position().y() - lastPosition.y();
    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

    if (distance < 0.05f) {
      stuckCounter++;
    } else {
      stuckCounter = 0;
    }

    lastPosition = positionComponent.position();

    return stuckCounter >= stuckLimit;
  }

  private boolean reachedTarget(Entity entity) {
    float maxDistance = 0.01f;

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

    int x = Math.round(positionComponent.position().x());
    int y = Math.round(positionComponent.position().y());

    return switch (direction) {
      case UP -> new Point(x, y + 1);
      case RIGHT -> new Point(x + 1, y);
      case DOWN -> new Point(x, y - 1);
      case LEFT -> new Point(x - 1, y);
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
