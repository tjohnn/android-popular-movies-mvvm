package com.tjohnn.popularmovies.ui.moviedetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tjohnn.popularmovies.R;
import com.tjohnn.popularmovies.data.dtos.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private List<Trailer> items;
    private OnTrailerItemListener mListener;

    public TrailersAdapter(@NonNull List<Trailer> trailers, @NonNull OnTrailerItemListener listener) {
        items = trailers;
        mListener = listener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailers, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bindData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<Trailer> results) {
        items = results;
        notifyDataSetChanged();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_trailer)
        TextView nameTextView;

        @BindView(R.id.tv_trailer_size)
        TextView sizeTextView;

        Trailer mTrailer;

        TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> mListener.onItemClicked(mTrailer));
        }

        void bindData(Trailer trailer){
            mTrailer = trailer;
            nameTextView.setText(trailer.name);
            sizeTextView.setText(trailer.size);
        }
    }


    interface OnTrailerItemListener {
       void  onItemClicked(Trailer trailer);
    }
}
