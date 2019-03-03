package com.mario.homely.ui.photos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mario.homely.R;
import com.mario.homely.responses.PhotoResponse;
import com.mario.homely.retrofit.generator.AuthType;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.PhotoService;
import com.mario.homely.retrofit.services.PropertyService;
import com.mario.homely.util.UtilToken;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private static final String ARG_COLUMN_COUNT = "column-count";
    private String propertyId;
    private String mParam2;
    private Context ctx;
    private String jwt;
    RecyclerView recycler;
    PhotosAdapter adapter;
    private int mColumnCount = 1;
    private List<String> photosIdArray = new ArrayList<>();
    private List<PhotoResponse> photoResponsesArray;

    private PhotosListener mListener;

    public PhotosFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(int columnCount) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            propertyId = getArguments().getString("propertyId");
            mParam2 = getArguments().getString(ARG_PARAM2);
            photosIdArray = getArguments().getStringArrayList("propertyPhotos");
        }
        jwt = UtilToken.getToken(getContext());
    }

    private void loadPhotos() {
        PhotoService service = ServiceGenerator.createService(PhotoService.class, jwt, AuthType.JWT);
        for (String photoId : photosIdArray) {
            Call<PhotoResponse> call = service.getOne(photoId);
            call.enqueue(new Callback<PhotoResponse>() {
                @Override
                public void onResponse(Call<PhotoResponse> call, Response<PhotoResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Request Error", Toast.LENGTH_SHORT).show();
                    } else {
                        photoResponsesArray.add(response.body());
                        if (photoResponsesArray.isEmpty())
                            Toast.makeText(ctx,"No Photos Uploaded for this property", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PhotoResponse> call, Throwable t) {
                    Log.e("Network Failure", t.getMessage());
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        adapter = new PhotosAdapter(ctx, photosIdArray, mListener);
        recycler.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_photos, container, false);
        if (layout instanceof View) {
            ctx = layout.getContext();
            recycler = layout.findViewById(R.id.fragment_photos);
            if (mColumnCount <= 1) {
                recycler.setLayoutManager(new LinearLayoutManager(ctx));
            } else {
                recycler.setLayoutManager(new GridLayoutManager(ctx, mColumnCount));
            }
            photoResponsesArray = new ArrayList<>();
            loadPhotos();
            adapter = new PhotosAdapter(ctx, photosIdArray, mListener);
            recycler.setAdapter(adapter);
            layout.findViewById(R.id.fab_photos_add).setOnClickListener(v -> mListener.addImage(propertyId));
        }
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        if (context instanceof PhotosListener) {
            mListener = (PhotosListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PhotosListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
