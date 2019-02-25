package com.mario.homely.ui.properties.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mario.homely.R;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.UserResponse;
import com.mario.homely.retrofit.services.UserService;
import com.mario.homely.util.UtilToken;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PropertiesListAdapter extends RecyclerView.Adapter<PropertiesListAdapter.ViewHolder> {
    private final PropertiesListListener mListener;
    UserResponse user;
    private List<PropertyResponse> data;
    private Context context;
    private UserService service;
    private String jwt;

    public PropertiesListAdapter(Context ctx, List<PropertyResponse> data, PropertiesListListener mListener) {
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
//        Glide.with(context).load(data.get(i).getIcon()).into(viewHolder.icon);
//        viewHolder.title.setText(data.get(i).getName());
//        viewHolder.body.setText(data.get(i).getDescription());
//        viewHolder.points.setText(String.valueOf(data.get(i).getPoints()));
//        if (data.get(i).isEarned()) {
//            viewHolder.earned.setVisibility(View.VISIBLE);
//        }

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPropertyClick(v, viewHolder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title, description, rooms, size;
        public final ImageView coverImage;
        public PropertyResponse mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            title = itemView.findViewById(R.id.property_item_title);
            description = itemView.findViewById(R.id.tv_property_custom_item_description);
            rooms = itemView.findViewById(R.id.tv_property_custom_item_rooms);
            size = itemView.findViewById(R.id.tv_property_custom_item_size);
            coverImage = itemView.findViewById(R.id.property_item_bgImage);
        }

    }
}
