package com.tool.kustiit.myshop.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tool.kustiit.myshop.Interface.ItemClickListener;
import com.tool.kustiit.myshop.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productName , productPrice , productQuantity ;
    private ItemClickListener itemClickListener;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.product);
        productPrice = itemView.findViewById(R.id.proudct_cart_price);
        productQuantity  = itemView.findViewById(R.id.quantity);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view , getAdapterPosition() , false);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
