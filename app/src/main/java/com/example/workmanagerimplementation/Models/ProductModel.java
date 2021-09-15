package com.example.workmanagerimplementation.Models;

import android.widget.Toast;

import com.example.workmanagerimplementation.Models.Pojo.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md.harun or rashid on 17,April,2021
 * BABL, Bangladesh,
 */
public class ProductModel {

    public ArrayList<Product> getAllProductList(){
        ArrayList<Product> productList=new ArrayList<>();

        productList.add(new Product("1","dim","dkfjkjf","100","dfjkdjf","dim"));
        productList.add(new Product("1","product1","dkfjkjf","22","dfjkdjf","mama"));
        productList.add(new Product("1","product1","dkfjkjf","333","dfjkdjf","dim"));
        productList.add(new Product("1","dim","dkfjkjf","33","dfjkdjf","df"));
        productList.add(new Product("1","product1","dkfjkjf","2","dfjkdjf","mach"));
        productList.add(new Product("1","product1","dkfjkjf","4","dfjkdjf","dim"));
        productList.add(new Product("1","dim","dkfjkjf","4","dfjkdjf","oimia"));
        productList.add(new Product("1","product1","dkfjkjf","4","dfjkdjf","dim"));
        productList.add(new Product("1","product1","dkfjkjf","4","dfjkdjf","mim"));

        return productList;
    }

    public ArrayList<Product> filterProductByCategory(String categoryName,List<Product> productList){
        ArrayList<Product> filteredProduct=new ArrayList<>();

        for(int i=0;i<productList.size();i++){
            Product product=productList.get(i);
            if(product.getCategory()==categoryName){
                filteredProduct.add(product);
            }
        }

        return filteredProduct;
    }
}
