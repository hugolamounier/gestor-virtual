package br.edu.ufvjm.gestorvirtual.MySQL;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

import br.edu.ufvjm.gestorvirtual.LoginActivity;
import br.edu.ufvjm.gestorvirtual.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySQLHelper {

    private Context mContext;
    private String API_READ_URL = "http://gestorapi.hlserver.ga/read.php";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();


    public MySQLHelper(Context context) {
        this.mContext = context;
    }

    public void checkLogin(String email, String password) throws IOException
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // TextView mTextView = ((Activity)mContext).findViewById(R.id.textView2); // Seleciona elementos do contexto, ou seja, da activity que invocou essa classe.

        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_READ_URL).newBuilder();
        urlBuilder.addQueryParameter("func", "checkLogin");
        String API_URL = urlBuilder.build().toString();

        RequestBody requestBody = new MultipartBody.Builder() // Adiciona os parâmetros de POST
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .build();

        Request request = new Request.Builder() // Cria uma request HTTP
                .addHeader("Content-Type", " application/x-www-form-urlencoded")
                .post(requestBody)
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful())
                {
                    final String myResponse = response.body().string();

                }
            }
        });
    }
}
