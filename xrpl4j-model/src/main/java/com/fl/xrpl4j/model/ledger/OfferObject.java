package com.fl.xrpl4j.model.ledger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fl.xrpl4j.model.transactions.Address;
import com.fl.xrpl4j.model.transactions.CurrencyAmount;
import com.fl.xrpl4j.model.transactions.OfferCreate;
import com.google.common.primitives.UnsignedInteger;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;
import com.fl.xrpl4j.model.transactions.Hash256;
import com.fl.xrpl4j.model.flags.Flags;

import java.util.Optional;

/**
 * The Offer object type describes an offer to exchange currencies, more traditionally known as an order.
 *
 * @see "https://xrpl.org/offer.html"
 */
@Immutable
@JsonSerialize(as = ImmutableOfferObject.class)
@JsonDeserialize(as = ImmutableOfferObject.class)
public interface OfferObject extends LedgerObject {

  static ImmutableOfferObject.Builder builder() {
    return ImmutableOfferObject.builder();
  }

  /**
   * The value 0x006F, mapped to the string "Offer", indicates that this object is a {@link OfferObject} object.
   *
   * @return Always {@link LedgerEntryType#OFFER}.
   */
  @JsonProperty("LedgerEntryType")
  @Value.Derived
  default LedgerEntryType ledgerEntryType() {
    return LedgerEntryType.OFFER;
  }

  /**
   * The sender of the {@link OfferObject}. Cashing the {@link OfferObject} debits this address's balance.
   *
   * @return The {@link Address} of the offer sender.
   */
  @JsonProperty("Account")
  Address account();

  /**
   * A bit-map of boolean flags.
   *
   * @return A {@link Flags.OfferFlags}.
   */
  @JsonProperty("Flags")
  Flags.OfferFlags flags();

  /**
   * The sequence number of the {@link OfferCreate} transaction that
   * created this offer.
   *
   * @return An {@link UnsignedInteger} representing the sequence number.
   */
  @JsonProperty("Sequence")
  UnsignedInteger sequence();

  /**
   * The remaining amount and type of currency requested by the offer creator.
   *
   * @return A {@link CurrencyAmount}.
   */
  @JsonProperty("TakerPays")
  CurrencyAmount takerPays();


  /**
   * The remaining amount and type of currency being provided by the offer creator.
   *
   * @return A {@link CurrencyAmount}.
   */
  @JsonProperty("TakerGets")
  CurrencyAmount takerGets();


  /**
   * The ID of the Offer Directory that links to this offer.
   *
   * @return A {@link Hash256} containing the ID.
   */
  @JsonProperty("BookDirectory")
  Hash256 bookDirectory();

  /**
   * A hint indicating which page of the offer directory links to this object, in case the directory consists of
   * multiple pages.
   *
   * @return A {@link String} containing the hint.
   */
  @JsonProperty("BookNode")
  String bookNode();

  /**
   * A hint indicating which page of the sender's owner directory links to this object, in case the directory
   * consists of multiple pages.
   * Note: The object does not contain a direct link to the owner directory containing it,
   * since that value can be derived from the Account.
   *
   * @return A {@link String} containing the hint.
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
   * Indicates the time after which this offer is considered expired, in
   * <a href="https://xrpl.org/basic-data-types.html#specifying-time">seconds since the Ripple Epoch</a>.
   *
   * @return An {@link Optional} of type {@link UnsignedInteger} representing the expiration of this offer.
   */
  @JsonProperty("Expiration")
  Optional<UnsignedInteger> expiration();

  /**
   * The unique ID of the {@link OfferObject}.
   *
   * @return A {@link Hash256} containing the ID.
   */
  Hash256 index();

}
