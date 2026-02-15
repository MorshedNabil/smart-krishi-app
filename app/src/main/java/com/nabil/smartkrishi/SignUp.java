package com.nabil.smartkrishi;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    Spinner divisionSpinner, districtSpinner;
    EditText inputPhone, inputNid, inputVillage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        divisionSpinner = findViewById(R.id.sp_division);
        districtSpinner = findViewById(R.id.sp_district);
        inputPhone = findViewById(R.id.input_phone);
        inputNid = findViewById(R.id.input_nid);
        inputVillage = findViewById(R.id.input_village);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // ===================== Adding spinners data =======================
        String[] divisions = new String[]{"Dhaka", "Chittagong", "Khulna", "Rajshahi", "Barisal", "Sylhet", "Rangpur", "Mymensingh"};
        String[] districts = new String[]{"Dhaka", "Chittagong", "Khulna", "Rajshahi", "Barisal", "Sylhet", "Rangpur", "Mymensingh"};

        ArrayAdapter<String> adapter_divisions = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, divisions);
        ArrayAdapter<String> adapter_district = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);

        adapter_divisions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        divisionSpinner.setAdapter(adapter_divisions);
        districtSpinner.setAdapter(adapter_district);
        // =================================================================

        userRegister();


    }

    private void userRegister() {

        String phone = inputPhone.getText().toString().trim();
        String nidNumber = inputNid.getText().toString().trim();
        String villageName = inputVillage.getText().toString().trim();
        String division = divisionSpinner.getSelectedItem().toString().trim();
        String district = districtSpinner.getSelectedItem().toString().trim();

        if(nidNumber.isEmpty()){
            inputNid.setError("Please enter all the fields");
            inputNid.requestFocus();
            return;
        }
        else if(villageName.isEmpty()){
            inputVillage.setError("Please enter Village fields");
            inputVillage.requestFocus();
            return;
        }
        else if(phone.isEmpty()){
            inputPhone.setError("Please enter Phone fields");
            inputPhone.requestFocus();
            return;
        }

    }

}