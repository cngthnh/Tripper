package com.triplet.tripper.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Sprite {
    private int currentIndex;
    private int frameNum;
    private int left;
    private int top;
    Bitmap[] bitmaps;

    Sprite(Bitmap[] bitmaps, int left, int top) {
        this.left = left;
        this.top = top;
        this.bitmaps = bitmaps;
        this.frameNum = bitmaps.length;
        this.currentIndex = 0;
    }

    public void update() {
        currentIndex = (currentIndex + 1) % frameNum;
    }
    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        int width = canvas.getWidth() - this.left;
        int height = canvas.getHeight() - this.top;
        if (width < height)
            height = width;
        else
            width = height;
        canvas.drawBitmap(bitmaps[currentIndex], null,
                new RectF(this.left, this.top, width, height), null);
    }
}
