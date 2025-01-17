package com.fl.xrpl4j.model.jackson.modules;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fl.xrpl4j.model.client.common.LedgerIndex;

import java.io.IOException;

/**
 * Custom Jackson deserializer for {@link LedgerIndex}s.
 */
public class LedgerIndexDeserializer extends StdDeserializer<LedgerIndex> {

  protected LedgerIndexDeserializer() {
    super(LedgerIndex.class);
  }

  @Override
  public LedgerIndex deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
    return LedgerIndex.of(jsonParser.getValueAsString());
  }
}
