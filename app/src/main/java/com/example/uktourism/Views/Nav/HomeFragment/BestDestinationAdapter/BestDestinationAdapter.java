package com.example.uktourism.Views.Nav.HomeFragment.BestDestinationAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uktourism.Model.DataModel.Place;
import com.example.uktourism.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BestDestinationAdapter extends RecyclerView.Adapter<BestDestinationViewHolder> {

    private Context context;
    private ArrayList<Place> arrayList;

    public BestDestinationAdapter(Context context, ArrayList<Place> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public BestDestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new BestDestinationViewHolder(LayoutInflater.from(context).inflate(R.layout.horizontal_recycler_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BestDestinationViewHolder holder, int position) {
        holder.placeName.setText(arrayList.get(position).getPlaceName());
        holder.placeLocation.setText(arrayList.get(position).getLocation());
        setPicture(holder, arrayList.get(position).getPlacePic());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void setPicture(BestDestinationViewHolder holder, String imgUrl){

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
