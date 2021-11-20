package com.triplet.tripper.models.map;

import com.squareup.moshi.Json;

import java.util.List;

public class Direction {
    @Json(name="routes")
    public List<DirectionRoute> routes;

    @Json(name="waypoints")
    public List<Waypoint> waypoints;

    @Json(name="code")
    public String code;

    @Json(name="uuid")
    public String uuid;
}
