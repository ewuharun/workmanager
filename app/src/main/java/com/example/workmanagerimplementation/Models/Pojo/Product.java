package com.example.workmanagerimplementation.Models.Pojo;

/**
 * Created by Md.harun or rashid on 17,April,2021
 * BABL, Bangladesh,
 */
public class Product {
    private String id,name,logo,price,description,category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Product(String id, String name, String logo, String price, String description, String category) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.price = price;
        this.description = description;
        this.category=category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
