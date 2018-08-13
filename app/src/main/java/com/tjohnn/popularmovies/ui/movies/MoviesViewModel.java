package com.tjohnn.popularmovies.ui.movies;

import com.tjohnn.popularmovies.data.model.ArrayResponse;
import com.tjohnn.popularmovies.data.model.Movie;
import com.tjohnn.popularmovies.data.repository.MoviesRepository;
import com.tjohnn.popularmovies.utils.Constants;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;


public class MoviesViewModel {

    MoviesRepository moviesRepository;
    private BaseNavigator mNavigator;

    private BehaviorSubject<Boolean> loadingSubject;

    public MoviesViewModel(MoviesRepository moviesRepository, BaseNavigator baseNavigator){
        this.moviesRepository = moviesRepository;
        mNavigator = baseNavigator;
        loadingSubject = BehaviorSubject.create();
    }


    public BehaviorSubject<Boolean> getIsShowingLoadingIndicator() {
        return loadingSubject;
    }



    public Single<MoviesUiModel> loadMovies(String order, boolean forceRefresh){
        Single<ArrayResponse<Movie>> observer = null;
        if(Constants.BY_POPULARITY_VALUE.equals(order)){
            observer = moviesRepository.getMoviesByPopularity(forceRefresh);
        }
        else if (Constants.BY_TOP_RATED_VALUE.equals(order)) {
            observer = moviesRepository.getMoviesByRating(forceRefresh);
        }
        else {
            throw new RuntimeException("Invalid movies order type");
        }

        return observer
                .map(this::makeUiModel)
                .doOnSubscribe(__ -> loadingSubject.onNext(true))
                .doAfterSuccess(__ -> loadingSubject.onNext(false))
                .doOnError(__ -> loadingSubject.onNext(false));

    }

    public void openDetails(String movieId){
        mNavigator.openMovieDetailFragment(movieId);
    }

    private MoviesUiModel makeUiModel(ArrayResponse<Movie> response) {
        List<Movie> movies = response.results;
        boolean isMovieGirdVisible = !movies.isEmpty();
        return new MoviesUiModel(isMovieGirdVisible, response);
    }

}
