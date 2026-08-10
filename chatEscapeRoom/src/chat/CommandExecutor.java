package chat;

import contrib.components.QuizNpcComponent;
import core.Game;
import core.components.PositionComponent;
import core.network.messages.UIEvent;
import core.utils.Direction;
import core.utils.Point;
import quiz.Quiz;
import systems.ChatMoveSystem;

/**
 * Executes recognized chat commands.
 *
 * <p>The commands are for the movement of the game character, etc.
 */
public class CommandExecutor {

  /**
   * Executes the specified chat command.
   *
   * @param category The category of the command e.g., move.
   * @param action The action within the category e.g., up.
   */
  public static void execute(String category, String action) {
    switch (category) {
      case "move" -> handleMove(action);
      case "quiz" -> handleQuiz(action);
      case "menu" -> handleMenu(action);
      case "game" -> handleGame(action);
      default -> throw new IllegalStateException("Unexpected value: " + category);
    }
  }

  private static void handleMove(String action) {
    Direction direction =
        switch (action) {
          case "up" -> Direction.UP;
          case "down" -> Direction.DOWN;
          case "left" -> Direction.LEFT;
          case "right" -> Direction.RIGHT;
          default -> throw new IllegalStateException("Unexpected value: " + action);
        };
    ChatMoveSystem.addCommand(direction);
  }

  private static void handleQuiz(String action) {
    Quiz.handleAnswer(action);
  }

  private static void handleMenu(String action) {
    switch (action) {
      case "open" -> Game.network().broadcast(new UIEvent("menu open"), true);
      case "close" -> Game.network().broadcast(new UIEvent("menu close"), true);
      case "next" -> Game.network().broadcast(new UIEvent("menu next"), true);
    }
  }

  private static void handleGame(String action) {
    switch (action) {
      case "action" -> interact();
    }
  }

  private static void interact() {
    if (nearQuizMaster(3)) {
      Game.network().broadcast(new UIEvent("quiz open"), true);
      Quiz.setQuizIsActive(true);
      Quiz.startTime();
    }
  }

  private static boolean nearQuizMaster(float interactionDistance) {
    Boolean isNear = false;

    Point playerPosition =
        Game.allPlayers()
            .findFirst()
            .get()
            .fetch(PositionComponent.class)
            .map(PositionComponent::position)
            .orElse(null);
    Point npcPosition =
        Game.allEntities()
            .filter(entity -> entity.isPresent(QuizNpcComponent.class))
            .findFirst()
            .flatMap(entity -> entity.fetch(PositionComponent.class))
            .map(PositionComponent::position)
            .orElse(null);

    if (playerPosition != null && npcPosition != null) {
      float dx = playerPosition.x() - npcPosition.x();
      float dy = playerPosition.y() - npcPosition.y();
      float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

      if (distance < interactionDistance && distance > -interactionDistance) {
        isNear = true;
      }
    }

    return isNear;
  }
}
