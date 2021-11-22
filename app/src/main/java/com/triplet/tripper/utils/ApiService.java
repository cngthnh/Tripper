package com.triplet.tripper.utils;

import com.triplet.tripper.models.map.Direction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // https://docs.mapbox.com/api/navigation/directions/#retrieve-directions

    @GET("{profile}/{start_lng},{start_lat};{end_lng},{end_lat}")
    Call<Direction> getDirection(@Path("profile") String profile,
                                 @Path("start_lng") double start_lng,
                                 @Path("start_lat") double start_lat,
                                 @Path("end_lng") double end_lng,
                                 @Path("end_lat") double end_lat,
                                 @Query("annotations") String annotations,
                                 @Query("overview") String overview,
                                 @Query("geometries") String geometries,
                                 @Query("access_token") String access_token);
}