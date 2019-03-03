package com.mario.homely.ui.properties.myProperties;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mario.homely.R;
import com.mario.homely.responses.MyPropertiesResponse;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.UserResponse;
import com.mario.homely.retrofit.generator.AuthType;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.PropertyService;
import com.mario.homely.retrofit.services.UserService;
import com.mario.homely.util.UtilToken;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPropertiesListAdapter extends RecyclerView.Adapter<MyPropertiesListAdapter.ViewHolder>{
    private final MyPropertiesListListener mListener;
    UserResponse user;
    private List<MyPropertiesResponse> data;
    private Context context;
    private UserService userService;
    private PropertyService propertyService;
    private String jwt;

    public MyPropertiesListAdapter(Context ctx, List<MyPropertiesResponse> data, MyPropertiesListListener mListener) {
        this.data = data;
        this.context = ctx;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.property_custom_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        jwt = UtilToken.getToken(context);
        viewHolder.mItem = data.get(i);
        String[] photoArray = data.get(i).getPhotos();
        viewHolder.edit.setVisibility(View.VISIBLE);
        viewHolder.delete.setVisibility(View.VISIBLE);
        if (jwt == null)
            viewHolder.fav.setVisibility(View.GONE);
        viewHolder.isFav = data.get(i).isFav();
        if (viewHolder.isFav)
            viewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        if (photoArray != null) {
            Glide.with(context).load(photoArray[0]).into(viewHolder.coverImage);
        }
        viewHolder.title.setText(data.get(i).getTitle());
        String description = data.get(i).getDescription();
        if (description != null && description.length() > 50)
            viewHolder.description.setText(description.substring(0, 50) + "...");
        else if (description == null)
            viewHolder.description.setText("No description");
        else
            viewHolder.description.setText(description);
        viewHolder.rooms.setText(String.valueOf(data.get(i).getRooms()));
        viewHolder.size.setText(String.valueOf(data.get(i).getSize()) + " sqft");
        viewHolder.price.setText(String.valueOf(data.get(i).getPrice()) + " €");
        viewHolder.fav.setOnClickListener(v -> updateFav(viewHolder, data.get(i)));
        viewHolder.delete.setOnClickListener(v -> mListener.onPropertyDeleteClick(v, data.get(i)));
        viewHolder.edit.setOnClickListener(v -> mListener.onPropertyEditClick(v, data.get(i)));
        viewHolder.mView.setOnClickListener(v -> mListener.onPropertyClick(v, viewHolder.mItem));
    }

    void updateFav(ViewHolder v, MyPropertiesResponse p) {
        propertyService = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);
        if (v.isFav) {
            Call<UserResponse> call = propertyService.deleteAsFav(p.getId());
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.code() != 200) {
                        Toast.makeText(context, "Request Error", Toast.LENGTH_SHORT).show();
                    } else {
                        v.fav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                        v.isFav = false;
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(context, "Network Failure", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<UserResponse> call = propertyService.addAsFav(p.getId());
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.code() != 200) {
                        Toast.makeText(context, "Request Error", Toast.LENGTH_SHORT).show();
                    } else {
                        v.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        v.isFav = true;
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(context, "Network Failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title, description, rooms, size, price;
        public final ImageView coverImage;
        public final FloatingActionButton fav, edit, delete;
        public boolean isFav;
        public MyPropertiesResponse mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            title = itemView.findViewById(R.id.property_item_title);
            description = itemView.findViewById(R.id.tv_property_custom_item_description);
            rooms = itemView.findViewById(R.id.tv_property_custom_item_rooms);
            size = itemView.findViewById(R.id.tv_property_custom_item_size);
            price = itemView.findViewById(R.id.tv_property_custom_item_price);
            coverImage = itemView.findViewById(R.id.property_item_bgImage);
            fav = itemView.findViewById(R.id.fab_property_custom_item_fav);
            edit = itemView.findViewById(R.id.fab_property_custom_item_edit);
            delete = itemView.findViewById(R.id.fab_property_custom_item_delete);
        }

    }
}
