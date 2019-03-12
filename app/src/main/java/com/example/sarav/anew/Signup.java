package com.example.sarav.anew;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Signup extends AppCompatActivity {

    private  static final String TAG = "Signup";
    boolean flag = true;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

       final EditText pass = findViewById(R.id.pass);
        final  EditText cpass = findViewById(R.id.cpass);
        final EditText roll = findViewById(R.id.roll);
        final ImageView eye1 = findViewById(R.id.eye1);
        final  Button signup = findViewById(R.id.signup);
        final  ImageView eye2 = findViewById(R.id.eye2);
        final EditText name = findViewById(R.id.name);
        final  EditText ph = findViewById(R.id.ph);

        eye1.setOnClickListener((view)-> {
            if (!flag) {
                pass.setTransformationMethod(new PasswordTransformationMethod());
                flag = true;
                eye1.setImageResource(R.drawable.ic_visibility_off_black_24dp);
            } else {
                pass.setTransformationMethod(null);
                flag = false;
                eye1.setImageResource(R.drawable.ic_visibility_black_24dp);
            }
        });


        eye2.setOnClickListener((view)-> {
            if (!flag) {
                cpass.setTransformationMethod(new PasswordTransformationMethod());
                flag = true;
                eye2.setImageResource(R.drawable.ic_visibility_off_black_24dp);
            } else {
                cpass.setTransformationMethod(null);
                flag = false;
                eye2.setImageResource(R.drawable.ic_visibility_black_24dp);
            }
        });


        signup.setOnClickListener((View v)-> {
               String name1 = name.getText().toString();
               String roll1 = roll.getText().toString();
               String ph1 = ph.getText().toString();
               String pass1 = pass.getText().toString();
               String cpass1 = cpass.getText().toString();

            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            Editor editor = sharedpreferences.edit();

                if (check(name1,roll1,ph1,pass1,cpass1) == 0) {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("name", name1);
                        obj.put("roll", roll1);
                        obj.put("phone", ph1);
                        obj.put("password", pass1);
                        obj.put("cpassword", cpass1);
                        String URL = " https://bunk-manager.herokuapp.com/signup";

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                        final String mRequestBody = obj.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                            Toast.makeText(this,"SignUp Successfull",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("res", response);
//                            editor.putString("token",response);
//                            editor.putString("roll",roll1);
//                            editor.commit();
                            Toast.makeText(this,response,Toast.LENGTH_SHORT).show();
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
                }

        });
    }




   public int check(String nam, String rol, String p, String pas, String cpas) {
       if (nam.isEmpty()) {
           Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
           return 1;
       }else if (rol.length() != 10) {
           Toast.makeText(this, "Enter valid Roll number", Toast.LENGTH_SHORT).show();
           return 1;
       }else if(p.length()!=10){
         Toast.makeText(this,"Enter Valid Phone Number",Toast.LENGTH_SHORT).show();
         return 1;
       }  else if (pas.length() < 8) {
           Toast.makeText(this, "Enter minimum 8 characters for password", Toast.LENGTH_SHORT).show();
           return 1;
       }else if (!cpas.equals(pas)){
               Toast.makeText(this, "Enter same password for Confirm Password", Toast.LENGTH_SHORT).show();
               return 1;
       }else {
           return 0;
       }
   }
}