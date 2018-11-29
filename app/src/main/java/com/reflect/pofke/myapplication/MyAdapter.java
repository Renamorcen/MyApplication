package com.reflect.pofke.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ProductViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<ListItem> productList;

    //getting the context and product list with constructor
    public MyAdapter(Context mCtx, List<ListItem> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_item, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        ListItem product = productList.get(position);

        //binding the data with the viewholder views
        holder.textViewHead.setText(product.getHead());
        holder.textViewDesc.setText(product.getDesc());


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewHead, textViewDesc;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewHead = itemView.findViewById(R.id.textViewHead);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);

        }
    }
}