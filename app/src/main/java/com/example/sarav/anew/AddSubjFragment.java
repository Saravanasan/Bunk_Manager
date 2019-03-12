package com.example.sarav.anew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AddSubjFragment extends Fragment {

    private EditText cr, asubj;
    public Button submit,del;
    Activity context = getActivity();
    public static final String MyPREFERENCES = "MyPrefs";
    public  static int id  = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_addsubj, container, false);

        cr = v.findViewById(R.id.addcredit);
        asubj = v.findViewById(R.id.addsubj);
        del = v.findViewById(R.id.del);
        submit = v.findViewById(R.id.submit);


        submit.setOnClickListener((View w) -> {
            String subj1 = asubj.getText().toString();
            String crd = cr.getText().toString();
            if(check(subj1,crd)==0) {

                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String s1 = sharedpreferences.getString("roll", "");
                String s = sharedpreferences.getString("token", "");
                String subj[] = new String[12];
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("subject", subj1);
                    obj.put("credit", crd);
                    obj.put("roll", s1);

                    String URL = " https://bunk-manager.herokuapp.com/subjsave";

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                    final String mRequestBody = obj.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                        Toast.makeText(getContext(), "Successfully Inserted", Toast.LENGTH_SHORT).show();
                    }, error -> Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show()) {
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
                            headers.put("x-auth", s);
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
            }

        });

        del.setOnClickListener((View w) -> {
            String subj1 = asubj.getText().toString();
            String crd = cr.getText().toString();
            if(check(subj1,crd)==0) {

                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String s1 = sharedpreferences.getString("roll", "");
                String s = sharedpreferences.getString("token", "");
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("subject", subj1);
                    obj.put("roll", s1);

                    String URL = " https://bunk-manager.herokuapp.com/delete";

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                    final String mRequestBody = obj.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                        Toast.makeText(getContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    }, error -> Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show()) {
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
                            headers.put("x-auth", s);
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

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public int check(String subj, String cr) {
        if (subj.isEmpty()) {
            Toast.makeText(getContext(), "Subject cannot be empty", Toast.LENGTH_SHORT).show();
            return 1;
        }else if (cr.isEmpty()) {
            Toast.makeText(getContext(), "Credit cannot be empty", Toast.LENGTH_SHORT).show();
            return 1;
        }else {
            return 0;
        }
    }

}


