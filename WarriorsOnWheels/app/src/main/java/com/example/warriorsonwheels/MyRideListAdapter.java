package com.example.warriorsonwheels;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRideListAdapter extends ArrayAdapter {

    private final Activity context;

    private final ArrayList<String> date;
    private final ArrayList<String> time;
    private final ArrayList<String> arrival;

    public MyRideListAdapter(Activity context, ArrayList<String> time, ArrayList<String> arrival, ArrayList<String> date) {
        super(context, R.layout.riderow, arrival);
        this.context = context;
        this.date = date;
        this.time = time;
        this.arrival = arrival;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.riderow, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView timeTextField = (TextView) rowView.findViewById(R.id.departed1);
        TextView arrivalTextField =  rowView.findViewById(R.id.arrival1);
        TextView dateTextField =  rowView.findViewById(R.id.date1);

        //this code sets the values of the objects to values from the arrays
        timeTextField.setText(time.get(position));
        arrivalTextField.setText(arrival.get(position));
        dateTextField.setText((date.get(position)));

        return rowView;
    };
}
