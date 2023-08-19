package com.example.uktourism.Views.Nav.ProfileFragment.Tab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.uktourism.Model.DataModel.Place;
import com.example.uktourism.R;
import com.example.uktourism.ViewModel.PlaceDataViewModel;
import com.example.uktourism.Views.Nav.ProfileFragment.Tab.GridLayoutAdapter.GridLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridLayout gridLayout;
    private RecyclerView recyclerView;
    private GridLayoutAdapter gridLayoutAdapter;
    private List<Place> userPlaces = new ArrayList<>();

    private PlaceDataViewModel placeDataViewModel;

    public PostsTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostsTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostsTabFragment newInstance(String param1, String param2) {
        PostsTabFragment fragment = new PostsTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_posts_tab, container, false);

        //casting views
        recyclerView = view.findViewById(R.id.vertical_recycler_view);

        placeDataViewModel = new ViewModelProvider(this).get(PlaceDataViewModel.class);
        //observe data
        placeDataViewModel.getUserPlacesData().observe(this, new Observer<ArrayList<Place>>() {
            @Override
            public void onChanged(ArrayList<Place> places) {

                //setting up gridLayout Adapter
                gridLayoutAdapter = new GridLayoutAdapter(getContext(),places);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                recyclerView.setAdapter(gridLayoutAdapter);

                // Set item click listener
                gridLayoutAdapter.setOnItemClickListener(position -> {
                    // Handle item click event here
                    // You can use the position to get the clicked item's data
                    Activity currentActivity = getActivity();
                    Intent intent = new Intent(currentActivity, UserPostsActivity.class);
                    intent.putExtra("position",position);
                    startActivity(intent);

                });

            }
        });



        return view;
    }
}