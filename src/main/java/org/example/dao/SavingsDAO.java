package org.example.dao;

import org.example.entities.Client;
import org.example.entities.LoanType;
import org.example.entities.SavingsAccount;
import org.example.exception.DataNotFoundException;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SavingsDAO {

    private static final Logger logger = LoggerFactory.getLogger(SavingsDAO.class);
    private static SavingsDAO savingsDAO;
    private SessionFactory sessionFactory;

    private SavingsDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static SavingsDAO getSavingsDAO() {
        if (savingsDAO == null) {
            savingsDAO = new SavingsDAO();
        }
        return savingsDAO;
    }

    public Boolean generatedAccountNumberExists(String accNo) {
        logger.info("staring .generatedAccountNumberExists to check if the account number exists");
        try (Session session = sessionFactory.openSession()) {
            String existsASavingAccQuery = "SELECT COUNT(sa) FROM SavingsAccount sa WHERE sa.accountNumber = :accNo";
            Query<Long> query = session.createQuery(existsASavingAccQuery, Long.class);
            query.setParameter("accNo", accNo);
            Long count = query.uniqueResult();
            return count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error checking account number existence", e);
        }
    }

    public Boolean clientHaveSavingsAcc(Client client) {
        logger.info("Stating .clientHaveSavingsAcc to check if client has a savings account");
        Session session = sessionFactory.openSession();
        try {
            String existsASavingAccQuery = "SELECT COUNT(sa) FROM SavingsAccount sa WHERE sa.client = :client";
            Query<Long> query = session.createQuery(existsASavingAccQuery, Long.class);
            query.setParameter("client", client);
            Long count = query.uniqueResult();
            if (count > 0) {
                logger.info("Sorry savings acc already exists!");
            }
            return count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error checking client's savings account", e);
        }
    }

    public SavingsAccount getSavingsAcc(int id) {
        logger.info("starting .getSavingsAcc fetch savings account ");
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            SavingsAccount savingsAccount = session.get(SavingsAccount.class, id);
            if (savingsAccount == null) {
                throw new DataNotFoundException("Savings account not found for id " + id);
            }
            return savingsAccount;
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveSavingsAccount(SavingsAccount savingsAccount) {
        logger.info("Starting .saveSavingAccount to save savings account ");
        Transaction transaction = null;
        LoanType loanType = new LoanType();
        Client client = savingsAccount.getClient();
        loanType.setClient(client);
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(savingsAccount);
            session.save(loanType);
            transaction.commit();
            logger.info("Savings account created successfully");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save savings account and loan type", e);
        }
    }
}
