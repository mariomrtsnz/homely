package com.mario.homely.ui.properties.myProperties;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mario.homely.R;
import com.mario.homely.responses.MyPropertiesResponse;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.ResponseContainer;
import com.mario.homely.retrofit.generator.AuthType;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.PropertyService;
import com.mario.homely.retrofit.services.UserService;
import com.mario.homely.util.UtilToken;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPropertiesListFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    String jwt;
    PropertyService service;
    UserService userService;
    List<MyPropertiesResponse> items;
    MyPropertiesListAdapter adapter;
    SwipeRefreshLayout swipeLayout;
    RecyclerView recycler;
    private MyPropertiesListListener mListener;
    private Context ctx;
    private int mColumnCount = 1;
    private boolean asc;
//    MenuItem menuItemSort;
    private boolean earnedFilter = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public MyPropertiesListFragment() {}

    // TODO: Rename and change types and number of parameters
    public static MyPropertiesListFragment newInstance(int columnCount) {
        MyPropertiesListFragment fragment = new MyPropertiesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public void listMyProperties() {
        PropertyService service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);
        Call<ResponseContainer<MyPropertiesResponse>> call = service.getMyProperties();
        call.enqueue(new Callback<ResponseContainer<MyPropertiesResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<MyPropertiesResponse>> call, Response<ResponseContainer<MyPropertiesResponse>> response) {
                if (response.code() != 200) {
                    Toast.makeText(getActivity(), "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    items = response.body().getRows();
                    adapter = new MyPropertiesListAdapter(ctx, items, mListener);
                    recycler.setAdapter(adapter);
                    if (items.isEmpty())
                        Toast.makeText(ctx,"No Properties Owned", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<MyPropertiesResponse>> call, Throwable t) {
                Log.e("Network Failure", t.getMessage());
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        jwt = UtilToken.getToken(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_properties_list, container, false);

//        layout.findViewById(R.id.fab_map).setVisibility(View.GONE);
//        layout.findViewById(R.id.fab_list).setVisibility(View.VISIBLE);

        if (layout instanceof SwipeRefreshLayout) {
            ctx = layout.getContext();
            recycler = layout.findViewById(R.id.properties_list);
            if (mColumnCount <= 1) {
                recycler.setLayoutManager(new LinearLayoutManager(ctx));
            } else {
                recycler.setLayoutManager(new GridLayoutManager(ctx, mColumnCount));
            }
            items = new ArrayList<>();
            listMyProperties();
            adapter = new MyPropertiesListAdapter(ctx, items, mListener);
            recycler.setAdapter(adapter);
            swipeLayout = layout.findViewById(R.id.swipeContainer);
            swipeLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary), ContextCompat.getColor(getContext(), R.color.colorAccent));
            swipeLayout.setOnRefreshListener(() -> {
                listMyProperties();
                if (swipeLayout.isRefreshing()) {
                    swipeLayout.setRefreshing(false);
                }
            });
        }
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyPropertiesListListener) {
            mListener = (MyPropertiesListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MyPropertiesListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
