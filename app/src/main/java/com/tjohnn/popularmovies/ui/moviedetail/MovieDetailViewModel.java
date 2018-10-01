package com.tjohnn.popularmovies.ui.moviedetail;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.tjohnn.popularmovies.data.dtos.ArrayResponse;
import com.tjohnn.popularmovies.data.dtos.Review;
import com.tjohnn.popularmovies.data.dtos.Trailer;
import com.tjohnn.popularmovies.data.local.FavoriteMovie;
import com.tjohnn.popularmovies.data.dtos.Movie;
import com.tjohnn.popularmovies.data.repository.LocalMovieRepository;
import com.tjohnn.popularmovies.data.repository.MoviesRepository;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

public class MovieDetailViewModel extends ViewModel{


    private final String movieId;
    private final MoviesRepository moviesRepository;
    private LocalMovieRepository localMovieRepository;
    private final BaseNavigator baseNavigator;
    private BehaviorSubject<Boolean> loadingSubject;
    private BehaviorSubject<String> snackbarMessageSubject;
    LiveData<FavoriteMovie> favoriteMovie;

    MovieDetailViewModel(String movieId, MoviesRepository moviesRepository,
                         LocalMovieRepository localMovieRepository, BaseNavigator baseNavigator) {

        this.movieId = movieId;
        this.moviesRepository = moviesRepository;
        this.localMovieRepository = localMovieRepository;
        this.baseNavigator = baseNavigator;
        loadingSubject = BehaviorSubject.create();
        snackbarMessageSubject = BehaviorSubject.create();
        favoriteMovie = localMovieRepository.getFavoriteMovieById(Long.parseLong(movieId));
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


    public Single<ArrayResponse<Trailer>> getMovieTrailers(){
        return moviesRepository.getMovieTrailers(movieId);
    }

    public Single<ArrayResponse<Review>> getMovieReviews(){
        return moviesRepository.getMovieReviews(movieId);
    }

    public Completable addMovieToFavorite(Movie movie){
        return localMovieRepository.createFavoriteMovie(new FavoriteMovie(movie));
    }

    public Completable deleteFavoriteMovie(Movie movie){
        return localMovieRepository.deleteFavoriteMovie(new FavoriteMovie(movie));
    }

    public LiveData<FavoriteMovie> getFavoriteMovieById(){
        return favoriteMovie;
    }

    private MovieDetailUiModel createModel(Movie movie) {
        return new MovieDetailUiModel(movie);
    }

    public static class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {


        private final String movieId;
        private final MoviesRepository moviesRepository;
        private final LocalMovieRepository localMovieRepository;
        private final BaseNavigator baseNavigator;

        ViewModelFactory(String movieId, MoviesRepository moviesRepository,
                         LocalMovieRepository localMovieRepository, BaseNavigator baseNavigator) {
            this.movieId = movieId;
            this.moviesRepository = moviesRepository;
            this.localMovieRepository = localMovieRepository;
            this.baseNavigator = baseNavigator;
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new MovieDetailViewModel(movieId, moviesRepository, localMovieRepository,  baseNavigator);
        }
    }


}
