package starter;

import chat.CommandParser;
import chatEscapeRoom.level.Level01;
import chatEscapeRoom.level.Level02;
import contrib.systems.CollisionSystem;
import core.Game;
import core.game.PreRunConfiguration;
import core.level.loader.DungeonLoader;
import core.network.messages.ChatMessage;
import core.network.messages.TimeMessage;
import core.systems.MoveSystem;
import core.systems.input.InputSystem;
import core.utils.Tuple;
import mode.DemocracyMode;
import quiz.Quiz;
import systems.ChatMoveSystem;
import systems.TimeSystem;

/** Server for the ChatEscapeRoom. */
public class ChatServer {

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
          DungeonLoader.addLevel(Tuple.of("level01", Level01.class));
          DungeonLoader.addLevel(Tuple.of("level02", Level02.class));
          CommandParser.loadCommands();
          addSystems();
          registerMessageHandler();
          fillTimer();
          Quiz.loadQuestions(false);
        });

    Game.run();
  }

  private static void addSystems() {
    Game.remove(InputSystem.class);
    Game.add(new MoveSystem());
    Game.add(new CollisionSystem());
    Game.add(new ChatMoveSystem());
    Game.add(new TimeSystem(true));
  }

  private static void registerMessageHandler() {
    Game.network()
        .messageDispatcher()
        .registerHandler(
            ChatMessage.class,
            (session, message) -> {
              Game.network().broadcast(message, true);
              CommandParser.parseChatInput(message.message());
            });

    Game.network()
        .messageDispatcher()
        .registerHandler(
            TimeMessage.class,
            (session, message) -> {
              Game.network()
                  .broadcast(
                      new TimeMessage(message.timerName(), TimeSystem.getTime(message.timerName())),
                      true);
            });
  }

  private static void fillTimer() {
    TimeSystem.setTimer("gameTime", 3600);
    TimeSystem.addServerCheckFunction("gameTime", Quiz::closeGameUIs);

    TimeSystem.setTimer("modeTime", 0);
    TimeSystem.addServerCheckFunction(
        "modeTime",
        () -> {
          DemocracyMode.evaluate();
          DemocracyMode.resetTimer();
        });

    TimeSystem.setTimer("quizTime", 8000);
    TimeSystem.addServerCheckFunction("quizTime", Quiz::timeOver);
  }
}
