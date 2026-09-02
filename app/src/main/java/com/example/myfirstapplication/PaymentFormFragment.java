package com.example.myfirstapplication;

import android.annotation.SuppressLint;
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
import com.example.myfirstapplication.databinding.FragmentPaymentFormBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


public class PaymentFormFragment extends Fragment {



//    private EditText edtAmount, edtCardNo, edtCardHolderName, edtCvv, edtExpiryDate, edtRemarks;

    private IPaymentService paymentService;
    private boolean isBound = false;
    private FragmentPaymentFormBinding binding;



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
        //layout inflate
        binding = FragmentPaymentFormBinding.inflate(getLayoutInflater());
        return binding.getRoot();

//        return inflater.inflate(
//                R.layout.fragment_payment_form,
//                container,
//                false
//        );
    }


    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



//        edtAmount = view.findViewById(R.id.etAmount);
//        edtAmount.setText("Rs. ");
//        edtAmount.setSelection(edtAmount.length());
//        edtCardNo = view.findViewById(R.id.etCardNumber);
//        edtCardHolderName = view.findViewById(R.id.etCardHolderName);
//        edtCvv = view.findViewById(R.id.etCvv);
//        edtExpiryDate = view.findViewById(R.id.etExpiryDate);
//        edtRemarks = view.findViewById(R.id.etRemarks);

        binding.etAmount.setText("Rs. ");
        binding.etAmount.setSelection(binding.etAmount.length());
//        String cardNumber = binding.etCardNumber.getText().toString();
//        String cardHolderName = binding.etCardHolderName.getText().toString();
//        String cvv = binding.etCvv.getText().toString();
//        String expiryDate = binding.etExpiryDate.getText().toString();
//        String remarks = binding.etRemarks.getText().toString();






       // Button btnProceed = view.findViewById(R.id.btnProceed);
        binding.btnProceed.setOnClickListener(v->{
            if(binding.etAmount.getText().toString().trim().isEmpty() ||
                    binding.etCardNumber.getText().toString().trim().isEmpty()||
                    binding.etCardHolderName.getText().toString().trim().isEmpty()||
                    binding.etCvv.getText().toString().trim().isEmpty()||
                    binding.etExpiryDate.getText().toString().trim().isEmpty() ||
                    binding.etRemarks.getText().toString().trim().isEmpty()
            ){
                Toast.makeText(requireContext(), "Please fill all the field", Toast.LENGTH_SHORT).show();
                return;
            }

            String amount = binding.etAmount.getText().toString();

            String formattedAmount = amount
                    .replace("Rs.", "")
                    .replace(",", "")
                    .trim();

            double amountValue;

            try{
                amountValue = Double.parseDouble(formattedAmount);

            } catch (NumberFormatException e){
                binding.etAmount.setError("Enter a valid amount");
                binding.etAmount.requestFocus();
                return;
            }

            if(amountValue <=0){
                binding.etAmount.setError("Amount must be greater than 0");
                binding.etAmount.requestFocus();
                return;
            }


            String cardNo = binding.etCardNumber.getText().toString();
            if (!cardNo.matches("\\d{16}")) {
                binding.etCardNumber.setError("Card number must be 16 digits");
                binding.etCardNumber.requestFocus();
                return;
            }


            String cardHolderName = binding.etCardHolderName.getText().toString();
            if(binding.etCardHolderName.length()<3){
                binding.etCardHolderName.setError("Enter the valid card holder name");
                binding.etCardHolderName.requestFocus();
                return;
            }

            String cvv = binding.etCvv.getText().toString();

            String expiryDate = binding.etExpiryDate.getText().toString();

            if(!expiryDate.matches("\\d{2}/\\d{2}")){
                binding.etExpiryDate.setError("Use MM/YY format");
                binding.etExpiryDate.requestFocus();
                return;
            }
            int month = Integer.parseInt(expiryDate.substring(0,2));

            if(month<1 || month>12){
                binding.etExpiryDate.setError("Month must be between 01 and 12");
                binding.etExpiryDate.requestFocus();
                return;
            }

            String remarks = binding.etRemarks.getText().toString();


//        this will clear the field after submitting
            binding.etAmount.setText("");
            binding.etCardNumber.setText("");
            binding.etCardHolderName.setText("");
            binding.etCvv.setText("");
            binding.etExpiryDate.setText("");
            binding.etRemarks.setText("");

//        move cursor to the first field
            binding.etAmount.requestFocus();



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

        binding.etExpiryDate.addTextChangedListener(new TextWatcher() {
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

                String clean = input.replaceAll("\\D", "");

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

                binding.etExpiryDate.setText(formatted);



                binding.etExpiryDate.setSelection(formatted.length());
            }




        });

        binding.etAmount.addTextChangedListener(new TextWatcher() {
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
                    binding.etAmount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("\\D", "");

                    if (!cleanString.isEmpty()) {
                        try {

                            double parsed = Double.parseDouble(cleanString);

                            double formattedValue = parsed / 100;

                            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);

                            // 'Rs.'  will be print exactly what it is
                            DecimalFormat formatter = new DecimalFormat("'Rs. '###,###,##0.00", symbols);
                            String formatted = formatter.format(formattedValue);

                            current = formatted;
                            binding.etAmount.setText(formatted);
                            binding.etAmount.setSelection(formatted.length());
                        } catch (NumberFormatException ignored) {

                        }
                    } else {
                        current = "";
                        binding.etAmount.setText("");
                    }

                    binding.etAmount.addTextChangedListener(this);
                }

            }
        });
    }
}