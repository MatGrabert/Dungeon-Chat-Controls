package starter;

import chat.CommandParser;
import chatEscapeRoom.level.Level02;
import contrib.components.CollideComponent;
import contrib.entities.EntityFactory;
import contrib.systems.CollisionSystem;
import core.Entity;
import core.Game;
import core.components.PlayerComponent;
import core.components.PositionComponent;
import core.game.PreRunConfiguration;
import core.level.loader.DungeonLoader;
import core.network.messages.ChatMessage;
import core.systems.MoveSystem;
import core.systems.input.InputSystem;
import core.utils.Tuple;
import quiz.Quiz;
import systems.ChatMoveSystem;
import systems.TimeSystem;

/** Server for the ChatEscapeRoom. */
public class ChatServer {
  private static Entity hero;

  /**
   * Starts chat server
   *
   * @param args command-line arguments (not used in this starter)
   */
  public static void main(String[] args) {
    PreRunConfiguration.multiplayerEnabled(true);
    PreRunConfiguration.isNetworkServer(true);
    PreRunConfiguration.setServerAsHeroOwner(true);

    Game.userOnSetup(
        () -> {
          // DungeonLoader.addLevel(Tuple.of("level01", Level01.class));
          DungeonLoader.addLevel(Tuple.of("level02", Level02.class));
          CommandParser.loadCommands();
          Game.remove(InputSystem.class);
          Game.add(new ChatMoveSystem());
          Game.add(new MoveSystem());
          Game.add(new CollisionSystem());
          Game.add(new TimeSystem(true));

          Game.network()
              .messageDispatcher()
              .registerHandler(
                  ChatMessage.class,
                  (session, message) -> {
                    Game.network().broadcast(message, true);
                    CommandParser.parseChatInput(message.message());
                  });

          Quiz.loadQuestions(false);
        });

    Game.run();
    hero = EntityFactory.newHero();
    hero.add(new CollideComponent());
    hero.add(new PositionComponent());
    hero.add(new PlayerComponent());
    Game.add(hero);
  }
}
