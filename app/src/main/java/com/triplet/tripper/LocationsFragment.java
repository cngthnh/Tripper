package com.triplet.tripper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
        locationsAdapter = new LocationsAdapter(getContext());
        list = new ArrayList<>();
        getListLocationFromRealtimeDatabase();
        locationsAdapter.setData(list);

        binding.recyclerView.setAdapter(locationsAdapter);
        return view;
    }

    private void getListLocationFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("history/" + FirebaseAuth.getInstance().getUid());

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    LocationRecord locationRecord = dataSnapshot.getValue(LocationRecord.class);
                    list.add(locationRecord);
                }
                locationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}



