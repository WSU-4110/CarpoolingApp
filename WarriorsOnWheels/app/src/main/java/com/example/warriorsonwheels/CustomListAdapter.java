package com.example.warriorsonwheels;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    private final Activity context;

    private final ArrayList<String> departure;
    private final ArrayList<String> date;
    private final ArrayList<String> time;
    private final ArrayList<String> passengers;
    private final ArrayList<String> arrival;

    public CustomListAdapter(Activity context, ArrayList<String> dept, ArrayList<String> time, ArrayList<String> arrival, ArrayList<String> date, ArrayList<String> passengers) {

        super(context, R.layout.riderow, dept);
        this.context = context;
        this.departure = dept;
        this.date = date;
        this.time = time;
        this.arrival = arrival;
        this.passengers = passengers;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.riderow, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView departureTextField = (TextView) rowView.findViewById(R.id.left1);
        TextView timeTextField = (TextView) rowView.findViewById(R.id.departed1);
        TextView arrivalTextField =  rowView.findViewById(R.id.arrival1);
        TextView passengerTextField = (TextView) rowView.findViewById(R.id.numPasses1);
        TextView dateTextField =  rowView.findViewById(R.id.date1);

        //this code sets the values of the objects to values from the arrays
        departureTextField.setText(departure.get(position));
        timeTextField.setText(time.get(position));
        arrivalTextField.setText(arrival.get(position));
        passengerTextField.setText(passengers.get(position));
        dateTextField.setText((date.get(position)));

        return rowView;
    };
}
