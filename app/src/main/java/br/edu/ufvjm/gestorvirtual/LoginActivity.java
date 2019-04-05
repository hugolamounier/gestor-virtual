package br.edu.ufvjm.gestorvirtual;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufvjm.gestorvirtual.MySQL.MySQLHelper;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class LoginActivity extends AppCompatActivity {

    MySQLHelper MySQL = new MySQLHelper(this);
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private SessionHandler sessionHandler;

    AutoCompleteTextView email_input;
    TextInputEditText password_input;
    Button btnLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionHandler = new SessionHandler(getApplicationContext());
        if(sessionHandler.isLoggedIn())
        {
            loadMainActivity();
        }
        setContentView(R.layout.activity_login);
        email_input = (AutoCompleteTextView) findViewById(R.id.emailTi);
        password_input = (TextInputEditText) findViewById(R.id.passwordTi);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs())
                {
                    doLogin();
                }

            }
        });
    }

    private void doLogin(){

        String password = password_input.getText().toString();
        String email = email_input.getText().toString();
        // password = Hashing.sha256().hashString(password, Charsets.UTF_8).toString(); // hashing a senha em sha256


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
                        loadMainActivity();
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

    private void loadMainActivity()
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
    private boolean validateInputs() {
        if("".equals(email_input.getText().toString())){
            email_input.setError("E-mail não pode ser vazio.");
            email_input.requestFocus();
            return false;
        }
        if("".equals(password_input.getText().toString())){
            password_input.setError("Senha não pode ser vazia.");
            password_input.requestFocus();
            return false;
        }
        return true;
    }
}
