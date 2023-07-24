package com.example.ecommerceproject;

import android.view.View;

public interface Onclick {
    void checkbox(View v, int adapterPosition);
    void onEditProduct(View v,int position);

    void onAddProduct(View v,int productPosition);

    void onDeleteProduct(View v,int deletePosition);

}
