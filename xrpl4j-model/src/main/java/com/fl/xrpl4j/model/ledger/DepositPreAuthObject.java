package com.fl.xrpl4j.model.ledger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fl.xrpl4j.model.transactions.Address;
import com.fl.xrpl4j.model.transactions.DepositPreAuth;
import com.fl.xrpl4j.model.transactions.Hash256;
import com.fl.xrpl4j.model.transactions.Transaction;
import com.google.common.primitives.UnsignedInteger;
import org.immutables.value.Value;
import com.fl.xrpl4j.model.flags.Flags;

/**
 * Tracks a preauthorization from one account to another. {@link DepositPreAuth} transactions create these objects.
 *
 * <p>This has no effect on processing of {@link Transaction}s unless the account that provided the preauthorization
 * requires Deposit Authorization. In that case, the account that was preauthorized can send payments and
 * other transactions directly to the account that provided the preauthorization.
 * Preauthorizations are uni-directional, and have no effect on payments going the opposite direction.</p>
 */
@Value.Immutable
@JsonSerialize(as = ImmutableDepositPreAuthObject.class)
@JsonDeserialize(as = ImmutableDepositPreAuthObject.class)
public interface DepositPreAuthObject extends LedgerObject {

  static ImmutableDepositPreAuthObject.Builder builder() {
    return ImmutableDepositPreAuthObject.builder();
  }

  /**
   * The type of ledger object, which will always be "DepositPreauth" in this case.
   *
   * @return Always {@link LedgerEntryType#DEPOSIT_PRE_AUTH}.
   */
  @JsonProperty("LedgerEntryType")
  @Value.Derived
  default LedgerEntryType ledgerEntryType() {
    return LedgerEntryType.DEPOSIT_PRE_AUTH;
  }

  /**
   * The account that granted the preauthorization. (The destination of the preauthorized payments.)
   *
   * @return The {@link Address} of the account.
   */
  @JsonProperty("Account")
  Address account();

  /**
   * The account that received the preauthorization. (The sender of the preauthorized payments.)
   *
   * @return The {@link Address} of the account to authorize.
   */
  @JsonProperty("Authorize")
  Address authorize();

  /**
   * A bit-map of boolean flags. No flags are defined for {@link DepositPreAuthObject}s, so this value is always 0.
   *
   * @return Always {@link Flags#UNSET}.
   */
  @JsonProperty("Flags")
  @Value.Derived
  default Flags flags() {
    return Flags.UNSET;
  }

  /**
   * A hint indicating which page of the sender's owner directory links to this object, in case the directory
   * consists of multiple pages.
   *
   * <p>Note: The object does not contain a direct link to the owner directory containing it, since that value can be
   * derived from the Account.
   *
   * @return A {@link String} containing the owner node hint.
   */
  @JsonProperty("OwnerNode")
  String ownerNode();

  /**
   * The identifying hash of the transaction that most recently modified this object.
   *
   * @return A {@link Hash256} containing the previous transaction hash.
   */
  @JsonProperty("PreviousTxnID")
  Hash256 previousTransactionId();

  /**
   * The index of the ledger that contains the transaction that most recently modified this object.
   *
   * @return An {@link UnsignedInteger} representing the previous transaction ledger sequence.
   */
  @JsonProperty("PreviousTxnLgrSeq")
  UnsignedInteger previousTransactionLedgerSequence();

  /**
   * The unique ID of the {@link DepositPreAuthObject}.
   *
   * @return A {@link Hash256} containing the ID.
   */
  Hash256 index();
}
