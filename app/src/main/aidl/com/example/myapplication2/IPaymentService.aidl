// IPaymentService.aidl
package com.example.myapplication2;
import com.example.myapplication2.TransactionData;

interface IPaymentService {
 void processPayment(
        double amount,
        String cardNo,
        String cardHolderName,
        String cvv,
        String expiryDate,
        String remarks
    );

List<TransactionData> getTransactions();
void deleteTransaction(int transactionId);

}