package com.ngenious.ibrahim.liny.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.ChatRoom;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by ibrahim on 04/06/17.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>{
//    Context mContext;
    List<ChatRoom> chatRoomList;
    private static String TAG = "ChatRoomAdapter";
    public ChatRoomAdapter(List<ChatRoom> chatRoomList) {
//        this.mContext = mContext;
        this.chatRoomList = chatRoomList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_rooms_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatRoom chatRoom = chatRoomList.get(position);
        Long unixTimestamp = chatRoom.getTimestamp();
        Date date = new Date(unixTimestamp*1000);
            //a faire si aujourdhui affich timeFormat sinon dateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String dateOutput = dateFormat.format(date);
        String timeOutput = timeFormat.format(date);



        holder._name.setText(chatRoom.getName());
        holder._lastMessage.setText(chatRoom.getLastMessage());
        holder._timestamp.setText(dateOutput+","+timeOutput);
        //timestamp to be change
        holder._unreadCount.setText(String.valueOf(chatRoom.getUnreadCount()));

        Log.i(TAG, "name : "+chatRoom.getName()+"\n"+"lastMessage : "+chatRoom.getLastMessage()+"\n"+"timestamp : "+dateOutput+"\n"+"unreadCount : "+chatRoom.getUnreadCount());

    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*String id, name, lastMessage;
        long timestamp;
        int unreadCount;*/
        TextView _name,_lastMessage, _timestamp, _unreadCount;
        public ViewHolder(View itemView) {
            super(itemView);
            _name = (TextView)itemView.findViewById(R.id.name);
            _lastMessage =(TextView)itemView.findViewById(R.id.message);
            _timestamp = (TextView)itemView.findViewById(R.id.timestamp);
            _unreadCount = (TextView)itemView.findViewById(R.id.count);

        }

        @Override
        public void onClick(View v) {
            // ici on va aller aux message de la Room message
        Log.i(TAG, "OnClick"+getAdapterPosition()+" "+getLayoutPosition()+" "+v.getId());
        }
    }
}
