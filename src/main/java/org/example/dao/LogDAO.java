package org.example.dao;

import org.apache.log4j.Logger;
import org.example.entities.LogRecord;
import org.example.exception.DataNotFoundException;
import org.example.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

public class LogDAO {

    private static final Logger logger = Logger.getLogger(LogDAO.class);
    private static LogDAO logDAO;

    private LogDAO() {
    }

    public static LogDAO getLogDAO() {
        if (logDAO == null) {
            logDAO = new LogDAO();
        }
        return logDAO;
    }

    public void saveLog(LogRecord logRecord) {
        logger.info("Starting .saveLog to save a log record");
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(logRecord);
            transaction.commit();
            logger.info("Log saved successfully");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public void deleteLog(int id) {
        logger.info("Starting .deleteLog to delete a log by id");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            LogRecord logRecord = session.get(LogRecord.class, id);
            if (logRecord != null) {
                session.delete(logRecord);
                transaction.commit();
                logger.info("Log deleted successfully");
            } else {
                logger.info("Failed to delete log record");
                throw new DataNotFoundException("Log record not found");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Boolean logStatus(int id) {
        logger.info("Checking log status for id: " + id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LogRecord logRecord = session.get(LogRecord.class, id);
            if (logRecord != null) {
                logger.info(" user authenticated! ");
                return logRecord.getLogStatus();
            } else {
                throw new DataNotFoundException("Log record not found");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
