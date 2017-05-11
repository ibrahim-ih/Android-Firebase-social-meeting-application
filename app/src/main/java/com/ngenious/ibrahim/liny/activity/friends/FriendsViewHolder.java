package com.ngenious.ibrahim.liny.activity.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Friend;
import com.ngenious.ibrahim.liny.profile.fragment.gallery.app.AppController;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ibrahim on 02/05/17.
 */

public class FriendsViewHolder extends RecyclerView.ViewHolder {
    private TextView _displayName;
    private CircleImageView _profilePicture;
    private ImageButton _message;
    private ImageButton _details;
    public FriendsViewHolder(View itemView) {
        super(itemView);
        _displayName = (TextView)itemView.findViewById(R.id.displayNameTextView);
        _profilePicture = (CircleImageView)itemView.findViewById(R.id.profileImage);
        _message = (ImageButton)itemView.findViewById(R.id.messageImageButton);
        _details=(ImageButton)itemView.findViewById(R.id.detailsImageButton);
    }
    public void bind(Friend friend){
        _displayName.setText(friend.getDisplayName());
        Glide.with(AppController.getInstance())
                .load(friend.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(_profilePicture);

    }
}
