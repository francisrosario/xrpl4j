package com.fl.xrpl4j.model.client.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fl.xrpl4j.model.transactions.XrpCurrencyAmount;
import com.google.common.primitives.UnsignedInteger;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

/**
 * Information about queued transactions sent by a given account. This information describes the state of the local
 * rippled server, which may be different from other servers in the peer-to-peer XRP Ledger network. Some fields
 * may be omitted because the values are calculated "lazily" by the queuing mechanism.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableQueueData.class)
@JsonDeserialize(as = ImmutableQueueData.class)
public interface QueueData {

  static ImmutableQueueData.Builder builder() {
    return ImmutableQueueData.builder();
  }

  /**
   * Number of queued transactions from this address.
   *
   * @return An {@link UnsignedInteger} representing the number of transactions.
   */
  @JsonProperty("txn_count")
  UnsignedInteger transactionCount();

  /**
   * Whether a transaction in the queue changes this address's ways of authorizing transactions. If true,
   * this address can queue no further transactions until that transaction has been executed or dropped from the queue.
   *
   * @return {@code true} if a transaction in the queue changes this account's way of authorizing transactions,
   *     otherwise {@code false}.
   */
  @JsonProperty("auth_change_queued")
  @Value.Default
  default boolean authChangeQueued() {
    return false;
  }

  /**
   * The lowest Sequence Number among transactions queued by this address.
   *
   * @return An optionally-present {@link UnsignedInteger}.
   */
  @JsonProperty("lowest_sequence")
  Optional<UnsignedInteger> lowestSequence();

  /**
   * The highest Sequence Number among transactions queued by this address.
   *
   * @return An optionally-present {@link UnsignedInteger}.
   */
  @JsonProperty("highest_sequence")
  Optional<UnsignedInteger> highestSequence();

  /**
   * Integer amount of drops of XRP, represented as a {@link String}, that could be debited from this address
   * if every transaction in the queue consumes the maximum amount of XRP possible.
   *
   * @return An optionally-present {@link XrpCurrencyAmount}.
   */
  @JsonProperty("max_spend_drops_total")
  Optional<XrpCurrencyAmount> maxSpendDropsTotal();

  /**
   * A {@link List} of {@link QueueTransaction}s containing information about each queued transaction from this address.
   *
   * @return A {@link List} of {@link QueueTransaction}s.
   */
  List<QueueTransaction> transactions();
}
