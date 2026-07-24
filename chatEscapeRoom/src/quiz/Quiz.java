package quiz;

import com.badlogic.gdx.Gdx;
import core.utils.JsonHandler;
import ui.EndScreen;
import ui.QuizUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Quiz {
  private static final List<String> questions = new ArrayList<>();
  private static final List<QuizAnswers> quizAnswers = new ArrayList<>();
  private static int currentQuestion = 0;
  private static boolean receivedAnAnswer = false;
  private static String givenAnswer;

  public static void loadQuestions() {
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

    QuizUI.fillQuizUI(questions.get(currentQuestion), quizAnswers.get(currentQuestion).answers());
    QuizUI.setQuizVisible(false);
  }

  public static void handleAnswer(String answer) {
    if(Objects.equals(answer, "next")) {
      receivedAnAnswer = false;
      nextQuestion();
    } else {
      evaluateResponse(answer.charAt(0));
    }
  }

  private static void evaluateResponse(char selectedAnswer) {
    if(!receivedAnAnswer) {
      char correctAnswer = quizAnswers.get(currentQuestion).correct().charAt(0);

      if(Objects.equals(selectedAnswer, correctAnswer)) {
        QuizUI.setAnswerColor(correctAnswer, true);
        hideAnswers(selectedAnswer, correctAnswer);
      } else {
        QuizUI.setAnswerColor(correctAnswer, true);
        QuizUI.setAnswerColor(selectedAnswer, false);
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
    QuizUI.setAnswerAVisible(selectedAnswer == 'a' || correctAnswer == 'a');
    QuizUI.setAnswerBVisible(selectedAnswer == 'b' || correctAnswer == 'b');
    QuizUI.setAnswerCVisible(selectedAnswer == 'c' || correctAnswer == 'c');
    QuizUI.setAnswerDVisible(selectedAnswer == 'd' || correctAnswer == 'd');
  }

  private static void nextQuestion() {
      resetQuiz();
      EndScreen.addResultLabels(questions.get(currentQuestion), givenAnswer, letterToAnswer(quizAnswers.get(currentQuestion).correct().charAt(0)));
      currentQuestion++;
    if (currentQuestion == questions.size()) {
      QuizUI.setQuizVisible(false);
      //EndScreen.setEndScreenVisible(true);
      currentQuestion = -1;
    } else {
      QuizUI.fillQuizUI(questions.get(currentQuestion), quizAnswers.get(currentQuestion).answers());
    }
  }

  private static void resetQuiz() {
    QuizUI.setAnswerAVisible(true);
    QuizUI.setAnswerBVisible(true);
    QuizUI.setAnswerCVisible(true);
    QuizUI.setAnswerDVisible(true);

    QuizUI.setAnswerColor('r', true);
  }
}
