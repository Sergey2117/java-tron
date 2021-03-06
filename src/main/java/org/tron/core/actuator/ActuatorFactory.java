package org.tron.core.actuator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tron.core.capsule.TransactionCapsule;
import org.tron.core.db.Manager;
import org.tron.protos.Protocol;
import org.tron.protos.Protocol.Transaction.Contract;
import org.tron.protos.Protocol.Transaction.TransactionType;

public class ActuatorFactory {

  private static final Logger logger = LoggerFactory.getLogger("ActuatorFactory");
  public static final ActuatorFactory INSTANCE = new ActuatorFactory();

  private ActuatorFactory() {
  }

  public static ActuatorFactory getInstance() {
    return INSTANCE;
  }

  public static List<Actuator> createActuator(TransactionCapsule transactionCapsule,
      Manager manager) {
    List<Actuator> actuatorList = Lists.newArrayList();
    if (null == transactionCapsule || null == transactionCapsule.getInstance()) {
      logger.info("transactionCapsule or Transaction is null");
      return actuatorList;
    }
//    if (null == manager) {
//      logger.info("manager is null");
//      return actuatorList;
//    }
    Preconditions.checkNotNull(manager, "manager is null");
    Protocol.Transaction.raw rawData = transactionCapsule.getInstance().getRawData();
    if (TransactionType.ContractType.equals(rawData.getType())) {
      rawData.getContractList()
          .forEach(contract -> actuatorList.add(getActuatorByContract(contract, manager)));
    }
    return actuatorList;
  }

  private static Actuator getActuatorByContract(Contract contract, Manager manager) {
    switch (contract.getType()) {
      case AccountCreateContract:
        return new CreateAccountActuator(contract.getParameter(), manager);
      case TransferContract:
        return new TransferActuator(contract.getParameter(), manager);
      case TransferAssertContract:
        break;
      case VoteAssetContract:
        break;
      case VoteWitnessContract:
        return new VoteWitnessActuator(contract.getParameter(), manager);
      case WitnessCreateContract:
        return new WitnessCreateActuator(contract.getParameter(), manager);
      case AssetIssueContract:
        break;
      case DeployContract:
        break;
      default:

    }
    return null;
  }

}
