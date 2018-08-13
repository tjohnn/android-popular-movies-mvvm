package com.tjohnn.popularmovies.ui.moviedetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tjohnn.popularmovies.R;
import com.tjohnn.popularmovies.ui.BaseView;
import com.tjohnn.popularmovies.utils.Constants;
import com.tjohnn.popularmovies.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MovieDetailFragment extends DaggerFragment {

    public static final String TAG = "MovieDetailFragment";
    public static final String MOVIE_ID_KEY = "MovieDataKey";

    @Inject
    Picasso picasso;

    @Inject
    BaseView baseView;

    @Inject
    MovieDetailViewModel mViewModel;

    @Inject
    AppCompatActivity mActivity;

    @Inject
    CompositeDisposable compositeDisposable;

    @BindView(R.id.iv_backdrop) ImageView backDropImageView;
    @BindView(R.id.iv_poster) ImageView posterImageView;
    @BindView(R.id.tv_title) TextView titleTextView;
    @BindView(R.id.tv_rating) TextView ratingTextView;
    @BindView(R.id.tv_release_date) TextView dateTextView;
    @BindView(R.id.tv_plot_synopsis) TextView synopsisTextView;
    @BindView(R.id.progress_bar_wrapper) FrameLayout progressBarWrapper;
    @BindView(R.id.message_wrapper) LinearLayout messageWrapper;
    @BindView(R.id.tv_message) TextView messageView;
    @BindView(R.id.btn_retry) Button retryButton;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance(String movieId) {
        Bundle args = new Bundle();
        args.putString(MOVIE_ID_KEY, movieId);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_detail, container, false);
        ButterKnife.bind(this, view);

        retryButton.setOnClickListener(this::reloadPage);
        subscribeToViewModel();
        return view;
    }

    private void reloadPage(View view) {
        if(compositeDisposable != null) compositeDisposable.clear();
        messageWrapper.setVisibility(View.GONE);
        subscribeToViewModel();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle("Movie Detail");
        baseView.displayUpNavigation(true);
    }



    private void subscribeToViewModel() {
        compositeDisposable.add(mViewModel.getMovieDetailUiModel()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateView, this::showNetworkError));

        compositeDisposable.add(mViewModel.getIsShowingLoadingIndicator()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setLoadingIndicator,
                        error -> {
                            Timber.d("Error showing loading indicator: %s", error.getMessage());
                            error.printStackTrace();
                        }
                ));

    }

    private void setLoadingIndicator(Boolean show) {
        progressBarWrapper.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showNetworkError(Throwable throwable) {
        String error = Utils.processNetworkError(throwable);
        messageView.setText(error);
        messageWrapper.setVisibility(View.VISIBLE);
    }

    private void updateView(MovieDetailUiModel movieDetailUiModel) {
        titleTextView.setText(movieDetailUiModel.getMovie().originalTitle);
        ratingTextView.setText(TextUtils.concat(String.valueOf(movieDetailUiModel.getMovie().voteAverage), "/10"));
        dateTextView.setText(movieDetailUiModel.getMovie().releaseDate);
        synopsisTextView.setText(movieDetailUiModel.getMovie().overview);
        picasso.load(TextUtils.concat(
                Constants.POSTERS_BASE_URL,
                Constants.POSTERS_W_500,
                movieDetailUiModel.getMovie().backdropPath).toString())
        .error(R.drawable.backdrop_placeholder).into(backDropImageView);

        picasso.load(TextUtils.concat(
                Constants.POSTERS_BASE_URL,
                Constants.POSTERS_W_342,
                movieDetailUiModel.getMovie().posterPath).toString())
                .error(R.drawable.placeholder_movie_image).into(posterImageView);
    }

}
