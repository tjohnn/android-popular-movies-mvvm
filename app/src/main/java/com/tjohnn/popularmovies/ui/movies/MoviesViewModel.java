package com.tjohnn.popularmovies.ui.movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.tjohnn.popularmovies.data.dtos.ArrayResponse;
import com.tjohnn.popularmovies.data.dtos.Movie;
import com.tjohnn.popularmovies.data.repository.LocalMovieRepository;
import com.tjohnn.popularmovies.data.repository.MoviesRepository;
import com.tjohnn.popularmovies.utils.Constants;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;


public class MoviesViewModel extends AndroidViewModel {

    private final MoviesRepository moviesRepository;
    private BaseNavigator mNavigator;
    private LocalMovieRepository localMovieRepository;

    private BehaviorSubject<Boolean> loadingSubject;
    private BehaviorSubject<Boolean> loadingMoreSubject;
    private LiveData<MoviesUiModel> movieLiveData;
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private int currentPage;
    private int totalPages;

    MoviesViewModel(@NonNull Application application, MoviesRepository moviesRepository, BaseNavigator baseNavigator, LocalMovieRepository localMovieRepository){
        super(application);
        this.moviesRepository = moviesRepository;
        mNavigator = baseNavigator;
        this.localMovieRepository = localMovieRepository;
        loadingSubject = BehaviorSubject.create();
        loadingMoreSubject = BehaviorSubject.create();
        movies.setValue(new ArrayList<>());
        movieLiveData = Transformations.map(localMovieRepository.getFavoriteMovies(), this::makeUiModel);
    }


    public BehaviorSubject<Boolean> getIsShowingLoadingIndicator() {
        return loadingSubject;
    }


    public BehaviorSubject<Boolean> getIsShowingLoadingMoreIndicator() {
        return loadingMoreSubject;
    }


    public LiveData<MoviesUiModel> getMovieLiveData() {
        resetPage();
        return movieLiveData;
    }

    public Single<MoviesUiModel> loadMovies(String order, boolean forceRefresh, boolean sortMethodChanged){
        Timber.d(movies.getValue().toString());
        Timber.d(currentPage+"");
        Timber.d(totalPages+"");
        if(sortMethodChanged) {
            resetPage();
        }
        else if(!movies.getValue().isEmpty()) {
            return Single.just(makeUiModel(movies.getValue()));
        }
        else {
            Timber.d("Movies list is empty");
        }

        return getMoviesUiModel(order, forceRefresh, false);

    }

    public Single<MoviesUiModel> loadMoreMovies(String order){
        currentPage++;
        Timber.d(movies.getValue().toString());
        Timber.d(currentPage+"");
        Timber.d(totalPages+"");
        if(currentPage > totalPages) {
            throw new RuntimeException("No more movies to load.");
        }


        return getMoviesUiModel(order, true, true);

    }

    private Single<MoviesUiModel> getMoviesUiModel(String order, boolean forceRefresh, boolean isLoadingMore) {
        Single<ArrayResponse<Movie>> observable;
        if(Constants.BY_POPULARITY_VALUE.equals(order)){
            observable = moviesRepository.getMoviesByPopularity(forceRefresh, currentPage);
        }
        else if (Constants.BY_TOP_RATED_VALUE.equals(order)) {
            observable = moviesRepository.getMoviesByRating(forceRefresh, currentPage);
        }
        else {
            throw new RuntimeException("Invalid movies order type");
        }

        return observable
                .map(r -> {
                    currentPage = r.page;
                    totalPages = r.totalPages;
                    return r.results;
                })
                .map(this::makeUiModel)
                .doOnSubscribe(__ -> {
                    if (isLoadingMore) loadingMoreSubject.onNext(true);
                    else loadingSubject.onNext(true);
                })
                .doAfterSuccess(__ -> {
                    if (isLoadingMore) loadingMoreSubject.onNext(false);
                    else loadingSubject.onNext(false);
                })
                .doOnError(__ -> {
                    if (isLoadingMore) loadingMoreSubject.onNext(false);
                    else loadingSubject.onNext(false);
                });
    }


    private void resetPage() {
        currentPage = 1;
        movies.setValue(new ArrayList<>());
    }

    public void openDetails(String movieId){
        mNavigator.openMovieDetailFragment(movieId);
    }

    private MoviesUiModel makeUiModel(List<Movie> movies) {
        List<Movie> l = this.movies.getValue();
        l.addAll(movies);
        this.movies.postValue(l);
        boolean isMovieGirdVisible = !movies.isEmpty();
        return new MoviesUiModel(isMovieGirdVisible, this.movies.getValue());
    }

    public static class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        private Application application;
        private final MoviesRepository moviesRepository;
        private final LocalMovieRepository localMovieRepository;
        private final BaseNavigator baseNavigator;

        ViewModelFactory(@NonNull Application application, MoviesRepository moviesRepository,
                         LocalMovieRepository localMovieRepository, BaseNavigator baseNavigator) {
            this.application = application;
            this.moviesRepository = moviesRepository;
            this.localMovieRepository = localMovieRepository;
            this.baseNavigator = baseNavigator;
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new MoviesViewModel(application, moviesRepository, baseNavigator, localMovieRepository);
        }
    }

}
