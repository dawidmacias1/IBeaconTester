package com.mob.ibeacontester;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kontakt.sdk.android.common.profile.IBeaconDevice;

import java.util.List;

public class BeaconAdapter extends RecyclerView.Adapter {

    private RecyclerView mRecyclerView;
    private List<IBeaconDevice> mBeacon;

    public BeaconAdapter(List<IBeaconDevice> pBeacon, RecyclerView pRecyclerView){
        mBeacon = pBeacon;
        mRecyclerView = pRecyclerView;
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDistance;
        private TextView mBattery;

        public MyViewHolder(View pItem) {
            super(pItem);
            mTitle = (TextView) pItem.findViewById(R.id.beacon_title);
            mDistance = (TextView) pItem.findViewById(R.id.beacon_distance);
            mBattery = (TextView) pItem.findViewById(R.id.beacon_battery);


        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.beacone_layout, viewGroup, false);

        // tworzymy i zwracamy obiekt ViewHolder
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        IBeaconDevice beacon = mBeacon.get(i);

        switch ( beacon.getUniqueId()) {
            case "TV7G":
                ((MyViewHolder) viewHolder).mTitle.setText("WEEIA");
                break;
            case "qhGr":
                ((MyViewHolder) viewHolder).mTitle.setText("FTIMS");
                break;
            case "oYQa":
                ((MyViewHolder) viewHolder).mTitle.setText("Lodex");
                break;

        }
//        ((MyViewHolder) viewHolder).mTitle.setText(String.format("ID: %s", beacon.getUniqueId()));
        ((MyViewHolder) viewHolder).mDistance.setText(String.format("Distance: %s", String.valueOf(roundTwoPlaces(beacon.getDistance()))));
        ((MyViewHolder) viewHolder).mBattery.setText(String.format("Battery level: %s", String.valueOf(beacon.getBatteryPower())));

    }

    @Override
    public int getItemCount() {
        return mBeacon.size();
    }

    private double roundTwoPlaces(double in){
        return Math.round(in * 100.0) / 100.0;
    }


}
