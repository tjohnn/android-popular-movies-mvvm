package com.tjohnn.popularmovies.ui.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tjohnn.popularmovies.R;
import com.tjohnn.popularmovies.data.dtos.Movie;
import com.tjohnn.popularmovies.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movie> mItems;
    private MovieItemLister mListener;
    private Picasso picasso;

    public MoviesAdapter(List<Movie> movies, MovieItemLister movieItemLister, Picasso picasso) {
        mItems = movies;
        mListener = movieItemLister;
        this.picasso = picasso;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie_list, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindItem(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public void updateItems(List<Movie> movies) {
        mItems = movies;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private Movie movie;
        @BindView(R.id.iv_poster)
        ImageView posterView;
        @BindView(R.id.progress_bar_wrapper)
        FrameLayout progressBar;

        Callback imageLoadCallBack = new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onError() {

            }
        };

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> mListener.onMovieItemClicked(String.valueOf(movie.id)));
            posterView.setTag(imageLoadCallBack);
        }

        void bindItem(Movie movie){
            this.movie = movie;
            picasso.load(Constants.POSTERS_BASE_URL + Constants.POSTERS_W_185 + movie.posterPath)
            .error(R.drawable.placeholder_movie_image)
            .into(posterView, imageLoadCallBack);
        }

    }


    interface MovieItemLister {
        void onMovieItemClicked(String movieId);
    }
}
