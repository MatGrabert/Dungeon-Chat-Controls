package core.network.codec.converters;

import com.google.protobuf.Parser;
import core.network.codec.MessageConverter;
import core.network.messages.ChatMessage;

/** Converter for chat-messages. */
public class ChatMessageConverter
    implements MessageConverter<ChatMessage, core.network.proto.ChatMessage> {
  private static final byte WIRE_TYPE_ID = 64;

  @Override
  public core.network.proto.ChatMessage toProto(ChatMessage message) {
    return core.network.proto.ChatMessage.newBuilder()
        .setPlayerId(message.playerID())
        .setMessage(message.message())
        .build();
  }

  @Override
  public ChatMessage fromProto(core.network.proto.ChatMessage proto) {
    return new ChatMessage((short) proto.getPlayerId(), proto.getMessage());
  }

  @Override
  public Class<ChatMessage> domainType() {
    return ChatMessage.class;
  }

  @Override
  public Class protoType() {
    return core.network.proto.ChatMessage.class;
  }

  @Override
  public Parser parser() {
    return core.network.proto.ChatMessage.parser();
  }

  @Override
  public byte wireTypeId() {
    return WIRE_TYPE_ID;
  }
}
