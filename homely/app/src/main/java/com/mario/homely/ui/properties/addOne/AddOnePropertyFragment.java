package com.mario.homely.ui.properties.addOne;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mario.homely.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddOnePropertyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddOnePropertyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddOnePropertyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnAdd;
    private EditText title, description, price, rooms, size, categoryId, address, zipcode, city, province;


    private AddPropertyListener mListener;

    public AddOnePropertyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddOnePropertyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddOnePropertyFragment newInstance(String param1, String param2) {
        AddOnePropertyFragment fragment = new AddOnePropertyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        btnAdd = getActivity().findViewById(R.id.btn_add_one_property_submit);
        btnAdd.setOnClickListener(v -> mListener.onAddSubmit(title, description, price, rooms, size, categoryId, address, zipcode, city, province));
        return inflater.inflate(R.layout.fragment_add_one_property, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddPropertyListener) {
            mListener = (AddPropertyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddPropertyListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
