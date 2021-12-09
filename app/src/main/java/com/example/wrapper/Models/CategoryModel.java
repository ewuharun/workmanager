package com.example.wrapper.Models;

import com.example.wrapper.Models.Pojo.Category;

import java.util.ArrayList;

/**
 * Created by Md.harun or rashid on 17,April,2021
 * BABL, Bangladesh,
 */
public class CategoryModel {
    public ArrayList<Category> getCategoryList(){
        ArrayList<Category> categories=new ArrayList<>();
        categories.add(0,new Category("1","Select Category"));
        categories.add(new Category("2","dim"));
        return categories;
    }
}
