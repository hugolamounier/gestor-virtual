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

public class MySQLHelper {

    private Context mContext;
    public final static String API_READ_URL = "http://gestorapi.hlserver.ga/read.php";
    public final static String API_LOGIN_URL = "http://gestorapi.hlserver.ga/login.php";
    public final static String API_INSERT_URL = "http://gestorapi.hlserver.ga/insert.php";

    public final static String SERVER_URI = "http://gestorapi.hlserver.ga";

    public MySQLHelper(Context context) {
        this.mContext = context;
    }

}
