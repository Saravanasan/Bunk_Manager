package com.example.sarav.anew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewData extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    List<listview> adList;
    ListView listView;
    JSONArray res;
    int status = 0;
    String subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlist);

        Intent n = getIntent();
        Bundle b = new Bundle();
        b =  n.getExtras();
        int f = b.getInt("flag");
        String subj = b.getString("subjects");
        subject = subj;



        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        String roll = sharedpreferences.getString("roll","");
        String tok =  sharedpreferences.getString("token","");

        adList = new ArrayList<>();
        listView = findViewById(R.id.list);


        if(f==1){
            try {
                JSONObject obj = new JSONObject();
                obj.put("roll",roll);
                obj.put("subject", subj);
                String URL = " https://bunk-manager.herokuapp.com/getbunksubj";
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                final String mRequestBody = obj.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                    status = 1;
                    try {
                        res = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    func();
                }, error -> Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show()) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }


                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("x-auth",tok);
                        return headers;
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {

                            try {
                                responseString = new String(response.data , "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                requestQueue.add(stringRequest);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    if(f==2){
        try {
            JSONObject obj = new JSONObject();
            obj.put("roll",roll);
            obj.put("subject", subj);
            String URL = " https://bunk-manager.herokuapp.com/getbunkall";


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            final String mRequestBody = obj.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                status = 2;
                try {
                    res = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                func();
            }, error -> Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show()) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("x-auth",tok);
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }


                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {

                        try {
                            responseString = new String(response.data , "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}


void func(){

        if(status == 1) {
            try {
               setlist1();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(status == 2) {
            try {
                setlist();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Adapter adapter = new Adapter(this, R.layout.cell, adList);

        listView.setAdapter(adapter);

        ListAdapter listadp = listView.getAdapter();
        if (listadp != null) {
            int totalHeight = 0;
            for (int i = 0; i < listadp.getCount(); i++) {
                View listItem = listadp.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listadp.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }

    }

    public void setlist() throws JSONException {
        for(int i=0; i<res.length(); i++) {
            JSONObject jsonobj = res.getJSONObject(i);
            JSONArray jsa = jsonobj.getJSONArray("bunk");
            for (int i1 = 0; i1 < jsa.length(); i1++) {
                JSONObject jsonobj1 = jsa.getJSONObject(i1);
                adList.add(new listview(jsonobj.getString("subject"), jsonobj1.getString("bunkdate"), jsonobj1.getString("bunkhour")));
            }
        }
    }


    public void setlist1() throws JSONException {
        for(int i=0; i<res.length(); i++) {
            JSONObject jsonobj = res.getJSONObject(i);
                adList.add(new listview(subject, jsonobj.getString("bunkdate"), jsonobj.getString("bunkhour")));
            }
    }
}

