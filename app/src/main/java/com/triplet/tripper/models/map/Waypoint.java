package com.triplet.tripper.models.map;

import com.squareup.moshi.Json;

import java.util.List;

public class Waypoint {
    @Json(name="distance")
    public double distance;

    @Json(name="name")
    public String name;

    @Json(name="location")
    public List<Double> location;
}
