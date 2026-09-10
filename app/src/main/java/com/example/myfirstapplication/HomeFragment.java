package com.example.myfirstapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication2.IPaymentService;
import com.example.myapplication2.TransactionData;
import com.example.myfirstapplication.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment {

    private IPaymentService paymentService;
    private boolean isBound;
    private RecentTransactionAdapter recentTransactionAdapter;
    private FragmentHomeBinding binding;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

//    create a connection for service
    private final ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        paymentService = IPaymentService.Stub.asInterface(iBinder);
        isBound = true;
        Log.d("AIDL", "Connected to payment service");
        loadRecentTransactions();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

        isBound = false;
        paymentService = null;
        Log.d("AIDL", "Service is diconnected");
    }
};

    public void onStart(){
        super.onStart();
        Intent intent = new Intent("com.example.myapplication2.IPaymentService");
        intent.setPackage("com.example.myapplication2");

        boolean result = requireContext().bindService(intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE);

        Log.d("AIDL_Client", "bindService result: " + result);
    }

    public void onStop(){
        super.onStop();
        if(isBound){
            requireContext().unbindService(serviceConnection);
            isBound = false;
        }
    }

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        List<TransactionData> recentTransactions = new ArrayList<>();

        //sample view



        recentTransactionAdapter = new RecentTransactionAdapter(recentTransactions);
        binding.recyclerRecentTransactions.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
        binding.recyclerRecentTransactions.setAdapter(recentTransactionAdapter);

//view all transactions
        binding.tvViewAll.setOnClickListener(v->{
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new TransactionHistoryFragment())
                    .addToBackStack(null)
                    .commit();
        });




    }

    private void loadRecentTransactions() {

        if (!isBound || paymentService == null) {
            Log.d("AIDL", "Payment service is not connected");
            return;
        }

        executorService.execute(() -> {
            try {

                List<TransactionData> transactionData =
                        paymentService.getTransactions();

                Log.d(
                        "Home",
                        "Transactions received: " + transactionData.size()
                );


                int totalTransactions = transactionData.size();

                double totalAmount = 0;

                int todayTransactions = 0;
                double todayAmount = 0;

                String today = new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.US
                ).format(new Date());



                for (TransactionData transaction : transactionData) {

                    double amount =
                            Double.parseDouble(transaction.getAmount());

                    totalAmount += amount;

                    String datetime = transaction.getDatetime();

                    if (datetime != null &&
                            datetime.startsWith(today)) {

                        todayTransactions++;
                        todayAmount += amount;
                    }
                }



                List<TransactionData> recentTransactions =
                        transactionData.subList(
                                0,
                                Math.min(3, transactionData.size())
                        );

                final int finalTotalTransactions = totalTransactions;
                final double finalTotalAmount = totalAmount;
                final int finalTodayTransactions = todayTransactions;
                final double finalTodayAmount = todayAmount;

                requireActivity().runOnUiThread(() -> {

                    binding.tvTotalTransactions.setText(
                            String.valueOf(finalTotalTransactions)
                    );

                    binding.tvTotalAmount.setText(
                            String.format(
                                    Locale.US,
                                    "Rs. %,.2f",
                                    finalTotalAmount
                            )
                    );

                    binding.tvTodayTransactions.setText(
                            String.valueOf(finalTodayTransactions)
                    );

                    binding.tvTodayAmount.setText(
                            String.format(
                                    Locale.US,
                                    "Rs. %,.2f",
                                    finalTodayAmount
                            )
                    );

                    recentTransactionAdapter.updateTransactions(
                            recentTransactions
                    );
                });

            } catch (RemoteException e) {

                Log.e(
                        "Home",
                        "Failed to get transactions",
                        e
                );
            }
        });
    }


}