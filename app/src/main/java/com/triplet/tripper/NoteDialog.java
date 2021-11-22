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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
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
    private LocationRecord log;
    LatLng latLng;


    EditText edtTitle;
    EditText edtContent;
    EditText edtDate;
    EditText edtLocation;
    MaterialButton btSave;
    MaterialButton btImg;
    MaterialButton btVideo;
    MaterialButton btCancel;
    ImageView imgView;
    TextView nameVideo;



    public NoteDialog(GoogleMap curMap, Marker marker) {
        map = curMap;
        this.marker = marker;
        this.latLng = marker.getPosition();
    }

    public NoteDialog(LocationRecord locationRecord){
        this.log = locationRecord;
        latLng = new LatLng(locationRecord.getLatitude(), locationRecord.getLongitude());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_note,null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("history");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        findView(view);

        if (log != null) {
            setView();
        }

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
        
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDialog.this.uploadData();
                dialog.dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marker != null) marker.remove();
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private void setView() {
        edtTitle.setText(log.getEvent());
        edtContent.setText(log.getContent());
        edtDate.setText(log.getDate());
        edtLocation.setText(log.getLocation());
        if(!log.getImageUrl().isEmpty()){
            Glide.with(getActivity()).load(log.getImageUrl()).into(imgView);
        }

        if(!log.getVideoUrl().isEmpty()){

        }
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
        }

    }


    private void uploadData() {
        String titleText = edtTitle.getText().toString();
        String dateText = edtDate.getText().toString();
        String contentText = edtContent.getText().toString();
        String locationText = edtLocation.getText().toString();



        String lat = String.valueOf(latLng.latitude);
        String lng = String.valueOf(latLng.longitude);


        LocationRecord location = new LocationRecord(" ", dateText, titleText,locationText, contentText, image,video, latLng.latitude, latLng.longitude);

        if(imgUri != null){
            uploadDataFileToFireBase(imgUri, location);
        }
        else {
            location.setImageUrl("");
        }
        if(videoUri != null){
            uploadDataFileToFireBase(videoUri, location);
        }
        else {
            location.setVideoUrl("");
        }

        String id = (lat.replace(".", "-") + "_" + lng.replace(".", "-") + "_" + location.getDate());
        myRef.child(FirebaseAuth.getInstance().getUid()).child(id).setValue(location, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                //Toast.makeText(getContext(), "Set value success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLocation(LocationRecord location, String fileUUID, String type) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getUid())
                .child(fileUUID);
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String lat = String.valueOf(latLng.latitude);
                String lng = String.valueOf(latLng.longitude);
                if(type == "mp4")
                {
                    location.setVideoUrl(uri.toString());
                }
                else {
                    location.setImageUrl (uri.toString());
                }
                String id = (lat.replace(".", "-") + "_" + lng.replace(".", "-") + "_" + location.getDate());
                myRef.child(FirebaseAuth.getInstance().getUid()).child(id).setValue(location, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    }
                });
            }
        });
    }

    private void uploadDataFileToFireBase(Uri uri, LocationRecord location) {

        String fileUUID = String.valueOf(UUID.randomUUID());
        String type = getFileExtension(uri);

        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getUid())
                .child(fileUUID);
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                saveLocation(location, fileUUID, type);

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
        btCancel = view.findViewById(R.id.cancel);
        imgView = view.findViewById(R.id.img_view);
        nameVideo = view.findViewById(R.id.tv_name_video);
    }
}
