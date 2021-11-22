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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.triplet.tripper.models.location.FileUrl;
import com.triplet.tripper.models.location.LocationRecord;

import java.util.UUID;

public class NoteDialog extends AppCompatDialogFragment {
    private GoogleMap map;
    private Marker marker;
    private Uri imgUri, videoUri, audioUri;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String image;
    private String video;
    private String audio;
    private String titleText;
    private String dateText;
    private String contentText;
    private String locationText;
    private String provinceText;

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
        Intent audioIntent =  new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(audioIntent, 3);
    }

    private void pickVideo() {
        Intent videoIntent =  new Intent();
        videoIntent.setAction(Intent.ACTION_GET_CONTENT);
        videoIntent.setType("video/*");
        startActivityForResult(videoIntent, 2);
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
        titleText = edtTitle.getText().toString();
        dateText = edtDate.getText().toString();
        contentText = edtContent.getText().toString();
        locationText = edtLocation.getText().toString();

        FileUrl fileUrl = new FileUrl();

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference dataRef1 = database1.getReference();

        if(imgUri != null){
            uploadDataFileToFireBase(imgUri);
            dataRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    FileUrl url = snapshot.getValue(FileUrl.class);
                    if (url!=null){
                        Log.e("Name", url.getFileUrl());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        if(videoUri != null){
            uploadDataFileToFireBase(videoUri);

            //Log.e("Name", video);
        }



        LatLng latLng = marker.getPosition();
        String lat = String.valueOf(latLng.latitude);
        String lng = String.valueOf(latLng.longitude);
        String id = (lat + "_" + lng).replace(".", "_") + "_" + dateText.replace("-", "_");

        LocationRecord location = new LocationRecord(latLng, dateText, titleText, locationText, contentText, image, video, "");

        myRef.child(FirebaseAuth.getInstance().getUid() + "/" + id ).setValue(location, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getActivity(), "Set value success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadDataFileToFireBase(Uri uri) {

        StorageReference fileRef;
        UUID uuid = UUID.randomUUID();
        Boolean check;
        if(getFileExtension(uri) == "mp4"){
            fileRef = storageRef.child(FirebaseAuth.getInstance().getUid() + "/video/" + System.currentTimeMillis() + uuid + "." + getFileExtension(uri));
        }
        else if(getFileExtension(uri) == "mp3")
        {
            fileRef = storageRef.child(FirebaseAuth.getInstance().getUid() + "/audio/" + System.currentTimeMillis() + uuid + "." + getFileExtension(uri));
        }
        else {
            fileRef = storageRef.child(FirebaseAuth.getInstance().getUid() + "/image/" + System.currentTimeMillis() + uuid + "." + getFileExtension(uri));
        }
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dataRef = database.getReference("url");
                        dataRef.setValue(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Uploading Failed !!", Toast.LENGTH_SHORT).show();
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
