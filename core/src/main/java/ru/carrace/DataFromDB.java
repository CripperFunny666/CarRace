package ru.carrace;

import com.google.gson.annotations.SerializedName;

public class DataFromDB {
    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("score")
    int score;

    @SerializedName("coins")
    int coins;

    @SerializedName("created")
    String created;
}
