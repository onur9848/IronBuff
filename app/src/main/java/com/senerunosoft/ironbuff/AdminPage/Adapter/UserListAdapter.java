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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends ArrayAdapter<String> {

    private List<String> userList;
    private List<Uri> userImg;
    private List<String> userDocList;
    private final Context context;
    private final LayoutInflater inflater;
    private ViewHolder holder;

    public UserListAdapter(@NonNull Context context, List<String> userList, List<String> userDocList, List<Uri> userImg) {
        super(context, 0);
        this.context = context;
        this.userList = userList;
        this.userDocList = userDocList;
        this.userImg = userImg;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(R.layout.user_list,null);

            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.user_list_view);
            holder.userImgView = (ImageView) convertView.findViewById(R.id.listview_user_image);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (userImg != null && userList != null){
            holder.userName.setText(userList.get(position).toString());
            Picasso.get().load(userImg.get(position)).into(holder.userImgView);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "position"+position, Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("DocId",userDocList.get(position));
                    NavDirections directions = AdminFragmentDirections.adminToAddUserTrainingProgram();
                    Navigation.findNavController(view).navigate(directions.getActionId(),bundle);


                }
            });

        }

        return convertView;
    }

    @Override
    public int getPosition(@Nullable String item) {
        return super.getPosition(item);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    private static class ViewHolder {
        TextView userName;
        ImageView userImgView;
    }

}
