package com.ngenious.ibrahim.liny.chat;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Chat;
import com.ngenious.ibrahim.liny.model.UserCard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ibrahim on 04/06/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    private static String TAG = "ChatAdapter" ;
    //    Context mContext;
    private List<Chat> chatList;
    private String uName;
    private int SELF = 100;
    private static String today;
    //    List<UserCard>userCardList;
    protected Context mContext;
    public ChatAdapter(Context mContext,List<Chat> chatList, String uName) {
        this.mContext = mContext;
        this.chatList = chatList;
        this.uName = uName;

        Calendar calendar = Calendar.getInstance();
       today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = chatList.get(position);

//        holder._name.setText(chat.getUsers().getDisplayName());
        holder._message.setText(chat.getMessage());
        Glide.with(mContext)
                .load(chat.getUsers().getPicture())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder._image);
//        holder._image.setImageURI(chat.getUsers().getPicture());
        Log.i(TAG, "get Name : "+chat.getUsers().getDisplayName().trim() );

       Long unixTimestamp = chat.getCreatedAt();
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


    }

    @Override
    public int getItemViewType(int position) {
        Chat message = chatList.get(position);
        Log.i(TAG, "uName : "+uName);
        if (message.getUsers().getDisplayName().equals(uName)) {
            return SELF;
        }

        return position;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*String id, name, lastMessage;
        long timestamp;
        int unreadCount;*/
        TextView _name,_message, _timestamp;
        ImageView _image;
        public ViewHolder(View itemView) {
            super(itemView);
            _name = (TextView)itemView.findViewById(R.id.name);
            _message =(TextView)itemView.findViewById(R.id.message);
            _timestamp = (TextView)itemView.findViewById(R.id.timestamp);
            _image = (ImageView)itemView.findViewById(R.id.chatimageview);


        }

        @Override
        public void onClick(View v) {
            // ici on va aller aux message de la Room message
        }
    }

/*    private void getTimestamp(){
        String timeStampStr = concateTime();
        Log.i(TAG, "timeStampStr : "+timeStampStr);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = simpleDateFormat.parse(timeStampStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        unixTime = (long) date.getTime() / 1000;
        Log.i(TAG, "unixTime :"+ unixTime);
    }*/
}
