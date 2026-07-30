package core.network.messages;

import java.util.List;

/**
 * ChatMessages between server and client.
 *
 * @param question Current question
 * @param answers Current answers as list
 * @param correct Correct answer
 * @param forEndScreen If the message is used for the endscreen
 */
public record QuizMessage(
    String question, List<String> answers, String correct, Boolean forEndScreen)
    implements NetworkMessage {}
