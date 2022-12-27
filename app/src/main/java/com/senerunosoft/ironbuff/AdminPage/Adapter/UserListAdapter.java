package com.senerunosoft.ironbuff.AdminPage.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.senerunosoft.ironbuff.AdminPage.AdminFragmentDirections;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.TrainingListViewAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.UserTable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends ArrayAdapter<String> {

    private List<UserTable> list;
    private final LayoutInflater inflater;
    private ViewHolder holder;

    public UserListAdapter(@NonNull Context context, List<UserTable> list) {
        super(context, 0);
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.user_list, null);

            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.user_list_view);
            holder.userImgView = (ImageView) convertView.findViewById(R.id.listview_user_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list !=null) {
            UserTable table = list.get(position);
            holder.userName.setText(table.getUserName());
            Picasso.get().load(table.getImageUrl()).into(holder.userImgView);

        }
        return convertView;
    }



    @Override
    public int getCount() {
        return list.size();
    }

    private static class ViewHolder {
        TextView userName;
        ImageView userImgView;
    }

}
