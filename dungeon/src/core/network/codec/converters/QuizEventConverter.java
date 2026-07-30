package core.network.codec.converters;

import com.google.protobuf.Parser;
import core.network.codec.MessageConverter;
import core.network.messages.QuizEvent;

/** Converter for quiz events. */
public class QuizEventConverter
    implements MessageConverter<QuizEvent, core.network.proto.QuizEvent> {
  private static final byte WIRE_TYPE_ID = 62;

  @Override
  public core.network.proto.QuizEvent toProto(QuizEvent message) {
    return core.network.proto.QuizEvent.newBuilder()
        .setAnswer(message.answer())
        .setCorrect(message.correct())
        .setChangeColor(message.changeColor())
        .setSetVisible(message.setVisible())
        .build();
  }

  @Override
  public QuizEvent fromProto(core.network.proto.QuizEvent proto) {
    return new QuizEvent(
        proto.getAnswer(), proto.getCorrect(), proto.getChangeColor(), proto.getSetVisible());
  }

  @Override
  public Class<QuizEvent> domainType() {
    return QuizEvent.class;
  }

  @Override
  public Class<core.network.proto.QuizEvent> protoType() {
    return core.network.proto.QuizEvent.class;
  }

  @Override
  public Parser<core.network.proto.QuizEvent> parser() {
    return core.network.proto.QuizEvent.parser();
  }

  @Override
  public byte wireTypeId() {
    return WIRE_TYPE_ID;
  }
}
