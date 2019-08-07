package br.edu.ufvjm.gestorvirtual;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class GetVolleyResponse {

    private Context mContext;

    public GetVolleyResponse(Context mContext) {
        this.mContext = mContext;
    }

    public void getResponse(String url, JSONObject jsonObject, final VolleyCallback callback)
    {
        RequestQueue queue = MySingleton.getInstance(mContext).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext, error + "error", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }
}
