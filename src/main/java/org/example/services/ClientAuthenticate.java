package org.example.services;

import org.example.dao.ClientDAO;
import org.example.dao.LogDAO;
import org.example.entities.Client;
import org.example.entities.LogRecord;
import org.example.exception.DataNotFoundException;

import javax.security.auth.login.CredentialException;
import java.util.Objects;

public class ClientAuthenticate {
    private static ClientAuthenticate clientAuthenticate;

    private ClientAuthenticate() {
    }

    public static ClientAuthenticate getClientAuthenticate() {
        if (clientAuthenticate == null)
            return new ClientAuthenticate();
        else
            return clientAuthenticate;
    }

    public void validateUser(int id, String password) throws DataNotFoundException, CredentialException {
        LogRecord logRecord = new LogRecord();
        Client client = ClientDAO.getClientDAO().getClient(id);
        if (Objects.equals(client.getPassword(), password)) {
            logRecord.setClient(client);
            logRecord.setLogStatus(true);
            LogDAO.getLogDAO().saveLog(logRecord);
        } else throw new CredentialException("Wrong username or password");


    }


}
