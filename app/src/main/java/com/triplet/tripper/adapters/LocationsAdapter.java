package com.triplet.tripper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;
import com.triplet.tripper.R;
import com.triplet.tripper.models.location.Location;

import java.util.List;


public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>{

    private Context mcontext;
    private List<Location> mListLocation;

    public LocationsAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void setData(List<Location> mListLocation) {
        this.mListLocation = mListLocation;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = mListLocation.get(position);
        if(location == null){
            return;
        }
        holder.province.setText(location.getProvince());
        holder.date.setText(location.getDate());
        holder.event.setText("Nội dung: " + location.getEvent());
        holder.location.setText("Địa điểm: " + location.getLocation());
        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.foldingCell.toggle(false);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListLocation != null){
            return mListLocation.size();
        }
        return 0;
    }

    public  class LocationViewHolder extends RecyclerView.ViewHolder{

        private FoldingCell foldingCell;
        private TextView event;
        private TextView location;
        private TextView date;
        private TextView province;



        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            event = itemView.findViewById(R.id.event);
            province = itemView.findViewById(R.id.province);
            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.location);
        }
    }
}
