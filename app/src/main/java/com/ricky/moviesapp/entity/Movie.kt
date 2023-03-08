package com.ricky.moviesapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie(
    @SerializedName("Title")
    @Expose
    val title: String,

    @SerializedName("Year")
    @Expose
    val year: String,

    @SerializedName("imdbID")
    @Expose
    @PrimaryKey
    val imdbID: String,

    @SerializedName("Type")
    @Expose
    val type: String,

    @SerializedName("Poster")
    @Expose
    val posterUrl: String,

    @SerializedName("Genre")
    @Expose
    val genre: String = "",

    @SerializedName("Actors")
    @Expose
    val actors: String = "",

    @SerializedName("Plot")
    @Expose
    val plot: String = "",

    @SerializedName("imdbRating")
    @Expose
    val rating: String = "",

    val hidden: Boolean = false
)