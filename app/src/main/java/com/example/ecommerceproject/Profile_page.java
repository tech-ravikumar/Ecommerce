package com.example.ecommerceproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile_page extends AppCompatActivity {

    EditText pname,pemail,pnumber;
    RequestQueue queue;
    String encodeImageString;
    Bitmap bitmap;
    int pid1;
    String ab;
    ImageView profileimage,backPressed,logbtn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button  sbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        getSupportActionBar().hide();
        profileimage=findViewById(R.id.imageView);
        pname=findViewById(R.id.name);
        pemail=findViewById(R.id.email);
        pnumber=findViewById(R.id.number);
        sbtn=findViewById(R.id.submit);
        logbtn=findViewById(R.id.logout);
        backPressed=findViewById(R.id.imageView5);
        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sharedPreferences=getSharedPreferences("credentials",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed();
            }
        });
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
            }
        });

        ab=sharedPreferences.getString("id","0");
        //pid1 = Integer.parseInt(ab);
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
                Toast.makeText(Profile_page.this, "successfully updated", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        //String aa=sharedPreferences.getString("email","0");
        queue = Volley.newRequestQueue(Profile_page.this);
        fetchUserDetails();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Uri filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                profileimage.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString=android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }
    private void proceed(){
        editor.clear();
        editor.apply();
        startActivity(new Intent(Profile_page.this, Login_Page.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        Toast.makeText(Profile_page.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void fetchUserDetails(){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url=Url.BASEURL+"ShowProfileDetails.php?id=";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+ab, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseprofile",response);
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    Gson gson=new Gson();
                    Log.d("responseprofile1",response);
                    Profile mm=gson.fromJson(String.valueOf(jsonObject),Profile.class);
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
    public void editProfile(){
        final String vname=pname.getText().toString().trim();
        final String vemail=pemail.getText().toString().trim();
        final String vnumber=pnumber.getText().toString().trim();
        String url=Url.BASEURL+"UpdateUserData.php";
        StringRequest request=new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.d("resp1",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> map=new HashMap<String, String>();
                map.put("id",ab);
                map.put("upload",encodeImageString);
                map.put("name",vname);
                map.put("email",vemail);
                map.put("number",vnumber);

                return map;
            }
        };
        request.setShouldCache(false);
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void setViews(Profile mm) {
        String imageUrl=Url.BASEURL+"upload/";
        String image = imageUrl +mm.getImage();
        Log.d("response2", ""+mm.getName());
        pname.setText(""+mm.getName());
        pemail.setText(""+mm.getEmail());
        pnumber.setText(""+mm.getNumber());
        Glide.with(Profile_page.this).load(Uri.parse(image)).into(profileimage);
    }

}