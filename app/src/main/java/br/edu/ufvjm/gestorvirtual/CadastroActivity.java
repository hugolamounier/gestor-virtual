package br.edu.ufvjm.gestorvirtual;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CadastroActivity extends AppCompatActivity {

    TextView emailEt;
    TextView emailConEt;
    TextView passwordEt;
    TextView passwordConET;
    TextView nameET;
    TextView dateET;
    TextView genderError;
    RadioGroup genderRg;
    Button btnSingUp;

    private String EMAIL;
    private String EMAILC;
    private String PASS;
    private String PASSC;
    private String NAME;
    private String DATE;
    private Button BTN;



    private void emptyInput(){ //Confere se não existe nenhum campo vazio
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
            dateET.setError("Nome não pode ser vazio.");
            dateET.requestFocus();

        }
        if(genderRg.getCheckedRadioButtonId()==-1){
            genderError.setText("Selecione um genero");

        }

    }

    private void validateInput(){ //Confere se os emails e senhas são compativeis
        if (emailEt.getText().toString()!=emailConEt.getText().toString()){
            emailConEt.setError("E-mail não correspondentes");
        }
        if (passwordEt.getText().toString()!=passwordConET.getText().toString()) {
            passwordConET.setError("Senhas não compativeis");
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cadastro);

        emailEt = (TextView) findViewById(R.id.emailEt);
        emailConEt = findViewById(R.id.emailConEt);
        passwordEt= findViewById(R.id.passwordET);
        passwordConET = findViewById(R.id.passwordConET);
        nameET = findViewById(R.id.nameET);
        dateET = findViewById(R.id.dateET);
        BTN = findViewById(R.id.btnSignUp);
        genderRg =findViewById(R.id.genderRg);
        genderError = findViewById(R.id.genderError);
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
                validateInput();
            }

        });

    }

}
