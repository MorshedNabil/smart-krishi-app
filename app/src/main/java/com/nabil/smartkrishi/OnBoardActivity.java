package com.nabil.smartkrishi;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class OnBoardActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    FrameLayout myFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_board);

        // Keeps the contents of the app safe from going under the system bars.
        View root = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        bottomNav = findViewById(R.id.bottomNav);
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, systemBars.bottom);
            return insets;
        });
        // =====================================================================================
        myFrame = findViewById(R.id.myFrame);

        bottomNav.getOrCreateBadge(R.id.nav_notifications).setNumber(12);

        // ================== Home Fragment (By Default) =======================
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.myFrame, new HomeFragment());
        fTransaction.commit();
        // =================================================

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                FragmentManager fManager1 = getSupportFragmentManager();
                FragmentTransaction fTransaction1 = fManager1.beginTransaction();
                fTransaction1.replace(R.id.myFrame, new HomeFragment());

                fTransaction1.commit();
            } else if (item.getItemId() == R.id.nav_loan) {
//                    FragmentManager fManager = getSupportFragmentManager();
//                    FragmentTransaction fTransaction = fManager.beginTransaction();
//                    fTransaction.replace(R.id.myFrame, new BankLoanFragment());
//
//                    fTransaction.commit();
            } else if (item.getItemId() == R.id.nav_products) {
//                    FragmentManager fManager = getSupportFragmentManager();
//                    FragmentTransaction fTransaction = fManager.beginTransaction();
//                    fTransaction.replace(R.id.myFrame, new MyProductsragment());
//
//                    fTransaction.commit();
            } else if (item.getItemId() == R.id.nav_notifications) {
//                    FragmentManager fManager = getSupportFragmentManager();
//                    FragmentTransaction fTransaction = fManager.beginTransaction();
//                    fTransaction.replace(R.id.myFrame, new NotificationsFragment());
//
//                    fTransaction.commit();
            }

            return true;
        });

    }

}

