package com.mario.homely.ui.properties.listview;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mario.homely.R;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.responses.ResponseContainer;
import com.mario.homely.retrofit.generator.ServiceGenerator;
import com.mario.homely.retrofit.services.PropertyService;
import com.mario.homely.retrofit.services.UserService;

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

public class PropertiesListFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    String jwt;
    PropertyService service;
    UserService userService;
    List<PropertyResponse> items;
    PropertiesListAdapter adapter;
    SwipeRefreshLayout swipeLayout;
    RecyclerView recycler;
    private PropertiesListListener mListener;
    private Context ctx;
    private int mColumnCount = 1;
    private boolean asc;
//    MenuItem menuItemSort;
    private boolean earnedFilter = false;

//    @Override
//    protected FragmentToolbar builder() {
//        return new Builder()
//                .withMenu(R.menu.badges_menu)
//                .withTitle(R.string.badges_toolbar_title)
//                .build();
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.badges_menu, menu);
//        menuItemSort = menu.findItem(R.id.ascending);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.ascending:
//                if (asc) {
//                    listBadgesAndEarnedSort(UtilToken.getId(ctx));
//                    this.asc = !this.asc;
//                    menuItemSort.setIcon(R.drawable.ic_baseline_sort_down_24px);
//                } else {
//                    listBadgesAndEarnedSort(UtilToken.getId(ctx));
//                    menuItemSort.setIcon(R.drawable.ic_baseline_sort_up_24px);
//                    this.asc = !this.asc;
//                }
//                return true;
//            case R.id.badges_earned_filter:
//                if(item.isChecked()){
//                    earnedFilter = !earnedFilter;
//                    item.setChecked(earnedFilter);
//                    listBadgesAndEarned();
//                } else {
//                    earnedFilter = !earnedFilter;
//                    item.setChecked(earnedFilter);
//                    listBadgesAndEarnedFiltered();
//                }
////                updateTextView();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    public PropertiesListFragment() {}

    // TODO: Rename and change types and number of parameters
    public static PropertiesListFragment newInstance(int columnCount) {
        PropertiesListFragment fragment = new PropertiesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public void listProperties() {
        PropertyService service = ServiceGenerator.createService(PropertyService.class);
        Call<ResponseContainer<PropertyResponse>> call = service.listProperties();
        call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                if (response.code() != 200) {
                    Toast.makeText(getActivity(), "Request Error", Toast.LENGTH_SHORT).show();
                } else {
                    items = response.body().getRows();
                    adapter = new PropertiesListAdapter(ctx, items, mListener);
                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_properties_list, container, false);

        if (layout instanceof SwipeRefreshLayout) {
            ctx = layout.getContext();
            recycler = layout.findViewById(R.id.properties_list);
            if (mColumnCount <= 1) {
                recycler.setLayoutManager(new LinearLayoutManager(ctx));
            } else {
                recycler.setLayoutManager(new GridLayoutManager(ctx, mColumnCount));
            }
            items = new ArrayList<>();
            listProperties();
            adapter = new PropertiesListAdapter(ctx, items, mListener);
            recycler.setAdapter(adapter);

            swipeLayout = layout.findViewById(R.id.swipeContainer);
            swipeLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary), ContextCompat.getColor(getContext(), R.color.colorAccent));
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    listProperties();
                    if (swipeLayout.isRefreshing()) {
                        swipeLayout.setRefreshing(false);
                    }
                }
            });
        }
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PropertiesListListener) {
            mListener = (PropertiesListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PropertiesListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
