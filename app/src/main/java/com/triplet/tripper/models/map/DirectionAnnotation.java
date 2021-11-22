package com.triplet.tripper.models.map;

import com.squareup.moshi.Json;

import java.util.List;

public class DirectionAnnotation {
    @Json(name="maxspeed")
    public List<MaxSpeedAnnotation> maxspeed;
}
