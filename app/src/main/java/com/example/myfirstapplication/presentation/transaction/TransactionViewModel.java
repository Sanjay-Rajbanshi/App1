package com.example.myfirstapplication.presentation.transaction;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication2.TransactionData;
import com.example.myfirstapplication.domain.repository.TransactionRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    private TransactionRepository transactionRepository;
    private final MutableLiveData<List<TransactionData>> transactions = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public TransactionViewModel (TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }
    public LiveData<List<TransactionData>>getTransactions(){
        return transactions;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public void loadTransactions(){
        executorService.execute(()->{
            try {
                List<TransactionData> transactionDataList = transactionRepository.getTransactions();
                transactions.postValue(transactionDataList);
            }catch (Exception e){
                e.printStackTrace();

            }
        });
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void deleteTransaction(int transactionId){
        executorService.execute(()->{
            try {
                transactionRepository.deleteTransaction(transactionId);
                loadTransactions();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    @Override
    public void onCleared(){
        super.onCleared();
        executorService.shutdown();
    }
}
