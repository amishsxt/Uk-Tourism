package com.example.uktourism.Views.Nav.ProfileFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.uktourism.R;
import com.example.uktourism.ViewModel.UserDataViewModel;
import com.example.uktourism.Views.Nav.ProfileFragment.ViewPagerAdapter.ViewPagerAdapter;
import com.example.uktourism.Views.ProgressBar.ProgressDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout personalInfo,donations,settings,aboutUs;
    private TextView userName,userEmail;
    private CircleImageView userPfp;
    private ProgressBar imgProgressBar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private UserDataViewModel userDataViewModel;


    public ProfileFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Casting views
        personalInfo=view.findViewById(R.id.card01);
        userName=view.findViewById(R.id.user_name);
        userEmail=view.findViewById(R.id.user_email);
        userPfp=view.findViewById(R.id.user_pfp);
        imgProgressBar = view.findViewById(R.id.image_progress_bar);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager2);

        //Observing user data
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getLiveData().observe(getViewLifecycleOwner(), user ->{

            // Update UI with user data
            userName.setText(user.getName());
            userEmail.setText(user.getEmail());
            setPicture(userPfp,user.getUserPfp());

            hideProgressDialog();
        });

        // Set up the ViewPager2 adapter
        viewPager2.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), getLifecycle()));

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if(position == 0){
                tab.setText("Posts");
            }
            else{
                tab.setText("FavPosts");
            }
        }).attach();

        //Navigating to Perosnal Information activity
        personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity currentActivity = getActivity();
                Intent intent = new Intent(currentActivity, PersInfoActivity.class);
                currentActivity.startActivity(intent);
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