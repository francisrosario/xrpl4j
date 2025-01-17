package com.fl.xrpl4j.model.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import com.ripple.cryptoconditions.Condition;
import org.immutables.value.Value;
import com.fl.xrpl4j.model.flags.Flags;
import com.fl.xrpl4j.model.flags.Flags.TransactionFlags;

import java.util.Optional;

/**
 * Sequester XRP until the escrow process either finishes or is canceled.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableEscrowCreate.class)
@JsonDeserialize(as = ImmutableEscrowCreate.class)
public interface EscrowCreate extends Transaction {

  static ImmutableEscrowCreate.Builder builder() {
    return ImmutableEscrowCreate.builder();
  }

  /**
   * Set of {@link Flags.TransactionFlags}s for this {@link EscrowCreate}, which only allows {@code tfFullyCanonicalSig}
   * flag.
   *
   * <p>The value of the flags cannot be set manually, but exists for JSON serialization/deserialization only and for
   * proper signature computation in rippled.
   *
   * @return Always {@link Flags.TransactionFlags} with {@code tfFullyCanonicalSig} set.
   */
  @JsonProperty("Flags")
  @Value.Derived
  default TransactionFlags flags() {
    return new TransactionFlags.Builder().tfFullyCanonicalSig(true).build();
  }

  /**
   * Amount of XRP, in drops, to deduct from the sender's balance and escrow. Once escrowed, the XRP can either go to
   * the {@link EscrowCreate#destination()} address (after the {@link EscrowCreate#finishAfter()} time) or returned to
   * the sender (after the {@link EscrowCreate#cancelAfter()} time).
   *
   * @return An {@link XrpCurrencyAmount} representing the amount of the escrow.
   */
  @JsonProperty("Amount")
  XrpCurrencyAmount amount();

  /**
   * Address to receive escrowed XRP.
   *
   * @return The {@link Address} of the destination account.
   */
  @JsonProperty("Destination")
  Address destination();

  /**
   * Arbitrary tag to further specify the destination for this escrowed payment, such as a hosted recipient at the
   * destination address.
   *
   * @return An {@link Optional} of type {@link UnsignedInteger} representing the tag of the destination account.
   */
  @JsonProperty("DestinationTag")
  Optional<UnsignedInteger> destinationTag();

  /**
   * The time, in seconds since the Ripple Epoch, when this escrow expires.
   *
   * <p>This value is immutable - the funds can only be returned to the sender after this time.
   *
   * @return An {@link Optional} of type {@link UnsignedLong} representing the cancel after time.
   */
  @JsonProperty("CancelAfter")
  Optional<UnsignedLong> cancelAfter();

  /**
   * The time, in seconds since the Ripple Epoch, when the escrowed XRP can be released to the recipient.
   *
   * <p>This value is immutable - the funds cannot move until this time is reached.
   *
   * @return An {@link Optional} of type {@link UnsignedLong} representing the finish after time.
   */
  @JsonProperty("FinishAfter")
  Optional<UnsignedLong> finishAfter();

  /**
   * Hex value representing a PREIMAGE-SHA-256 crypto-condition. The funds can only be delivered to the recipient if
   * this condition is fulfilled.
   *
   * @return An {@link Optional} of type {@link Condition} containing the escrow condition.
   */
  @JsonProperty("Condition")
  Optional<Condition> condition();

  /**
   * Validate cancelAfter, finishAfter, and condition fields.
   */
  @Value.Check
  default void check() {
    if (cancelAfter().isPresent() && finishAfter().isPresent()) {
      Preconditions.checkState(
          finishAfter().get().compareTo(cancelAfter().get()) < 0,
          "If both CancelAfter and FinishAfter are specified, the FinishAfter time must be before the CancelAfter time."
      );
    }
  }
}
