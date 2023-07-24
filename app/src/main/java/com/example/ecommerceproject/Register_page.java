package com.example.ecommerceproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Register_page extends AppCompatActivity {

    ImageView profile,backPressed;
    EditText ename,eemail,ephone,epassword;

    Button submit;

    Bitmap bitmap;
    String encodeImageString;
    public static final String UPLOAD_URL = Url.BASEURL+"Register_Ecommerce.php";



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        getSupportActionBar().hide();
        profile=findViewById(R.id.imageView);
        ename=findViewById(R.id.name);
        eemail=findViewById(R.id.email);
        backPressed=findViewById(R.id.imageView5);
        ephone=findViewById(R.id.number);
        epassword=findViewById(R.id.password);
        submit=findViewById(R.id.signup);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ename.getText().length()>0&&eemail.getText().length()>0&&ephone.getText().length()>0&&epassword.getText().length()>0) {

                    uploaddatatodb();
                    onBackPressed();




                }else {
                    Toast.makeText(Register_page.this, "Plz Fill All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
            }
        });

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
                profile.setImageBitmap(bitmap);
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

    private void uploaddatatodb()
    {
        ename=findViewById(R.id.name);
        eemail=findViewById(R.id.email);
        ephone=findViewById(R.id.number);
        epassword=findViewById(R.id.password);
        final String vname=ename.getText().toString().trim();
        final String vemail=eemail.getText().toString().trim();
        final String vnumber=ephone.getText().toString().trim();
        final String vpassword=epassword.getText().toString().trim();

        StringRequest request=new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                ename.setText("");
                eemail.setText("");
                ephone.setText("");
                epassword.setText("");
                profile.setImageResource(R.drawable.manlogo_origin);
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
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
                map.put("upload",encodeImageString);
                map.put("name",vname);
                map.put("email",vemail);
                map.put("number",vnumber);
                map.put("password",vpassword);

                return map;
            }
        };


        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}