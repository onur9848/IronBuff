package com.senerunosoft.ironbuff.MainMenuFragment.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.MessageListTable;
import com.senerunosoft.ironbuff.table.UserTable;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class MessageListAdapter extends ArrayAdapter<UserTable> {
    private LayoutInflater inflater;
    private List<MessageListTable> list;
    private String uuid;
    ViewHolder holder;

    public MessageListAdapter(@NonNull Context context, List<MessageListTable> list, String uuid) {
        super(context, 0);
        this.inflater = LayoutInflater.from(context);
        Collections.sort(list,Collections.reverseOrder());
        this.list = list;
        this.uuid = uuid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.message_list_cardview, null, false);
            holder = new ViewHolder();

            holder.username = (TextView) convertView.findViewById(R.id.cardview_user_name);
            holder.userImg = (ImageView) convertView.findViewById(R.id.cardview_user_img);
            holder.lastMessage = (TextView) convertView.findViewById(R.id.cardview_last_message);
            holder.timestamp = (TextView) convertView.findViewById(R.id.message_timestamp);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            holder.bind(list.get(position), uuid);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public static class ViewHolder {
        TextView username, lastMessage, timestamp;
        ImageView userImg;

        void bind(final MessageListTable table, String uuid) {
            int i = 0;
            if (table.getUsers().get(0).equals(uuid)) {
                i = 1;
            }
            SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
            username.setText(table.getUserName().get(i));
            lastMessage.setText(table.getLastMessage());
            timestamp.setText(timeformat.format(table.getLastMessageTime()));
            Picasso.get().load(table.getUserImg().get(i)).into(userImg);

        }
    }

}
