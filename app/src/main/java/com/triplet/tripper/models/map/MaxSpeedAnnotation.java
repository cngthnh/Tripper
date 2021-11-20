package com.triplet.tripper.models.map;

import com.squareup.moshi.Json;

public class MaxSpeedAnnotation {
    @Json(name="unknown")
    public boolean unknown = false;

    @Json(name="speed")
    public int speed;

    @Json(name="unit")
    public String unit;
}
