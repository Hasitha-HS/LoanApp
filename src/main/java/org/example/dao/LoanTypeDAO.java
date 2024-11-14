package org.example.dao;

import org.example.entities.LoanType;
import org.example.exception.DataNotFoundException;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoanTypeDAO {

    private static final Logger logger = LoggerFactory.getLogger(LoanTypeDAO.class);
    private static LoanTypeDAO loanTypeDAO;

    private LoanTypeDAO() {}

    public static LoanTypeDAO getLoanTypeDAO() {
        if (loanTypeDAO == null) {
            loanTypeDAO = new LoanTypeDAO();
        }
        return loanTypeDAO;
    }

    public void saveType(LoanType loanType) {
        logger.info("Starting .saveType to save a loan type");
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(loanType);
            transaction.commit();
            logger.info("Loan type saved successfully");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save loan type", e);
        }
    }


    public LoanType getLoanType(int id) {
        logger.info("starting .getLoanType(int) to get a client by id");
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            LoanType loanType = session.get(LoanType.class, id);
            if (loanType == null)
                throw new DataNotFoundException("Loan not registered initially ");
            return loanType;

        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
