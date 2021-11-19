package com.triplet.tripper.models.map;

import com.squareup.moshi.Json;

import java.util.List;

public class Marker {
    @Json(name = "lat")
    public double lat;

    @Json(name = "lng")
    public double lng;

    @Json(name = "name_maker")
    public String name_marker;

    @Json(name = "words")
    public List<String> words;
}
