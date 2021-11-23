package com.triplet.tripper.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.foldingcell.FoldingCell;
import com.triplet.tripper.MainActivity;
import com.triplet.tripper.MapsFragment;
import com.triplet.tripper.NoteDialog;
import com.triplet.tripper.R;
import com.triplet.tripper.models.location.LocationRecord;
import com.triplet.tripper.views.PointHolderView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>{

    private Context mContext = null;
    private Activity mActivity = null;
    private List<LocationRecord> mListLocation;



    public LocationsAdapter(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void setData(List<LocationRecord> mListLocation) {
        this.mListLocation = mListLocation;
    }


    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationRecord location = mListLocation.get(position);
        if(location == null){
            return;
        }
        holder.province.setText(location.getProvince());
        holder.date.setText(location.getDate());
        holder.event.setText("Nội dung: " + location.getEvent());
        holder.location.setText("Địa điểm: " + location.getLocation());

        holder.date_bot.setText(location.getDate());
        holder.location_bot.setText((location.getLocation()));
        holder.content_bot.setText(location.getContent());
        if(location.getImageUrl() != null){
            if(!location.getImageUrl().isEmpty()){
                Glide.with(mContext).load(location.getImageUrl()).into(holder.landscapeImg);
            }
        }

        holder.pointTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PointHolderView pointHolderView = (PointHolderView) (mActivity.findViewById(R.id.point_holder));
                pointHolderView.setLat(location.getLatitude());
                pointHolderView.setLng(location.getLongitude());
                ((BottomNavigationView) ((MainActivity) mActivity).findViewById(R.id.bottom_navigation))
                        .setSelectedItemId(R.id.menu_map);
            }
        });
        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.foldingCell.toggle(false);
            }
        });

        holder.video_bt_bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(location.getVideoUrl()));
                intent.setDataAndType(Uri.parse(location.getVideoUrl()), "video/mp4");
                mContext.startActivity(intent);
            }
        });

        holder.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.menuBtn);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.menu_edit:
                                NoteDialog noteDialog = new NoteDialog(location);
                                noteDialog.show(((AppCompatActivity)mContext).getSupportFragmentManager(), "Note Dialog");
                                break;
                            case R.id.menu_delete:
                                String id = (location.getLatitude().toString().replace(".", "-") + "_" + location.getLongitude().toString().replace(".", "-") + "_" + location.getDate());
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference dataRef = database.getReference("history/" + FirebaseAuth.getInstance().getUid()).child(id);
                                dataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        notifyDataSetChanged();
                                    }
                                });
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_TEXT, holder.event.getText().toString()
                        .replace("Nội dung: ", "") + " #Tripper");

                Uri uri = saveImageExternal(getBitmapFromView(holder.landscapeImg));
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/*");
                Intent chooser = Intent.createChooser(intent, "Chia sẻ kỷ niệm của bạn");

                List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    mContext.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                mContext.startActivity(chooser);
            }
        });
    }



    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Uri saveImageExternal(Bitmap image) {
        Uri uri = null;
        try {
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = FileProvider.getUriForFile(
                    mContext,
                    "com.triplet.tripper.provider",
                    file);
        } catch (Exception e) {
            Log.d("TripperIO", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;

    }

    @Override
    public int getItemCount() {
        if(mListLocation != null){
            return mListLocation.size();
        }
        return 0;
    }

    public void filterList(List<LocationRecord> filteredList) {
        mListLocation = filteredList;
        notifyDataSetChanged();
    }



    public  class LocationViewHolder extends RecyclerView.ViewHolder{

        private FoldingCell foldingCell;
        private TextView event;
        private TextView location;
        private TextView date;
        private TextView province;
        private TextView event_bot;
        private TextView location_bot;
        private TextView date_bot;
        private TextView content_bot;
        private ImageButton video_bt_bot;
        private ImageView landscapeImg;
        private ImageButton shareBtn;
        private ImageButton menuBtn;
        private ImageButton pointTo;


        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            event = itemView.findViewById(R.id.event);
            province = itemView.findViewById(R.id.province);
            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.location);
            event_bot = itemView.findViewById(R.id.event_bot);
            location_bot = itemView.findViewById(R.id.location_bot);
            date_bot = itemView.findViewById(R.id.date_bot);
            content_bot = itemView.findViewById(R.id.content_bot);
            video_bt_bot = itemView.findViewById(R.id.bt_video_bot);
            landscapeImg = itemView.findViewById(R.id.landscape_image);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            menuBtn = itemView.findViewById(R.id.bt_menu);
            pointTo = itemView.findViewById(R.id.bt_addr_bot);
        }
    }
}
