package br.edu.ufvjm.gestorvirtual;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapProvider extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final String TAG_LOG = "MapProvider";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.overlay_map_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        SupportMapFragment smf = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        smf.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try
        {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE); // recupera o serviço de localização do android
            Criteria criteria = new Criteria();
            mMap = googleMap;
            mMap.setOnMapClickListener(this);
            mMap.setMyLocationEnabled(true);


            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
            if(location!=null)
            {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 19.0f), 1800, null);
        }
        }catch(SecurityException e){
            Log.e(TAG_LOG, "Erro", e);
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();

        CardView locationMarker = (CardView)getView().findViewById(R.id.locationMarker);
        TextView locationAddress = (TextView)getView().findViewById(R.id.locationAddress);
        ImageButton addReportButton = (ImageButton)getView().findViewById(R.id.fab_addReport);

        Animation aniFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
        Animation aniFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        if(addReportButton.getVisibility() == View.INVISIBLE && locationMarker.getVisibility() == View.INVISIBLE)
        {
            locationMarker.setAnimation(aniFadeIn);
            addReportButton.setAnimation(aniFadeIn);
            addReportButton.setVisibility(View.VISIBLE);
            addReportButton.bringToFront();
            locationMarker.setVisibility(View.VISIBLE);
        }

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            locationAddress.setText(address);

        } catch (IOException e) {
            Log.e(TAG_LOG, "Geocoder Error", e);
            e.printStackTrace();
        }

        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        marker.title("Relatar problema aqui");
        mMap.addMarker(marker);
    }
}
