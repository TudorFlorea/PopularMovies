package com.example.tudor.popularmovies.adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tudor.popularmovies.R;
import com.example.tudor.popularmovies.data.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tudor on 09.04.2018.
 */

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ReviewViewHolder> {

    private ArrayList<Review> mReviews;
    private Context mContext;

    public ReviewsRecyclerViewAdapter(ArrayList<Review> reviews, Context context) {
        this.mReviews = reviews;
        this.mContext = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_review_item, null);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review trailer = mReviews.get(position);
        holder.setData(trailer);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private Review mReview;

        @BindView(R.id.single_review_cv) CardView mReviewCardView;
        @BindView(R.id.review_content_tv) TextView mReviewContentTextView;
        @BindView(R.id.review_author_tv) TextView mReviewAuthorTextView;
        @BindView(R.id.review_read_more_btn) Button mReviewReadMoreButton;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mReviewCardView.getLayoutParams().width = getWindowWidth();

        }

        public void setData(Review review) {
            mReviewContentTextView.setText(review.getShrotContent());
            mReviewAuthorTextView.setText(review.getAuthor());
            mReviewReadMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Working", Toast.LENGTH_SHORT).show();
                }
            });
        }

        int getWindowWidth() {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.x - 20;
        }
    }

}
