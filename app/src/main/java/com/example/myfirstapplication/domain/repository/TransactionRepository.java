package com.example.myfirstapplication.domain.repository;

import android.os.RemoteException;

import com.example.myapplication2.IPaymentService;
import com.example.myapplication2.TransactionData;

import java.util.List;

public class TransactionRepository {
    private IPaymentService paymentService;

    public void setPaymentService(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }
    public List<TransactionData> getTransactions() throws Exception{
        if(paymentService == null){
            throw new IllegalStateException("Payment service is not connected");
        }
        return paymentService.getTransactions();
    }
    public void deleteTransaction(int transactionId)
        throws RemoteException{
            if(paymentService == null){
                throw new IllegalStateException("Payment service is not connected.");

            }
        paymentService.deleteTransaction(transactionId);
        }



}
