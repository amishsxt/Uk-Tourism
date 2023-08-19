package com.example.uktourism.Views.Nav.ProfileFragment.Tab.GridLayoutAdapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uktourism.R;

public class PostsViewHolder extends RecyclerView.ViewHolder {

    ImageView placePic;
    FrameLayout card;
    ProgressBar imgProgressbar;
    LinearLayout linearLayout;

    public PostsViewHolder(@NonNull View itemView) {
        super(itemView);

        placePic=itemView.findViewById(R.id.place_pic);
        card=itemView.findViewById(R.id.card);
        imgProgressbar=itemView.findViewById(R.id.image_progress_bar);
        linearLayout=itemView.findViewById(R.id.linear_layout);
    }
}
