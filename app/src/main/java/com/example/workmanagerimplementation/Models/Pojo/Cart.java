package com.example.workmanagerimplementation.Models.Pojo;

/**
 * Created by Md.harun or rashid on 18,April,2021
 * BABL, Bangladesh,
 */
public class Cart {
    Product product;
    String isAdded;
    String qty;
    public Cart(){

    }

    public Cart(Product product, String isAdded, String qty) {
        this.product = product;
        this.isAdded = isAdded;
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(String isAdded) {
        this.isAdded = isAdded;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
