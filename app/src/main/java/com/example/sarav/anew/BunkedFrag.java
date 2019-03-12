package com.example.sarav.anew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BunkedFrag extends Fragment {
    Button getsubj,getall;
    Spinner bunksubj;
    public static final String MyPREFERENCES = "MyPrefs";
    private int flag = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v2 = inflater.inflate(R.layout.bunkfrag, container, false);

        getsubj = v2.findViewById(R.id.getsubj);
        getall = v2.findViewById(R.id.getall);
        bunksubj = v2.findViewById(R.id.bunksubj);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        String rollno = sharedpreferences.getString("roll","");
        String tok =  sharedpreferences.getString("token","");


        try {
            JSONObject obj = new JSONObject();
            obj.put("roll", rollno);

            String URL = " https://bunk-manager.herokuapp.com/getdata";

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            final String mRequestBody = obj.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                String[] res = new String[15];
                res = response.split(",");
                String[] res1 = new String[15];
                for(int i=0;i<res.length;i++){
                    res1[i] = res[i].replaceAll("\\p{P}","");
                }

                try{

                    List<String> spinnerArray = new ArrayList<>();
                    for(int i=0;i<res.length;i++){
                        spinnerArray.add(res1[i]);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bunksubj.setAdapter(adapter);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show()) {
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
                            responseString = new String(response.data, "UTF-8");
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


        getsubj.setOnClickListener((View v)->{
            flag = 1;
            String s = bunksubj.getSelectedItem().toString();
            Bundle b = new Bundle();
            b.putInt("flag",flag);
            b.putString("subjects",s);
            Intent i = new Intent(getActivity(),ViewData.class);
            i.putExtras(b);
            startActivity(i);
        });

        getall.setOnClickListener((View v)->{
            flag = 2;
            Bundle b = new Bundle();
            b.putInt("flag",flag);
            Intent i = new Intent(getActivity(),ViewData.class);
            i.putExtras(b);
            startActivity(i);
        });

        return v2;
    }
}
