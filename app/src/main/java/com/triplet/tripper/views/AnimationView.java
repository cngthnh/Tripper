package com.triplet.tripper.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.triplet.tripper.R;
import com.triplet.tripper.views.Sprite;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AnimationView extends View {

    private Bitmap fullframe;
    private ArrayList<Sprite> sprites;
    private Timer timer;
    private int pattern;
    static private int patternNum = 6;

    public AnimationView(Context context) {
        super(context);
        sprites = new ArrayList<>();
        prepareContents();
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sprites = new ArrayList<>();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimationView);
        this.pattern = a.getInteger(R.styleable.AnimationView_pattern, 0);

        prepareContents();
    }

    public int nextPattern()
    {
        return (this.pattern + 1) % patternNum;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        this.sprites.clear();
        prepareContents();
    }

    private void prepareContents() {
        switch (this.pattern) {
            case 0:
                createStar(0, 0);
                break;
            case 1:
                createCat(0,0);
                break;
            case 2:
                createRocket(0,0);
                break;
            case 3:
                createHamster(0, 0);
                break;
            case 4:
                createGhost(0,0);
                break;
            case 5:
                createDrone(0,0);
                break;
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < sprites.size(); i++)
                    sprites.get(i).update();

                postInvalidate();
            }
        };

        timer = new Timer();
        switch (this.pattern) {
            case 0:
                timer.schedule(timerTask, 1000, 90);
                break;
            case 1:
                timer.schedule(timerTask, 1000, 80);
                break;
            case 2:
                timer.schedule(timerTask, 1000, 75);
                break;
            case 3:
                timer.schedule(timerTask, 1000, 70);
                break;
            case 4:
                timer.schedule(timerTask, 1000, 90);
                break;
            case 5:
                timer.schedule(timerTask, 1000, 60);
        }
    }

    private void createStar(int left, int top)
    {
        Bitmap[] bitmaps = new Bitmap[38];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0001);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0002);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0003);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0004);
        bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0005);
        bitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0006);
        bitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0007);
        bitmaps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0008);
        bitmaps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0009);
        bitmaps[9] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0010);
        bitmaps[10] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0011);
        bitmaps[11] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0012);
        bitmaps[12] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0013);
        bitmaps[13] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0014);
        bitmaps[14] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0015);
        bitmaps[15] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0016);
        bitmaps[16] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0017);
        bitmaps[17] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0018);
        bitmaps[18] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0019);
        bitmaps[19] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0020);
        bitmaps[20] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0021);
        bitmaps[21] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0022);
        bitmaps[22] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0023);
        bitmaps[23] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0024);
        bitmaps[24] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0025);
        bitmaps[25] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0026);
        bitmaps[26] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0027);
        bitmaps[27] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0028);
        bitmaps[28] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0029);
        bitmaps[29] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0030);
        bitmaps[30] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0031);
        bitmaps[31] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0032);
        bitmaps[32] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0033);
        bitmaps[33] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0034);
        bitmaps[34] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0035);
        bitmaps[35] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0036);
        bitmaps[36] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0037);
        bitmaps[37] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0020_0038);

        Sprite sprite = new Sprite(bitmaps, left, top);
        sprites.add(sprite);
    }

    private void createGhost(int left, int top)
    {
        Bitmap[] bitmaps = new Bitmap[38];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0001);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0002);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0003);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0004);
        bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0005);
        bitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0006);
        bitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0007);
        bitmaps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0008);
        bitmaps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0009);
        bitmaps[9] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0010);
        bitmaps[10] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0011);
        bitmaps[11] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0012);
        bitmaps[12] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0013);
        bitmaps[13] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0014);
        bitmaps[14] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0015);
        bitmaps[15] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0016);
        bitmaps[16] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0017);
        bitmaps[17] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0018);
        bitmaps[18] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0019);
        bitmaps[19] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0020);
        bitmaps[20] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0021);
        bitmaps[21] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0022);
        bitmaps[22] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0023);
        bitmaps[23] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0024);
        bitmaps[24] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0025);
        bitmaps[25] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0026);
        bitmaps[26] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0027);
        bitmaps[27] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0028);
        bitmaps[28] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0029);
        bitmaps[29] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0030);
        bitmaps[30] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0031);
        bitmaps[31] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0032);
        bitmaps[32] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0033);
        bitmaps[33] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0034);
        bitmaps[34] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0035);
        bitmaps[35] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0036);
        bitmaps[36] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0037);
        bitmaps[37] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0051_0038);

        Sprite sprite = new Sprite(bitmaps, left, top);
        sprites.add(sprite);
    }

    private void createCat(int left, int top)
    {
        Bitmap[] bitmaps = new Bitmap[38];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0001);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0002);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0003);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0004);
        bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0005);
        bitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0006);
        bitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0007);
        bitmaps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0008);
        bitmaps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0009);
        bitmaps[9] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0010);
        bitmaps[10] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0011);
        bitmaps[11] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0012);
        bitmaps[12] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0013);
        bitmaps[13] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0014);
        bitmaps[14] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0015);
        bitmaps[15] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0016);
        bitmaps[16] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0017);
        bitmaps[17] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0018);
        bitmaps[18] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0019);
        bitmaps[19] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0020);
        bitmaps[20] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0021);
        bitmaps[21] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0022);
        bitmaps[22] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0023);
        bitmaps[23] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0024);
        bitmaps[24] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0025);
        bitmaps[25] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0026);
        bitmaps[26] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0027);
        bitmaps[27] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0028);
        bitmaps[28] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0029);
        bitmaps[29] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0030);
        bitmaps[30] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0031);
        bitmaps[31] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0032);
        bitmaps[32] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0033);
        bitmaps[33] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0034);
        bitmaps[34] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0035);
        bitmaps[35] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0036);
        bitmaps[36] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0037);
        bitmaps[37] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0047_0038);

        Sprite sprite = new Sprite(bitmaps, left, top);
        sprites.add(sprite);
    }

    private void createHamster(int left, int top)
    {
        Bitmap[] bitmaps = new Bitmap[50];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0001);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0002);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0003);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0004);
        bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0005);
        bitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0006);
        bitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0007);
        bitmaps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0008);
        bitmaps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0009);
        bitmaps[9] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0010);
        bitmaps[10] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0011);
        bitmaps[11] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0012);
        bitmaps[12] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0013);
        bitmaps[13] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0014);
        bitmaps[14] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0015);
        bitmaps[15] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0016);
        bitmaps[16] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0017);
        bitmaps[17] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0018);
        bitmaps[18] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0019);
        bitmaps[19] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0020);
        bitmaps[20] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0021);
        bitmaps[21] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0022);
        bitmaps[22] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0023);
        bitmaps[23] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0024);
        bitmaps[24] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0025);
        bitmaps[25] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0026);
        bitmaps[26] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0027);
        bitmaps[27] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0028);
        bitmaps[28] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0029);
        bitmaps[29] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0030);
        bitmaps[30] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0031);
        bitmaps[31] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0032);
        bitmaps[32] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0033);
        bitmaps[33] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0034);
        bitmaps[34] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0035);
        bitmaps[35] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0036);
        bitmaps[36] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0037);
        bitmaps[37] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0038);
        bitmaps[38] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0039);
        bitmaps[39] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0040);
        bitmaps[40] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0041);
        bitmaps[41] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0042);
        bitmaps[42] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0043);
        bitmaps[43] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0044);
        bitmaps[44] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0045);
        bitmaps[45] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0046);
        bitmaps[46] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0047);
        bitmaps[47] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0048);
        bitmaps[48] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0049);
        bitmaps[49] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0056_0050);

        Sprite sprite = new Sprite(bitmaps, left, top);
        sprites.add(sprite);
    }

    private void createDrone(int left, int top)
    {
        Bitmap[] bitmaps = new Bitmap[68];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0001);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0002);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0003);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0004);
        bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0005);
        bitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0006);
        bitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0007);
        bitmaps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0008);
        bitmaps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0009);
        bitmaps[9] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0010);
        bitmaps[10] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0011);
        bitmaps[11] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0012);
        bitmaps[12] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0013);
        bitmaps[13] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0014);
        bitmaps[14] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0015);
        bitmaps[15] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0016);
        bitmaps[16] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0017);
        bitmaps[17] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0018);
        bitmaps[18] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0019);
        bitmaps[19] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0020);
        bitmaps[20] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0021);
        bitmaps[21] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0022);
        bitmaps[22] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0023);
        bitmaps[23] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0024);
        bitmaps[24] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0025);
        bitmaps[25] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0026);
        bitmaps[26] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0027);
        bitmaps[27] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0028);
        bitmaps[28] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0029);
        bitmaps[29] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0030);
        bitmaps[30] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0031);
        bitmaps[31] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0032);
        bitmaps[32] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0033);
        bitmaps[33] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0034);
        bitmaps[34] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0035);
        bitmaps[35] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0036);
        bitmaps[36] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0037);
        bitmaps[37] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0038);
        bitmaps[38] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0039);
        bitmaps[39] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0040);
        bitmaps[40] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0041);
        bitmaps[41] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0042);
        bitmaps[42] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0043);
        bitmaps[43] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0044);
        bitmaps[44] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0045);
        bitmaps[45] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0046);
        bitmaps[46] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0047);
        bitmaps[47] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0048);
        bitmaps[48] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0049);
        bitmaps[49] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0050);
        bitmaps[50] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0051);
        bitmaps[51] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0052);
        bitmaps[52] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0053);
        bitmaps[53] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0054);
        bitmaps[54] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0055);
        bitmaps[55] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0057);
        bitmaps[56] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0058);
        bitmaps[57] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0059);
        bitmaps[58] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0060);
        bitmaps[59] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0061);
        bitmaps[60] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0062);
        bitmaps[61] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0063);
        bitmaps[62] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0064);
        bitmaps[63] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0065);
        bitmaps[64] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0066);
        bitmaps[65] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0067);
        bitmaps[66] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0068);
        bitmaps[67] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0179_0069);

        Sprite sprite = new Sprite(bitmaps, left, top);
        sprites.add(sprite);
    }

    private void createRocket(int left, int top)
    {
        Bitmap[] bitmaps = new Bitmap[46];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0001);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0002);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0003);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0004);
        bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0005);
        bitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0006);
        bitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0007);
        bitmaps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0008);
        bitmaps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0009);
        bitmaps[9] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0010);
        bitmaps[10] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0011);
        bitmaps[11] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0012);
        bitmaps[12] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0013);
        bitmaps[13] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0014);
        bitmaps[14] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0015);
        bitmaps[15] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0016);
        bitmaps[16] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0017);
        bitmaps[17] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0018);
        bitmaps[18] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0019);
        bitmaps[19] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0020);
        bitmaps[20] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0021);
        bitmaps[21] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0022);
        bitmaps[22] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0023);
        bitmaps[23] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0024);
        bitmaps[24] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0025);
        bitmaps[25] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0026);
        bitmaps[26] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0027);
        bitmaps[27] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0028);
        bitmaps[28] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0029);
        bitmaps[29] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0030);
        bitmaps[30] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0031);
        bitmaps[31] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0032);
        bitmaps[32] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0033);
        bitmaps[33] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0034);
        bitmaps[34] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0035);
        bitmaps[35] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0036);
        bitmaps[36] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0037);
        bitmaps[37] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0038);
        bitmaps[38] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0039);
        bitmaps[39] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0040);
        bitmaps[40] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0041);
        bitmaps[41] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0042);
        bitmaps[42] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0043);
        bitmaps[43] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0044);
        bitmaps[44] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0045);
        bitmaps[45] = BitmapFactory.decodeResource(getResources(), R.drawable.pet0036_0046);

        Sprite sprite = new Sprite(bitmaps, left, top);
        sprites.add(sprite);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        renderAll();
        canvas.drawBitmap(fullframe, 0, 0, null);
    }

    private void renderAll() {
        fullframe = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(fullframe);
        canvas.drawColor(Color.argb(0, 255, 255, 255));
        for (int i = 0; i < sprites.size(); ++i)
            sprites.get(i).draw(canvas);
    }
}
