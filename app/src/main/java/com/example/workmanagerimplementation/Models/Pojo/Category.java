package com.example.workmanagerimplementation.Models.Pojo;

/**
 * Created by Md.harun or rashid on 17,April,2021
 * BABL, Bangladesh,
 */
public class Category {
    private String product_code;
    private String category;

    public Category(String product_code, String category) {
        this.product_code = product_code;
        this.category = category;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
