package com.tool.kustiit.myshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.hbb20.CountryCodePicker;

public class ConfirmFinalOrder extends AppCompatActivity {


    private DatabaseReference mRef;
    private EditText name , phone , adress , city;
    private CountryCodePicker cpp ;
    private Button confirmBtn;
    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);



        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Amount Of Products are" + totalAmount, Toast.LENGTH_LONG).show();


        
        name = (EditText)findViewById(R.id.shipmentName);
        phone = (EditText)findViewById(R.id.shipNumber);
        adress = (EditText)findViewById(R.id.shipNumber);
        city = (EditText)findViewById(R.id.shipCity);
        
        confirmBtn = (Button)findViewById(R.id.ConfirmShipBtn);


        final String fName = name.getText().toString();
        final String phn = phone.getText().toString();
        final String adr = adress.getText().toString();
        final String ct = city.getText().toString();


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmYourOrder(fName , phn , adr , ct);
            }
        });

    }

    private void ConfirmYourOrder(String fName, String phn, String adr, String ct) {


    }
}
