package br.edu.ufvjm.gestorvirtual;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReportIssueActivity extends FragmentActivity implements OnMapReadyCallback{


    private LatLng markerLocation;
    private GoogleMap mMap;
    private TextView locationAddress;
    private static final String TAG_LOG = "ReportIssueActivity";

    String[] SPINNERLIST = {"Android Material Design", "Material Design Spinner", "Spinner Using Material Library", "Material Spinner Example"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);


        //Adicione a back_arrow na actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("Reportar um problema");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);

        //Objetos
        locationAddress = (TextView)findViewById(R.id.addressTv);



        Bundle latlngLocation = getIntent().getParcelableExtra("ISSUE_LOCATION");
        markerLocation = latlngLocation.getParcelable("markerLocation");

        //Solicita as informações de endereço utilizando as informações latlng
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(markerLocation.latitude, markerLocation.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            locationAddress.setText(address);
        } catch (IOException e) {
            Log.e(TAG_LOG, "Geocoder Error", e);
            e.printStackTrace();
        }

        //Add Itens to spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.issueTypeSpinner);
        materialDesignSpinner.setAdapter(arrayAdapter);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle latlngLocation = getIntent().getParcelableExtra("ISSUE_LOCATION");
        markerLocation = latlngLocation.getParcelable("markerLocation");

        mMap.addMarker(new MarkerOptions().position(markerLocation).title("Problema relatado"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 18.5f), 800, null);
    }
}
