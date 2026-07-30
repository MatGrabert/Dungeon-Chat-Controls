package starter;

import chat.CommandParser;
import chatEscapeRoom.level.Level01;
import chatEscapeRoom.level.Level02;
import contrib.components.CollideComponent;
import contrib.entities.EntityFactory;
import contrib.systems.CollisionSystem;
import core.Entity;
import core.Game;
import core.components.PlayerComponent;
import core.components.PositionComponent;
import core.level.loader.DungeonLoader;
import core.systems.MoveSystem;
import core.systems.input.InputSystem;
import core.utils.Tuple;
import quiz.Quiz;
import systems.ChatMoveSystem;
import ui.ChatUI;
import ui.EndScreen;
import ui.MenuUI;
import ui.QuizUI;

/**
 * Entry point for running the chatEscapeRoom.
 *
 * <p>This starter initializes the game with a chat and an input-area.
 *
 * <p>Usage: run with the Gradle task {@code runChatEscapeRoom}.
 */
public class ChatEscapeRoom {
  private static Entity hero;

  /**
   * Main entry point to launch the ChatEscapeRoom.
   *
   * @param args command-line arguments (not used in this starter)
   */
  public static void main(String[] args) {
    Game.userOnSetup(
        () -> {
          DungeonLoader.addLevel(Tuple.of("level01", Level01.class));
          DungeonLoader.addLevel(Tuple.of("level02", Level02.class));
          Game.remove(InputSystem.class);
          Game.add(new ChatMoveSystem());
          Game.add(new MoveSystem());
          Game.add(new CollisionSystem());
          CommandParser.loadCommands();
          createUIs();
          Quiz.loadQuestions(true);
          hero = EntityFactory.newHero();
          hero.add(new CollideComponent());
          hero.add(new PositionComponent());
          hero.add(new PlayerComponent());
          Game.add(hero);
        });
    Game.frameRate(30);
    Game.windowTitle("ChatEscapeRoom");
    Game.run();
  }

  private static void createUIs() {
    ChatUI chatUI = new ChatUI();
    Game.stage().ifPresent(QuizUI::create);
    Game.stage().ifPresent(MenuUI::create);
    Game.stage().ifPresent(EndScreen::create);
    Game.stage().ifPresent(chatUI::create);
  }
}
