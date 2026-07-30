package core.network.messages;

/**
 * Message between server and client.
 *
 * @param event The new event for the ui
 */
public record UIEvent(String event) implements NetworkMessage {}
