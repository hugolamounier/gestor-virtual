package br.edu.ufvjm.gestorvirtual;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Button BTN;





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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cadastro);

        emailEt = (EditText) findViewById(R.id.emailEt);
        emailConEt = findViewById(R.id.emailConEt);
        passwordEt= findViewById(R.id.passwordET);
        passwordConET = findViewById(R.id.passwordConET);
        nameET = findViewById(R.id.nameET);
        dateET = findViewById(R.id.dateET);
        BTN = findViewById(R.id.btnSignUp);
        genderRg =findViewById(R.id.genderRg);
        genderError = findViewById(R.id.genderError);
        dateET.addTextChangedListener(new MaskWatcher("##/##/####"));
        BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMAIL = emailEt.getText().toString();
                EMAILC = emailConEt.getText().toString();
                PASS = passwordEt.getText().toString();
                PASSC = passwordConET.getText().toString();
                NAME = nameET.getText().toString();
                DATE = dateET.getText().toString();
                emptyInput();

                if(!validInput(EMAIL)){
                    emailEt.setError("Email Inválido");
                }
                if (!EMAIL.equals(EMAILC)){
                    emailConEt.setError("E-mails incompativeis");

                }
                if(!PASS.equals(PASSC)){
                    passwordConET.setError("Senhas incompativeis");
                }
            }

        });

    }

}
