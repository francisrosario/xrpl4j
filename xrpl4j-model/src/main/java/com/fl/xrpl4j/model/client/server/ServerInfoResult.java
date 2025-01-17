package com.fl.xrpl4j.model.client.server;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Immutable;
import com.fl.xrpl4j.model.client.XrplResult;

/**
 * Result of a "server_info" rippled API method request.
 */
@Immutable
@JsonSerialize(as = ImmutableServerInfoResult.class)
@JsonDeserialize(as = ImmutableServerInfoResult.class)
public interface ServerInfoResult extends XrplResult {

  static ImmutableServerInfoResult.Builder builder() {
    return ImmutableServerInfoResult.builder();
  }

  /**
   * Information about the requested server.
   *
   * @return A {@link ServerInfo}.
   */
  ServerInfo info();

}
