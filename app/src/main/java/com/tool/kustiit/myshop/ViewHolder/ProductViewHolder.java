package com.tool.kustiit.myshop.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tool.kustiit.myshop.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productNametxt , ProductDescription;
    public ImageView productImg;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImg = (ImageView) itemView.findViewById(R.id.productImage);
        productNametxt = (TextView) itemView.findViewById(R.id.productName);
        ProductDescription = (TextView) itemView.findViewById(R.id.productDescription);
    }

    @Override
    public void onClick(View view) {



    }
}
