package com.triplet.tripper;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.triplet.tripper.databinding.MapLayoutBinding;
import com.triplet.tripper.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.triplet.tripper.databinding.MapLayoutBinding;
import com.triplet.tripper.models.location.LocationRecord;
import com.triplet.tripper.models.map.Direction;
import com.triplet.tripper.utils.ApiService;
import com.triplet.tripper.utils.Client;
import com.triplet.tripper.views.PointHolderView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap curMap;
    private FloatingSearchView searchView;
    private FusedLocationProviderClient fusedLocationClient;
    private Activity activity;
    private static final int SPEECH_REQUEST_CODE = 0;
    MapLayoutBinding binding;
    Marker dstMarker, srcMarker;
    int directingState = 0;
    Marker searching;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    List<RecentSearch> searchList;

    ApiService service = Client.createService(ApiService.class);
    private ArrayList<Polyline> routes = null;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            curMap = googleMap;



            int nightModeFlags =
                    getContext().getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    curMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.map_night_style));
                    break;

                case Configuration.UI_MODE_NIGHT_NO:

                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    curMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.map_light_style));
                    break;
            }

            searchView = getActivity().findViewById(R.id.search_location);
            curMap.setBuildingsEnabled(true);

            configureSearchView();
            setCurrentMap();

            markerAll();

            if (ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                onMapReady(curMap);
            }
            curMap.setPadding(dp2px(0), dp2px(200), 0, dp2px(100));
            curMap.setMyLocationEnabled(true);
            curMap.getUiSettings().setMapToolbarEnabled(true);
            curMap.getUiSettings().setZoomControlsEnabled(true);
            configureMyLocationButton();
            configureCompassButton();

            PointHolderView pointHolderView = (PointHolderView) (getActivity().findViewById(R.id.point_holder));

            if (pointHolderView.getLat() != null && pointHolderView.getLng() != null) {
                LatLng myPosition = new LatLng(pointHolderView.getLat(), pointHolderView.getLng());
                pointHolderView.setLng(null);
                pointHolderView.setLat(null);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(myPosition)
                        .zoom(17)
                        .tilt(30)
                        .build();
                curMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
            else fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng myPosition = new LatLng(location.getLatitude(),location.getLongitude());
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(myPosition)
                                    .zoom(17)
                                    .tilt(30)
                                    .build();
                            curMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    });
        }
    };


    private void markerAll() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("history/" + FirebaseAuth.getInstance().getUid());

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    try {
                        LocationRecord locationRecord = dataSnapshot.getValue(LocationRecord.class);
                        LatLng latLng = new LatLng(locationRecord.getLatitude(), locationRecord.getLongitude());

                        curMap.addMarker(new MarkerOptions().position(latLng).title(" ")
                                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_purple_marker)));
                    } catch (Exception e) {
                        continue;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void configureCompassButton() {
        View compassButton = this.getView().findViewWithTag("GoogleMapCompass");
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
        rlp.leftMargin = dp2px(340);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_location_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void configureMyLocationButton() {
        View locationButton = ((View) activity.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.topMargin = dp2px(200);
    }
    public int dp2px(float dp) {
        return (int)(dp * getContext().getResources().getDisplayMetrics().density);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = context.getDrawable(vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void setCurrentMap() {
        curMap.setOnMapLongClickListener(latLng -> {
            switch (directingState) {
                case 0:
                    Marker marker = curMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_purple_marker)));
                    NoteDialog noteDialog = new NoteDialog(curMap, marker);
                    noteDialog.show(getActivity().getSupportFragmentManager(),"Note Dialog");
                    break;
                case 1:
                    srcMarker = curMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_blue_flag)));
                    directingState = 2;
                    binding.navTooltip.setText("Nhấn giữ một điểm khác trên bản đồ để chọn đích đến");
                    break;
                case 2:
                    dstMarker = curMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_red_flag)));
                    directingState = 3;
                    drawRoutes();
                    fadeOut(binding.navTooltip);
                    break;
            }
        });
        //marker click
        curMap.setOnMarkerClickListener(marker -> {
            float zoom = curMap.getCameraPosition().zoom;
            curMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),zoom+12.5f));
            zoom = curMap.getCameraPosition().zoom;
            NoteDialog noteDialog = new NoteDialog(curMap, marker);
            noteDialog.show(getActivity().getSupportFragmentManager(),"Note Dialog");
            return false;
        });
    }
    public List<RecentSearch> removeDuplicates(List<RecentSearch> recentSearches) {
        List<RecentSearch> list = new ArrayList<>();
        int count = 0;
        for (int i = 0; i<recentSearches.size();i++) {
            for (int j = 0; j<list.size();j++){
                if (list.get(j).word.equals(recentSearches.get(i).word))
                    count++;
            }
            if (count == 0)
                list.add(recentSearches.get(i));
            count = 0;
        }
        return list;
    }

    private void configureSearchView() {
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFocus() {
                Collections.reverse(searchList);
                List<RecentSearch> list = removeDuplicates(searchList);
                if (list.size()>=5)
                    list.remove(0);
                searchView.swapSuggestions(list);
            }

            @Override
            public void onFocusCleared() {

            }
        });
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (searching != null)
                    searching.remove();
            }
        });

        searchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                if (searching != null)
                    searching.remove();
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSearchAction(String currentQuery) {
                if (searching != null)
                    searching.remove();
                String location = currentQuery;
                searchList.add(new RecentSearch(location));
                addRecentSearch(location);
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (addressList.size() == 0) {
                        Toast.makeText(getContext(), "Không tìm thấy " + location, Toast.LENGTH_LONG).show();
                    } else {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        searching =
                        curMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0))
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_red_marker)));
                        curMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }
                }
                return;
            }
        });
        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                startSpeechRecognizer();
            }
        });

    }
    private void startSpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            searchView.setSearchText(spokenText);
            searchView.setSearchFocused(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addRecentSearch(String location) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            DatabaseReference ref = mDatabase.getReference("search");
            ref.child(mAuth.getUid()).child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        int inc;
                        try {
                            inc = task.getResult().getValue(Integer.class);
                        } catch (Exception e) {
                            inc = 0;
                        }
                        if (inc == 5)
                        {
                            ref.removeValue();
                            inc = 0;
                        }
                        ref.child(mAuth.getUid()).child("count").setValue(inc+1);
                        ref.child(mAuth.getUid()).child(String.valueOf(inc+1)).setValue(location);
                    }
                }
            });
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getRecentSearch() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            DatabaseReference ref = mDatabase.getReference("search");
            ref.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        int inc;
                        try {
                            inc = task.getResult().child("count").getValue(Integer.class);
                        }
                        catch (Exception e) {
                            inc = 0;
                        }
                        for (int i = inc;i>=1;i--){
                            searchList.add(new RecentSearch(task.getResult().child(String.valueOf(i)).getValue(String.class)));
                        }
                    }
                }
            });
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private void fadeIn(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setInterpolator(new DecelerateInterpolator()).setDuration(500).setListener(null);
    }

    private void fadeOut(View view) {
        view.animate()
                .alpha(0f)
                .setDuration(500)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = MapLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        searchList = new ArrayList<>();
        getRecentSearch();

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);

        fab.setImageResource(R.drawable.ic_directions);

        fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FloatingActionButton fab = (FloatingActionButton) (getActivity().findViewById(R.id.fab));
                        if (directingState == 0) {

                            binding.navTooltip.setText("Nhấn giữ một điểm trên bản đồ để chọn nơi bắt đầu");

                            fab.setBackgroundTintList(AppCompatResources.getColorStateList(getContext(), R.color.red));
                            fab.setImageResource(R.drawable.ic_close);
                            // fab.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

                            fadeIn(binding.navTooltip);
                            directingState = 1;
                        }
                        else {
                            fab.setBackgroundTintList(AppCompatResources.getColorStateList(getContext(), R.color.light_rose));
                            fab.setImageResource(R.drawable.ic_directions);
                            fadeOut(binding.navTooltip);
                            removeRoutes();
                        }
                    }
                });

        return view;
    }

    private void removeRoutes() {
        if (srcMarker != null) {
            srcMarker.remove();
            srcMarker = null;
        }
        if (dstMarker != null) {
            dstMarker.remove();
            dstMarker = null;
        }
        if (routes != null) {
            for (Polyline polyline : routes) {
                polyline.remove();
            }
            routes.clear();
            routes = null;
        }
        directingState = 0;
    }

    private void drawRoutes() {

        LatLng source, destination;
        source = srcMarker.getPosition();
        destination = dstMarker.getPosition();

        Call<Direction> call = service.getDirection("driving",
                source.longitude,
                source.latitude,
                destination.longitude,
                destination.latitude,
                "maxspeed",
                "full",
                "geojson",
                getString(R.string.mapbox_access_token));
        call.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(@NonNull Call<Direction> call, @NonNull Response<Direction> response) {
                if (response.isSuccessful()) {
                    Direction direction = response.body();

                    routes = new ArrayList<>();

                    for (int i=0; i<direction.routes.size(); ++i) {
                        ArrayList<LatLng> points = new ArrayList<>();

                        for (int j=0; j<direction.routes.get(i).geometry.coordinates.size(); ++j)
                            points.add(new LatLng(
                                    direction.routes.get(i).geometry.coordinates.get(j).get(1),
                                    direction.routes.get(i).geometry.coordinates.get(j).get(0)));

                        Polyline polyline = curMap.addPolyline(new PolylineOptions()
                                .addAll(points)
                                .color(Color.CYAN)
                                .jointType(JointType.ROUND)
                                .width(12));

                        routes.add(polyline);
                    }
                }
                else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Direction> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            activity = getActivity();
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        }
    }

}