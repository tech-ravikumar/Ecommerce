package com.example.ecommerceproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductClick extends AppCompatActivity {

    RequestQueue queue;

    ImageView imageV;
    int cid1;

    TextView ttitle,tsubtitle,tcutprice,tprice,toff,cid,tdate;
    List<Product> arraylist;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_click);
        getSupportActionBar().hide();


        Intent intent = getIntent();
        String cid2 = intent.getStringExtra("key");
        cid1 = Integer.parseInt(cid2);

        imageV=findViewById(R.id.img_view);
        ttitle=findViewById(R.id.txt_title);
        tsubtitle=findViewById(R.id.txt2);
        tcutprice=findViewById(R.id.txt3);
        tprice=findViewById(R.id.txt4);
        toff=findViewById(R.id.offs);
        cid =findViewById(R.id.cid);
        tdate=findViewById(R.id.cdate);



       // recyclerView = findViewById(R.id.trending);
        arraylist = new ArrayList<>();
        queue = Volley.newRequestQueue(ProductClick.this);
        fetchProduct();

    }
    private void fetchProduct(){
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

        queue.add(stringRequest);
    }

    private void setViews(Product mm) {
        String imageUrl=Url.BASEURL+"upload/";
        String image = imageUrl +mm.getPimage();
        Log.d("response", ""+mm.getPsubtittle());
        ttitle.setText(""+mm.getPtittle());
        tsubtitle.setText(""+mm.getPsubtittle());
        tcutprice.setText(""+mm.getPcutprice());
        tprice.setText(""+mm.getPprice());
        toff.setText(""+mm.getPoff());
        tdate.setText(""+mm.getPdate());
        Glide.with(ProductClick.this).load(Uri.parse(image)).into(imageV);
    }
}