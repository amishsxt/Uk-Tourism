package com.example.uktourism.Views.Nav.ProfileFragment.Tab.GridLayoutAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uktourism.Model.DataModel.Place;
import com.example.uktourism.R;
import com.example.uktourism.Views.Nav.HomeFragment.AllDestinationAdapter.AllDestinationViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridLayoutAdapter extends RecyclerView.Adapter<PostsViewHolder> {

    private Context context;
    private ArrayList<Place> userPlaces;
    private OnItemClickListener listener;

    public GridLayoutAdapter(Context context, ArrayList<Place> userPlaces) {
        this.context = context;
        this.userPlaces = userPlaces;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_place_layout, parent, false);
        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {

        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int imageWidth = screenWidth / 3;
        holder.linearLayout.getLayoutParams().width = imageWidth;
        holder.linearLayout.getLayoutParams().height = imageWidth;

        setPicture(holder, userPlaces.get(position).getPlacePic());

        holder.linearLayout.setOnClickListener(view -> {
            if(listener!=null){
                listener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userPlaces.size();
    }

    private void setPicture(PostsViewHolder holder, String imgUrl){

        // Show the progress bar
        holder.imgProgressbar.setVisibility(View.VISIBLE);
        holder.card.setVisibility(View.GONE);

        Picasso.get()
                .load(imgUrl)
                .error(R.drawable.img_error)
                .into(holder.placePic, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Hide the progress bar on success
                        holder.imgProgressbar.setVisibility(View.GONE);
                        holder.card.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Hide the progress bar on success
                        holder.imgProgressbar.setVisibility(View.GONE);
                        holder.card.setVisibility(View.VISIBLE);
                    }
                });
    }
}


