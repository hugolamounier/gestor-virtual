package br.edu.ufvjm.gestorvirtual;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.CalendarContract.CalendarCache.URI;

public class ReportIssueActivity extends FragmentActivity implements OnMapReadyCallback{


    private LatLng markerLocation;
    private GoogleMap mMap;
    private TextView locationAddress;
    private Button addImagmeBtn, submitIssueBtn;
    private static final String TAG_LOG = "ReportIssueActivity";
    private String currentPhotoPath;
    private CircleImageView imgViewThumb;

    private SessionHandler sessionHandler;

    private Uri pictureUri;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ISSUE_STATUS = "issue_status";
    private static final String KEY_ISSUE_CAT = "issue_cat";
    private static final String KEY_ISSUE_DESC = "issue_description";
    private static final String KEY_REPORT_DATE = "report_date";



    String[] SPINNERLIST = {"1", "2", "3", "4"}; //Categorias de problemas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        setPictureUri(null);

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


        //Objetos
        locationAddress = (TextView)findViewById(R.id.addressTv);
        submitIssueBtn = (Button)findViewById(R.id.submitIssue);
        addImagmeBtn = (Button)findViewById(R.id.addImage);

        //Pega as informações passadas para a Activity.
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


        addImagmeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPictureUri() == null) {
                    dispatchTakePictureIntent();
                }else{
                    File unusedFile = new File(getPictureUri().getEncodedPath()); //Deleta o arquivo que não foi utilizado antes de tirar uma nova foto
                    try {
                        unusedFile.delete();
                        dispatchTakePictureIntent();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        submitIssueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                String emailLogged = sessionHandler.returnLoggedUser();
                try
                {
                    jsonObject.put(KEY_LAT, markerLocation.latitude);
                    jsonObject.put(KEY_LNG, markerLocation.longitude);
                    jsonObject.put(KEY_EMAIL, emailLogged);
                }catch (JSONException e){
                    Log.e(TAG_LOG, "", e);
                }
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
    protected void onDestroy() {
        if(getPictureUri() != null) {
            File unsedFile = new File(getPictureUri().getEncodedPath()); //Deleta o arquivo que não foi utilizado
            try {
                unsedFile.delete();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imgViewThumb = (CircleImageView) findViewById(R.id.imgViewThumb);

            imgViewThumb.setImageBitmap(BitmapFactory.decodeFile(getPictureUri().getEncodedPath())); //Transforma a imagem em bitmap e carrega no imageview

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle latlngLocation = getIntent().getParcelableExtra("ISSUE_LOCATION");
        markerLocation = latlngLocation.getParcelable("markerLocation");

        mMap.addMarker(new MarkerOptions().position(markerLocation).title("Problema relatado"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 18.5f), 800, null);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}
