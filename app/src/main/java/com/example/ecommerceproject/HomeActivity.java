package com.example.ecommerceproject;

import static android.system.Os.getpid;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ImageCarousel carousel;
    ImageView profile;
    SearchView searchView;
    RequestQueue queue;
    String aa;
    TextView headText;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CustomRecycler_Adapter cc;
    LinearLayout linearLayout;
    Calendar c;
    int year,month,day;
    List<Product> arraylist;
    RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.trending);
        c = Calendar.getInstance();
        searchView=findViewById(R.id.searchProducts);
        sharedPreferences=getSharedPreferences("credentials",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        linearLayout=findViewById(R.id.cale);
        headText=findViewById(R.id.textView);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        HomeActivity.this,
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
                                    Log.d("ondateset",dateValue+"");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Log.d("ondateset",dateValue+"");

                                productSearchByDate(dateValue);

                            }
                        },
                        year, month, day);



                datePickerDialog.show();
            }
        });
        arraylist = new ArrayList<>();
        profile = findViewById(R.id.imageViewProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(HomeActivity.this, Profile_page.class);
                startActivity(in);
            }
        });

         cc = new CustomRecycler_Adapter(HomeActivity.this, arraylist);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(cc);
        recyclerView.setHasFixedSize(true);
        cc.setonItemClick(new Onclick() {
            @Override
            public void checkbox(View v, int adapterPosition) {
                Log.d("res", arraylist.get(adapterPosition).getPid() + "");
                Intent intent = new Intent(HomeActivity.this, ProductClick.class);
                intent.putExtra("key", arraylist.get(adapterPosition).getPid());
                startActivity(intent);
            }
            @Override
            public void onEditProduct(View v, int position) {
                Log.d("res", arraylist.get(position).getPid() + "");
                Intent intent = new Intent(HomeActivity.this,ProductEditAct.class);
                intent.putExtra("keys", arraylist.get(position).getPid());
                startActivity(intent);
            }
            @Override
            public void onDeleteProduct(View v, int deletePosition) {
                int a= Integer.parseInt(arraylist.get(deletePosition).getPid()+"");
                Toast.makeText(HomeActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                deleteProducts(a);
                fetchProducts();
            }
            @Override
            public void onAddProduct(View v, int productPosition) {
              v.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      startActivity(new Intent(HomeActivity.this,UploadProduct.class));
                  }
              });
            }
        });
        //search product
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchProduct(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        // image slider
        carousel = findViewById(R.id.imageslider);
        ArrayList<CarouselItem> banner = new ArrayList<>();
        banner.add(new CarouselItem("https://images-eu.ssl-images-amazon.com/images/G/31/img19/laptops/750x450-1_1545999822._CB457223703_.jpg", ""));
        banner.add(new CarouselItem("https://economictimes.indiatimes.com/thumb/msid-94677060,width-1200,height-900,resizemode-4,imgsize-37878/best-laptops-and-tablets-at-huge-discounts-in-amazon-sale-today.jpg?from=mdr", ""));
        banner.add(new CarouselItem("https://cdn.mos.cms.futurecdn.net/DnrckHRgetDkkD6hJMoA2K.jpg", ""));
        banner.add(new CarouselItem("https://www.grabon.in/indulge/wp-content/uploads/2022/06/Upcoming-Amazon-Sales-in-India.jpg", ""));
        carousel.setData(banner);

        queue = Volley.newRequestQueue(HomeActivity.this);
        fetchProducts();
        aa=sharedPreferences.getString("id","0");
        fetchUserDetails();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                // Code to be executed when the add button is clicked
                Toast.makeText(this, "Menu Item is Pressed", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void fetchProducts(){
        arraylist.clear();
        cc.notifyDataSetChanged();
        String url=Url.BASEURL+"Product_Ecommerce.php";
        String addButton=Url.BASEURL+"upload/addButton.png";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Gson gson=new Gson();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Product mm=gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)),Product.class);
                        arraylist.add(mm);
                    }
                    Product mm2=new Product("",addButton,"","","","","","");
                    arraylist.add(mm2);
                    cc.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
    public void deleteProducts(int pos){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url=Url.BASEURL+"DeleteProduct.php?pid=";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+pos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("delete",response);
                progressDialog.dismiss();
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
    public void searchProduct(String search){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Searching...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        arraylist.clear();
        cc.notifyDataSetChanged();
        String url=Url.BASEURL+"SearchProduct.php?search=";
        String addButton=Url.BASEURL+"upload/addButton.png";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+search, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("searchresponse",response);
                progressDialog.dismiss();
                try {
                    Gson gson = new Gson();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Product mm = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), Product.class);
                        arraylist.add(mm);

                    }
                    Product mm2=new Product("",addButton,"","","","","","");
                    arraylist.add(mm2);
                    cc.notifyDataSetChanged();
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
    private void fetchUserDetails(){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url=Url.BASEURL+"ShowProfileDetails.php?id=";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+aa, new Response.Listener<String>() {
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
    private void setViews(Profile mm) {
        String imageUrl=Url.BASEURL+"upload/";
        String image = imageUrl +mm.getImage();
        Log.d("response2", ""+mm.getName());
        headText.setText("Welcome "+mm.getName());
        Glide.with(HomeActivity.this).load(Uri.parse(image)).into(profile);
    }
    public void productSearchByDate(String newdate) {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Searching...");
        progressDialog.setCancelable(false);
        progressDialog.show();
            arraylist.clear();
            cc.notifyDataSetChanged();
            String url=Url.BASEURL+"ProductSearchByDate.php?pdate=";
            String addButton =Url.BASEURL+"upload/addButton.png";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url+newdate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("dataresponse",response);
                    progressDialog.dismiss();
                    try {
                        Gson gson = new Gson();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Product mm = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), Product.class);
                            arraylist.add(mm);

                        }
                        Product mm2=new Product("",addButton,"","","","","","");
                        arraylist.add(mm2);
                        cc.notifyDataSetChanged();
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

}