package com.tool.kustiit.myshop;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tool.kustiit.myshop.Model.Products;

public class ProductDetailActivity extends AppCompatActivity {


    private ElegantNumberButton elegantNumberButton ;
    //private FloatingActionButton addtocartbtn;
    private ImageView productImgDetail ;
    private TextView price , description , name ;
    private FirebaseAuth mAuth;

    private  String ProductID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );



        elegantNumberButton = (ElegantNumberButton)findViewById(R.id.productcounter);
       // addtocartbtn = (FloatingActionButton)findViewById(R.id.addtoCarddetail);
        productImgDetail = (ImageView)findViewById(R.id.productsDetail);
        price = (TextView)findViewById(R.id.productDetailPrice);
        description = (TextView)findViewById(R.id.productDesciptionDetail);
        name = (TextView)findViewById(R.id.productnameDetail);

        ProductID = getIntent().getStringExtra("pid");
        mAuth = FirebaseAuth.getInstance();



        getProductDetails(ProductID);

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
