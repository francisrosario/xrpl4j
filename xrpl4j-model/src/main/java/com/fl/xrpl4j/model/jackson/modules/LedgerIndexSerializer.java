package com.fl.xrpl4j.model.jackson.modules;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fl.xrpl4j.model.client.common.LedgerIndex;

import java.io.IOException;

/**
 * Custom Jackson serializer for {@link LedgerIndex}s.
 */
public class LedgerIndexSerializer extends StdScalarSerializer<LedgerIndex> {

  public LedgerIndexSerializer() {
    super(LedgerIndex.class, false);
  }

  @Override
  public void serialize(LedgerIndex ledgerIndex, JsonGenerator gen, SerializerProvider provider) throws IOException {
    if (isInteger(ledgerIndex)) {
      gen.writeNumber(Integer.parseInt(ledgerIndex.value()));
    } else {
      gen.writeString(ledgerIndex.value());
    }
  }

  private boolean isInteger(final LedgerIndex value) {
    try {
      Integer.parseInt(value.value());
      // is an integer!
      return true;
    } catch (NumberFormatException e) {
      // not an integer!
      return false;
    }
  }
}
