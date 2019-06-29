package com.tool.kustiit.myshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrder extends AppCompatActivity {


    private DatabaseReference mRef;
    private EditText name , phone , adress , city;
    private CountryCodePicker cpp ;
    private Button confirmBtn;
    private String totalAmount = "";

    private FirebaseAuth mAuth ;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        cpp = (CountryCodePicker)findViewById(R.id.ccp);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Amount For Products You Purchased Is " + totalAmount, Toast.LENGTH_LONG).show();


        
        name = (EditText)findViewById(R.id.shipmentName);
        phone = (EditText)findViewById(R.id.shipNumber);
        adress = (EditText)findViewById(R.id.shipAdress);
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
try {
        if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(phone.getText()) && !TextUtils.isEmpty(adress.getText()) && !TextUtils.isEmpty(city.getText())) {
            String SaveCurrentTime;
            String SaveCurrentDate;

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            SaveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("MMM dd, yyyy");
            SaveCurrentTime = currentTime.format(calendar.getTime());


            mUser = mAuth.getCurrentUser();

            final String uID = mUser.getUid();
            final DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(uID);

            HashMap<String, Object> orderData = new HashMap<>();

            orderData.put("total_ammount", totalAmount);
            orderData.put("user_name", name.getText().toString());
            orderData.put("phone_number", phone.getText().toString());
            orderData.put("adress", adress.getText().toString());
            orderData.put("city", city.getText().toString());
            orderData.put("country", cpp.getSelectedCountryName());
            orderData.put("date", SaveCurrentDate);
            orderData.put("time", SaveCurrentTime);
            orderData.put("order_state", "Not Shipped");


            orderReference.updateChildren(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("CartList")
                                .child("User_view")
                                .child(uID).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ConfirmFinalOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(ConfirmFinalOrder.this, HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(ConfirmFinalOrder.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });
        }
        else {

            Toast.makeText(this, "Check Missing Fields", Toast.LENGTH_SHORT).show();
        }

    }
catch (Exception e){
    Log.e("MYAPP", "exception: " + e.getMessage());
    Log.e("MYAPP", "exception: " + e.toString());
    e.printStackTrace();
    displayExceptionMessage(this, e.getMessage());
}






    }
    public static void displayExceptionMessage(Context context, String msg)
    {
        Toast.makeText(context, msg , Toast.LENGTH_LONG).show();
    }
}
