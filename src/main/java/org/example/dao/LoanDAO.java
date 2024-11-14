package org.example.dao;

import org.example.entities.Loan;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoanDAO {

    private static final Logger logger = LoggerFactory.getLogger(LoanDAO.class);
    private static LoanDAO loanDAO;

    private LoanDAO() {}

    public static LoanDAO getLoanDAO() {
        if (loanDAO == null) {
            loanDAO = new LoanDAO();
        }
        return loanDAO;
    }

    public void saveLoan(Loan loan) {
        logger.info("Starting .saveLoan to save a loan");
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(loan);
            transaction.commit();
            logger.info("Loan saved successfully");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save loan", e);
        }
    }

    public void clearLoanDAO() {
        logger.info("Clearing LoanDAO instance");
        loanDAO = null;
        logger.info("LoanDAO cleared");
    }
}
