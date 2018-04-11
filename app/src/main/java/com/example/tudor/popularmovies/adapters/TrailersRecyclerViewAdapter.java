package com.example.tudor.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tudor.popularmovies.R;
import com.example.tudor.popularmovies.data.Trailer;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tudor on 07.04.2018.
 */

public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.TrailerViewHolder> {

    private ArrayList<Trailer> mTrailers;
    private Context mContext;
    public TrailersRecyclerViewAdapter(ArrayList<Trailer> trailers, Context context) {
        this.mTrailers = trailers;
        this.mContext = context;
    }

    @Override
    public TrailersRecyclerViewAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_trailer_item, null);
        return new TrailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrailersRecyclerViewAdapter.TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        holder.setData(trailer);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Trailer mTrailer;
        @BindView(R.id.trailer_thumbnail_iv) ImageView trailerThumbnail;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }


        public void setData(Trailer trailer) {
            this.mTrailer = trailer;

            Picasso.with(mContext)
                    .load(trailer.getImageUrl())
                    .into(trailerThumbnail);

        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mTrailer.getVideoUrl()));
            mContext.startActivity(i);
        }
    }

}
