package com.swipecrafts.library.spinner;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.swipecrafts.school.R;

/**
 * Created by Madhusudan Sapkota on 3/30/2018.
 */
public class CustomSpinnerAdapter<M extends SpinnerModel> extends ArrayAdapter<M> {

    private Context context;
    private M[] values;

    public CustomSpinnerAdapter(Context context, int textViewResourceId, M[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public M getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateItems(M[] values){
        this.values = values;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(this.getContext()).inflate(R.layout.simple_spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.sp_item);

        label.setText(values[position].getDisplayText());

        if (position == 0) label.setTextColor(Color.GRAY);

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(this.getContext()).inflate(R.layout.simple_spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.sp_item);

        label.setText(values[position].getDisplayText());
        if (position == 0) label.setTextColor(Color.GRAY);
        return label;
    }
}
