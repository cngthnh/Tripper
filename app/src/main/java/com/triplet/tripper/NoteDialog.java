package com.triplet.tripper;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.triplet.tripper.adapters.WordNoteAdapter;
import com.triplet.tripper.models.location.FileUrl;
import com.triplet.tripper.models.location.Location;
import com.triplet.tripper.models.map.WordNote;

import java.util.ArrayList;

public class NoteDialog extends AppCompatDialogFragment {
    private GoogleMap map;
    private Marker marker;
    private RecyclerView recyclerView;
    private WordNoteAdapter wordNoteAdapter;
    private ArrayList<WordNote> wordNoteList;
    private Uri imgUri, videoUri, audioUri;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FileUrl image;
    private FileUrl video;
    private FileUrl audio;

    EditText edtTitle;
    EditText edtContent;
    EditText edtDate;
    EditText edtLocation;
    Button btSave;
    Button btImg;
    Button btVideo;
    Button btAudio;
    ImageView imgView;
    TextView nameVideo;
    TextView nameAudio;


    public NoteDialog(GoogleMap curMap, Marker marker) {
        map = curMap;
        this.marker = marker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_note,null);
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("history");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        findView(view);

        btImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImg();
            }
        });
        
        btVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickVideo();
            }
        });
        
        btAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickAudio();
            }
        });
        
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDialog.this.uploadData();
            }
        });

        return builder.create();
    }

    private void pickAudio() {
        Intent imgIntent =  new Intent();
        imgIntent.setAction(Intent.ACTION_GET_CONTENT);
        imgIntent.setType("audio/*");
        startActivityForResult(imgIntent, 3);
    }

    private void pickVideo() {
        Intent imgIntent =  new Intent();
        imgIntent.setAction(Intent.ACTION_GET_CONTENT);
        imgIntent.setType("video/*");
        startActivityForResult(imgIntent, 2);
    }

    private void pickImg() {
        Intent imgIntent =  new Intent();
        imgIntent.setAction(Intent.ACTION_GET_CONTENT);
        imgIntent.setType("image/*");
        startActivityForResult(imgIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            if(requestCode == 1){
                imgUri = data.getData();
                imgView.setImageURI(imgUri);
            }
            else if(requestCode == 2)
            {
                videoUri = data.getData();
                nameVideo.setText(data.getData().getPath());
            }
            else if(requestCode == 3)
            {
                audioUri = data.getData();
                nameAudio.setText(data.getData().getPath());

            }
        }

    }



    private void uploadData() {
        String titleText = edtTitle.getText().toString();
        String dateText = edtDate.getText().toString();
        String contentText = edtContent.getText().toString();
        String locationText = edtLocation.getText().toString();


        uploadDataFileToFireBase(imgUri, image);


        LatLng latLng = marker.getPosition();
        String lat = String.valueOf(latLng.latitude);
        String lng = String.valueOf(latLng.longitude);
        String id = (lat + "_" + lng).replace(".", "_") + "_" + dateText.replace("-", "_");

        Location location = new Location(" ", dateText, titleText,locationText, contentText, image,null, null);

        myRef.child(id).setValue(location, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getActivity(), "Set value success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadDataFileToFireBase(Uri uri, FileUrl image) {

        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("1");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FileUrl fileUrl = new FileUrl(uri.toString());
                        Log.e("Name", uri.toString());
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Uploading Failed !!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Log.e("Name", fileRef.toString());
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void findView(View view) {
        edtTitle = view.findViewById(R.id.edt_title);
        edtContent = view.findViewById(R.id.edt_content);
        edtDate = view.findViewById(R.id.edt_date);
        edtLocation = view.findViewById(R.id.edt_location);
        btSave = view.findViewById(R.id.bt_save);
        btImg = view.findViewById(R.id.bt_add_img);
        btVideo = view.findViewById(R.id.bt_add_video);
        btAudio = view.findViewById(R.id.bt_add_audio);
        imgView = view.findViewById(R.id.img_view);
        nameVideo = view.findViewById(R.id.tv_name_video);
        nameAudio = view.findViewById(R.id.tv_name_audio);
    }
}
