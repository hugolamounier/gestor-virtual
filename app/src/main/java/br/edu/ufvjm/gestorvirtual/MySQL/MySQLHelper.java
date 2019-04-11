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
    public String API_READ_URL = "http://gestorapi.hlserver.ga/read.php";
    public String API_LOGIN_URL = "http://gestorapi.hlserver.ga/login.php";

    public MySQLHelper(Context context) {
        this.mContext = context;
    }

}
