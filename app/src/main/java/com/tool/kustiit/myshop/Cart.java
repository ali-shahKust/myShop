package com.tool.kustiit.myshop;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tool.kustiit.myshop.Model.Carts;
import com.tool.kustiit.myshop.ViewHolder.CartViewHolder;

public class Cart extends AppCompatActivity {

    private TextView totalPrice , txtmsg1;
    private RecyclerView myView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextBtn;

    private FirebaseAuth mAuth;

    private  int ProducttotalPrice = 0 ;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();



        totalPrice = (TextView)findViewById(R.id.totalPrice);
        txtmsg1 = (TextView)findViewById(R.id.msg1);
        myView = (RecyclerView) findViewById(R.id.myCartList);
        myView.setHasFixedSize(true);

        nextBtn = (Button) findViewById(R.id.nextBtn);

        layoutManager = new LinearLayoutManager(this);
        myView.setLayoutManager(layoutManager);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalPrice.setText(String.valueOf(ProducttotalPrice));

                Intent intent = new Intent(Cart.this , ConfirmFinalOrder.class);
                intent.putExtra("Total Price", String.valueOf(ProducttotalPrice));

                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckState();

        mUser = mAuth.getCurrentUser();

        final String uID = mUser.getUid();

        final DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("CartList");

        FirebaseRecyclerOptions<Carts> options =
                new FirebaseRecyclerOptions.Builder<Carts>().
                        setQuery(cartList.child("User_view").child(uID)
                                .child("Products"), Carts.class)
                                    .build() ;


        FirebaseRecyclerAdapter<Carts , CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Carts, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Carts model) {

                        holder.productQuantity.setText("Quantity = " +model.getQuantity());
                        holder.productPrice.setText("Price = "+model.getPrice());
                        holder.productName.setText(model.getpName());

                        int perProductTotalPrice = (Integer.valueOf(model.getPrice())) * Integer.valueOf(model.getQuantity());

                        ProducttotalPrice = ProducttotalPrice + perProductTotalPrice;
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options [] = new CharSequence[]{


                                        "Edit" ,
                                        "Remove"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                                builder.setTitle("Cart Options");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i == 0){
                                            Intent intent  = new Intent(Cart.this, ProductDetailActivity.class);
                                            intent.putExtra("pid" , model.getPid());
                                            startActivity(intent);
                                        }

                                        if (i == 1){
                                            cartList.child("User_view")
                                                    .child(uID)
                                                    .child("Products")
                                                    .child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        Toast.makeText(Cart.this, "Item Removed", Toast.LENGTH_SHORT).show();

                                                        Intent intent  = new Intent(Cart.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                    builder.show();
                            }
                        });
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

    private void CheckState(){

        mUser = mAuth.getCurrentUser();

        final String uID = mUser.getUid();
        DatabaseReference orderRef =
                FirebaseDatabase.getInstance().getReference().child("Orders").child(uID);
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String order_state = dataSnapshot.child("order_state").getValue().toString();

                    String name = dataSnapshot.child("user_name").getValue().toString();
                    if (order_state.equals("Shipped")){

                        totalPrice.setText("Shipped ");
                        myView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        txtmsg1.setText("Congratulation Your Final Order Has Been Shipped.");
                        nextBtn.setVisibility(View.GONE);

                        Toast.makeText(Cart.this, "Please Give us A Feedback When you Receive Your Product", Toast.LENGTH_SHORT).show();

                    }
                    else if (order_state.equals("Not Shipped")){
                        totalPrice.setText("Not Shipped");
                        myView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        nextBtn.setVisibility(View.GONE);

                        Toast.makeText(Cart.this, "You Can Purchase More Order Once You receive the Last Order You Purchase", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
