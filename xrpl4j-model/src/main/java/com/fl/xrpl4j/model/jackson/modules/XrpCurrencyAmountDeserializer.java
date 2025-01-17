package com.fl.xrpl4j.model.jackson.modules;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fl.xrpl4j.model.transactions.XrpCurrencyAmount;

import java.io.IOException;

/**
 * Custom Jackson deserializer for {@link XrpCurrencyAmount}s.
 */
public class XrpCurrencyAmountDeserializer extends StdDeserializer<XrpCurrencyAmount> {

  public XrpCurrencyAmountDeserializer() {
    super(XrpCurrencyAmount.class);
  }

  @Override
  public XrpCurrencyAmount deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
    return XrpCurrencyAmount.ofDrops(jsonParser.getValueAsLong());
  }
}
