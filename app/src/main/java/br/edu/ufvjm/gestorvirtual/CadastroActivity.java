package br.edu.ufvjm.gestorvirtual;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.edu.ufvjm.gestorvirtual.MySQL.MySQLHelper;

public class CadastroActivity extends AppCompatActivity {

    EditText emailEt;
    EditText emailConEt;
    EditText passwordEt;
    EditText passwordConET;
    EditText nameET;
    EditText dateET;
    TextView genderError;
    RadioGroup genderRg;

    private String EMAIL;
    private String EMAILC;
    private String PASS;
    private String PASSC;
    private String NAME;
    private String DATE;
    private String GENDER;
    private Button BTN;

    MySQLHelper MySQL = new MySQLHelper(this);

    private static final String STATUS_KEY = "status";
    private static final String MESSAGE_KEY = "message";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_PROFPIC = "profile";

    private String PROF_DEFAULT_PIC_URI = MySQL.SERVER_URI+"/images/profile/default.jpg";
    private static final String TAG = "Cadastro";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cadastro);

        //Objetos
        emailEt = (EditText) findViewById(R.id.emailEt);
        emailConEt = findViewById(R.id.emailConEt);
        passwordEt= findViewById(R.id.passwordET);
        passwordConET = findViewById(R.id.passwordConET);
        nameET = findViewById(R.id.nameET);
        dateET = findViewById(R.id.dateET);
        dateET.addTextChangedListener(new MaskWatcher("##/##/####"));
        BTN = findViewById(R.id.btnSignUp);
        genderRg = findViewById(R.id.genderRg);
        genderError = findViewById(R.id.genderError);

        BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recupera o radiobutton marcado
                int genderId = genderRg.getCheckedRadioButtonId();
                int gender;

                EMAIL = emailEt.getText().toString();
                EMAILC = emailConEt.getText().toString();
                PASS = passwordEt.getText().toString();
                PASSC = passwordConET.getText().toString();
                NAME = nameET.getText().toString();
                DATE = dateET.getText().toString();

                //Pega o valor da string do radiobutton marcado e converte os sexos para int
                RadioButton sex = (RadioButton)findViewById(genderId);
                if(sex.getText().toString().equals(getResources().getString(R.string.C_Masculino)))
                {
                    gender = 0; //Masculino
                }else if(sex.getText().toString().equals(getResources().getString(R.string.C_Feminino))){
                    gender = 1; //Feminino
                }else{
                    gender = 2; //Outros
                }


                emptyInput(); // Verifica se existe campos vazios.

                if(!validInput(EMAIL)){
                    emailEt.setError("Email Inválido");
                }else if (!EMAIL.equals(EMAILC)){
                    emailConEt.setError("E-mails incompativeis");

                }else if(!PASS.equals(PASSC)){
                    passwordConET.setError("Senhas incompativeis");
                }else{
                    JSONObject jsonObject = new JSONObject();
                    try{
                        jsonObject.put("func", "singUp");
                        jsonObject.put(KEY_EMAIL, EMAIL);
                        jsonObject.put(KEY_PASSWORD, PASS);
                        jsonObject.put(KEY_NAME, NAME);
                        jsonObject.put(KEY_GENDER, gender);
                        jsonObject.put(KEY_BIRTHDAY, DATE);
                        jsonObject.put(KEY_PROFPIC, PROF_DEFAULT_PIC_URI);

                    }catch (JSONException e){
                        Log.e(TAG, "JSONObjectCreation", e);
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MySQL.APU_INSERT_URL, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try
                            {
                                if(response!=null && response.length()>0 && response.getInt(STATUS_KEY)==1)
                                {

                                    finish();
                                }
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                }
            }

        });

    }

    private void emptyInput(){ //Confere se não existe nenhum campo vazio
        EditText emailEt = (EditText) findViewById(R.id.emailEt);
        EditText emailConEt =(EditText) findViewById(R.id.emailConEt);
        EditText passwordEt = (EditText) findViewById(R.id.passwordET);
        EditText passwordConET = (EditText) findViewById(R.id.passwordConET);
        EditText nameET = (EditText) findViewById(R.id.nameET);
        EditText dateET = (EditText) findViewById(R.id.dateET);

        if ("".equals(emailEt.getText().toString())){
            emailEt.setError("E-mail não pode ser vazio.");
            emailEt.requestFocus();

        }
        if ("".equals(emailConEt.getText().toString())){
            emailConEt.setError("E-mail não pode ser vazio.");
            emailConEt.requestFocus();

        }
        if ("".equals(passwordEt.getText().toString())){
            passwordEt.setError("Senha não pode ser vazia.");
            passwordEt.requestFocus();

        }
        if ("".equals(passwordConET.getText().toString())){
            passwordConET.setError("Senha não pode ser vazia.");
            passwordConET.requestFocus();

        }
        if ("".equals(nameET.getText().toString())) {
            nameET.setError("Nome não pode ser vazio.");
            nameET.requestFocus();

        }
        if ("".equals(dateET.getText().toString())) {
            dateET.setError("A data não pode estar vazia.");
            dateET.requestFocus();

        }
        if(genderRg.getCheckedRadioButtonId()==-1){
            genderError.setText("Selecione um genero");

        }

    }

    private boolean validInput(String EMAIL){

        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(EMAIL);
        boolean matchF = m.matches();
        if (matchF){
            return true;
        }
        else {
            return false;
        }
    }

}
