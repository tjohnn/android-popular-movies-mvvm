package com.tjohnn.popularmovies.ui.moviedetail;

import com.tjohnn.popularmovies.data.model.Movie;
import com.tjohnn.popularmovies.data.repository.MoviesRepository;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

public class MovieDetailViewModel {


    private final String movieId;
    private final MoviesRepository moviesRepository;
    private final BaseNavigator baseNavigator;
    private BehaviorSubject<Boolean> loadingSubject;

    public MovieDetailViewModel(String movieId, MoviesRepository moviesRepository, BaseNavigator baseNavigator) {

        this.movieId = movieId;
        this.moviesRepository = moviesRepository;
        this.baseNavigator = baseNavigator;
        loadingSubject = BehaviorSubject.create();
    }


    public BehaviorSubject<Boolean> getIsShowingLoadingIndicator() {
        return loadingSubject;
    }


    public Single<MovieDetailUiModel> getMovieDetailUiModel(){
        if (movieId == null) {
            return Single.error(new Exception("Movie id cannot be empty"));
        }
        return moviesRepository.getMovieById(movieId)
                .map(this::createModel)
                .doOnSubscribe(__ -> loadingSubject.onNext(true))
                .doAfterSuccess(__ -> loadingSubject.onNext(false))
                .doOnError(__ -> loadingSubject.onNext(false));
    }

    private MovieDetailUiModel createModel(Movie movie) {
        return new MovieDetailUiModel(movie);
    }



}
