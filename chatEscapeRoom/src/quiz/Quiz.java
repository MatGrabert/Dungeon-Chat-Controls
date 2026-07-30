package quiz;

import com.badlogic.gdx.Gdx;
import core.Game;
import core.network.messages.QuizEvent;
import core.network.messages.QuizMessage;
import core.network.messages.UIEvent;
import core.utils.JsonHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import ui.QuizUI;

/** The Quiz. */
public class Quiz {
  private static final List<String> questions = new ArrayList<>();
  private static final List<QuizAnswers> quizAnswers = new ArrayList<>();
  private static int currentQuestion = 0;
  private static boolean receivedAnAnswer = false;
  private static String givenAnswer;

  /**
   * Loads the quiz questions and answers from a JSON file into the lists.
   *
   * @param clientLoads Fills the quiz ui, if the client loads questions
   */
  public static void loadQuestions(Boolean clientLoads) {
    String quizPath = "quiz/quiz-qna.json";
    String contentAsString = Gdx.files.internal(quizPath).readString("UTF-8");
    Map<String, Object> contentAsMap = JsonHandler.readJson(contentAsString);

    for (Map.Entry<String, Object> entry : contentAsMap.entrySet()) {
      questions.add(entry.getKey());
      Map<?, ?> questionMap = (Map<?, ?>) entry.getValue();

      List<String> answers = new ArrayList<>();

      for (Object answer : (List<?>) questionMap.get("answers")) {
        answers.add(String.valueOf(answer));
      }

      String correctAnswer = String.valueOf(questionMap.get("correct"));

      quizAnswers.add(new QuizAnswers(answers, correctAnswer));
    }

    if (clientLoads) {
      QuizUI.fillQuizUI(questions.get(currentQuestion), quizAnswers.get(currentQuestion).answers());
      QuizUI.setQuizVisible(false);
    }
  }

  /**
   * This deals with a participant's answer to the quiz.
   *
   * @param answer The Answer of a participant.
   */
  public static void handleAnswer(String answer) {
    if (Objects.equals(answer, "next")) {
      receivedAnAnswer = false;
      nextQuestion();
    } else {
      evaluateResponse(answer.charAt(0));
    }
  }

  private static void evaluateResponse(char selectedAnswer) {
    if (!receivedAnAnswer) {
      char correctAnswer = quizAnswers.get(currentQuestion).correct().charAt(0);

      if (Objects.equals(selectedAnswer, correctAnswer)) {
        Game.network()
            .broadcast(new QuizEvent(String.valueOf(correctAnswer), true, true, false), true);
        hideAnswers(selectedAnswer, correctAnswer);
      } else {
        Game.network()
            .broadcast(new QuizEvent(String.valueOf(correctAnswer), true, true, false), true);
        Game.network()
            .broadcast(new QuizEvent(String.valueOf(selectedAnswer), false, true, false), true);
        hideAnswers(selectedAnswer, correctAnswer);
      }
      givenAnswer = letterToAnswer(selectedAnswer);

      receivedAnAnswer = true;
    }
  }

  private static String letterToAnswer(char selectedAnswer) {
    return switch (selectedAnswer) {
      case 'a' -> quizAnswers.get(currentQuestion).answers().get(0);
      case 'b' -> quizAnswers.get(currentQuestion).answers().get(1);
      case 'c' -> quizAnswers.get(currentQuestion).answers().get(2);
      case 'd' -> quizAnswers.get(currentQuestion).answers().get(3);
      default -> throw new IllegalStateException("Unexpected value: " + selectedAnswer);
    };
  }

  private static void hideAnswers(char selectedAnswer, char correctAnswer) {
    Game.network()
        .broadcast(
            new QuizEvent(
                "a",
                selectedAnswer == correctAnswer,
                false,
                selectedAnswer == 'a' || correctAnswer == 'a'),
            true);
    Game.network()
        .broadcast(
            new QuizEvent(
                "b",
                selectedAnswer == correctAnswer,
                false,
                selectedAnswer == 'b' || correctAnswer == 'b'),
            true);
    Game.network()
        .broadcast(
            new QuizEvent(
                "c",
                selectedAnswer == correctAnswer,
                false,
                selectedAnswer == 'c' || correctAnswer == 'c'),
            true);
    Game.network()
        .broadcast(
            new QuizEvent(
                "d",
                selectedAnswer == correctAnswer,
                false,
                selectedAnswer == 'd' || correctAnswer == 'd'),
            true);
  }

  private static void nextQuestion() {
    resetQuiz();
    List<String> givenAnswerInList = new ArrayList<>();
    givenAnswerInList.add(givenAnswer);
    Game.network()
        .broadcast(
            new QuizMessage(
                questions.get(currentQuestion),
                givenAnswerInList,
                letterToAnswer(quizAnswers.get(currentQuestion).correct().charAt(0)),
                true),
            true);
    currentQuestion++;
    if (currentQuestion == questions.size()) {
      Game.network().broadcast(new UIEvent("quiz close"), true);
      Game.network().broadcast(new UIEvent("endscreen open"), true);
      Game.network().broadcast(new UIEvent("chat close"), true);
      currentQuestion = -1;
    } else {
      Game.network().broadcast(new UIEvent("quiz next"), true);
      Game.network()
          .broadcast(
              new QuizMessage(
                  questions.get(currentQuestion),
                  quizAnswers.get(currentQuestion).answers(),
                  quizAnswers.get(currentQuestion).correct(),
                  false),
              true);
    }
  }

  private static void resetQuiz() {
    Game.network().broadcast(new QuizEvent("a", true, false, true), true);
    Game.network().broadcast(new QuizEvent("b", true, false, true), true);
    Game.network().broadcast(new QuizEvent("c", true, false, true), true);
    Game.network().broadcast(new QuizEvent("d", true, false, true), true);

    Game.network().broadcast(new QuizEvent("r", true, true, true), true);
  }
}
