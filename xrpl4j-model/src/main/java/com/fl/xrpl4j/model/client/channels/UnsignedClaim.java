package com.fl.xrpl4j.model.client.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fl.xrpl4j.model.transactions.Hash256;
import com.fl.xrpl4j.model.transactions.PaymentChannelClaim;
import com.fl.xrpl4j.model.transactions.XrpCurrencyAmount;
import org.immutables.value.Value;

/**
 * A payment channel claim that can be signed by the source account of a payment channel
 * and presented to the destination account. Once the destination account has this information, as well
 * as the signature, it can submit a {@link PaymentChannelClaim} to claim their XRP.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableUnsignedClaim.class)
@JsonDeserialize(as = ImmutableUnsignedClaim.class)
public interface UnsignedClaim {

  static ImmutableUnsignedClaim.Builder builder() {
    return ImmutableUnsignedClaim.builder();
  }

  /**
   * The Channel ID of the channel that provides the XRP.
   *
   * @return A {@link Hash256} containing the Channel ID.
   */
  @JsonProperty("Channel")
  Hash256 channel();

  /**
   * The amount of XRP, in drops, that the signature of this claim authorizes.
   *
   * @return An {@link XrpCurrencyAmount} representing the amount of the claim.
   */
  @JsonProperty("Amount")
  XrpCurrencyAmount amount();

}
