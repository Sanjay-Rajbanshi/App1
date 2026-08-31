package com.example.myfirstapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public  class PaymentCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "PaymentCompletedReceiver";
    public static final String  ACTION_PAYMENT_COMPLETED = "com.example.myapplication2.PAYMENT_COMPLETED";
    public static final String EXTRA_TRANSACTION_ID = "transaction_id";
    public static final String EXTRA_APPLICATION = "application";
    public static final String ACTION_REFRESH_HISTORY = "com.example.myapplication2.REFRESH_HISTORY";
    public static final String CHANNEL_ID = "Payment_notifications";
    public static final String EXTRA_AMOUNT = "amount";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ACTION_PAYMENT_COMPLETED.equals(intent.getAction())) {
            return;
        }
        createNotificationChannel(context);
        int transactionId = intent.getIntExtra(EXTRA_TRANSACTION_ID, -1);
        String application = intent.getStringExtra(EXTRA_APPLICATION);
        String amount = intent.getStringExtra(EXTRA_AMOUNT);

        com.example.myapplication2.TransactionData transactionData = intent.getParcelableExtra("transaction");
        Log.d(TAG, "Payment completed broadcast received");
        Log.d(TAG, "Transaction ID = " + transactionId);
        Log.d(TAG, "Application = " + application);

        Intent refreshIntent = new Intent(ACTION_REFRESH_HISTORY);
        refreshIntent.setPackage(context.getPackageName());
        refreshIntent.putExtra(EXTRA_TRANSACTION_ID, transactionId);
        refreshIntent.putExtra(EXTRA_APPLICATION, application);
        context.sendBroadcast(refreshIntent);


        // create Intent
        Intent notificationIntent = new Intent(context, TransactionDetailedActivity.class);

//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra(EXTRA_TRANSACTION_ID, transactionId);


        //forward the parcelable data
        if(transactionData != null){
            notificationIntent.putExtra("transaction", transactionData);

        }


        // create pendingIntent

//        PendingIntent pendingIntent = TaskStackBuilder.create(context)
//                .addNextIntentWithParentStack(notificationIntent)
//                .getPendingIntent(
//                        transactionId,
//                        PendingIntent.FLAG_UPDATE_CURRENT |
//                                PendingIntent.FLAG_IMMUTABLE
//                );

        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.putExtra("openHistory", true);
         PendingIntent pendingIntent = TaskStackBuilder.create(context)
                 .addNextIntent(mainIntent)
                 .addNextIntent(notificationIntent)
                 .getPendingIntent(transactionId,
                         PendingIntent.FLAG_UPDATE_CURRENT |
                         PendingIntent.FLAG_IMMUTABLE);


        // create notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Payment Successful")
                .setContentText("Payment of Rs " + amount + " was successful")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        //to connect notification tap to detail activity
        manager.notify(
                transactionId,
                builder.build());



    }


//    create notification channel

    public void createNotificationChannel(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Payment Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Payment Completion Notifications");

            android.app.NotificationManager manager = context.getSystemService(android.app.NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }



}
