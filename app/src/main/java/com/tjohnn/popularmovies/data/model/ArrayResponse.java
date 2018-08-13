package com.tjohnn.popularmovies.data.model;

import java.util.List;

public class ArrayResponse<T> {

    public int page;
    public int totalResults;
    public int totalPages;
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
