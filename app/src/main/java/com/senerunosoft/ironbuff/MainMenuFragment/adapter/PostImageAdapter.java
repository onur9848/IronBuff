package com.senerunosoft.ironbuff.MainMenuFragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.components.IMarker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.senerunosoft.ironbuff.MainMenuFragment.addBeforeAfterPostFragment;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.PostTable;
import com.squareup.picasso.Picasso;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

public class PostImageAdapter extends RecyclerView.Adapter<PostImageAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<String> PhotoImg;
    boolean editPhoto;
    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void getPosition(); //pass any things
    }

    public PostImageAdapter(Context context, List<String> PhotoImg, boolean editPhoto) {
        this.inflater = LayoutInflater.from(context);
        this.PhotoImg = PhotoImg;
        this.editPhoto = editPhoto;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.img_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String postImg = PhotoImg.get(position);
        Picasso.get().load(postImg).into(holder.imageView);
        editphoto(holder, position);

    }

    private void editphoto(@NotNull ViewHolder holder, int position) {
        if (editPhoto) {
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(), "nabre", Toast.LENGTH_SHORT).show();
                    PhotoImg.remove(position);
                    notifyDataSetChanged();
                    return false;
                }
            });

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PhotoImg.size() == position + 1) {
                        onItemClick.getPosition();
                    }

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return PhotoImg.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.post_image_view);
        }
    }


}
