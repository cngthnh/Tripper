package com.triplet.tripper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MarkerDialog extends AppCompatDialogFragment {

    private EditText nameMarker;
    private LatLng currentLatLng;
    private GoogleMap curMap;

    public MarkerDialog(GoogleMap curMap, LatLng currentLatLng) {
        this.curMap = curMap;
        this.currentLatLng = currentLatLng;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_marker,null);
        builder.setView(view)
                .setTitle("Marker")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameMarker.getText().toString();
                        curMap.addMarker(new MarkerOptions().title(name).position(currentLatLng)
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_purple_marker)));
                        //saveData(name, currentLatLng);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        nameMarker = (EditText) view.findViewById(R.id.text_marker);
        return builder.create();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void saveData(String name, LatLng currentLatLng) {
        SharedPreferences preferences = getActivity().getSharedPreferences("count-maker",Context.MODE_PRIVATE);
        SharedPreferences.Editor counter = preferences.edit();
        int curCount= preferences.getInt("count",-10);
        if (curCount < 0){
            counter.putInt("count",0);
        }


    }

}
