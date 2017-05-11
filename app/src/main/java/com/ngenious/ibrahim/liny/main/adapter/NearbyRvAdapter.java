package com.ngenious.ibrahim.liny.main.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Users;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ibrahim on 04/05/17.
 */

public class NearbyRvAdapter extends RecyclerView.Adapter<NearbyRvAdapter.NearbyRvHolders> {
    private List<Users> users;

    protected Context context;
    public NearbyRvAdapter(Context context, List<Users>users){
        this.users = users;
        this.context = context;
    }


    public static class NearbyRvHolders extends RecyclerView.ViewHolder {
        @BindView(R.id.profileImageView)
        ImageView _profileImageView;
        @BindView(R.id.nameAgeTxt)
        TextView _nameAgeTxt;
        @BindView(R.id.locationNameTxt)
        TextView _locationNameTxt;
        List<Users> usersObject;

        public NearbyRvHolders(View itemView, List<Users> usersObject) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.usersObject = usersObject;
        }

    }

    @Override
    public NearbyRvHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        NearbyRvHolders viewHolder = null;

        //create a new View
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_card, parent, false);
        viewHolder = new NearbyRvHolders(layoutView, users);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NearbyRvHolders holder, int position) {

        holder._nameAgeTxt.setText(users.get(position).getDisplayName());
        holder._locationNameTxt.setText(users.get(position).getAge());
        Glide.with(context)
                .load(Uri.parse(users.get(position).getPicture()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder._profileImageView);
        Log.i("Adapter", "name :"+users.get(position).getPicture());

    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }
}
