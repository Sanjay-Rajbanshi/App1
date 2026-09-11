package com.example.myfirstapplication.presentation.transaction;

import com.example.myapplication2.TransactionData;

public interface OnTransactionActionListener {

    void onDeleteTransaction(int transactionId);

    void onTransactionClick(TransactionData transaction);
}