package com.example.uktourism.Views.Nav.HomeFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.uktourism.Model.DataModel.Place;
import com.example.uktourism.R;
import com.example.uktourism.ViewModel.PlaceDataViewModel;
import com.example.uktourism.ViewModel.UserDataViewModel;
import com.example.uktourism.Views.Nav.HomeFragment.AllDestinationAdapter.AllDestinationAdapter;
import com.example.uktourism.Views.Nav.HomeFragment.BestDestinationAdapter.BestDestinationAdapter;
import com.example.uktourism.Views.ProgressBar.ProgressDialogFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView horizontalRecyclerView, verticalRecyclerView;
    private LinearLayout addPlaceBtn;
    private ProgressBar progressBar;
    private BestDestinationAdapter bestDestinationAdapter;
    private AllDestinationAdapter allDestinationAdapter;
    private CircleImageView userPfp;
    private ProgressBar imgProgressBar;
    private TextView userName;
    private UserDataViewModel userDataViewModel;
    private PlaceDataViewModel placeDataViewModel;
    private ArrayList<Place> placeAdapterArrayList;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //casting view
        horizontalRecyclerView = view.findViewById(R.id.horizontal_recycler_view);
        verticalRecyclerView = view.findViewById(R.id.vertical_recycler_view);
        userName = view.findViewById(R.id.user_name);
        userPfp = view.findViewById(R.id.user_pfp);
        imgProgressBar = view.findViewById(R.id.image_progress_bar);
        addPlaceBtn = view.findViewById(R.id.add_place_btn);

        showProgressDialog();

        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        placeDataViewModel = new ViewModelProvider(this).get(PlaceDataViewModel.class);

        //Observing user data
        userDataViewModel.getLiveData().observe(getViewLifecycleOwner(), user ->{

            // Update UI with user data
            userName.setText(user.getName());
            setPicture(userPfp,user.getUserPfp());

            hideProgressDialog();

        });

        //addPlace Btn
        addPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity currentActivity = getActivity();
                Intent intent = new Intent(currentActivity, AddPlaceActivity.class);
                currentActivity.startActivity(intent);
            }
        });

        //horizontal Recycler Adapter
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        //vertical Recycler Adapter
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        placeDataViewModel.getAllPlacesData().observe(this, new Observer<ArrayList<Place>>() {
            @Override
            public void onChanged(ArrayList<Place> places) {

                placeAdapterArrayList = places;
                allDestinationAdapter = new AllDestinationAdapter(getContext(),placeAdapterArrayList);
                verticalRecyclerView.setAdapter(allDestinationAdapter);

                bestDestinationAdapter = new BestDestinationAdapter(getContext(),placeAdapterArrayList);
                horizontalRecyclerView.setAdapter(bestDestinationAdapter);

            }
        });


        return view;

    }

    private void setPicture(ImageView imageView, String imgUrl){
        // Show the progress bar
        imgProgressBar.setVisibility(View.VISIBLE);
        userPfp.setVisibility(View.GONE);

        Picasso.get()
                .load(imgUrl)
                .error(R.drawable.img_error)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Hide the progress bar on success
                        imgProgressBar.setVisibility(View.GONE);
                        userPfp.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Hide the progress bar on success
                        imgProgressBar.setVisibility(View.GONE);
                        userPfp.setVisibility(View.VISIBLE);
                    }
                });

    }

    private void showProgressDialog(){
        ProgressDialogFragment progressDialog = new ProgressDialogFragment();
        progressDialog.show(getChildFragmentManager(), "progress_dialog_tag");
    }

    private void hideProgressDialog(){
        ProgressDialogFragment progressDialog = (ProgressDialogFragment) getChildFragmentManager().findFragmentByTag("progress_dialog_tag");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}