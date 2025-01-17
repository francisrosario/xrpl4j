package com.fl.xrpl4j.model.client.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fl.xrpl4j.model.ledger.LedgerObject;
import com.fl.xrpl4j.model.transactions.Address;
import com.fl.xrpl4j.model.transactions.Hash256;
import com.fl.xrpl4j.model.transactions.Marker;
import com.google.common.primitives.UnsignedInteger;
import org.immutables.value.Value;
import com.fl.xrpl4j.model.client.XrplRequestParams;
import com.fl.xrpl4j.model.client.common.LedgerIndex;

import java.util.Optional;

/**
 * Represents the request parameters for an "account_objects" rippled API call.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableAccountObjectsRequestParams.class)
@JsonDeserialize(as = ImmutableAccountObjectsRequestParams.class)
public interface AccountObjectsRequestParams extends XrplRequestParams {

  static ImmutableAccountObjectsRequestParams.Builder builder() {
    return ImmutableAccountObjectsRequestParams.builder();
  }

  /**
   * Construct an {@link AccountObjectsRequestParams} for a given account and otherwise default parameters.
   *
   * @param classicAddress The classic {@link Address} of the account to request objects for.
   *
   * @return An {@link AccountObjectsRequestParams} for the given {@link Address}.
   */
  static AccountObjectsRequestParams of(Address classicAddress) {
    return builder()
        .account(classicAddress)
        .build();
  }

  /**
   * The unique XRPL {@link Address} for the account.
   *
   * @return The unique XRPL {@link Address} for the account.
   */
  Address account();

  /**
   * If included, filter results to include only this type of ledger object.
   *
   * @return An optionally-present {@link AccountObjectType} to filter by.
   */
  Optional<AccountObjectType> type();

  /**
   * If true, the response only includes {@link LedgerObject}s that would block this
   * account from being deleted. The default is false.
   *
   * @return {@code true} if requesting only ledger objects that would block this account from being deleted, otherwise
   *     {@code false}.
   */
  @JsonProperty("deletion_blockers_only")
  @Value.Default
  default boolean deletionBlockersOnly() {
    return false;
  }

  /**
   * A 20-byte hex string for the ledger version to use.
   *
   * @return An optionally-present {@link Hash256} containing the ledger hash.
   */
  @JsonProperty("ledger_hash")
  Optional<Hash256> ledgerHash();

  /**
   * The ledger index of the ledger to use, or a shortcut {@link String} to choose a ledger automatically.
   *
   * @return A {@link LedgerIndex}. Defaults to {@link LedgerIndex#CURRENT}.
   */
  @JsonProperty("ledger_index")
  @Value.Default
  default LedgerIndex ledgerIndex() {
    return LedgerIndex.CURRENT;
  }

  /**
   * The maximum number of {@link LedgerObject}s to include in the resulting
   * {@link AccountObjectsResult#accountObjects()}. Must be within the inclusive range 10 to 400 on non-admin
   * connections. The default is 200.
   *
   * @return An optionally-present {@link UnsignedInteger} denoting the response limit.
   */
  Optional<UnsignedInteger> limit();

  /**
   * Value from a previous paginated response. Resume retrieving data where that response left off.
   *
   * @return An optionally-present {@link String} containing the marker.
   */
  Optional<Marker> marker();

  /**
   * The enum Account object type.
   */
  enum AccountObjectType {
    /**
     * Check account object type.
     */
    CHECK("check"),
    /**
     * Desposit pre auth account object type.
     */
    DESPOSIT_PRE_AUTH("deposit_preauth"),
    /**
     * Escrow account object type.
     */
    ESCROW("escrow"),
    /**
     * Offer account object type.
     */
    OFFER("offer"),
    /**
     * Payment channel account object type.
     */
    PAYMENT_CHANNEL("payment_channel"),
    /**
     * Signer list account object type.
     */
    SIGNER_LIST("signer_list"),
    /**
     * Ticket account object type.
     */
    TICKET("ticket"),
    /**
     * State account object type.
     */
    STATE("state");

    private final String value;

    AccountObjectType(String value) {
      this.value = value;
    }

    /**
     * Value string.
     *
     * @return the string
     */
    @JsonValue
    public String value() {
      return value;
    }
  }
}
