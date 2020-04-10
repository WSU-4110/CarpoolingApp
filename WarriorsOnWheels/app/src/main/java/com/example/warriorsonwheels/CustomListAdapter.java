package com.example.warriorsonwheels;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    private final Activity context;

    //to store the animal images
    private final ArrayList<String> location;

    //to store the list of countries
    private final ArrayList<String> time;

    //to store the list of countries
    private final ArrayList<String> driverName;

    public CustomListAdapter(Activity context, ArrayList<String> location, ArrayList<String> time, ArrayList<String> drivername) {

        super(context, R.layout.riderow, location);

        this.context = context;
        this.location = location;
        this.time = time;
        this.driverName = drivername;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.riderow, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView locationTextField = (TextView) rowView.findViewById(R.id.left1);
        TextView departureTextField = (TextView) rowView.findViewById(R.id.departed1);
        TextView driverTextFeild =  rowView.findViewById(R.id.driverName1);

        //this code sets the values of the objects to values from the arrays
        locationTextField.setText(location.get(position));
        departureTextField.setText(time.get(position));
        driverTextFeild.setText(driverName.get(position));

        return rowView;
    };
}
