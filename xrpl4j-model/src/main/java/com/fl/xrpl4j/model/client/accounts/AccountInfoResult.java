package com.fl.xrpl4j.model.client.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fl.xrpl4j.model.ledger.AccountRootObject;
import org.immutables.value.Value;
import com.fl.xrpl4j.model.client.XrplResult;
import com.fl.xrpl4j.model.client.common.LedgerIndex;

import java.util.Optional;

/**
 * The result of an "account_info" rippled API call, containing information about a given account on the XRP Ledger.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableAccountInfoResult.class)
@JsonDeserialize(as = ImmutableAccountInfoResult.class)
public interface AccountInfoResult extends XrplResult {

  static ImmutableAccountInfoResult.Builder builder() {
    return ImmutableAccountInfoResult.builder();
  }

  /**
   * The {@link AccountRootObject} ledger object with this account's information, as stored in the ledger.
   *
   * @return The returned {@link AccountRootObject}.
   */
  @JsonProperty("account_data")
  AccountRootObject accountData();

  /**
   * (Omitted if ledger_index is provided instead) The ledger index of the current in-progress ledger,
   * which was used when retrieving this information.
   *
   * @return An optionally-present {@link LedgerIndex}.
   */
  @JsonProperty("ledger_current_index")
  Optional<LedgerIndex> ledgerCurrentIndex();

  /**
   * (Omitted if ledger_current_index is provided instead) The ledger index of the ledger version used when
   * retrieving this information. The information does not contain any changes from ledger versions newer than this one.
   *
   * @return An optionally-present {@link LedgerIndex}.
   */
  @JsonProperty("ledger_index")
  Optional<LedgerIndex> ledgerIndex();

  /**
   * (Omitted unless queue specified as true and querying the current open ledger.)
   * Information about queued transactions sent by this account. This information describes the state of
   * the local rippled server, which may be different from other servers in the peer-to-peer XRP Ledger network.
   * Some fields may be omitted because the values are calculated "lazily" by the queuing mechanism.
   *
   * @return An optionally-present {@link QueueData}.
   */
  @JsonProperty("queue_data")
  Optional<QueueData> queueData();

  /**
   * True if this data is from a validated ledger version; if false, this data is not final.
   *
   * @return {@code true} if this data is from a validated ledger version, otherwise {@code false}.
   */
  @Value.Default
  default boolean validated() {
    return false;
  }

}
