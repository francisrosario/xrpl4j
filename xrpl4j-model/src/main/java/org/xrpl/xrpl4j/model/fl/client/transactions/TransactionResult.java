package org.xrpl.xrpl4j.model.fl.client.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.xrpl.xrpl4j.model.fl.client.XrplResult;
import org.xrpl.xrpl4j.model.fl.client.common.LedgerIndex;
import org.xrpl.xrpl4j.model.fl.jackson.modules.TransactionResultDeserializer;
import org.xrpl.xrpl4j.model.fl.transactions.Transaction;
import org.xrpl.xrpl4j.model.fl.transactions.TransactionMetadata;

import java.util.Optional;

/**
 * The result of a tx rippled API method call.
 *
 * @param <TxnType> The type of {@link Transaction} that was requested.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableTransactionResult.class)
@JsonDeserialize(using = TransactionResultDeserializer.class)
public interface TransactionResult<TxnType extends Transaction> extends XrplResult {

  static <T extends Transaction> ImmutableTransactionResult.Builder<T> builder() {
    return ImmutableTransactionResult.builder();
  }

  /**
   * The {@link Transaction} that was returned as a result of the "tx" call.
   *
   * @return A {@link Transaction} of type {@link TxnType}.
   */
  @JsonUnwrapped
  TxnType transaction();

  /**
   * The ledger index of the ledger that includes this {@link Transaction}.
   *
   * @return An optionally-present {@link LedgerIndex}.
   */
  @JsonProperty("ledger_index")
  Optional<LedgerIndex> ledgerIndex();

  /**
   * {@code true} if this data is from a validated ledger version; If {@code false}, this data is not final.
   *
   * @return {@code true} if this data is from a validated ledger version; If {@code false}, this data is not final.
   */
  @Value.Default
  default boolean validated() {
    return false;
  }

  /**
   * Metadata about the transaction if this data is from a validated ledger version.
   *
   * @return metadata or empty for non-validated transactions.
   */
  @JsonProperty("meta")
  Optional<TransactionMetadata> metadata();

}
