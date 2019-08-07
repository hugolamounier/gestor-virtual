package br.edu.ufvjm.gestorvirtual;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    MySQLHelper MySQL = new MySQLHelper();
    private static final String FUNC_KEY = "func";
    private static final String EMAIL_KEY = "email";
    private static final String STATUS_KEY = "status";
    private static final String NAME_KEY = "name";
    private static final String BIRTHDAY_KEY = "birthday";
    private static final String PROFILE_PICTURE_KEY = "profilePic";
    private static final String GENDER_KEY = "gender";
    private static final String PASSWORD_KEY = "password";
    private SessionHandler sessionHandler;
    User user = new User();
    TextView perfilText;
    CircleImageView perfilImg;


    public PerfilFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        //Objectos
        TextView perfilText = (TextView) view.getRootView().findViewById(R.id.perfilText);
        CircleImageView perfilImg = (CircleImageView) view.getRootView().findViewById(R.id.perfilImg);
        sessionHandler = new SessionHandler(getContext());

        FrameLayout loadScreen = (FrameLayout)view.findViewById(R.id.loadScreen);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MySQLHelper.API_READ_URL, jsonObject, new Response.Listener<JSONObject>() {
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
                        perfilText.setText(user.getName());
                        Glide.with(getContext()).load(user.getProfilePictureUri()).into(perfilImg);
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        return view;
    }

}
