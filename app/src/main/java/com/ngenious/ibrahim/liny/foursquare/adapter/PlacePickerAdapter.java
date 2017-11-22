package com.ngenious.ibrahim.liny.foursquare.adapter;

/**
 * Created by ibrahim on 16/05/17.
 */

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.foursquare.DetailsPlaceActivity;
import com.ngenious.ibrahim.liny.foursquare.PickedPlaceDialogFragment;
import com.ngenious.ibrahim.liny.foursquare.helpers.FoursquareResults;
import com.ngenious.ibrahim.liny.main.SearchFilterDialogFragment;

import java.util.List;

public class PlacePickerAdapter extends RecyclerView.Adapter<PlacePickerAdapter.ViewHolder> {

    // The application context for getting resources
    private Context context;

    // The list of results from the Foursquare API
    private List<FoursquareResults> results;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The venue fields to display
        TextView name;
        TextView address;
        TextView rating;
        TextView distance;
        String id;
        double latitude;
        double longitude;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            // Gets the appropriate view for each venue detail
            name = (TextView)v.findViewById(R.id.placePickerItemName);
            address = (TextView)v.findViewById(R.id.placePickerItemAddress);
            rating = (TextView)v.findViewById(R.id.placePickerItemRating);
            distance = (TextView)v.findViewById(R.id.placePickerItemDistance);
        }

        @Override
        public void onClick(View v) {



 /*           // Creates an intent to direct the user to a map view
            Context context = name.getContext();
            Intent i = new Intent(context, DetailsPlaceActivity.class);

            // Passes the crucial venue details onto the map view
            i.putExtra("name", name.getText());
            i.putExtra("ID", id);
            i.putExtra("latitude", latitude);
            i.putExtra("longitude", longitude);

            // Transitions to the map view.
            context.startActivity(i);*/
//////////////////////////
       /*     Context context = name.getContext();

            // Build the intent
            Uri location = Uri.parse("geo:<" + latitude  + ">,<" + longitude + ">?q=<" + latitude  + ">,<" + longitude + ">(" + name.getText() + ")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

// Verify it resolves
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
            boolean isIntentSafe = activities.size() > 0;

// Start an activity if it's safe
            if (isIntentSafe) {
                context.startActivity(mapIntent);
            }
*/
/*
       Context context = name.getContext();
            android.support.v4.app.DialogFragment
                    dialogFragment = PickedPlaceDialogFragment.newInstance();
            dialogFragment.show(((Activity) context).getFragmentManager(),"dialog");*/
            Context context = name.getContext();
            android.support.v4.app.FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();

            PickedPlaceDialogFragment pickedPlaceDialogFragment= new PickedPlaceDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("name", name.getText());
            bundle.putString("ID", id);
            bundle.putCharSequence("address", address.getText());
            bundle.putDouble("latitude", latitude);
            bundle.putDouble("longitude", longitude);

            pickedPlaceDialogFragment.setArguments(bundle);
            pickedPlaceDialogFragment.show(manager, "dialog");

        }
    }

    public PlacePickerAdapter(Context context, List<FoursquareResults> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public PlacePickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_picker, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Sets the proper rating colour, referenced from the Foursquare Brand Guide
        double ratingRaw = results.get(position).venue.rating;
        if (ratingRaw >= 9.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQKale));
        } else if (ratingRaw >= 8.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQGuacamole));
        } else if (ratingRaw >= 7.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQLime));
        } else if (ratingRaw >= 6.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQBanana));
        } else if (ratingRaw >= 5.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQOrange));
        } else if (ratingRaw >= 4.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQMacCheese));
        } else {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQStrawberry));
        }

        // Sets each view with the appropriate venue details
        holder.name.setText(results.get(position).venue.name);
        holder.address.setText(results.get(position).venue.location.address);
        holder.rating.setText(Double.toString(ratingRaw));
        holder.distance.setText(Integer.toString(results.get(position).venue.location.distance) + "m");

        // Stores additional venue details for the map view
        holder.id = results.get(position).venue.id;
        holder.latitude = results.get(position).venue.location.lat;
        holder.longitude = results.get(position).venue.location.lng;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

}