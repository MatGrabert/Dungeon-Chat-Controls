package core.network.codec.converters;

import com.google.protobuf.Parser;
import core.network.codec.MessageConverter;
import core.network.messages.QuizMessage;

/** Converter for quiz-messages. */
public class QuizMessageConverter
    implements MessageConverter<QuizMessage, core.network.proto.QuizMessage> {
  private static final byte WIRE_TYPE_ID = 61;

  @Override
  public core.network.proto.QuizMessage toProto(QuizMessage message) {
    return core.network.proto.QuizMessage.newBuilder()
        .setQuestion(message.question())
        .addAllAnswers(message.answers())
        .setCorrect(message.correct())
        .setForEndScreen(message.forEndScreen())
        .build();
  }

  @Override
  public QuizMessage fromProto(core.network.proto.QuizMessage proto) {
    return new QuizMessage(
        proto.getQuestion(), proto.getAnswersList(), proto.getCorrect(), proto.getForEndScreen());
  }

  @Override
  public Class<QuizMessage> domainType() {
    return QuizMessage.class;
  }

  @Override
  public Class protoType() {
    return core.network.proto.QuizMessage.class;
  }

  @Override
  public Parser parser() {
    return core.network.proto.QuizMessage.parser();
  }

  @Override
  public byte wireTypeId() {
    return WIRE_TYPE_ID;
  }
}
