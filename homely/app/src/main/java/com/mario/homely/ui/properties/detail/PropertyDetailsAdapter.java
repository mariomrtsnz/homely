package com.mario.homely.ui.properties.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mario.homely.R;
import com.mario.homely.ui.photos.FullscreenPhoto;
import com.mario.homely.util.UtilToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PropertyDetailsAdapter extends RecyclerView.Adapter<PropertyDetailsAdapter.ViewHolder> {
    private List<String> photosArray = new ArrayList<>();
    private Context context;

    public PropertyDetailsAdapter(Context ctx, List<String> data) {
        this.photosArray = data;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.property_details_custom_image, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        System.out.println(photosArray.get(i));
        if (photosArray != null) {
            Glide.with(context).load(photosArray.get(i)).into(viewHolder.image);
        }
        viewHolder.mView.setOnClickListener(v -> onPropertyClick(v, photosArray.get(i)));
    }

    private void onPropertyClick(View v, String selectedPicture) {
        Intent i = new Intent(context, FullscreenPhoto.class);
        i.putExtra("gallery", (Serializable) photosArray);
        i.putExtra("selectedPicture", selectedPicture);
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return photosArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            image = itemView.findViewById(R.id.galleryPhotoThumbnail);
        }

    }
}
