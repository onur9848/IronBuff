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
import com.senerunosoft.ironbuff.table.UserTable;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageListAdapter extends ArrayAdapter<UserTable> {
    private LayoutInflater inflater;
    private List<UserTable> list;
    ViewHolder holder;

    public MessageListAdapter(@NonNull Context context, List<UserTable> list) {
        super(context, 0);
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.message_list_cardview,null,false);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.cardview_user_name);
            holder.userImg =(ImageView) convertView.findViewById(R.id.cardview_user_img);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list!=null){
            holder.bind(list.get(position));
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public static class ViewHolder {
        TextView username,lastMessage;
        ImageView userImg;

        void bind(final UserTable table){
            username.setText(table.getUserName());
            Picasso.get().load(table.getImageUrl()).into(userImg);
        }
    }

}
