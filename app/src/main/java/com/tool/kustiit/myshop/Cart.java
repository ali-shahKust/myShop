package com.tool.kustiit.myshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

public class Cart extends AppCompatActivity {

    private TextView totalPrice;
    private RecyclerView myView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        totalPrice = (TextView)findViewById(R.id.totalPrice);

        myView = (RecyclerView) findViewById(R.id.myCartList);
        myView.setHasFixedSize(true);

        nextBtn = (Button) findViewById(R.id.nextBtn);

        layoutManager = new LinearLayoutManager(this);
        myView.setLayoutManager(layoutManager);
    }
}
