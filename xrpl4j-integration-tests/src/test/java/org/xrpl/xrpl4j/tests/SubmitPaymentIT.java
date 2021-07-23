package org.xrpl.xrpl4j.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.xrpl.xrpl4j.client.JsonRpcClientErrorException;
import org.xrpl.xrpl4j.model.fl.client.accounts.AccountInfoResult;
import org.xrpl.xrpl4j.model.fl.client.fees.FeeResult;
import org.xrpl.xrpl4j.model.fl.client.ledger.LedgerRequestParams;
import org.xrpl.xrpl4j.model.fl.client.ledger.LedgerResult;
import org.xrpl.xrpl4j.model.fl.client.transactions.SubmitResult;
import org.xrpl.xrpl4j.model.fl.client.transactions.TransactionResult;
import org.xrpl.xrpl4j.model.fl.transactions.Payment;
import org.xrpl.xrpl4j.model.fl.transactions.XrpCurrencyAmount;
import org.xrpl.xrpl4j.wallet.Wallet;

public class SubmitPaymentIT extends AbstractIT {

  public static final String SUCCESS_STATUS = "tesSUCCESS";

  @Test
  public void sendPayment() throws JsonRpcClientErrorException {
    Wallet sourceWallet = createRandomAccount();
    Wallet destinationWallet = createRandomAccount();

    FeeResult feeResult = xrplClient.fee();
    AccountInfoResult accountInfo = this.scanForResult(() -> this.getValidatedAccountInfo(sourceWallet.classicAddress()));
    XrpCurrencyAmount amount = XrpCurrencyAmount.ofDrops(12345);
    Payment payment = Payment.builder()
        .account(sourceWallet.classicAddress())
        .fee(feeResult.drops().openLedgerFee())
        .sequence(accountInfo.accountData().sequence())
        .destination(destinationWallet.classicAddress())
        .amount(amount)
        .signingPublicKey(sourceWallet.publicKey())
        .build();

    SubmitResult<Payment> result = xrplClient.submit(sourceWallet, payment);
    assertThat(result.engineResult()).isNotEmpty().get().isEqualTo(SUCCESS_STATUS);
    logger.info("Payment successful: https://testnet.xrpl.org/transactions/" +
      result.transactionResult().transaction().hash()
        .orElseThrow(() -> new RuntimeException("Result didn't have hash."))
    );

    TransactionResult<Payment> validatedPayment = this.scanForResult(
        () -> this.getValidatedTransaction(
            result.transactionResult().transaction().hash()
              .orElseThrow(() -> new RuntimeException("Result didn't have hash.")),
            Payment.class)
    );

    assertThat(validatedPayment.metadata().get().deliveredAmount()).hasValue(amount);
    assertThat(validatedPayment.metadata().get().transactionResult()).isEqualTo(SUCCESS_STATUS);

    assertPaymentCloseTimeMatchesLedgerCloseTime(validatedPayment);
  }

  @Test
  public void sendPaymentFromSecp256k1Wallet() throws JsonRpcClientErrorException {
    Wallet senderWallet = walletFactory.fromSeed("sp5fghtJtpUorTwvof1NpDXAzNwf5", true);
    logger.info("Generated source testnet wallet with address " + senderWallet.xAddress());

    fundAccount(senderWallet);

    Wallet destinationWallet = createRandomAccount();

    FeeResult feeResult = xrplClient.fee();
    AccountInfoResult accountInfo = this.scanForResult(() -> this.getValidatedAccountInfo(senderWallet.classicAddress()));

    Payment payment = Payment.builder()
        .account(senderWallet.classicAddress())
        .fee(feeResult.drops().openLedgerFee())
        .sequence(accountInfo.accountData().sequence())
        .destination(destinationWallet.classicAddress())
        .amount(XrpCurrencyAmount.ofDrops(12345))
        .signingPublicKey(senderWallet.publicKey())
        .build();

    SubmitResult<Payment> result = xrplClient.submit(senderWallet, payment);
    assertThat(result.engineResult()).isNotEmpty().get().isEqualTo("tesSUCCESS");
    logger.info("Payment successful: https://testnet.xrpl.org/transactions/" +
      result.transactionResult().transaction().hash()
        .orElseThrow(() -> new RuntimeException("Result didn't have hash."))
    );

    this.scanForResult(
        () -> this.getValidatedTransaction(
            result.transactionResult().transaction().hash()
              .orElseThrow(() -> new RuntimeException("Result didn't have hash.")),
            Payment.class)
    );
  }

  private void assertPaymentCloseTimeMatchesLedgerCloseTime(TransactionResult<Payment> validatedPayment)
      throws JsonRpcClientErrorException {
    LedgerResult ledger = xrplClient.ledger(
        LedgerRequestParams.builder().ledgerIndex(validatedPayment.ledgerIndex().get()).build());

    assertThat(validatedPayment.transaction().closeDateHuman()).isNotEmpty();
    assertThat(ledger.ledger().closeTimeHuman()).isNotEmpty();
    assertThat(validatedPayment.transaction().closeDateHuman().get())
        .isEqualTo(ledger.ledger().closeTimeHuman().get());
  }

}
