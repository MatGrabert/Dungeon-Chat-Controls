package starter;

import chat.CommandParser;
import core.Game;
import core.game.PreRunConfiguration;
import core.network.messages.ChatMessage;
import core.network.messages.QuizEvent;
import core.network.messages.QuizMessage;
import core.network.messages.TimeMessage;
import core.network.messages.UIEvent;
import java.util.Objects;

import mode.DemocracyMode;
import quiz.Quiz;
import systems.TimeSystem;
import ui.ChatUI;
import ui.EndScreen;
import ui.MenuUI;
import ui.QuizUI;

/** Test class. */
public class TestClient2 {
  private static ChatUI chatUI = new ChatUI();

  /**
   * Starts chat client
   *
   * @param args command-line arguments (not used in this starter)
   */
  public static void main(String[] args) {
    PreRunConfiguration.multiplayerEnabled(true);
    PreRunConfiguration.isNetworkServer(false);
    PreRunConfiguration.networkServerAddress("127.0.0.1");
    PreRunConfiguration.networkPort(7777);
    PreRunConfiguration.username("Player2");

    Game.userOnSetup(
        () -> {
          Game.add(new TimeSystem(false));

          Game.network()
              .messageDispatcher()
              .registerHandler(
                  ChatMessage.class,
                  ((session, message) -> {
                    chatUI.addMessage((short) message.playerID(), message.message());
                  }));

          Game.network()
              .messageDispatcher()
              .registerHandler(
                  UIEvent.class,
                  ((session, message) -> {
                    switch (message.event()) {
                      case "menu open" -> MenuUI.setMenuVisible(true);
                      case "menu close" -> MenuUI.setMenuVisible(false);
                      case "menu next" -> {
                        if (MenuUI.getGameMode().equals("Anarchie")) {
                          MenuUI.setGameMode("Demokratie");
                          DemocracyMode.setModeIsActive(true);
                        } else {
                          MenuUI.setGameMode("Anarchie");
                          DemocracyMode.setModeIsActive(false);
                        }
                      }
                      case "quiz open" -> QuizUI.setQuizVisible(true);
                      case "quiz close" -> QuizUI.setQuizVisible(false);
                      case "endscreen open" -> EndScreen.setEndScreenVisible(true);
                      case "chat close" -> chatUI.setChatUIVisible(false);
                    }
                  }));

          Game.network()
              .messageDispatcher()
              .registerHandler(
                  QuizEvent.class,
                  (((session, message) -> {
                    if (Objects.equals(message.answer(), "r")) {
                      QuizUI.setAnswerAVisible(true);
                      QuizUI.setAnswerBVisible(true);
                      QuizUI.setAnswerCVisible(true);
                      QuizUI.setAnswerDVisible(true);
                      QuizUI.setAnswerColor('r', true);
                    } else {
                      if (message.changeColor()) {
                        switch (message.answer()) {
                          case "a" -> QuizUI.setAnswerColor('a', message.correct());
                          case "b" -> QuizUI.setAnswerColor('b', message.correct());
                          case "c" -> QuizUI.setAnswerColor('c', message.correct());
                          case "d" -> QuizUI.setAnswerColor('d', message.correct());
                        }
                      } else {
                        switch (message.answer()) {
                          case "a" -> QuizUI.setAnswerAVisible(message.setVisible());
                          case "b" -> QuizUI.setAnswerBVisible(message.setVisible());
                          case "c" -> QuizUI.setAnswerCVisible(message.setVisible());
                          case "d" -> QuizUI.setAnswerDVisible(message.setVisible());
                        }
                      }
                    }
                  })));

          Game.network()
              .messageDispatcher()
              .registerHandler(
                  QuizMessage.class,
                  ((session, message) -> {
                    if (message.forEndScreen()) {
                      EndScreen.addResultLabels(
                          message.question(), message.answers().getFirst(), message.correct());
                    } else {
                      QuizUI.fillQuizUI(message.question(), message.answers());
                    }
                  }));

          Game.network()
              .messageDispatcher()
              .registerHandler(
                  TimeMessage.class,
                  ((session, message) -> {
                    TimeSystem.setTimer(message.timerName(), message.time());
                  }));

          CommandParser.loadCommands();
          createUIs();
          Quiz.loadQuestions(true);

          fillTimer();
        });

    Game.windowTitle("ChatEscapeRoom");
    Game.run();
  }

  private static void createUIs() {
    Game.stage().ifPresent(QuizUI::create);
    Game.stage().ifPresent(MenuUI::create);
    Game.stage().ifPresent(EndScreen::create);
    Game.stage().ifPresent(chatUI::create);
  }

  private static void fillTimer() {
    if(!TimeSystem.addClientUpdateFunction("modeTime", ChatUI::setModeTimeLabel)) {
      TimeSystem.setTimer("modeTime", 0);
      TimeSystem.addClientUpdateFunction("modeTime", ChatUI::setModeTimeLabel);
    }
    if(!TimeSystem.addClientCheckFunction("modeTime", DemocracyMode::resetTimer)) {
      TimeSystem.setTimer("modeTime", 0);
      TimeSystem.addClientCheckFunction("modeTime", DemocracyMode::resetTimer);
    }
    if(!TimeSystem.addClientUpdateFunction("gameTime", ChatUI::setGameTimeLabel)) {
      TimeSystem.setTimer("gameTime", 9999);
      TimeSystem.setSynchronizeTimer(TestClient2::synchronizeTimer);
      TimeSystem.addClientUpdateFunction("gameTime", ChatUI::setGameTimeLabel);
    }
    if(!TimeSystem.addClientUpdateFunction("quizTime", QuizUI::setTimerLabel)) {
      TimeSystem.setTimer("quizTime", 8000);
      TimeSystem.addClientUpdateFunction("quizTime", QuizUI::setTimerLabel);
    }
  }

  private static void synchronizeTimer(String timerName) {
    Game.network().send((short) Game.network().assignedClientId(), new TimeMessage(timerName, 0), true);
  }
}
