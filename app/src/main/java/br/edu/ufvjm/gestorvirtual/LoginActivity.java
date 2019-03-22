package br.edu.ufvjm.gestorvirtual;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import br.edu.ufvjm.gestorvirtual.MySQL.MySQLHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    MySQLHelper mysql = new MySQLHelper(this);
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mysql.checkLogin("hugolamo@gmail.com","019545");
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }
}
