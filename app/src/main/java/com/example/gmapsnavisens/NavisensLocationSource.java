package com.example.gmapsnavisens;

import com.google.android.gms.maps.LocationSource;

public class NavisensLocationSource implements LocationSource {
    public OnLocationChangedListener listener;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.listener=onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        this.listener=null;
    }
}
