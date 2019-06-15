package com.tool.kustiit.myshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tool.kustiit.myshop.Model.Products;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {


    private ElegantNumberButton elegantNumberButton ;
    //private FloatingActionButton addtocartbtn;
    private ImageView productImgDetail ;
    private TextView price , description , name ;
    private FirebaseAuth mAuth;
    private Button addtoCartbtn;


    private  String ProductID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        mAuth = FirebaseAuth.getInstance();

        addtoCartbtn = (Button)findViewById(R.id.addToCart);



        elegantNumberButton = (ElegantNumberButton)findViewById(R.id.productcounter);
       // addtocartbtn = (FloatingActionButton)findViewById(R.id.addtoCarddetail);
        productImgDetail = (ImageView)findViewById(R.id.productsDetail);
        price = (TextView)findViewById(R.id.productDetailPrice);
        description = (TextView)findViewById(R.id.productDesciptionDetail);
        name = (TextView)findViewById(R.id.productnameDetail);

        ProductID = getIntent().getStringExtra("pid");
        mAuth = FirebaseAuth.getInstance();



        getProductDetails(ProductID);

        addtoCartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCartList();
            }
        });

    }

    private void AddToCartList() {

        String SaveCurrentTime ;
        String SaveCurrentDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        SaveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("MMM dd, yyyy");
        SaveCurrentTime = currentTime.format(calendar.getTime());


     final    DatabaseReference cartItems = FirebaseDatabase.getInstance().getReference().child("CartList");

        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userId = null;
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        final HashMap<String, Object> add_data = new HashMap<>();

        add_data.put("pid", ProductID);
        add_data.put("pName", name.getText().toString());
        add_data.put("price", price.getText().toString());
        add_data.put("quantity", elegantNumberButton.getNumber());
        add_data.put("date", SaveCurrentDate);
        add_data.put("time", SaveCurrentTime);
        add_data.put("discount", "");

        final String finalUserId = userId;
        cartItems.child("User_view").child(userId).child("Products")
                .child(ProductID).updateChildren(add_data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    cartItems.child("Admin_view").child(finalUserId).child("Products")
                            .child(ProductID).updateChildren(add_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProductDetailActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent (ProductDetailActivity.this , HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });


    }

    private void getProductDetails(String productID) {


        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");


        productRef.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){




                    Products products = dataSnapshot.getValue(Products.class);



                    name.setText(products.getProductName());
                    price.setText(products.getPrice());
                    description.setText(products.getDescription());

                    Picasso.get().load(products.getImage()).into(productImgDetail);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
