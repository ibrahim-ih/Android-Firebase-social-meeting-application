package com.ngenious.ibrahim.liny.foursquare.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Destination;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ibrahim on 25/05/17.
 */

public class HistoryDestAdapter extends RecyclerView.Adapter<HistoryDestAdapter.MyViewHolder> {
    private List<Destination> destinationList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemAddress, itemDate, itemTime;

        public MyViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.destinationItemName);
            itemAddress = (TextView) view.findViewById(R.id.destinationItemAddress);
            itemDate = (TextView) view.findViewById(R.id.destinationItemDate);
            itemTime = (TextView) view.findViewById(R.id.destinationItemTime);
        }
    }




    public HistoryDestAdapter(FragmentActivity fragmentActivity, List<Destination> destinationList) {
        this.destinationList = destinationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_destination, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        holder.itemName.setText(destination.getPlaceName());
        holder.itemAddress.setText(destination.getAddress());
        Long unixTimestamp = destination.getTimestampUnix();
        Date date = new Date(unixTimestamp*1000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");



        String dateOutput = dateFormat.format(date);
        String timeOutput = timeFormat.format(date);
        holder.itemDate.setText(dateOutput);
        holder.itemTime.setText(timeOutput);
//        Log.i("test query : ", "date"+dateFormat.format(unixTimestamp));
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }
}