package com.example.myfirstapplication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication2.IPaymentService;


public class HomeFragment extends Fragment {

    private IPaymentService paymentService;
    private boolean isBound;

//    create a connection for service
    private final ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        paymentService = IPaymentService.Stub.asInterface(iBinder);
        isBound = true;
        Log.d("AIDL", "Connected to payment service");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);




    }
}