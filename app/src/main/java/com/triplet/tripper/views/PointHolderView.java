package com.triplet.tripper.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PointHolderView extends View {

    private Double lat = null;
    private Double lng = null;
    
    public PointHolderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
