package com.example.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TransactionViewModelFactory implements ViewModelProvider.Factory {
    private final TransactionRepository transactionRepository;

    public TransactionViewModelFactory(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull  Class<T> modelClass){
        if(modelClass.isAssignableFrom(TransactionViewModel.class)){
            return (T) new TransactionViewModel(transactionRepository);
        }
        throw new IllegalStateException("Unknown view model clss");
    }

}
