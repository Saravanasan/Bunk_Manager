package com.example.sarav.anew;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button login;
    private TextView register1;
    private EditText password, roll;
    private ImageView eye;
    public static final String MyPREFERENCES = "MyPrefs" ;
    boolean flag = true;
    private int num;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        String username = sharedpreferences.getString("roll","");
       String passw = sharedpreferences.getString("password","");

        if(username.isEmpty() ) {
            setContentView(R.layout.activity_main);
        }
        else{
            Intent intent1 = new Intent(getApplicationContext(),First.class);
            startActivity(intent1);
        }
        login = (Button) findViewById(R.id.login);
        Button sign = (Button) findViewById(R.id.signup);
        password = (EditText) findViewById(R.id.password);
        eye = (ImageView) findViewById(R.id.eye);
        roll = (EditText)findViewById(R.id.roll);


        try {
            sign.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            eye.setOnClickListener((view)-> {
                if (!flag) {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    flag = true;
                    eye.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                } else {
                    password.setTransformationMethod(null);
                    flag = false;
                    eye.setImageResource(R.drawable.ic_visibility_black_24dp);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            login.setOnClickListener((View v)->{
                String roll1 = roll.getText().toString();
                String pass = password.getText().toString();

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("roll", roll1);
                    obj.put("password", pass);
                    editor.putString("roll",roll1);
                    editor.putString("password",pass);
                    String URL = " https://bunk-manager.herokuapp.com/login";

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    final String mRequestBody = obj.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                        Toast.makeText(this,"Login Successfull",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), First.class);
                        i.putExtra("res", response);
                        editor.putString("token",response);
                        editor.commit();
                        startActivity(i);
                        finish();
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
