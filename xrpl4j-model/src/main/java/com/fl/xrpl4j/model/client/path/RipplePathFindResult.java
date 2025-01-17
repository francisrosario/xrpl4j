package com.fl.xrpl4j.model.client.path;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fl.xrpl4j.model.transactions.Address;
import com.fl.xrpl4j.model.transactions.Payment;
import org.immutables.value.Value;
import com.fl.xrpl4j.model.client.XrplResult;

import java.util.List;

/**
 * The result of a "ripple_path_find" rippled API method call.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableRipplePathFindResult.class)
@JsonDeserialize(as = ImmutableRipplePathFindResult.class)
public interface RipplePathFindResult extends XrplResult {

  static ImmutableRipplePathFindResult.Builder builder() {
    return ImmutableRipplePathFindResult.builder();
  }

  /**
   * A {@link List} of {@link PathAlternative}s with possible paths to take. If empty, then are are no paths
   * connecting the source and destination accounts.
   *
   * @return A {@link List} of {@link PathAlternative}s.
   */
  List<PathAlternative> alternatives();

  /**
   * Unique {@link Address} of the account that would receive a
   * {@link Payment} transaction.
   *
   * @return The unique {@link Address} of the destination account.
   */
  @JsonProperty("destination_account")
  Address destinationAccount();

  /**
   * {@link List} of {@link String}s representing the currencies that the {@link #destinationAccount()} accepts,
   * as 3-letter codes like "USD" or as 40-character hex like "015841551A748AD2C1F76FF6ECB0CCCD00000000".
   *
   * @return A {@link List} of {@link String}s of destination currency codes.
   */
  @JsonProperty("destination_currencies")
  List<String> destinationCurrencies();

}
