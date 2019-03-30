package br.edu.ufvjm.gestorvirtual;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import br.edu.ufvjm.gestorvirtual.MySQL.MySQLHelper;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class LoginActivity extends AppCompatActivity {

    MySQLHelper MySQL = new MySQLHelper(this);
    private Button login_btn;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private SessionHandler sessionHandler;

    AutoCompleteTextView email_input;
    TextInputEditText password_input;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_input = (AutoCompleteTextView) findViewById(R.id.email);
        password_input = (TextInputEditText) findViewById(R.id.password);
        mTextView = (TextView) findViewById(R.id.textView2);
        login_btn = (Button) findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doLogin();
            }
        });
    }

    private void doLogin(){

        String password = password_input.getText().toString();
        String email = email_input.getText().toString();
        password = Hashing.sha256().hashString(password, Charsets.UTF_8).toString(); // hashing a senha em sha256

        JSONObject request = new JSONObject();
        try{
        // colocar as informações do login na requisiçao JSON
            request.put(KEY_EMAIL, email);
            request.put(KEY_PASSWORD, password);
        }catch(JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, MySQL.API_LOGIN_URL, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    if(response.getInt(KEY_STATUS) == 1)
                    {
                        sessionHandler.loginUser(email);
                        // mudar para activity inicial do app
                    }else{
                        email_input.setError(response.getString(KEY_MESSAGE));
                        password_input.setError(response.getString(KEY_MESSAGE));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
}
