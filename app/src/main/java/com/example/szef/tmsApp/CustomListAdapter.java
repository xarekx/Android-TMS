package com.example.szef.tmsApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {

    private final Activity context;
    private final Integer[] imageID;
    private final String[] infoArray;
    private final String[] dataArray;
    private final int layoutRes;

    public CustomListAdapter(Activity context, Integer[] imageIDParam, String[] infoArrayParam, String[] dataArrayParam, @LayoutRes int layoutRest) {
        super(context, layoutRest, infoArrayParam);
        this.context = context;
        this.imageID = imageIDParam;
        this.infoArray = infoArrayParam;
        this.dataArray = dataArrayParam;
        this.layoutRes = layoutRest;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(layoutRes, null, true);

        TextView nameTextField = rowView.findViewById(R.id.clientName);
        TextView infoTextField = rowView.findViewById(R.id.loadDate);
        ImageView imageView = rowView.findViewById(R.id.imageList);


        nameTextField.setText(dataArray[position]);
        infoTextField.setText(infoArray[position]);
        imageView.setImageResource(imageID[position]);

        return rowView;
    }


}
