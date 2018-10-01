package com.tjohnn.popularmovies.data.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArrayResponse<T> {

    public long id;
    public int page;
    public int totalResults;
    public int totalPages;
    @SerializedName(value="results", alternate={"youtube"})
    public List<T> results;

    @Override
    public String toString() {
        return "ArrayReponse{" +
                "page=" + page +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                ", results=" + results +
                '}';
    }
}
