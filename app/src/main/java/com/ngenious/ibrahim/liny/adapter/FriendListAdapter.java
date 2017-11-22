package com.ngenious.ibrahim.liny.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.Filterable;
import android.widget.Filter;
import android.view.LayoutInflater;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.activity.MatchedActivity;
import com.ngenious.ibrahim.liny.model.Users;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Display friend list
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> implements Filterable {
    private List<Users> mArrayList;
    private List<Users> mFilteredList;
    private Context mContext;

    public FriendListAdapter(Context mContext, List<Users> arrayList) {
        this.mContext = mContext;

        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friends_cards_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendListAdapter.ViewHolder holder, int position) {

        Users friends = mFilteredList.get(position);
       holder._name.setText(friends.getDisplayName());
        holder._age.setText(friends.getAge()+" years");
        holder._location.setText(friends.getCity() +", "+friends.getCountry());

        Glide.with(mContext)
                .load(friends.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder._profilePic);
        holder._name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test", "name Clicked !");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Users> filteredList = new ArrayList<>();

                    for (Users androidVersion : mArrayList) {

                        if (androidVersion.getDisplayName().toLowerCase().contains(charString) || androidVersion.getFirstName().toLowerCase().contains(charString) || androidVersion.getLastName().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Users>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView _profilePic;
        TextView _name,_age, _location;
        public ViewHolder(View itemView) {
            super(itemView);
            _profilePic = (CircleImageView)itemView.findViewById(R.id.profileCirImageView);
            _name = (TextView)itemView.findViewById(R.id.nameTextView);
            _age = (TextView)itemView.findViewById(R.id.ageTextView);
            _location = (TextView)itemView.findViewById(R.id.locationTextView);

        }
    }

}