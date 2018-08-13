package com.tjohnn.popularmovies.ui.movies;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tjohnn.popularmovies.R;
import com.tjohnn.popularmovies.data.PreferenceHelper;
import com.tjohnn.popularmovies.ui.BaseView;
import com.tjohnn.popularmovies.utils.AutoFitRecyclerView;
import com.tjohnn.popularmovies.utils.Constants;
import com.tjohnn.popularmovies.utils.Utils;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MoviesFragment extends DaggerFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = MoviesFragment.class.getSimpleName();

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

    @BindView(R.id.rv_movies)
    AutoFitRecyclerView moviesRecyclerView;

    @BindView(R.id.tv_message)
    TextView messageView;


    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLoading;

    private String sortOrder;


    private MoviesAdapter.MovieItemLister movieItemListener = movieId -> {
        viewModel.openDetails(movieId);
    };

    public static MoviesFragment newInstance() {

        Bundle args = new Bundle();

        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesAdapter = new MoviesAdapter(new ArrayList<>(), movieItemListener, picasso);
        sortOrder = preferenceHelper.getMovieSortingOrder();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);
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
        radioGroup.check(Constants.BY_POPULARITY_VALUE.equals(sortOrder) ? R.id.radio_popularity : R.id.radio_rating);

        radioGroup.setOnCheckedChangeListener(((group, checkedId) -> {
            String order;
            if(checkedId == R.id.radio_popularity){
                order = Constants.BY_POPULARITY_VALUE;
            } else {
                order = Constants.BY_TOP_RATED_VALUE;
            }
            preferenceHelper.updateSortingOrder(order);
            sortOrder = order;
            compositeDisposable.clear();
            subscribeToViewModel(true);
            dialog.dismiss();
        }));

        dialog.show();
    }

    private void subscribeToViewModel(boolean forceRefresh) {

        compositeDisposable.add(viewModel.loadMovies(sortOrder, forceRefresh)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateView, this::showNetworkError));

        compositeDisposable.add(viewModel.getIsShowingLoadingIndicator()
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

    private void showNetworkError(Throwable throwable) {
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
        moviesAdapter.updateItems(moviesUiModel.getMovieList().results);
    }

    private void setLoadingIndicator(boolean show){
        isLoading = show;
        if(show) progressBarWrapper.setVisibility(View.VISIBLE);
        else progressBarWrapper.setVisibility(View.GONE);
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
