package com.triplet.tripper.models.map;

import com.squareup.moshi.Json;

import java.util.List;

public class Geometry {
    @Json(name="coordinates")
    public List<List<Double>> coordinates;
}
