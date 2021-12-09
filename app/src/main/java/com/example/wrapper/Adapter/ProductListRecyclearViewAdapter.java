package com.example.wrapper.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wrapper.Models.CartModel;
import com.example.wrapper.Models.Pojo.Cart;
import com.example.wrapper.Models.Pojo.Product;
import com.example.wrapper.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md.harun or rashid on 17,April,2021
 * BABL, Bangladesh,
 */
public class ProductListRecyclearViewAdapter extends RecyclerView.Adapter<ProductListRecyclearViewAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    InputFilter[] filters=new InputFilter[1];

    private EditText alertDialogQuantityEt;
    private TextView getAlertDialogTotalTv;




    public ProductListRecyclearViewAdapter(Context context, List<Product> productList) {
        this.context=context;
        this.productList=productList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_list_item,parent,false);

        return new ViewHolder(view);
    }

    ArrayList<Cart> cartList=new ArrayList<>();

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product=productList.get(position);


        CartModel cartModel=new CartModel();
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice()+" TK");



        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filters[0]=new InputFilter.LengthFilter(6);


                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                View view=LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog,null,false);

                alertDialogQuantityEt=view.findViewById(R.id.quantityEt);
                getAlertDialogTotalTv=view.findViewById(R.id.totalTv);

                alertDialogQuantityEt.setFilters(filters);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String quantity=alertDialogQuantityEt.getText().toString();

                        holder.linearLayout.setBackgroundColor(Color.YELLOW);
                        holder.qty.setText(quantity);
                        String totalPrice=String.valueOf(Integer.parseInt(quantity)*Integer.parseInt(product.getPrice()));
                        holder.totalPrice.setText(totalPrice+" TK");

                        holder.removeButton.setVisibility(View.VISIBLE);
                        holder.addButton.setVisibility(View.GONE);

                        Cart cart=new Cart(product,"1",quantity);
                        cartList.add(cart);


                        Log.e("Cart",new Gson().toJson(cartList));

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                builder.setCancelable(false);

                builder.setView(view);
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });



        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.linearLayout.setBackgroundColor(Color.WHITE);
                holder.qty.setText(null);
                holder.totalPrice.setText(null);
                holder.addButton.setVisibility(View.VISIBLE);
                holder.removeButton.setVisibility(View.GONE);
            }
        });






    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName,productPrice,qty,totalPrice;
        private ImageView productImage,addButton,removeButton;
        private LinearLayout linearLayout;
        

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            init(itemView);
        }

        private void init(View itemView) {
            productName=itemView.findViewById(R.id.productName);
            productPrice=itemView.findViewById(R.id.productPrice);
            qty=itemView.findViewById(R.id.productQty);
            totalPrice=itemView.findViewById(R.id.totalPrice);
            linearLayout=itemView.findViewById(R.id.linearLayout);
            productImage=itemView.findViewById(R.id.productImage);
            addButton=itemView.findViewById(R.id.addButton);
            removeButton=itemView.findViewById(R.id.removeButton);
        }
    }
    
}
