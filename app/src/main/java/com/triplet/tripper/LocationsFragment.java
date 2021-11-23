package com.triplet.tripper;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.triplet.tripper.adapters.LocationsAdapter;
import com.triplet.tripper.databinding.FragmentLocationsBinding;
import com.triplet.tripper.models.location.LocationRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationsFragment extends Fragment {

    FragmentLocationsBinding binding;
    LocationsAdapter locationsAdapter = null;
    List<LocationRecord> list;

    public LocationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLocationsBinding.inflate(inflater, container, false );
        View view = binding.getRoot();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);



        locationsAdapter = new LocationsAdapter(getContext(), getActivity());
        list = new ArrayList<>();
        getListLocationFromRealtimeDatabase();
        locationsAdapter.setData(list);
        locationsAdapter.notifyDataSetChanged();

        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.recyclerView.setAdapter(locationsAdapter);

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        return view;
    }

    private void filter(String s) {
        List<LocationRecord> filteredList = new ArrayList<>();
        for(LocationRecord item: list){
            if(item.getEvent().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(item);
            }
        }
        locationsAdapter.filterList(filteredList);
    }


    private void getListLocationFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("history/" + FirebaseAuth.getInstance().getUid());

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    getSnapshot(dataSnapshot);
                }
                locationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSnapshot(DataSnapshot dataSnapshot) {
        LocationRecord locationRecord = dataSnapshot.getValue(LocationRecord.class);
        list.add(locationRecord);
    }

}



