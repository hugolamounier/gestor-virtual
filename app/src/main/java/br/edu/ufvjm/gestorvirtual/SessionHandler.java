package br.edu.ufvjm.gestorvirtual;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    public void loginUser(String email) {
        mEditor.putString(KEY_EMAIL, email);
        Date date = new Date();

        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000); //Define a sessâo do usuário para expirar em 7 dias
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    public String returnLoggedUser()
    {
        String email = mPreferences.getString(KEY_EMAIL, "");
        return email;
    }

    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* Se SP nao possui um valor def. o usuario não está logado */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* verifica se a sessão expirou */
        return currentDate.before(expiryDate);
    }
    /* logout limpando SP */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }
}
