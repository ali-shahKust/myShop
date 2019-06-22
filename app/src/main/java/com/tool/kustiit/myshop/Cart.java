package com.tool.kustiit.myshop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tool.kustiit.myshop.Model.Carts;
import com.tool.kustiit.myshop.ViewHolder.CartViewHolder;

public class Cart extends AppCompatActivity {

    private TextView totalPrice;
    private RecyclerView myView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextBtn;

    private FirebaseAuth mAuth;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();



        totalPrice = (TextView)findViewById(R.id.totalPrice);

        myView = (RecyclerView) findViewById(R.id.myCartList);
        myView.setHasFixedSize(true);

        nextBtn = (Button) findViewById(R.id.nextBtn);

        layoutManager = new LinearLayoutManager(this);
        myView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mUser = mAuth.getCurrentUser();

        String uID = mUser.getUid();

        final DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("CartList");

        FirebaseRecyclerOptions<Carts> options =
                new FirebaseRecyclerOptions.Builder<Carts>().
                        setQuery(cartList.child("User_view").child(uID)
                                .child("Products"), Carts.class)
                                    .build() ;


        FirebaseRecyclerAdapter<Carts , CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Carts, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Carts model) {

                        holder.productQuantity.setText("Quantity = " +model.getQuantity());
                        holder.productPrice.setText("Per Product Price = "+model.getPrice());
                        holder.productName.setText("Name = " +model.getpName());
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_list, viewGroup , false);

                        CartViewHolder viewHolder = new CartViewHolder(view);
                        return viewHolder;

                    }
                };

        myView.setAdapter(adapter);
        adapter.startListening();
    }
}
