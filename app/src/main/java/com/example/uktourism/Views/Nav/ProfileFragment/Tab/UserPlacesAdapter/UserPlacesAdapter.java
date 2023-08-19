package com.example.uktourism.Views.Nav.ProfileFragment.Tab.UserPlacesAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uktourism.Model.DataModel.Place;
import com.example.uktourism.R;
import com.example.uktourism.Views.Nav.HomeFragment.AllDestinationAdapter.AllDestinationViewHolder;
import com.example.uktourism.Views.Nav.ProfileFragment.Tab.GridLayoutAdapter.GridLayoutAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserPlacesAdapter extends RecyclerView.Adapter<UserPostsViewHolder> {

    private Context context;
    private ArrayList<Place> arrayList;
    private OnItemClickListener listener;

    public UserPlacesAdapter(Context context, ArrayList<Place> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(UserPlacesAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public UserPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserPostsViewHolder(LayoutInflater.from(context).inflate(R.layout.user_posts_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserPostsViewHolder holder, int position) {
        holder.placeName.setText(arrayList.get(position).getPlaceName());
        holder.placeLocation.setText(arrayList.get(position).getLocation());
        setPicture(holder, arrayList.get(position).getPlacePic());

        holder.deleteBtn.setOnClickListener(view -> {
            if(listener!=null){
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void setPicture(UserPostsViewHolder holder, String imgUrl){

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
