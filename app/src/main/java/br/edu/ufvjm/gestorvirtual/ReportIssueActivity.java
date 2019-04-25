package br.edu.ufvjm.gestorvirtual;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.provider.CalendarContract.CalendarCache.URI;

public class ReportIssueActivity extends FragmentActivity implements OnMapReadyCallback{


    private LatLng markerLocation;
    private GoogleMap mMap;
    private TextView locationAddress;
    private Button addImagmeBtn;
    private static final String TAG_LOG = "ReportIssueActivity";
    private String currentPhotoPath;
    private ImageView imgViewThumb;

    private Uri pictureUri;

    static final int REQUEST_TAKE_PHOTO = 1;

    String[] SPINNERLIST = {"1", "2", "3", "4"};

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

        addImagmeBtn = (Button)findViewById(R.id.addImage);
        addImagmeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    public Uri getPictureUri() {
        return pictureUri;
    }
    public void setPictureUri(Uri pictureUri) {
        this.pictureUri = pictureUri;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG_LOG, "ReportIssueActivity_dispImage", ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                setPictureUri(URI.fromFile(photoFile));
            }
        }
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
