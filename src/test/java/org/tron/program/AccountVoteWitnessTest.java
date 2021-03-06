package org.tron.program;

import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tron.core.Constant;
import org.tron.core.capsule.AccountCapsule;
import org.tron.core.capsule.WitnessCapsule;
import org.tron.core.config.Configuration;
import org.tron.core.config.args.Args;
import org.tron.core.db.Manager;
import org.tron.protos.Protocol.AccountType;

public class AccountVoteWitnessTest {

  private static final Logger logger = LoggerFactory.getLogger("Test");
  private static Manager dbManager = new Manager();

  @BeforeClass
  public static void initConf() {
    //Args.setParam(new String[]{}, Configuration.getByPath(Constant.TEST_CONF));
    Args.setParam(new String[]{"-d", "output_witness"},
        Configuration.getByPath(Constant.TEST_CONF));
    dbManager.init();
  }

  @Test
  public void testAccountVoteWitness() {
    List<AccountCapsule> accountCapsuleList = getAccountList();
    List<WitnessCapsule> witnessCapsuleList = getWitnessList();
    accountCapsuleList.forEach(accountCapsule -> {
          dbManager.getAccountStore().put(accountCapsule.getAddress().toByteArray(), accountCapsule);
          printAccount(accountCapsule.getAddress());
        }
    );
    witnessCapsuleList.forEach(witnessCapsule ->
        dbManager.getWitnessStore().putWitness(witnessCapsule)
    );
    dbManager.updateWitness();
    printWitness(ByteString.copyFrom("00000000001".getBytes()));
    printWitness(ByteString.copyFrom("00000000002".getBytes()));
    printWitness(ByteString.copyFrom("00000000003".getBytes()));
    printWitness(ByteString.copyFrom("00000000004".getBytes()));
    printWitness(ByteString.copyFrom("00000000005".getBytes()));
    printWitness(ByteString.copyFrom("00000000006".getBytes()));
    printWitness(ByteString.copyFrom("00000000007".getBytes()));

  }

  private void printAccount(ByteString address) {
    AccountCapsule accountCapsule = dbManager.getAccountStore().get(address.toByteArray());
    if (null == accountCapsule) {
      logger.info("address is {}  , account is null", address.toStringUtf8());
      return;
    }
    logger.info("address is {}  ,countVoteSize is {}", accountCapsule.getAddress().toStringUtf8(),
        accountCapsule.getVotesList().size());
  }

  private void printWitness(ByteString address) {
    WitnessCapsule witnessCapsule = dbManager.getWitnessStore().getWitness(address);
    if (null == witnessCapsule) {
      logger.info("address is {}  , winess is null", address.toStringUtf8());
      return;
    }
    logger.info("address is {}  ,countVote is {}", witnessCapsule.getAddress().toStringUtf8(),
        witnessCapsule.getVoteCount());
  }

  private List<AccountCapsule> getAccountList() {
    List<AccountCapsule> accountCapsuleList = Lists.newArrayList();
    AccountCapsule accountTron = new AccountCapsule(
        ByteString.copyFrom("00000000001".getBytes()), ByteString.copyFromUtf8("Tron"),
        AccountType.Normal);
    AccountCapsule accountMarcus = new AccountCapsule(
        ByteString.copyFrom("00000000002".getBytes()), ByteString.copyFromUtf8("Marcus"),
        AccountType.Normal);
    AccountCapsule accountOlivier = new AccountCapsule(
        ByteString.copyFrom("00000000003".getBytes()), ByteString.copyFromUtf8("Olivier"),
        AccountType.Normal);
    AccountCapsule accountSasaXie = new AccountCapsule(
        ByteString.copyFrom("00000000004".getBytes()), ByteString.copyFromUtf8("SasaXie"),
        AccountType.Normal);
    AccountCapsule accountVivider = new AccountCapsule(
        ByteString.copyFrom("00000000005".getBytes()), ByteString.copyFromUtf8("Vivider"),
        AccountType.Normal);
    //accountTron addVotes
    accountTron.addVotes(accountMarcus.getAddress(), 100);
    accountTron.addVotes(accountOlivier.getAddress(), 100);
    accountTron.addVotes(accountSasaXie.getAddress(), 100);
    accountTron.addVotes(accountVivider.getAddress(), 100);

    //accountMarcus addVotes
    accountMarcus.addVotes(accountTron.getAddress(), 100);
    accountMarcus.addVotes(accountOlivier.getAddress(), 100);
    accountMarcus.addVotes(accountSasaXie.getAddress(), 100);
    accountMarcus.addVotes(ByteString.copyFrom("00000000006".getBytes()), 100);
    accountMarcus.addVotes(ByteString.copyFrom("00000000007".getBytes()), 100);
    //accountOlivier addVotes
    accountOlivier.addVotes(accountTron.getAddress(), 100);
    accountOlivier.addVotes(accountMarcus.getAddress(), 100);
    accountOlivier.addVotes(accountSasaXie.getAddress(), 100);
    accountOlivier.addVotes(accountVivider.getAddress(), 100);
    //accountSasaXie addVotes
    //accountVivider addVotes
    accountCapsuleList.add(accountTron);
    accountCapsuleList.add(accountMarcus);
    accountCapsuleList.add(accountOlivier);
    accountCapsuleList.add(accountSasaXie);
    accountCapsuleList.add(accountVivider);
    return accountCapsuleList;
  }

  private List<WitnessCapsule> getWitnessList() {
    List<WitnessCapsule> witnessCapsuleList = Lists.newArrayList();
    WitnessCapsule witnessTron = new WitnessCapsule(
        ByteString.copyFrom("00000000001".getBytes()), 0);
    WitnessCapsule witnessOlivier = new WitnessCapsule(
        ByteString.copyFrom("00000000003".getBytes()), 100);
    WitnessCapsule witnessVivider = new WitnessCapsule(
        ByteString.copyFrom("00000000005".getBytes()), 200);
    WitnessCapsule witnessSenaLiu = new WitnessCapsule(
        ByteString.copyFrom("00000000006".getBytes()), 300);
    witnessCapsuleList.add(witnessTron);
    witnessCapsuleList.add(witnessOlivier);
    witnessCapsuleList.add(witnessVivider);
    witnessCapsuleList.add(witnessSenaLiu);
    return witnessCapsuleList;
  }
}