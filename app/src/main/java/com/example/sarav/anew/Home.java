package com.example.sarav.anew;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public class Home extends Fragment {
    TextView date;
    Button sub;
    Spinner subj,number;
    LineChart lineChart;
    public static final String MyPREFERENCES = "MyPrefs";
    private DatePickerDialog.OnDateSetListener DateSetListener;
    public static final String TAG = "Home";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_frag,container,false);
        date = v.findViewById(R.id.date);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
        Date date1 = new Date();
        String s = dateFormat.format(date1);
        date.setText(s);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        Spinner dropdown = v.findViewById(R.id.subj);
        Spinner dropdown1 = v.findViewById(R.id.number);
        sub = v.findViewById(R.id.sub);
        String[] subj = new String[12];
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
                    dropdown.setAdapter(adapter);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(getContext(),"Network Error", Toast.LENGTH_SHORT).show()) {
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

        date.setOnClickListener((View v3)->{
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Dialog_MinWidth,DateSetListener,year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date.setText(new StringBuilder().append(i2).append("/").append(i1+1).append("/").append(i));
            }
        };

        String[] items1 = new String[]{"1", "2", "3", "4"};
        //Toast.makeText(getContext(),items1.toString(),Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items1);
        dropdown1.setAdapter(adapter1);

        sub.setOnClickListener((View v1)->{
            String bunksubj = dropdown.getSelectedItem().toString();
            String bunkdate = date.getText().toString();
            String bunkhr = dropdown1.getSelectedItem().toString();

            String s1 = sharedpreferences.getString("roll", "");
//            String subj[] =  new String[12];
            try {
                JSONObject obj = new JSONObject();
                obj.put("bunkdate",bunkdate);
                obj.put("bunkhour",bunkhr);
                JSONArray arr = new JSONArray();
                arr.put(obj);
                JSONObject main = new JSONObject();
                main.put("bunk",arr);
                main.put("subject",bunksubj);
                main.put("roll",s1);

                String URL = " https://bunk-manager.herokuapp.com/bunk";

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                final String mRequestBody = main.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response ->
                        Toast.makeText(getContext(), "Successfully Bunked", Toast.LENGTH_SHORT).show(),
                        error -> Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show()) {
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


        });

        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
