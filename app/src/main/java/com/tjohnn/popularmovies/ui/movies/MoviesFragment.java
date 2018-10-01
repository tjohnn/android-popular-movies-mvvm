package com.tjohnn.popularmovies.ui.movies;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tjohnn.popularmovies.R;
import com.tjohnn.popularmovies.data.PreferenceHelper;
import com.tjohnn.popularmovies.ui.BaseView;
import com.tjohnn.popularmovies.ui.moviedetail.MovieDetailFragment;
import com.tjohnn.popularmovies.utils.AutoFitRecyclerView;
import com.tjohnn.popularmovies.utils.Constants;
import com.tjohnn.popularmovies.utils.Utils;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;
import com.tjohnn.popularmovies.utils.navigator.Navigator;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MoviesFragment extends DaggerFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = MoviesFragment.class.getSimpleName();
    private static final String RECYCLER_VIEW_STATE_BUNDLE = "RecyclerViewLayoutBundle";


    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    MoviesViewModel viewModel;

    @Inject
    Picasso picasso;

    @Inject
    BaseView baseView;

    @Inject
    AppCompatActivity mActivity;

    @Inject
    BaseNavigator mNavigator;

    @Inject
    PreferenceHelper preferenceHelper;

    private MoviesAdapter moviesAdapter;

    @BindView(R.id.progress_bar_wrapper)
    FrameLayout progressBarWrapper;

    @BindView(R.id.progress_bar_wrapper_more)
    FrameLayout progressBarMoreWrapper;

    @BindView(R.id.rv_movies)
    AutoFitRecyclerView moviesRecyclerView;

    @BindView(R.id.tv_message)
    TextView messageView;


    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLoading;
    private boolean sortMethodChanged;

    private String sortOrder;


    private MoviesAdapter.MovieItemLister movieItemListener;

    public static MoviesFragment newInstance() {

        Bundle args = new Bundle();

        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortOrder = preferenceHelper.getMovieSortingOrder();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);

        movieItemListener = movieId -> {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movieId);
            Navigator.openFragment(getActivity().getSupportFragmentManager(), fragment, MovieDetailFragment.TAG, R.id.content_main);
        };
        moviesAdapter = new MoviesAdapter(new ArrayList<>(), movieItemListener, picasso);


        moviesRecyclerView.setHasFixedSize(true);
        moviesRecyclerView.setAdapter(moviesAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        subscribeToViewModel(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle("Popular Movies");
        baseView.displayUpNavigation(false);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE_BUNDLE);
            moviesRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(RECYCLER_VIEW_STATE_BUNDLE, moviesRecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.sort_movies){
            showSortingDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortingDialog() {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sort_movies);
        dialog.setTitle("Select sorting type");

        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);
        radioGroup.check(Constants.BY_POPULARITY_VALUE.equals(sortOrder) ?
                R.id.radio_popularity : Constants.BY_TOP_RATED_VALUE.equals(sortOrder) ? R.id.radio_rating : R.id.radio_favorite);

        radioGroup.setOnCheckedChangeListener(((group, checkedId) -> {
            String order;
            if(checkedId == R.id.radio_popularity){
                order = Constants.BY_POPULARITY_VALUE;
            } else if (checkedId == R.id.radio_rating){
                order = Constants.BY_TOP_RATED_VALUE;
            } else {
                order = Constants.BY_FAVOURITE;
            }
            preferenceHelper.updateSortingOrder(order);
            sortOrder = order;
            compositeDisposable.clear();
            sortMethodChanged = true;
            subscribeToViewModel(true);

            dialog.dismiss();
        }));

        dialog.show();
    }

    private void subscribeToViewModel(boolean forceRefresh) {

        if(Constants.BY_FAVOURITE.equals(sortOrder)){
            viewModel.getMovieLiveData().observe(this, this::updateView);
        } else {
            // remove livedata observer to avoid interruption
            viewModel.getMovieLiveData().removeObservers(this);

            compositeDisposable.add(viewModel.loadMovies(sortOrder, forceRefresh, sortMethodChanged)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateView, this::showError));
        }

        compositeDisposable.add(viewModel.getIsShowingLoadingIndicator()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setLoadingIndicator));


        moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(!isLoading && !recyclerView.canScrollVertically(1) && !Constants.BY_FAVOURITE.equals(sortOrder)){
                    compositeDisposable.clear();
                    compositeDisposable.add(viewModel.loadMoreMovies(sortOrder)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(MoviesFragment.this::updateView, MoviesFragment.this::showError));

                    compositeDisposable.add(viewModel.getIsShowingLoadingMoreIndicator()
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(MoviesFragment.this::setLoadingMoreIndicator));
                }
            }
        });
    }

    private void showError(Throwable throwable) {
        String message = Utils.processNetworkError(throwable).concat("\n").concat(getString(R.string.swipe_to_retry));
        messageView.setText(message);
        messageView.setVisibility(View.VISIBLE);
        moviesRecyclerView.setVisibility(View.GONE);
    }

    private void updateView(MoviesUiModel moviesUiModel) {
        if(moviesUiModel.isMoviesGridVisible()){
            moviesRecyclerView.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.GONE);
        }
        else if (Constants.BY_FAVOURITE.equals(sortOrder)){
            showError(new Throwable("You have not added any favorite movie."));
        }
        moviesAdapter.updateItems(moviesUiModel.getMovieList());

        sortMethodChanged = false;
    }

    private void setLoadingIndicator(boolean show){
        isLoading = show;
        if(show) progressBarWrapper.setVisibility(View.VISIBLE);
        else progressBarWrapper.setVisibility(View.GONE);
    }

    private void setLoadingMoreIndicator(boolean show){
        isLoading = show;
        if(show) progressBarMoreWrapper.setVisibility(View.VISIBLE);
        else progressBarMoreWrapper.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        if(!isLoading){
            compositeDisposable.clear();
            subscribeToViewModel(true);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
