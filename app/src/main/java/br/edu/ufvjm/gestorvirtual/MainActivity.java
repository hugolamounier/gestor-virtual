package br.edu.ufvjm.gestorvirtual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufvjm.gestorvirtual.MySQL.MySQLHelper;
import br.edu.ufvjm.gestorvirtual.MySQL.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionHandler sessionHandler;
    private static final String FUNC_KEY = "func";
    private static final String EMAIL_KEY = "email";
    private static final String STATUS_KEY = "status";
    private static final String MESSAGE_KEY = "message";

    //variáveis da array response da API
    private static final String NAME_KEY = "name";
    private static final String BIRTHDAY_KEY = "birthday";
    private static final String PROFILE_PICTURE_KEY = "profilePic";
    private static final String GENDER_KEY = "gender";
    private static final String PASSWORD_KEY = "password";


    MySQLHelper MySQL = new MySQLHelper(this);
    User user = new User();

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);

        sessionHandler = new SessionHandler(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Seleciona o leyout de navegação lateral
        View navView = navigationView.getHeaderView(0);

        //Gerenciando fragmentos
        fragmentManager = getSupportFragmentManager();

        // Inicia o mapa no fragmento
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mapView, new MapsFragment(), "MapFragment");
        fragmentTransaction.commitAllowingStateLoss();

        // Objetos
        CircleImageView navHeaderImageProfile = (CircleImageView)navView.findViewById(R.id.nav_header_profilePicture);
        TextView navHeaderUserName = (TextView)navView.findViewById(R.id.nav_header_userName);
        TextView navHeaderUserEmail = (TextView)navView.findViewById(R.id.nav_header_userEmail);
        FrameLayout loadScreen = (FrameLayout)findViewById(R.id.loadScreen);
        loadScreen.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        String email = sessionHandler.returnLoggedUser();
        try
        {
            jsonObject.put(FUNC_KEY, "userInfo");
            jsonObject.put(EMAIL_KEY, email);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MySQL.API_READ_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    if(response!=null && response.length()>0 && response.getInt(STATUS_KEY)==1)//verifica se a array ta vazia e a resposta do servidor 1=ok, 0=not ok
                    {
                        user.setEmail(email);
                        user.setName(response.getString(NAME_KEY));
                        user.setBirth(response.getString(BIRTHDAY_KEY));
                        user.setPassword(response.getString(PASSWORD_KEY));
                        user.setGender(response.getInt(GENDER_KEY));
                        user.setProfilePictureUri(response.getString(PROFILE_PICTURE_KEY));

                        loadNavHeaderUserInfo(navView.getContext(), user, navHeaderImageProfile, navHeaderUserName, navHeaderUserEmail);
                        loadScreen.setVisibility(View.GONE);
                    }
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }
    public void logout()
    {
        sessionHandler.logoutUser();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }
    public void loadNavHeaderUserInfo(Context mContext, User user, CircleImageView navHeaderImageProfile, TextView navHeaderUserName, TextView navHeaderUserEmail)
    {
        Glide.with(mContext).load(user.getProfilePictureUri()).into(navHeaderImageProfile);
        navHeaderUserName.setText(user.getName());
        navHeaderUserEmail.setText(user.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logout();
        }else if(id == R.id.nav_map){
            Intent i = new Intent(getApplicationContext(), MapsFragment.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
