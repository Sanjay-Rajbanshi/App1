//package com.example.myfirstapplication;
//
//import android.annotation.SuppressLint;
//import android.os.Build;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.myapplication2.IPaymentService;
//import com.example.myapplication2.TransactionData;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.IBinder;
//import android.util.Log;
//
//public class TransactionDetailedActivity extends AppCompatActivity {
//
//    private TextView transactionDate;
//    private TextView transactionTime;
//    private TextView transactionAmount;
//    private TextView transactionCardNo;
//    private TextView transactionCardHolderName;
//    private TextView transactionCvv;
//    private TextView transactionExpiryDate;
//    private TextView transactionRemarks;
//    private TextView transactionApplication;
//    private IPaymentService paymentService;
//    private boolean isBound = false;
//
//
//    private  final ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            paymentService = IPaymentService.Stub.asInterface(iBinder);
//            isBound = true;
//            Log.d("AIDL", "Detailed activity connected to payment service");
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            paymentService = null;
//isBound = false;
//Log.d("AIDL", "Detailed activity is disconnected");
//        }
//    };
//
//
//    // bind the service
//    public void onStart(){
//        super.onStart();
//        Intent intent = new Intent("com.example.myapplication2.IPaymentService");
//        intent.setPackage("com.example.myapplication2");
//        bindService(intent,
//                serviceConnection,
//                BIND_AUTO_CREATE);
//    }
//
//    public void onStop(){
//        super.onStop();
//        if(isBound){
//            unbindService(serviceConnection);
//            isBound = false;
//            paymentService = null;
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d("DETAIL", "TransactionDetailedActivity CREATED");
//
//
//        setContentView(R.layout.activity_transaction_detailed);
//
//
//        TransactionData transaction;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//            transaction = getIntent().getParcelableExtra("transaction", TransactionData.class);
//
//        }else {
//            //noinspection deprecation
//            transaction = getIntent().getParcelableExtra("transaction");
//        }
//
//        if (transaction == null) {
//            finish();
//            return;
//        }
//
//
//        // Find views
//        transactionApplication = findViewById(R.id.txtApplication);
//
//        transactionDate =
//                findViewById(R.id.txtTransactionDate);
//
//        transactionTime =
//                findViewById(R.id.txtTransactionTime);
//
//        transactionAmount =
//                findViewById(R.id.txtDetailAmount);
//
//        transactionCardNo =
//                findViewById(R.id.txtDetailCardNo);
//
//        transactionCardHolderName =
//                findViewById(R.id.txtDetailCardHolderName);
//
//        transactionCvv =
//                findViewById(R.id.txtCvv);
//
//        transactionExpiryDate =
//                findViewById(R.id.txtExpiryDate);
//
//        transactionRemarks =
//                findViewById(R.id.txtRemarks);
//
//
//        Button deleteTransaction = findViewById(R.id.btnDeleteTransaction);
//
//        deleteTransaction.setOnClickListener(view -> {
//            if(!isBound || paymentService == null){
//                Log.e("Delete", "Payment service is not connected");
//                return;
//            }
//            ExecutorService executorService = Executors.newSingleThreadExecutor();
//            executorService.execute(()->{
//                try{
//                    int transactionId = transaction.getTid();
//                    paymentService.deleteTransaction(transactionId);
//                    runOnUiThread(()->{
//                        Log.d("Delete", "Transaction Deleted: " + transactionId);
//                        finish();
//                    });
//                }catch (Exception e){
//                    Log.e("Delete", "Failed to delete transaction");
//                }
//            });
//
//                });
//
//
//
//        // Display transaction
//        showTransaction(transaction);
//
//
//
//
//    }
//    private String setApplicationName(String packageName){
//        if("com.example.myfirstapplication".equals(packageName)){
//            return "App 1";
//        }
//        if("com.example.myapplication2".equals(packageName)){
//            return "App 2";
//        }
//        return packageName;
//    }
//
//
//    @SuppressLint("SetTextI18n")
//    private void showTransaction(
//            TransactionData transaction) {
//        Log.d("DETAIL", "Transaction ID: " + transaction.getTid());
//        Log.d("DETAIL", "Application: " + transaction.getApplication());
//
//        String appName = setApplicationName(transaction.getApplication());
//
//        Log.d("DETAIL", "App name: " + appName);
//
//        transactionApplication.setText(appName);
//
//      //  transactionApplication.setText(setApplicationName(transaction.getApplication()));
//
//        transactionAmount.setText(
//                "Amount: " +
//                        transaction.getAmount()
//        );
//
//
//        transactionCardNo.setText(
//                "Card No: " +
//                        transaction.getCardNo()
//        );
//
//
//        transactionCardHolderName.setText(
//                "Card Holder Name: " +
//                        transaction.getCardHolderName()
//        );
//
//
//        transactionCvv.setText(
//                "CVV: " +
//                        transaction.getCvv()
//        );
//
//
//        transactionExpiryDate.setText(
//                "Expiry Date: " +
//                        transaction.getExpiryDate()
//        );
//
//
//        transactionRemarks.setText(
//                "Remarks: " +
//                        transaction.getRemarks()
//        );
//
//
//        // Date and time
//        String dateTime =
//                transaction.getDatetime();
//
//
//        if (dateTime != null &&
//                dateTime.contains(" ")) {
//
//            String[] parts =
//                    dateTime.split(" ");
//
//            transactionDate.setText(
//                    "Date: " + parts[0]
//            );
//
//            transactionTime.setText(
//                    "Time: " + parts[1]
//            );
//
//        } else {
//
//            transactionDate.setText(
//                    "Date: " +
//                            (dateTime != null
//                                    ? dateTime
//                                    : "N/A")
//            );
//
//            transactionTime.setText(
//                    "Time: N/A"
//            );
//        }
//
//
//
//
//    }
//
//}