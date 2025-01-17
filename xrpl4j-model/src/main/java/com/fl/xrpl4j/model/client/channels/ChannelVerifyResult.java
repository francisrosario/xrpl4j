package com.fl.xrpl4j.model.client.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import com.fl.xrpl4j.model.client.XrplResult;

/**
 * The result of a "channel_verify" rippled API request.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableChannelVerifyResult.class)
@JsonDeserialize(as = ImmutableChannelVerifyResult.class)
public interface ChannelVerifyResult extends XrplResult {

  static ImmutableChannelVerifyResult.Builder builder() {
    return ImmutableChannelVerifyResult.builder();
  }

  /**
   * If {@code true}, the signature is valid for the stated amount, channel, and public key.
   *
   * @return {@code true} if the signature was valid, otherwise {@code false}.
   */
  @JsonProperty("signature_verified")
  boolean signatureVerified();

}
