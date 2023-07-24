package com.example.ecommerceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductEditAct extends AppCompatActivity {

    EditText ttitle,tsubtitle,tcutprice,tprice,toff;
    TextView cdd;
    int cid1;



    Button submit;
    String cid2;
    RequestQueue queue;
    List<Product> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        ttitle=findViewById(R.id.e_title);
        tsubtitle=findViewById(R.id.etxt2);
        tcutprice=findViewById(R.id.etxt3);
        tprice=findViewById(R.id.etxt4);
        toff=findViewById(R.id.eoffs);
        cdd = findViewById(R.id.ecid);
        submit=findViewById(R.id.esubmit);


        Intent intent = getIntent();
        cid2 = intent.getStringExtra("keys");
        cid1 = Integer.parseInt(cid2);

        arraylist = new ArrayList<>();
        queue = Volley.newRequestQueue(ProductEditAct.this);
        fetchProduct();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ttitle.getText().length()>0&&tsubtitle.getText().length()>0&&tcutprice.getText().length()>0&&tprice.getText().length()>0&&toff.getText().length()>0) {
                    editProduct();
                    onBackPressed();
                    Toast.makeText(ProductEditAct.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProductEditAct.this, "Plz Fill All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fetchProduct(){
        arraylist.clear();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url=Url.BASEURL+"ProductOnClick.php?pid=";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+cid1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    Gson gson=new Gson();
                    Log.d("response",response);
                    Product mm=gson.fromJson(String.valueOf(jsonObject),Product.class);
                    setViews(mm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        });
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    private void editProduct(){

        arraylist.clear();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String vtittle=ttitle.getText().toString().trim();
        final String vsubtittle=tsubtitle.getText().toString().trim();
        final String vcutprice=tcutprice.getText().toString().trim();
        final String vprice=tprice.getText().toString().trim();
        final String voff=toff.getText().toString().trim();
        String url=Url.BASEURL+"EditProduct.php?pid=";
        StringRequest stringRequest1=new StringRequest(Request.Method.POST, url+cid1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                Log.d("res1",response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> map=new HashMap<String, String>();
                map.put("pid",cid2);
                map.put("tittle",vtittle);
                map.put("subtittle",vsubtittle);
                map.put("cutprice",vcutprice);
                map.put("price",vprice);
                map.put("off",voff);

                return map;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        stringRequest1.setShouldCache(false);
        queue.add(stringRequest1);
    }

    private void setViews(Product mm) {
        Log.d("response", ""+mm.getPsubtittle());
        ttitle.setText(""+mm.getPtittle());
        tsubtitle.setText(""+mm.getPsubtittle());
        tcutprice.setText(""+mm.getPcutprice());
        tprice.setText(""+mm.getPprice());
        toff.setText(""+mm.getPoff());

    }
}