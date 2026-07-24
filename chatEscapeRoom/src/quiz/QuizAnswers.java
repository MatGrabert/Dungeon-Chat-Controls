package quiz;

import java.util.List;

public record QuizAnswers(List<String> answers, String correct) {
  @Override
  public List<String> answers() {
    return answers;
  }

  @Override
  public String correct() {
    return correct;
  }
}
