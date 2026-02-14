package com.nabil.smartkrishi;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 1. Find the Spinner by its ID
        Spinner divisionSpinner = findViewById(R.id.sp_division);
        Spinner districtSpinner = findViewById(R.id.sp_district);

        // 2. Create the list of items
        String[] divisions = new String[]{"Dhaka", "Chittagong", "Khulna", "Rajshahi", "Barisal", "Sylhet", "Rangpur", "Mymensingh"};
        String[] districts = new String[]{"Dhaka", "Chittagong", "Khulna", "Rajshahi", "Barisal", "Sylhet", "Rangpur", "Mymensingh"};

        // 3. Create an ArrayAdapter
        // The second argument is a default layout provided by Android for how each item in the dropdown will look.
        // The third argument is the data source.
        ArrayAdapter<String> adapter_divisions = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, divisions);
        ArrayAdapter<String> adapter_district = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);

        // 4. Specify the layout to use when the list of choices appears
        adapter_divisions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 5. Apply the adapter to the spinner
        divisionSpinner.setAdapter(adapter_divisions);
        districtSpinner.setAdapter(adapter_district);


        // --- Your other code ---
    }
}