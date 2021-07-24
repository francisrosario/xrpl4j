package org.xrpl.xrpl4j.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.primitives.UnsignedInteger;
import org.junit.jupiter.api.Test;
import org.xrpl.xrpl4j.client.JsonRpcClientErrorException;
import org.fl.xrpl4j.model.client.accounts.AccountInfoResult;
import org.fl.xrpl4j.model.client.fees.FeeResult;
import org.fl.xrpl4j.model.client.transactions.SubmitResult;
import org.fl.xrpl4j.model.transactions.AccountSet;
import org.fl.xrpl4j.model.transactions.SetRegularKey;
import org.xrpl.xrpl4j.wallet.Wallet;

public class SetRegularKeyIT extends AbstractIT {

  @Test
  void setRegularKeyOnAccount() throws JsonRpcClientErrorException {
    //////////////////////////
    // Create a random account
    Wallet wallet = createRandomAccount();

    //////////////////////////
    // Wait for the account to show up on ledger
    AccountInfoResult accountInfo = scanForResult(() -> getValidatedAccountInfo(wallet.classicAddress()));

    //////////////////////////
    // Generate a new wallet locally
    Wallet newWallet = walletFactory.randomWallet(true).wallet();

    //////////////////////////
    // Submit a SetRegularKey transaction with the new wallet's address so that we
    // can sign future transactions with the new wallet's keypair
    FeeResult feeResult = xrplClient.fee();
    SetRegularKey setRegularKey = SetRegularKey.builder()
        .account(wallet.classicAddress())
        .fee(feeResult.drops().openLedgerFee())
        .sequence(accountInfo.accountData().sequence())
        .regularKey(newWallet.classicAddress())
        .signingPublicKey(wallet.publicKey())
        .build();

    SubmitResult<SetRegularKey> setResult = xrplClient.submit(wallet, setRegularKey);
    assertThat(setResult.engineResult()).isNotEmpty().get().isEqualTo("tesSUCCESS");
    logger.info("SetRegularKey transaction successful. https://testnet.xrpl.org/transactions/{}",
        setResult.transactionResult().transaction().hash()
              .orElseThrow(() -> new RuntimeException("Result didn't have hash."))
    );

    //////////////////////////
    // Verify that the SetRegularKey transaction worked by submitting empty
    // AccountSet transactions, signed with the new wallet key pair, until
    // we get a successful response.
    scanForResult(
        () -> {
          AccountSet accountSet = AccountSet.builder()
              .account(wallet.classicAddress())
              .fee(feeResult.drops().openLedgerFee())
              .sequence(accountInfo.accountData().sequence().plus(UnsignedInteger.ONE))
              .signingPublicKey(newWallet.publicKey())
              .build();

          try {
            return xrplClient.submit(newWallet, accountSet);
          } catch (JsonRpcClientErrorException e) {
            throw new RuntimeException(e.getMessage(), e);
          }
        }
    );

  }

  @Test
  void removeRegularKeyFromAccount() throws JsonRpcClientErrorException {
    //////////////////////////
    // Create a random account
    Wallet wallet = createRandomAccount();

    //////////////////////////
    // Wait for the account to show up on ledger
    AccountInfoResult accountInfo = scanForResult(() -> getValidatedAccountInfo(wallet.classicAddress()));

    //////////////////////////
    // Generate a new wallet locally
    Wallet newWallet = walletFactory.randomWallet(true).wallet();

    //////////////////////////
    // Submit a SetRegularKey transaction with the new wallet's address so that we
    // can sign future transactions with the new wallet's keypair
    FeeResult feeResult = xrplClient.fee();
    SetRegularKey setRegularKey = SetRegularKey.builder()
        .account(wallet.classicAddress())
        .fee(feeResult.drops().openLedgerFee())
        .sequence(accountInfo.accountData().sequence())
        .regularKey(newWallet.classicAddress())
        .signingPublicKey(wallet.publicKey())
        .build();

    SubmitResult<SetRegularKey> setResult = xrplClient.submit(wallet, setRegularKey);
    assertThat(setResult.engineResult()).isNotEmpty().get().isEqualTo("tesSUCCESS");
    logger.info("SetRegularKey transaction successful. https://testnet.xrpl.org/transactions/{}",
        setResult.transactionResult().transaction().hash()
              .orElseThrow(() -> new RuntimeException("Result didn't have hash."))
    );

    //////////////////////////
    // Verify that the SetRegularKey transaction worked by submitting empty
    // AccountSet transactions, signed with the new wallet key pair, until
    // we get a successful response.
    scanForResult(
        () -> {
          AccountSet accountSet = AccountSet.builder()
              .account(wallet.classicAddress())
              .fee(feeResult.drops().openLedgerFee())
              .sequence(accountInfo.accountData().sequence().plus(UnsignedInteger.ONE))
              .signingPublicKey(newWallet.publicKey())
              .build();

          try {
            return xrplClient.submit(newWallet, accountSet);
          } catch (JsonRpcClientErrorException e) {
            throw new RuntimeException(e.getMessage(), e);
          }
        }
    );


    SetRegularKey removeRegularKey = SetRegularKey.builder()
        .account(wallet.classicAddress())
        .fee(feeResult.drops().openLedgerFee())
        .sequence(accountInfo.accountData().sequence().plus(UnsignedInteger.valueOf(2)))
        .signingPublicKey(wallet.publicKey())
        .build();

    SubmitResult<SetRegularKey> removeResult = xrplClient.submit(wallet, removeRegularKey);
    assertThat(removeResult.engineResult()).isNotEmpty().get().isEqualTo("tesSUCCESS");
    logger.info("SetRegularKey transaction successful. https://testnet.xrpl.org/transactions/{}",
        removeResult.transactionResult().transaction().hash()
              .orElseThrow(() -> new RuntimeException("Result didn't have hash."))
    );

    scanForResult(
        () -> getValidatedAccountInfo(wallet.classicAddress()),
        infoResult -> !infoResult.accountData().regularKey().isPresent()
    );
  }
}
