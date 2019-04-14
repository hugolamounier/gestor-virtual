package br.edu.ufvjm.gestorvirtual;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

public class CreatePermission extends Permission {
    private static final int REQUEST_PERMISSION = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        requestAppPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET},
                R.string.permission,REQUEST_PERMISSION);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        //Do anything when permisson granted
        Intent i = new Intent(CreatePermission.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
