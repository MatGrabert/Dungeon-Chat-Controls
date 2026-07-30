package core.network.messages;

/**
 * ChatMessages between server and client.
 *
 * @param playerID ID of the current player
 * @param message Current message
 */
public record ChatMessage(Short playerID, String message) implements NetworkMessage {}
