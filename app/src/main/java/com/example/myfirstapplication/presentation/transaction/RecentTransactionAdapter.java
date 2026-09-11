package com.example.myfirstapplication.presentation.transaction;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.TransactionData;
import com.example.myfirstapplication.databinding.ItemRecentTransactionBinding;

import java.util.List;
import java.util.Locale;

public class RecentTransactionAdapter
        extends RecyclerView.Adapter<RecentTransactionAdapter.ViewHolder> {

    private List<TransactionData> transactions;

    public RecentTransactionAdapter(List<TransactionData> transactions) {
        this.transactions = transactions;
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        ItemRecentTransactionBinding binding =
                ItemRecentTransactionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new ViewHolder(binding);
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        TransactionData transaction = transactions.get(position);

        // Mask card number
        String cardNumber = transaction.getCardNumber();
        holder.binding.tvCardNumber.setText(
                maskCardNumber(cardNumber)
        );

        // Amount
        double amount = Double.parseDouble(transaction.getAmount());

        holder.binding.tvTransactionAmount.setText(
                String.format(Locale.US, "Rs. %,.2f", amount)
        );

        // Date
        holder.binding.tvTransactionDate.setText(
                transaction.getDatetime()
        );
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateTransactions(List<TransactionData> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    private String maskCardNumber(String cardNumber) {

        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }

        return "**** **** **** " +
                cardNumber.substring(cardNumber.length() - 4);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemRecentTransactionBinding binding;

        ViewHolder(ItemRecentTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}