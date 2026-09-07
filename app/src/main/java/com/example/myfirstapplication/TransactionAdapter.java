package com.example.myfirstapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.TransactionData;

import java.util.ArrayList;
import java.util.List;

interface OnTransactionActionListener{
    void onDeleteTransaction(int transactionId);
    void onTransactionClick(TransactionData transaction);
}
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

    private String getApplicationName(String packageName){
        if("com.example.myfirstapplication".equals(packageName)){
            return "App 1";
        }
        if("com.example.myapplication2".equals(packageName)){
            return "App 2";
        }
        return packageName;
    }

//    @SuppressLint("SetTextI18n")
//    private void showTransactionDialog(
//            TransactionData transaction) {
//
//        View view = LayoutInflater.from(context)
//                .inflate(
//                        R.layout.activity_transaction_detailed,
//                        null
//                );
//        TextView application = view.findViewById(R.id.txtApplication);
//        TextView amount =
//                view.findViewById(R.id.txtDetailAmount);
//
//        TextView cardNo =
//                view.findViewById(R.id.txtDetailCardNo);
//
//        TextView cardHolderName =
//                view.findViewById(
//                        R.id.txtDetailCardHolderName
//                );
//
//        TextView cvv =
//                view.findViewById(R.id.txtCvv);
//
//        TextView expiry =
//                view.findViewById(R.id.txtExpiryDate);
//
//        TextView remarks =
//                view.findViewById(R.id.txtRemarks);
//
//        TextView date =
//                view.findViewById(R.id.txtTransactionDate);
//
//        TextView time =
//                view.findViewById(R.id.txtTransactionTime);
//
//        Button btnDelete =
//                view.findViewById(
//                        R.id.btnDeleteTransaction
//                );
//
//
//        application.setText("Data Source: " + getApplicationName(transaction.getApplication()));
//
//        amount.setText(
//                "Amount : " + transaction.getAmount()
//        );
//
//        cardNo.setText(
//                "Card No : " + transaction.getCardNo()
//        );
//
//        cardHolderName.setText(
//                "Name : " + transaction.getCardHolderName()
//        );
//
//        cvv.setText(
//                "CVV : " + transaction.getCvv()
//        );
//
//        expiry.setText(
//                "Expiry : " + transaction.getExpiryDate()
//        );
//
//        remarks.setText(
//                transaction.getRemarks()
//        );
//
//        String dateTime =
//                transaction.getDatetime();
//
//        if (dateTime != null &&
//                dateTime.contains(" ")) {
//
//            String[] parts =
//                    dateTime.split(" ");
//
//            date.setText(
//                    "Date: " + parts[0]
//            );
//
//            time.setText(
//                    "Time: " + parts[1]
//            );
//
//        } else {
//
//            date.setText(
//                    "Date: " +
//                            (dateTime != null
//                                    ? dateTime
//                                    : "N/A")
//            );
//
//            time.setText("Time: N/A");
//        }
//
//
//
//      AlertDialog detailDialog = new AlertDialog.Builder(context)
//                .setTitle("Transaction Detail")
//                .setView(view)
//                .setPositiveButton("Close", null)
//                .show();
//
//        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(context)
//                .setTitle("Delete Transaction")
//                .setMessage(
//                        "Are you sure you want to delete this transaction?"
//                )
//                .setNegativeButton(
//                        "Cancel",
//                        null
//                )
////                .setPositiveButton(
////                        "Delete",
////                        (dialog, which) -> deleteTransaction(
////                                transaction.getTid(),
////                                detailDialog
////                        )
////                )
//
//
//                .setPositiveButton("Delete", (dialog, which) ->
//                        listener.onDeleteTransaction(
//                                transaction.getTid()))
//                .show());
//
//        detailDialog.show();
//
//
//    }


}

