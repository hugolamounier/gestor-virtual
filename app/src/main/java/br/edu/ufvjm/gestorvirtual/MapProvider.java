package br.edu.ufvjm.gestorvirtual;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapProvider extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final String TAG_LOG = "MapProvider";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try
        {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE); // recupera o serviço de localização do android
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            mMap = googleMap;
            mMap.setOnMapClickListener(this);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            mMap.setMyLocationEnabled(true);
        }catch(SecurityException e){
            Log.e(TAG_LOG, "Erro", e);
        }


        LatLng diamantina = new LatLng(-18.245175, -43.600411);

        MarkerOptions marker = new MarkerOptions();
        marker.position(diamantina);
        marker.title("Diamantina");


        mMap.addMarker(marker);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(diamantina, 15.5f), 1500, null);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
