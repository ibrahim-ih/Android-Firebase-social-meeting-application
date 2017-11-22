package com.ngenious.ibrahim.liny.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Destination;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ibrahim on 16/06/17.
 */

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {
    private static String TAG = "InvitationAdapter";
    protected Context mContext;
//    private List<String>invitationList;
    private List<Destination> destinationList;
    private List<String>peopleIdList;
    private List<String>destitaionIdList;

    public InvitationAdapter(Context context, List<Destination>destinationList,List<String>peopleIdList,List<String>destitaionIdList) {
        this.mContext = context;
        this.destinationList = destinationList;
        this.peopleIdList = peopleIdList;
        this.destitaionIdList = destitaionIdList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitations_cards, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        final String peopleId = peopleIdList.get(position);
        final String destinationId = destitaionIdList.get(position);
        final String displayNameDest = destination.getUsers().getDisplayName();
        holder._name.setText(displayNameDest);
        holder._place.setText(destination.getPlaceName());

        Glide.with(mContext)
                .load(destination.getUsers().getPicture())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder._profilePic);



        Long unixTimestamp = destination.getTimestampUnix();
        Date date = new Date(unixTimestamp*1000);
        //a faire si aujourdhui affich timeFormat sinon dateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

        String dateOutput = dateFormat.format(date);
        String timeOutput = timeFormat.format(date);
        String dayOutput = dayFormat.format(date);

        Calendar calendar = Calendar.getInstance();
        String toDay = dayFormat.format(calendar.getTime().getTime());

        if (toDay.equals(dayOutput)){
            holder._timestamp.setText(timeOutput);
        }else {
            holder._timestamp.setText(timeOutput+","+dateOutput);
        }
        Log.d(TAG, "onBindViewHolder : layoutPosition : "
                + holder.getLayoutPosition()
                +"adapterPosition"
                + holder.getAdapterPosition());

        holder
                ._accepted
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "ACCEPTED");

                        Intent intent = new Intent("custom-order");
                        intent.putExtra("Accepted", destinationId);//peopleId
                        intent.putExtra("peopleId", peopleId);
                        intent.putExtra("displayName", displayNameDest);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }
                });
        holder
                ._ignored
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Ignored");
                        Intent intent = new Intent("custom-order");
                        intent.putExtra("Ignored", destinationId);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView _name, _place, _timestamp;
        CircleImageView _profilePic;
        ImageButton _accepted, _ignored;

        public ViewHolder(View itemView) {
            super(itemView);
            _name = (TextView) itemView.findViewById(R.id.nameTextView);
            _place = (TextView) itemView.findViewById(R.id.placeNameTextView);
            _timestamp = (TextView) itemView.findViewById(R.id.timeTextView);
            _profilePic = (CircleImageView)itemView.findViewById(R.id.profileCirImageView);
            _accepted = (ImageButton)itemView.findViewById(R.id.acceptedImageButton);
            _ignored = (ImageButton)itemView.findViewById(R.id.ignoredImageButton);
//            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
          /*  switch (v.getId()) {
                case R.id.acceptedImageButton:
//                    mItemListener.onItemClicked(); // You can send any field or model as a param here.
                    Log.d(TAG, "AcceptedButton : layoutPosition : "
                            + getLayoutPosition()
                            +"adapterPosition"
                            + getAdapterPosition());
                    break;
                case R.id.ignoredImageButton:
                    Log.d(TAG, "ignoredImageButton : layoutPosition : "
                            + getLayoutPosition()
                            +"adapterPosition"
                            + getAdapterPosition());
                    break;
                default:
                    break;
            }*/

            }

    }
}
