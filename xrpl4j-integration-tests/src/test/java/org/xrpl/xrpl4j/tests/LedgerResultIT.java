package org.xrpl.xrpl4j.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.xrpl.xrpl4j.client.JsonRpcClientErrorException;
import com.fl.xrpl4j.model.client.common.LedgerIndex;
import com.fl.xrpl4j.model.client.ledger.LedgerRequestParams;
import com.fl.xrpl4j.model.client.ledger.LedgerResult;

/**
 * These tests ensure {@link LedgerResult}s can be constructed from all of the different JSON responses
 * rippled sends back.
 */
public class LedgerResultIT extends AbstractIT {

  @Test
  void getValidatedLedgerResult() throws JsonRpcClientErrorException {
    final LedgerResult ledgerResult = xrplClient.ledger(LedgerRequestParams.builder()
      .ledgerIndex(LedgerIndex.VALIDATED)
      .build());
    assertThat(ledgerResult.ledgerIndex()).isNotEmpty();
    assertThat(ledgerResult.ledgerHash()).isNotEmpty();
    assertThat(ledgerResult.ledgerCurrentIndex()).isEmpty();
    assertThat(ledgerResult.ledger().closeTimeHuman()).isNotEmpty();
    assertThat(ledgerResult.ledger().parentCloseTime()).isNotEmpty();
  }

  @Test
  void getCurrentLedgerResult() throws JsonRpcClientErrorException {
    final LedgerResult ledgerResult = xrplClient.ledger(LedgerRequestParams.builder()
      .ledgerIndex(LedgerIndex.CURRENT)
      .build());
    assertThat(ledgerResult.ledgerIndex()).isEmpty();
    assertThat(ledgerResult.ledgerHash()).isEmpty();
    assertThat(ledgerResult.ledgerCurrentIndex()).isNotEmpty();
    assertThat(ledgerResult.ledger().closeTimeHuman()).isEmpty();
    assertThat(ledgerResult.ledger().parentCloseTime()).isEmpty();
  }

  @Test
  void getClosedLedgerResult() throws JsonRpcClientErrorException {
    final LedgerResult ledgerResult = xrplClient.ledger(LedgerRequestParams.builder()
      .ledgerIndex(LedgerIndex.CLOSED)
      .build());
    assertThat(ledgerResult.ledgerIndex()).isNotEmpty();
    assertThat(ledgerResult.ledgerHash()).isNotEmpty();
    assertThat(ledgerResult.ledgerCurrentIndex()).isEmpty();
    assertThat(ledgerResult.ledger().closeTimeHuman()).isNotEmpty();
    assertThat(ledgerResult.ledger().parentCloseTime()).isNotEmpty();
  }
}
