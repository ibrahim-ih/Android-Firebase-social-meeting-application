package com.ngenious.ibrahim.liny.main.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Created by ibrahim on 05/05/17.
 */

public class ProfileCardsAdapter extends BaseAdapter {


    private List<Users> users;

    protected Context context;
    public ProfileCardsAdapter(Context context, List<Users>users){
        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public Object getItem(int position) {
        return this.users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.profile_card, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        //setting data to views
        holder._nameAgeTxt.setText(users.get(position).getDisplayName());
       holder._locationNameTxt.setText(users.get(position).getAge());
//        holder._locationNameTxt.setText("hello");

        Glide.with(context)
                .load(Uri.parse(users.get(position).getPicture()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder._profileImageView);
        Log.i("Adapter", "name :"+users.get(position).getPicture());

//        holder.avatar.setImageBitmap(decodeSampledBitmapFromResource(activity.getResources(),
//                getItem(position).getDrawableId(), AVATAR_WIDTH, AVATAR_HEIGHT));

        return convertView;    }

    private class ViewHolder{

        private ImageView _profileImageView;
        private TextView _nameAgeTxt,_locationNameTxt;


        public ViewHolder(View view) {
           _profileImageView = (ImageView)view.findViewById(R.id.profileImageView);
            _nameAgeTxt = (TextView)view.findViewById(R.id.nameAgeTxt);
            _locationNameTxt=(TextView)view.findViewById(R.id.locationNameTxt);

        }
    }
}
