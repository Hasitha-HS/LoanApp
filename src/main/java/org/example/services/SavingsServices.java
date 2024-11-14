package org.example.services;

import org.example.Utilities.AccountNumberGenerator;
import org.example.dao.ClientDAO;
import org.example.dao.SavingsDAO;
import org.example.entities.Client;
import org.example.entities.SavingsAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SavingsServices {

    Logger logger = LoggerFactory.getLogger(SavingsServices.class);

    public void getNewAccNumber(int id) {
        logger.info("Generating new savings account number ");
        Client client = ClientDAO.getClientDAO().getClient(id);
        if (client!=null&& !SavingsDAO.getSavingsDAO().clientHaveSavingsAcc(client)) {

            String accNO = AccountNumberGenerator.generateAccountNumber();
            while (SavingsDAO.getSavingsDAO().generatedAccountNumberExists(accNO))
            {
                accNO = AccountNumberGenerator.generateAccountNumber();
            }
            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.setAccountNumber(accNO);
            savingsAccount.setClient(ClientDAO.getClientDAO().getClient(id));
            logger.info("Creating new savings account with account number {}",accNO);
            SavingsDAO.getSavingsDAO().saveSavingsAccount(savingsAccount);
        }
    }

}
