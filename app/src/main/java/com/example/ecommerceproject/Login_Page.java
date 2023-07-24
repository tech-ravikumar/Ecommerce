package com.example.ecommerceproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login_Page extends AppCompatActivity {

    TextView register,skipbtn;

    EditText eemail,epassword;
    Button login;
    RequestQueue queue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String apiurl=Url.BASEURL+"Login_Ecommerce.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().hide();
        register=findViewById(R.id.vregister);
        eemail=findViewById(R.id.vemail);
        epassword=findViewById(R.id.vpassword);
        login=findViewById(R.id.vsignin);

        sharedPreferences=getSharedPreferences("credentials",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        skipbtn=findViewById(R.id.skip);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Login_Page.this,Register_page.class);
                startActivity(in);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eemail.getText().length()>0&&epassword.getText().length()>0){
                    loginUser();
                }
                else{
                    Toast.makeText(Login_Page.this, "invalid username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Login_Page.this,HomeActivity.class);
                startActivity(in);
                finish();

            }
        });
        queue = Volley.newRequestQueue(Login_Page.this);

       checklogoutmsg();


    }
   /* public void login_process(View view)
    {
        eemail=findViewById(R.id.vemail);
        epassword=findViewById(R.id.vpassword);

        String qry="?email="+eemail.getText().toString().trim()+"&password="+epassword.getText().toString().trim();

        class dbprocess extends AsyncTask<String,Void,String>
        {
            @Override
            protected  void onPostExecute(String data)
            {
                if(data.equals("found"))
                {
                    editor.putString("uname",eemail.getText().toString());
                    editor.apply();
                    Toast.makeText(Login_Page.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login_Page.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
                else
                {
                    eemail.setText("");
                    epassword.setText("");
                    Toast.makeText(Login_Page.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected String doInBackground(String... params)
            {
                String furl=params[0];

                try
                {
                    URL url=new URL(furl);
                    HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    return br.readLine();

                }catch (Exception ex)
                {
                    return ex.getMessage();
                }
            }
        }

        dbprocess obj=new dbprocess();
        obj.execute(apiurl+qry);

    }*/

    private void loginUser(){

        final String vemail=eemail.getText().toString().trim();
        final String vpassword=epassword.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, apiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loginRes",response);

                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getBoolean("status")) {
                        Gson gson = new Gson();
                        JSONObject userObj = jsonObject.getJSONObject("data");
                        Profile user = gson.fromJson(String.valueOf(userObj), Profile.class);
                        editor.putString("id", String.valueOf(user.getId()));
                        editor.putString("email", user.getEmail());
                        editor.putString("number", user.getNumber());
                        editor.apply();
                        Log.d("loginRes", sharedPreferences.getString("email", ""));
                        Toast.makeText(Login_Page.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login_Page.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();

                    }
                    else {
                        Toast.makeText(Login_Page.this, "Incorrect email and password", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("email",vemail);
                map.put("password",vpassword);
                return map;
            }
        };
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void checklogoutmsg()
    {

        sharedPreferences=getSharedPreferences("credentials", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        String aa=sharedPreferences.getString("id","0");

        if(aa!="0"){
            startActivity(new Intent(Login_Page.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();

        }

    }
}