package core.network.codec.converters;

import com.google.protobuf.Parser;
import core.network.codec.MessageConverter;
import core.network.messages.TimeMessage;

/** Converter for time-messages. */
public class TimeMessageConverter
    implements MessageConverter<TimeMessage, core.network.proto.TimeMessage> {
  private static final byte WIRE_TYPE_ID = 60;

  @Override
  public core.network.proto.TimeMessage toProto(TimeMessage message) {
    return core.network.proto.TimeMessage.newBuilder()
        .setTimerName(message.timerName())
        .setTime(message.time())
        .build();
  }

  @Override
  public TimeMessage fromProto(core.network.proto.TimeMessage proto) {
    return new TimeMessage(proto.getTimerName(), proto.getTime());
  }

  @Override
  public Class<TimeMessage> domainType() {
    return TimeMessage.class;
  }

  @Override
  public Class protoType() {
    return core.network.proto.TimeMessage.class;
  }

  @Override
  public Parser parser() {
    return core.network.proto.TimeMessage.parser();
  }

  @Override
  public byte wireTypeId() {
    return WIRE_TYPE_ID;
  }
}
