package com.fl.xrpl4j.model.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import com.ripple.cryptoconditions.Condition;
import com.ripple.cryptoconditions.Fulfillment;
import org.immutables.value.Value;
import com.fl.xrpl4j.model.flags.Flags;
import com.fl.xrpl4j.model.immutables.FluentCompareTo;

import java.util.Objects;
import java.util.Optional;

/**
 * Deliver XRP from a held payment to the recipient.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableEscrowFinish.class)
@JsonDeserialize(as = ImmutableEscrowFinish.class)
public interface EscrowFinish extends Transaction {

  static ImmutableEscrowFinish.Builder builder() {
    return ImmutableEscrowFinish.builder();
  }

  /**
   * Compute the fee for the supplied {@code fulfillment}. The minimum transaction cost to submit an EscrowFinish
   * transaction increases if it contains a fulfillment. If the transaction contains a fulfillment, the transaction cost
   * is 330 drops of XRP plus another 10 drops for every 16 bytes in size of the preimage.
   *
   * @param currentLedgerFeeDrops The number of drops that the ledger demands at present.
   * @param fulfillment           The {@link Fulfillment} that is being presented to the ledger for computation
   *                              purposes.
   *
   * @return An {@link XrpCurrencyAmount} representing the computed fee.
   * @see "https://xrpl.org/escrowfinish.html"
   */
  static XrpCurrencyAmount computeFee(final XrpCurrencyAmount currentLedgerFeeDrops, final Fulfillment fulfillment) {
    Objects.requireNonNull(currentLedgerFeeDrops);
    Objects.requireNonNull(fulfillment);

    UnsignedLong newFee =
        currentLedgerFeeDrops.value() // <-- usually 10 drops, per the docs.
            // <-- https://github.com/ripple/rippled/blob/develop/src/ripple/app/tx/impl/Escrow.cpp#L362
            .plus(UnsignedLong.valueOf(320))
            // <-- 10 drops for each additional 16 bytes.
            .plus(UnsignedLong.valueOf(10 * (fulfillment.getDerivedCondition().getCost() / 16)));
    return XrpCurrencyAmount.of(newFee);
  }

  /**
   * Set of {@link Flags.TransactionFlags}s for this {@link EscrowFinish}, which only allows {@code tfFullyCanonicalSig}
   * flag.
   *
   * <p>The value of the flags cannot be set manually, but exists for JSON serialization/deserialization only and for
   * proper signature computation in rippled.
   *
   * @return Always {@link Flags.TransactionFlags} with {@code tfFullyCanonicalSig} set.
   */
  @JsonProperty("Flags")
  @Value.Derived
  default Flags.TransactionFlags flags() {
    return new Flags.TransactionFlags.Builder().tfFullyCanonicalSig(true).build();
  }

  /**
   * {@link Address} of the source account that funded the escrow payment.
   *
   * @return The {@link Address} of the source account.
   */
  @JsonProperty("Owner")
  Address owner();

  /**
   * The {@link EscrowCreate#sequence()} of the transaction that created the escrow to cancel.
   *
   * @return An {@link UnsignedInteger} representing the sequence number.
   */
  @JsonProperty("OfferSequence")
  UnsignedInteger offerSequence();

  /**
   * Hex value matching the previously-supplied PREIMAGE-SHA-256 crypto-condition of the held payment.
   *
   * @return An {@link Optional} of type {@link Condition} containing the escrow condition.
   */
  @JsonProperty("Condition")
  Optional<Condition> condition();

  /**
   * Hex value of the PREIMAGE-SHA-256 crypto-condition fulfillment matching the held payment's {@code condition}.
   *
   * @return An {@link Optional} of type {@link Fulfillment} containing the fulfillment for the escrow's condition.
   */
  @JsonProperty("Fulfillment")
  Optional<Fulfillment<?>> fulfillment();

  /**
   * Validate fields.
   */
  @Value.Check
  default void check() {
    fulfillment().ifPresent(f -> {
        UnsignedLong feeInDrops = fee().value();
        Preconditions.checkState(condition().isPresent(),
            "If a fulfillment is specified, the corresponding condition must also be specified.");
        Preconditions.checkState(FluentCompareTo.is(feeInDrops).greaterThanEqualTo(UnsignedLong.valueOf(330)),
            "If a fulfillment is specified, the fee must be set to 330 or greater.");
      }
    );
    condition().ifPresent($ -> Preconditions.checkState(fulfillment().isPresent(),
        "If a condition is specified, the corresponding fulfillment must also be specified."));
  }

}
