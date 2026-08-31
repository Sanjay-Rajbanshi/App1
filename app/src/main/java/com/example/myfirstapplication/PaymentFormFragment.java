package com.example.myfirstapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextWatcher;

import com.example.myapplication2.IPaymentService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


public class PaymentFormFragment extends Fragment {



    private EditText edtAmount, edtCardNo, edtCardHolderName, edtCvv, edtExpiryDate, edtRemarks;

    private IPaymentService paymentService;
    private boolean isBound = false;



    //create a connection for service
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//        PaymentBoundService.PaymentBinder binder = (PaymentBoundService.PaymentBinder)service;
//        paymentBoundService = binder.getService();
//            this is with AIDl
            paymentService = IPaymentService.Stub.asInterface(service);
            isBound = true;
            Log.d("AIDL", "Connected to payment service");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
//            paymentBoundService = null;
            paymentService = null;
            Log.d("AIDL", "Disconnected from Payment Service");
        }
    };

//    Bind the service

    public void onStart(){
        super.onStart();

//        bind the service
        Intent intent = new Intent("com.example.myapplication2.IPaymentService");
        intent.setPackage("com.example.myapplication2");
        boolean result =  requireContext().bindService(intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE);
        Log.d("AIDL_CLIENT", "bindService result = " + result
        );

    }


    //    Unbind service
    public void onStop(){
        super.onStop();
        if(isBound){
            requireContext().unbindService(serviceConnection);
            isBound = false;
        }

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_payment_form,
                container,
                false
        );
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtAmount = view.findViewById(R.id.etAmount);
        edtAmount.setText("Rs. ");
        edtAmount.setSelection(edtAmount.length());
        edtCardNo = view.findViewById(R.id.etCardNumber);
        edtCardHolderName = view.findViewById(R.id.etCardHolderName);
        edtCvv = view.findViewById(R.id.etCvv);
        edtExpiryDate = view.findViewById(R.id.etExpiryDate);
        edtRemarks = view.findViewById(R.id.etRemarks);



        Button btnProceed = view.findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(v->{
            if(edtAmount.getText().toString().trim().isEmpty() ||
                    edtCardNo.getText().toString().trim().isEmpty()||
                    edtCardHolderName.getText().toString().trim().isEmpty()||
                    edtCvv.getText().toString().trim().isEmpty()||
                    edtExpiryDate.getText().toString().trim().isEmpty() ||
                    edtRemarks.getText().toString().trim().isEmpty()
            ){
                Toast.makeText(requireContext(), "Please fill all the field", Toast.LENGTH_SHORT).show();
                return;
            }

            String amount = edtAmount.getText().toString();

            String formattedAmount = amount
                    .replace("Rs.", "")
                    .replace(",", "")
                    .trim();

            double amountValue;

            try{
                amountValue = Double.parseDouble(formattedAmount);

            } catch (NumberFormatException e){
                edtAmount.setError("Enter a valid amount");
                edtAmount.requestFocus();
                return;
            }

            if(amountValue <=0){
                edtAmount.setError("Amount must be greater than 0");
                edtAmount.requestFocus();
                return;
            }


            String cardNo = edtCardNo.getText().toString();
            if (!cardNo.matches("\\d{16}")) {
                edtCardNo.setError("Card number must be 16 digits");
                edtCardNo.requestFocus();
                return;
            }


            String cardHolderName = edtCardHolderName.getText().toString();
            if(edtCardHolderName.length()<3){
                edtCardHolderName.setError("Enter the valid card holder name");
                edtCardHolderName.requestFocus();
                return;
            }

            String cvv = edtCvv.getText().toString();

            String expiryDate = edtExpiryDate.getText().toString();

            if(!expiryDate.matches("\\d{2}/\\d{2}")){
                edtExpiryDate.setError("Use MM/YY format");
                edtExpiryDate.requestFocus();
                return;
            }
            int month = Integer.parseInt(expiryDate.substring(0,2));

            if(month<1 || month>12){
                edtExpiryDate.setError("Month must be between 01 and 12");
                edtExpiryDate.requestFocus();
                return;
            }

            String remarks = edtRemarks.getText().toString();


//        this will clear the field after submitting
            edtAmount.setText("");
            edtCardNo.setText("");
            edtCardHolderName.setText("");
            edtCvv.setText("");
            edtExpiryDate.setText("");
            edtRemarks.setText("");

//        move cursor to the first field
            edtAmount.requestFocus();



            if (isBound && paymentService != null) {

                try {

                    paymentService.processPayment(
                            amountValue,
                            cardNo,
                            cardHolderName,
                            cvv,
                            expiryDate,
                            remarks
                    );

                    Log.d(
                            "AIDL_CLIENT",
                            "processPayment() called"
                    );

                } catch (RemoteException e) {

                    Log.e(
                            "AIDL_CLIENT",
                            "AIDL payment call failed",
                            e
                    );

                }

            } else {

                Toast.makeText(
                        requireContext(),
                        "Service is not connected",
                        Toast.LENGTH_SHORT
                ).show();
            }

        });

        edtExpiryDate.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) return;

                String input = s.toString();

                if (input.equals(current)) {
                    return;
                }

                String clean = input.replaceAll("[^\\d]", "");

                // it will allow maximum 4 digits (MMYY)
                if (clean.length() > 4) {
                    clean = clean.substring(0, 4);
                }

                String formatted;

                if (clean.length() <= 2) {
                    formatted = clean;
                } else {
                    formatted = clean.substring(0, 2) + "/" + clean.substring(2);
                }

                current = formatted;

                edtExpiryDate.setText(formatted);



                edtExpiryDate.setSelection(formatted.length());
            }




        });

        edtAmount.addTextChangedListener(new TextWatcher() {
            private String current;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    edtAmount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[\\D]", "");

                    if (!cleanString.isEmpty()) {
                        try {

                            double parsed = Double.parseDouble(cleanString);

                            double formattedValue = parsed / 100;

                            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);

                            // 'Rs.'  will be print exactly what it is
                            DecimalFormat formatter = new DecimalFormat("'Rs. '###,###,##0.00", symbols);
                            String formatted = formatter.format(formattedValue);

                            current = formatted;
                            edtAmount.setText(formatted);
                            edtAmount.setSelection(formatted.length());
                        } catch (NumberFormatException e) {

                        }
                    } else {
                        current = "";
                        edtAmount.setText("");
                    }

                    edtAmount.addTextChangedListener(this);
                }

            }
        });
    }
}