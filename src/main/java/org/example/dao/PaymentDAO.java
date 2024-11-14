package org.example.dao;

import org.example.entities.PaymentPlan;
import org.example.exception.DataNotFoundException;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PaymentDAO {

    private static final Logger logger = LoggerFactory.getLogger(PaymentDAO.class);
    private static PaymentDAO paymentDAO;

    private PaymentDAO() {}

    public static PaymentDAO getPaymentDAO() {
        if (paymentDAO == null) {
            paymentDAO = new PaymentDAO();
        }
        return paymentDAO;
    }

    public void savePaymentRecord(PaymentPlan paymentPlan) {
        logger.info("starting .savePaymentRecord Saving payment record ");
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(paymentPlan);
            transaction.commit();
            logger.info("Payment record saved successfully");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save payment record", e);
        }
    }

    public PaymentPlan getPaymentRecord(int id) {
        logger.info("starting .getPaymentRecord to Fetch payment record with id");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            PaymentPlan paymentPlan = session.get(PaymentPlan.class, id);
            if (paymentPlan == null) {
                throw new DataNotFoundException("Payment record not found for id " + id);
            }
            return paymentPlan;
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePaymentRecord(PaymentPlan paymentPlan) {
        logger.info("staring .updatePaymentRecord to Update payment record for plan}");
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(paymentPlan);
            transaction.commit();
            logger.info("Payment record updated successfully");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update payment record", e);
        }
    }

    public List<PaymentPlan> getPayRecords() {
        logger.info("starting .getPayRecords to get all payment records");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PaymentPlan", PaymentPlan.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching payment records", e);
        }
    }
}
