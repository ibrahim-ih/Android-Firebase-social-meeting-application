package com.ngenious.ibrahim.liny.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Users;
import com.ngenious.ibrahim.liny.profile.SimpleProfileActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ibrahim on 18/06/17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{
private static String TAG = "FriendsAdapter";
    private Context mContext;
    private List<Users> friendsList;
    private List<String>friendUid;
    public FriendsAdapter(Context mContext, List<Users> friendsList, List<String>friendUid){
        this.mContext = mContext;
        this.friendsList = friendsList;
        this.friendUid = friendUid;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_cards_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        Users friends = friendsList.get(position);
        final String idFriend = friendUid.get(position);
        holder._name.setText(friends.getDisplayName());
        holder._age.setText(friends.getAge()+" years");
        holder._location.setText(friends.getCity() +", "+friends.getCountry());

        Glide.with(mContext)
                .load(friends.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder._profilePic);

        holder
                .itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Clicked");
                Log.d(TAG, "idFriend"+idFriend);
                Intent intent = new Intent(mContext,SimpleProfileActivity.class);
                intent.putExtra("userId", idFriend );
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView _profilePic;
        TextView _name,_age, _location;
//        private Context context;
        public ViewHolder(View itemView) {
            super(itemView);

            _profilePic = (CircleImageView)itemView.findViewById(R.id.profileCirImageView);
            _name = (TextView)itemView.findViewById(R.id.nameTextView);
            _age = (TextView)itemView.findViewById(R.id.ageTextView);
            _location = (TextView)itemView.findViewById(R.id.locationTextView);

//           this.context=context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {


        /*    Intent intent = new Intent(context,SimpleProfileActivity.class);
            context.startActivity(intent);*/
        }
    }
}
