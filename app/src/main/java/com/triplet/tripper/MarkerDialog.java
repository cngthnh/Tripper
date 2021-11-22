package com.triplet.tripper;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
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

        builder.setView(view);

        AlertDialog dialog = builder.create();

        nameMarker = (EditText) view.findViewById(R.id.makerName);

        ((MaterialButton) view.findViewById(R.id.addMaker)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String name = nameMarker.getText().toString();
                if (name.length() < 1) {
                    Toast.makeText(getContext(), "Bạn chưa nhập tên điểm đánh dấu", Toast.LENGTH_LONG).show();
                    return;
                }
                curMap.addMarker(new MarkerOptions().title(name).position(currentLatLng)
                        .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_purple_marker)));
            }
        });

        ((MaterialButton) view.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
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
