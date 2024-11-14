package org.example.dao;

import org.example.entities.Client;
import org.example.exception.DataNotFoundException;
import org.example.exception.RequestNotAuthroizedException;
import org.example.exception.RequsetArgumentsNotFoundException;
import org.example.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClientDAO {
    Logger logger = LoggerFactory.getLogger(ClientDAO.class);
    private static ClientDAO clientDAO;

    private ClientDAO() {
    }

    public static ClientDAO getClientDAO() {
        if (clientDAO == null) {
            clientDAO = new ClientDAO();
        }
        return clientDAO;
    }


    public void saveClient(Client client) {
        logger.info("Starting .saveClient(Client) to save a client to database");
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        session.save(client);
        transaction.commit();
        logger.info("Client saved");

    }

    public Client getClient(int id) {
        logger.info("starting .getClient(int) to get a client by id");
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Client client = session.get(Client.class, id);
            if (client == null) {
                logger.info("user not found with id {}", id);
                throw new DataNotFoundException("user not found with id " + id);
            }
            return client;

        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateClient(Client client) {
        logger.info("stating .updateClient(Client) to update the client with new client attributes provided via client ");
        Transaction transaction;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            if (client.getUserId() > 0) {
                session.update(client);
            } else {
                if (transaction == null) {
                    transaction.rollback();
                }
                throw new RequsetArgumentsNotFoundException("user id not available in the request");
            }
            transaction.commit();
        } catch (RequsetArgumentsNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteClient(int id) {
        logger.info("Staring .deleteClient(int) to delete a client by id");
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Client client = session.get(Client.class, id);
            if (client != null) {
                session.delete(client);
            } else throw new DataNotFoundException("user doesnt exist");
            transaction.commit();
        } catch (DataNotFoundException e) {
            throw new RuntimeException();
        }
    }

    public List<Client> getClients(int id) {
        logger.info("starting .getClients to get all the clients the is available in the database");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (LogDAO.getLogDAO().logStatus(id)) {
                return session.createQuery("from Client", Client.class).list();
            } else throw new RequestNotAuthroizedException("user not authorized to view this data");
        } catch (RequestNotAuthroizedException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearClientDAO() {
        logger.info("starting .clearClientDAO to clear ClientDAO object instance");
        clientDAO = null;
        logger.info("clientDAO cleared!");
    }
}

