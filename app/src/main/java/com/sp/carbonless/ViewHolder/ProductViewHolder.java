package com.sp.carbonless.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sp.carbonless.Interface.ItemClickListener;
import com.sp.carbonless.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtSellerName, txtProductPrice, txtProductTime;
    public ImageView imageView;
    public ItemClickListener listener;


    public ProductViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtSellerName = (TextView) itemView.findViewById(R.id.mcv_username);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        txtProductTime = (TextView) itemView.findViewById(R.id.product_time);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
