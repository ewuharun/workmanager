package com.example.workmanagerimplementation.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.workmanagerimplementation.Adapter.CategorySpinnerAdapter;
import com.example.workmanagerimplementation.Adapter.ProductListRecyclearViewAdapter;
import com.example.workmanagerimplementation.Models.CartModel;
import com.example.workmanagerimplementation.Models.CategoryModel;
import com.example.workmanagerimplementation.Models.Pojo.Cart;
import com.example.workmanagerimplementation.Models.Pojo.Category;
import com.example.workmanagerimplementation.Models.Pojo.Product;
import com.example.workmanagerimplementation.Models.ProductModel;
import com.example.workmanagerimplementation.R;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    private Spinner spinner;
    private RecyclerView recyclerView;
    private ProductListRecyclearViewAdapter productListAdapter;
    private String searchCategory;
    private Cart cart;
    private CartModel cartModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);


        init(view);

        CategoryModel categoryModel=new CategoryModel();
        ArrayList<Category> categoryList=categoryModel.getCategoryList();
        CategorySpinnerAdapter adapter=new CategorySpinnerAdapter(getContext(),categoryList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category=categoryList.get(position);
                searchCategory=category.getCategory();

                ProductModel productModel=new ProductModel();
                ArrayList<Product> allProductList=productModel.getAllProductList();



                if(searchCategory=="Select Category"){
                    productListAdapter=new ProductListRecyclearViewAdapter(getContext(),allProductList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(productListAdapter);


                }else{
                    ArrayList<Product> filteredProductList=productModel.filterProductByCategory(searchCategory,allProductList);
                    productListAdapter=new ProductListRecyclearViewAdapter(getContext(),filteredProductList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(productListAdapter);
                    //adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CartModel cartModel=new CartModel();
        Toast.makeText(getContext(), new Gson().toJson(cartModel.getCartList()), Toast.LENGTH_SHORT).show();





        return view;
    }

    private void init(View view) {
        spinner=view.findViewById(R.id.spinner);
        recyclerView=view.findViewById(R.id.recyclearView);
    }
}