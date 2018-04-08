package com.example.tudor.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tudor.popularmovies.R;

/**
 * Created by tudor on 07.04.2018.
 */

public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.TrailerViewHolder> {


    @Override
    public TrailersRecyclerViewAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_trailer_item, null);

        return new TrailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrailersRecyclerViewAdapter.TrailerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {


        public TrailerViewHolder(View itemView) {
            super(itemView);
        }
    }

}
