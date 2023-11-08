package com.professionalandroid.apps.airport;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.professionalandroid.apps.airport.databinding.ListItemAirportBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AirportRecyclerViewAdapter extends RecyclerView.Adapter<AirportRecyclerViewAdapter.ViewHolder> {
    private final List<Airport> mAirport;
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);
    private static final NumberFormat MAGNITUDE_FORMAT =
            new DecimalFormat("0.0");
    public AirportRecyclerViewAdapter(List<Airport> airports){
        mAirport = airports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ListItemAirportBinding binding = ListItemAirportBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Airport airport = mAirport.get(position);
        holder.binding.setAirport(airport);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mAirport.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final ListItemAirportBinding binding;

        public ViewHolder(ListItemAirportBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            binding.setMagnitudeformat(MAGNITUDE_FORMAT);
        }
    }
}
