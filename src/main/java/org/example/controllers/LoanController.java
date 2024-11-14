package org.example.controllers;

import org.example.entities.Loan;
import org.example.entities.PaymentPlan;
import org.example.services.LoanServices;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/loan")
public class LoanController {

    LoanServices loanServices = new LoanServices();

    @POST
    @Path("/apply")
    @Produces(MediaType.APPLICATION_JSON)
    public void createLoan(Loan loan){
        loanServices.createLoan(loan);
    }

    @POST
    @Path("/register-payment")
    @Produces(MediaType.APPLICATION_JSON)
    public void registerPayment(PaymentPlan paymentPlan){
        loanServices.registerInstallment(paymentPlan);
    }

    @POST
    @Path("/view-details/payment-plan")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PaymentPlan> viewPaymentPlan(String loanNo){
        return null;
    }
}
