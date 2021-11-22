package com.triplet.tripper.models.map;

import com.squareup.moshi.Json;

import java.util.List;

public class DirectionLeg {
    @Json(name="via_waypoints")
    public List<Waypoint> via_waypoints;

    @Json(name="annotation")
    public DirectionAnnotation annotation;

    @Json(name="weight")
    public double weight;

    @Json(name="duration")
    public double duration;

    @Json(name="distance")
    public double distance;

    @Json(name="summary")
    public String summary;
}
