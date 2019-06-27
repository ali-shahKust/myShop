package com.tool.kustiit.myshop.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tool.kustiit.myshop.Interface.ItemClickListener;
import com.tool.kustiit.myshop.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productNametxt , ProductDescription, ProductPrice;
    public ImageView productImg;
    public ItemClickListener itemClickListener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImg = (ImageView) itemView.findViewById(R.id.productImage);
        productNametxt = (TextView) itemView.findViewById(R.id.productName);
        ProductDescription = (TextView) itemView.findViewById(R.id.productDescription);
        ProductPrice = (TextView)itemView.findViewById(R.id.productPrice);
    }


    public void setItemClickListener(ItemClickListener listener){
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
itemClickListener.onClick(view , getAdapterPosition() , false);


    }
}
