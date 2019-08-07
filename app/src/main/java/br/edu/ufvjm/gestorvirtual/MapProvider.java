package br.edu.ufvjm.gestorvirtual;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
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
    private LatLng markerLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overlay_map_fragment, container, false);

        ImageButton addReportButton = (ImageButton)view.findViewById(R.id.fab_close);
        addReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ReportIssueActivity.class);
                //Adicione a variavel latlng em um bundle e envia pra Activity que será aberta
                Bundle latlngLocation = new Bundle();
                latlngLocation.putParcelable("markerLocation", markerLocation);
                i.putExtra("ISSUE_LOCATION", latlngLocation);
                startActivity(i);
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SupportMapFragment smf = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.reportIssueMapFragment));

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

            // Pega a ultima localização e move a camera pra ela
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
        mMap.clear(); // Limpa os marcadores do mapa

        //Objetos do fragmento
        CardView locationMarker = (CardView)getView().findViewById(R.id.locationMarker);
        TextView locationAddress = (TextView)getView().findViewById(R.id.locationAddress);
        ImageButton addReportButton = (ImageButton)getView().findViewById(R.id.fab_close);

        //Criar as animações
        Animation aniFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
        Animation aniFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        //Torna os objetos visíveis se não estiverem
        if(addReportButton.getVisibility() == View.INVISIBLE && locationMarker.getVisibility() == View.INVISIBLE)
        {
            locationMarker.setAnimation(aniFadeIn);
            addReportButton.setAnimation(aniFadeIn);
            addReportButton.setVisibility(View.VISIBLE);
            addReportButton.bringToFront();
            locationMarker.setVisibility(View.VISIBLE);
        }

        //Solicita as informações de endereço utilizando as informações latlng
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            locationAddress.setText(address);
        } catch (IOException e) {
            Log.e(TAG_LOG, "Geocoder Error", e);
            e.printStackTrace();
        }

        // Faz a marcação do local selecionado no mapa
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        mMap.addMarker(marker);
        markerLocation = latLng;
    }
}

