package com.nabil.smartkrishi;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class OnBoardActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    FrameLayout myFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_board);

        bottomNav = findViewById(R.id.bottomNav);
        myFrame = findViewById(R.id.myFrame);

        bottomNav.getOrCreateBadge(R.id.nav_notifications).setNumber(12);

        // ================== By Default =======================
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.myFrame, new HomeFragment());

        fTransaction.commit();
        // =================================================

        // ===============================
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home)
                {
                    FragmentManager fManager = getSupportFragmentManager();
                    FragmentTransaction fTransaction = fManager.beginTransaction();
                    fTransaction.replace(R.id.myFrame, new HomeFragment());

                    fTransaction.commit();
                }
                else if(item.getItemId() == R.id.nav_loan)
                {
//                    FragmentManager fManager = getSupportFragmentManager();
//                    FragmentTransaction fTransaction = fManager.beginTransaction();
//                    fTransaction.replace(R.id.myFrame, new BankLoanFragment());
//
//                    fTransaction.commit();
                }
                else if (item.getItemId() == R.id.nav_products)
                {
//                    FragmentManager fManager = getSupportFragmentManager();
//                    FragmentTransaction fTransaction = fManager.beginTransaction();
//                    fTransaction.replace(R.id.myFrame, new MyProductsragment());
//
//                    fTransaction.commit();
                }
                else if (item.getItemId() == R.id.nav_notifications)
                {
//                    FragmentManager fManager = getSupportFragmentManager();
//                    FragmentTransaction fTransaction = fManager.beginTransaction();
//                    fTransaction.replace(R.id.myFrame, new NotificationsFragment());
//
//                    fTransaction.commit();
                }

                return true;
            }
        });

    }

}

