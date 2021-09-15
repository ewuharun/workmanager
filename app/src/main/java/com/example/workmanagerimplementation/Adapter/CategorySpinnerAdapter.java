package com.example.workmanagerimplementation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.workmanagerimplementation.Models.Pojo.Category;
import com.example.workmanagerimplementation.R;

import java.util.List;

/**
 * Created by Md.harun or rashid on 17,April,2021
 * BABL, Bangladesh,
 */
public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    public CategorySpinnerAdapter(Context context, List<Category> categoryList) {
        super(context, 0, categoryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Category category=getItem(position);

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.spinner_custom_layout,parent,false);
        }
        TextView textView=convertView.findViewById(R.id.sptextView);
        textView.setText(category.getCategory());
        textView.setTag(category);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Category category=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_custom_layout, parent, false);
        }
        TextView textView1= (TextView) convertView.findViewById(R.id.sptextView);
        textView1.setText(category.getCategory());
        textView1.setTag(category);
        return convertView;
    }


}
