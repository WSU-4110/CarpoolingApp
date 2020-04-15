package com.example.warriorsonwheels;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PassengerListAdapter extends ArrayAdapter {

    private final Activity context;

    private final ArrayList<String> passengers;

    public PassengerListAdapter(Activity context, ArrayList<String> passengers) {

        super(context, R.layout.passengerrow, passengers);

        this.context = context;
        this.passengers = passengers;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.passengerrow, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView passengerTextField = (TextView) rowView.findViewById(R.id.passName);

        //this code sets the values of the objects to values from the arrays

        for (int i = 0; i < passengers.size(); i++)
        {
            Log.i("-------------------------------------------------------------",passengers.get(i) + " i = " + i + "Position =  " + position);
        }

        passengerTextField.setText(passengers.get(position));

        return rowView;
    };
}
