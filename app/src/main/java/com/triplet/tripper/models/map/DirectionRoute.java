package com.triplet.tripper.models.map;

import com.squareup.moshi.Json;

import java.util.List;

public class DirectionRoute {
    @Json(name="weight_name")
    public String weight_name;

    @Json(name="weight")
    public double weight;

    @Json(name="duration")
    public double duration;

    @Json(name="distance")
    public double distance;

    @Json(name="legs")
    public List<DirectionLeg> legs;

    @Json(name="geometry")
    public Geometry geometry;

}
