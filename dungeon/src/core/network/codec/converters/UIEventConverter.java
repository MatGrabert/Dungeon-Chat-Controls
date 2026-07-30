package core.network.codec.converters;

import com.google.protobuf.Parser;
import core.network.codec.MessageConverter;
import core.network.messages.UIEvent;

/** Converter for ui-events. */
public class UIEventConverter implements MessageConverter<UIEvent, core.network.proto.UIEvent> {
  private static final byte WIRE_TYPE_ID = 63;

  @Override
  public core.network.proto.UIEvent toProto(UIEvent message) {
    return core.network.proto.UIEvent.newBuilder().setEvent(message.event()).build();
  }

  @Override
  public UIEvent fromProto(core.network.proto.UIEvent proto) {
    return new UIEvent(proto.getEvent());
  }

  @Override
  public Class<UIEvent> domainType() {
    return UIEvent.class;
  }

  @Override
  public Class<core.network.proto.UIEvent> protoType() {
    return core.network.proto.UIEvent.class;
  }

  @Override
  public Parser<core.network.proto.UIEvent> parser() {
    return core.network.proto.UIEvent.parser();
  }

  @Override
  public byte wireTypeId() {
    return WIRE_TYPE_ID;
  }
}
