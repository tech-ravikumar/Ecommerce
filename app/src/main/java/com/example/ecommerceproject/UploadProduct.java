package com.example.ecommerceproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadProduct extends AppCompatActivity {

    ImageView pimage;
    EditText etittle,esubtittle,ecutprice,eprice,eoff;
    Button ebutton;
    Bitmap bitmap;

    TextView date;
    Calendar c;
    int year,month,day;
    LinearLayout linearLayout;
    String encodeImageString;

    public static final String UPLOAD_URL = Url.BASEURL+"InsertProductFromDevice.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        pimage=findViewById(R.id.imge);
        etittle=findViewById(R.id.u_title);
        esubtittle=findViewById(R.id.utxt2);
        ecutprice=findViewById(R.id.utxt3);
        eprice=findViewById(R.id.utxt4);
        eoff=findViewById(R.id.uoffs);
        ebutton=findViewById(R.id.usubmit);
        linearLayout=findViewById(R.id.cal);
        date=findViewById(R.id.showCalender);

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UploadProduct.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year2,
                                                  int monthOfYear, int dayOfMonth) {
                                year = year2;
                                month = monthOfYear;
                                day = dayOfMonth;

                                String dateValue = "";

                                String dateString;
                                dateString=dayOfMonth+"-"+(monthOfYear+1)+"-"+year2;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date newdate= sdf.parse(dateString);
                                    dateValue=sdf.format(newdate);
                                    date.setText(dateValue);
                                    Log.d("ondateset",dateValue+"");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Log.d("ondateset",dateValue+"");
                                //fetchAppsUsages(dateValue);

                            }
                        },
                        year, month, day);



                datePickerDialog.show();
            }

        });

        long dates = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateStrings = sdf.format(dates);
        date.setText(dateStrings);

        pimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
            }
        });

        ebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etittle.getText().length()>0&&esubtittle.getText().length()>0&&ecutprice.getText().length()>0&&eprice.getText().length()>0&&eoff.getText().length()>0&&encodeImageString.length()>0) {

                    uploaddatatodb();
                    onBackPressed();
                    Toast.makeText(UploadProduct.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();




                }else {
                    Toast.makeText(UploadProduct.this, "Plz Fill All Details", Toast.LENGTH_SHORT).show();
                }
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
                pimage.setImageBitmap(bitmap);
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

        final String vtittle=etittle.getText().toString().trim();
        final String vsubtittle=esubtittle.getText().toString().trim();
        final String vcutprice=ecutprice.getText().toString().trim();
        final String vprice=eprice.getText().toString().trim();
        final String voff=eoff.getText().toString().trim();
        final String vdate=date.getText().toString().trim();

        StringRequest request=new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
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
                map.put("upload",encodeImageString);
                map.put("tittle",vtittle);
                map.put("subtittle",vsubtittle);
                map.put("cutprice",vcutprice);
                map.put("price",vprice);
                map.put("off",voff);
                map.put("date",vdate);

                return map;
            }
        };


        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}