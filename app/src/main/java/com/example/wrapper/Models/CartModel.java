package com.example.wrapper.Models;

import com.example.wrapper.Models.Pojo.Cart;

import java.util.ArrayList;

/**
 * Created by Md.harun or rashid on 18,April,2021
 * BABL, Bangladesh,
 */
public class CartModel {

    ArrayList<Cart> cartArrayList=new ArrayList<>();
    public void add(Cart cart){
        cartArrayList.add(cart);
    }

    public ArrayList<Cart> getCartList(){
        return cartArrayList;
    }
    public CartModel(){

    }
}
