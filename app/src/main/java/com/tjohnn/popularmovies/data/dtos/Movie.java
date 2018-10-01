package com.tjohnn.popularmovies.data.dtos;

import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import com.tjohnn.popularmovies.data.local.FavoriteMovie;

import java.util.List;

public class Movie implements Parcelable {

    @Ignore
    public int voteCount;
    @Ignore
    public List<Integer> genreIds;
    public long id;
    @Ignore
    public boolean video;
    @Ignore
    public boolean adult;
    public double voteAverage;
    @Ignore
    public double popularity;
    public String title;
    public String posterPath;
    public String backdropPath;
    @Ignore
    public String originalLanguage;
    public String originalTitle;
    public String overview;
    public String releaseDate;

    public Movie(long id, double voteAverage, String title, String posterPath, String backdropPath,
                 String originalTitle, String overview, String releaseDate) {
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public Movie(FavoriteMovie movie) {
        id = movie.id;
        originalTitle = movie.originalTitle;
        overview = movie.overview;
        voteAverage = movie.voteAverage;
        releaseDate = movie.releaseDate;
        backdropPath = movie.backdropPath;
        posterPath = movie.posterPath;
        title = movie.title;
    }

    protected Movie(Parcel in) {
        voteCount = in.readInt();
        id = in.readLong();
        video = in.readByte() != 0;
        adult = in.readByte() != 0;
        voteAverage = in.readDouble();
        popularity = in.readDouble();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie{" +
                "voteCount=" + voteCount +
                ", genreIds=" + genreIds +
                ", id=" + id +
                ", video=" + video +
                ", adult=" + adult +
                ", voteAverage=" + voteAverage +
                ", popularity=" + popularity +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(voteCount);
        parcel.writeLong(id);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeDouble(voteAverage);
        parcel.writeDouble(popularity);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }
}
