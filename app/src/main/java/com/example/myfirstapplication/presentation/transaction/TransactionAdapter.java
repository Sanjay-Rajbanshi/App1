package com.example.myfirstapplication.presentation.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.TransactionData;
import com.example.myfirstapplication.R;

import java.util.ArrayList;
import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {


    //  private final Context context;

    private final List<TransactionData> transactionList;


    private final OnTransactionActionListener listener;


    public TransactionAdapter(
            Context context,
            List<TransactionData> transactionList,
            OnTransactionActionListener listener) {

        //  this.context = context;
        this.transactionList = new ArrayList<>(transactionList);
        this.listener = listener;

    }
//    public void setPaymentService(IPaymentService paymentService){
//        this.paymentService = paymentService;
//    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);

        return new TransactionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull TransactionViewHolder holder,
            int position) {

        TransactionData transaction =
                transactionList.get(position);

        holder.application.setText(getApplicationName(transaction.getApplication()));

        holder.transactionAmount.setText(
                "Amount: " + transaction.getAmount()
        );

        holder.itemView.setOnClickListener(v -> listener.onTransactionClick(transaction));

//        holder.itemView.setOnClickListener(v ->
//
//
//                showTransactionDialog(transaction)
//        );
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTransactionList(
            List<TransactionData> newList) {

        transactionList.clear();

        if (newList != null) {
            transactionList.addAll(newList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder
            extends RecyclerView.ViewHolder {

        TextView transactionAmount;
        TextView application;

        @SuppressLint("SetTextI18n")
        public TransactionViewHolder(
                @NonNull View itemView) {

            super(itemView);

            application = itemView.findViewById(R.id.tdApplication);

            transactionAmount =
                    itemView.findViewById(R.id.tdAmount);
        }


    }

    private String getApplicationName(String packageName) {
        if ("com.example.myfirstapplication".equals(packageName)) {
            return "App 1";
        }
        if ("com.example.myapplication2".equals(packageName)) {
            return "App 2";
        }
        return packageName;
    }
}