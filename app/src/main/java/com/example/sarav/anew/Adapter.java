package com.example.sarav.anew;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends ArrayAdapter<listview> {

    List<listview> adList;
    Context context;
    int resource;
    ProgressDialog p;

    public Adapter(Context context, int resource, List<listview> adList) {
        super(context, resource, adList);
        this.context = context;
        this.resource = resource;
        this.adList = adList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);
        p = new ProgressDialog(getContext());
        TextView name = view.findViewById(R.id.name);
        TextView date = view.findViewById(R.id.date);
        TextView hr = view.findViewById(R.id.hr);

        listview ad1 = adList.get(position);

        name.setText(ad1.getSubj());
        date.setText(ad1.getDate());
        hr.setText(ad1.getHr());

        return view;
    }

}
