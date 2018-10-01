package com.tjohnn.popularmovies.data.dtos;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    public String name;
    public String size;
    @SerializedName(value = "source", alternate = {"key"})
    public String source;
    public String type;

    @Override
    public String toString() {
        return "Trailer{" +
                "name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
