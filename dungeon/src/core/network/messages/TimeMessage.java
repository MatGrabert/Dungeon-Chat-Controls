package core.network.messages;

/**
 * TimeMessage between server and client.
 *
 * @param timerName Name of the instance, that should get the message.
 * @param time Current message
 */
public record TimeMessage(String timerName, int time) implements NetworkMessage {}
