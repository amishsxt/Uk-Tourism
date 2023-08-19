package com.example.uktourism.Views.Nav.FavFragment.FavAdapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uktourism.R;
import com.google.android.material.imageview.ShapeableImageView;

public class FavViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView placePic;
    TextView placeName,placeLocation;
    FrameLayout card, likeBtn;
    ProgressBar imgProgressbar;


    public FavViewHolder(@NonNull View itemView) {
        super(itemView);

        placePic=itemView.findViewById(R.id.place_pic);
        placeName=itemView.findViewById(R.id.place_name);
        placeLocation=itemView.findViewById(R.id.place_location);
        card=itemView.findViewById(R.id.card);
        imgProgressbar=itemView.findViewById(R.id.image_progress_bar);
        likeBtn=itemView.findViewById(R.id.like_btn);
    }
}
