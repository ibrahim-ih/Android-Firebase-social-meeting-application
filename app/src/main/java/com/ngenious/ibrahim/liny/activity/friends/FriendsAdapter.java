package com.ngenious.ibrahim.liny.activity.friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Friend;
import com.ngenious.ibrahim.liny.model.Users;

import java.util.List;

/**
 * Created by ibrahim on 02/05/17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {
    List<Friend> list;
    public FriendsAdapter(List<Friend>list){
        this.list = list;
    }
    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_cards, parent,false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        Friend friend = list.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
