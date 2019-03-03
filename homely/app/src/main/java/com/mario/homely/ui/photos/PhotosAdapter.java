package com.mario.homely.ui.photos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mario.homely.R;
import com.mario.homely.responses.PhotoResponse;
import com.mario.homely.retrofit.services.PhotoService;
import com.mario.homely.retrofit.services.UserService;
import com.mario.homely.util.UtilToken;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private final PhotosListener mListener;
    private Context context;
    private String jwt;
    private UserService userService;
    private List<PhotoResponse> data;
    private PhotoService photoService;

    public PhotosAdapter(Context ctx, List<PhotoResponse> data, PhotosListener mListener) {
        this.data = data;
        this.context = ctx;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_custom_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        jwt = UtilToken.getToken(context);
        viewHolder.mItem = data.get(i);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView image;
        public final FloatingActionButton delete, add;
        public PhotoResponse mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            image = itemView.findViewById(R.id.property_item_bgImage);
            delete = itemView.findViewById(R.id.fab_photo_custom_delete);
            add = itemView.findViewById(R.id.fab_photos_add);
        }

    }
}
