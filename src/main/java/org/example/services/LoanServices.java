package org.example.services;


import org.example.Utilities.AccountNumberGenerator;
import org.example.dao.LoanDAO;
import org.example.dao.LoanTypeDAO;
import org.example.entities.Loan;
import org.example.entities.LoanType;
import org.example.entities.PaymentPlan;
import org.example.exception.RequsetArgumentsNotFoundException;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoanServices {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoanServices.class);
    static Logger logger = LoggerFactory.getLogger(LoanServices.class);
    Session session = HibernateUtil.getSessionFactory().openSession();
    Loan loan = new Loan();
    LoanType loanType = new LoanType();
    Transaction transaction;

    public boolean loanExists(int id, String type) {
        logger.info("checking if user have a loan of type {}", type);
        try {
            loanType = LoanTypeDAO.getLoanTypeDAO().getLoanType(id);
            if (loanType != null && type.equalsIgnoreCase("car")) {
                return ifNull(loanType.getCarLoan());
            } else if (loanType != null && type.equalsIgnoreCase("jewellery")) {
                return ifNull(loanType.getJewelleryLoan());
            } else if (loanType != null && type.equalsIgnoreCase("education"))
                return ifNull(loanType.getEducationLoan());
            else throw new RequsetArgumentsNotFoundException("Either USER_ID or TYPE OF LOAN is incorrectly inputted");
        } catch (RequsetArgumentsNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean ifNull(Boolean bool) {
        if (bool == null) bool = false;
        return bool;
    }

    public void createLoan(Loan loan) {
        //checks if there's a loan to the specific user and the loan type
        if (!loanExists(loan.getClient().getUserId(), loan.getTypeLoan())) {
            logger.info("Eligible");
            loan.setLoanNumber(AccountNumberGenerator.generateLoanId());
            LoanDAO.getLoanDAO().saveLoan(loan);
            switch (loan.getTypeLoan()) {
                case "car":
                    loanType.setCarLoan(true);
                    break;
                case "education":
                    loanType.setEducationLoan(true);
                    break;
                case "jewellery":
                    loanType.setJewelleryLoan(true);
                    break;
                default:
                    log.info("Wrong loan type");
            }
            logger.info("saving loanType changes");
            LoanTypeDAO.getLoanTypeDAO().saveType(loanType);
        }
    }

    public void registerInstallment(PaymentPlan paymentPlan) {
        logger.info("Registering payment plan");
        paymentPlan.setInstallmentId(AccountNumberGenerator.generatePaymentId());
        transaction = session.beginTransaction();
        session.save(paymentPlan);
        transaction.commit();
    }


}
