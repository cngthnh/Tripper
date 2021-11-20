package com.triplet.tripper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.triplet.tripper.adapters.LocationsAdapter;
import com.triplet.tripper.databinding.FragmentLocationsBinding;
import com.triplet.tripper.models.location.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationsFragment extends Fragment {

    FragmentLocationsBinding binding;
    LocationsAdapter locationsAdapter = null;


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
        locationsAdapter = new LocationsAdapter(getActivity());
        locationsAdapter.setData(getDataLocations());
        binding.recyclerView.setAdapter(locationsAdapter);
        return view;
    }


    private List<Location> getDataLocations(){
        List<Location> list = new ArrayList<>();
        list.add(new Location("Quảng Nam", "01-01", "Tết dương lich", "Nhà"));
        list.add(new Location("Hồ Chí Minh", "22-01", "Phượt Sài Gòn", "Quận 1"));
        list.add(new Location("Đà nẵng", "09-02", "Đi cắm trại với bạn", "Làng Vân, Đèo Hải Vân"));
        list.add(new Location("Hồ Chí Minh", "22-02", "Phượt Sài Gòn", "Quận 1, Quận 2"));
        return  list;
    }
}



