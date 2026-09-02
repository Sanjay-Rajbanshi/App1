package com.example.myfirstapplication;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication2.IPaymentService;
import com.example.myfirstapplication.databinding.ActivityMainBinding;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;
private IPaymentService paymentService;
private boolean isBound = false;

    public IPaymentService getPaymentService(){
    return paymentService;
}

private final ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        paymentService = IPaymentService.Stub.asInterface(iBinder);
        isBound = true;

        Log.d("AIDL", "Connected to payment service");
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        if(currentFragment instanceof TransactionHistoryFragment){
            ((TransactionHistoryFragment) currentFragment).onPaymentServiceConnected(paymentService);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

        paymentService = null;
        isBound = false;

        Log.d("AIDL", "Disconnected from the payment service");
    }
};

@Override
protected void onStart(){
    super.onStart();
    Intent intent = new Intent("com.example.myapplication2.IPaymentService");
    intent.setPackage("com.example.myapplication2");
    boolean result = bindService(intent,
            serviceConnection,
            BIND_AUTO_CREATE);
    Log.d("AIDL Client", "bindService result" + result);
}

@Override
protected void onStop(){
    super.onStop();
    if(isBound){
        unbindService(serviceConnection);
        isBound = false;
        paymentService = null;
    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            if(getIntent().getBooleanExtra("openHistory", false)){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                new TransactionHistoryFragment())
                        .commit();
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                new HomeFragment())
                        .commit();
            }
        }


//Home button

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnHome.setOnClickListener(v -> getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView,
                        HomeFragment.class,
                        null)
                .setReorderingAllowed(true)
                .commit());


        //payment button

//        inflate the layout


// access the button safely using binding object
        binding.btnPayment.setOnClickListener(v->{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, PaymentFormFragment.class, null )
                    .setReorderingAllowed(true)
                    .commit();
        });





//history button
//        ImageButton imgBtn = findViewById(R.id.btnPaymentHistory);

       binding.btnPaymentHistory.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
          fragmentManager.beginTransaction()
                 .replace(R.id.fragmentContainerView, TransactionHistoryFragment.class, null)
                .setReorderingAllowed(true)
                .commit();



        });
//back button
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                    Fragment currentFragment = getSupportFragmentManager()
                            .findFragmentById(R.id.fragmentContainerView);
                    if( currentFragment instanceof PaymentFormFragment){
                        showHomeFragment();
                    } else if (currentFragment instanceof TransactionHistoryFragment) {
                        showHomeFragment();
                    }
                    else {
                        setEnabled(false);
                        getOnBackPressedDispatcher().onBackPressed();
                        setEnabled(true);
                    }


            }
        });




//notification permission

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.POST_NOTIFICATIONS
                    },
                    NOTIFICATION_PERMISSION_REQUEST_CODE
            );
        }


        


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                    |
                            WindowInsetsCompat.Type.ime()
            );
            view.setPadding(
                    insets.left,
                    insets.top,
                    insets.right,
                    insets.bottom
            );
            return windowInsets;

        });
    }




    private void showHomeFragment(){
    getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainerView, new HomeFragment())
            .commit();
    }
}