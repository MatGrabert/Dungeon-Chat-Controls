package quiz;

import java.util.List;

/**
 * QuizAnswers includes all possible answers and the correct answer.
 *
 * @param answers All possible answers
 * @param correct The correct answer
 */
public record QuizAnswers(List<String> answers, String correct) {}
