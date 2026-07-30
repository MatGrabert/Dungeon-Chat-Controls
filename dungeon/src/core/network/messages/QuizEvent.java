package core.network.messages;

/**
 * Message between server and client.
 *
 * @param answer The current answer
 * @param correct If the answer was correct
 * @param changeColor If the color of the answer label should change
 * @param setVisible If the label of the answer should be visible/unvisible
 */
public record QuizEvent(String answer, Boolean correct, Boolean changeColor, Boolean setVisible)
    implements NetworkMessage {}
