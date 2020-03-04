package com.mabnets.www.unimart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import maes.tech.intentanim.CustomIntent;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class location extends AppCompatActivity {
    RecyclerView rvloco;
    private Mycommand mycommand;
    final String Tag=this.getClass().getName();
    private ProgressDialog pd;
    private ImageLoaderConfiguration config;
    private FileCacher<String> urlcacher;
    private FileCacher<String> deliverycacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getSupportActionBar().setTitle("Select your location");
        urlcacher=new FileCacher<>(getApplicationContext(),"url.txt");
        deliverycacher=new FileCacher<>(getApplicationContext(),"delivery.txt");

        config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();


        rvloco=(RecyclerView)findViewById(R.id.rvlocations);

        ImageLoader.getInstance().init(config);

        mycommand=new Mycommand(this);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading");

        rvloco.setHasFixedSize(true);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rvloco.setLayoutManager(manager);
        delivery();
        if(urlcacher.hasCache()) {
            startActivity(new Intent(location.this, index.class));
            location.this.finish();


        }else{

            loadlocation();
        }







    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(location.this,"right-to-left");
    }
    private void loadlocation(){
        String url="http://unimart.mabnets.com/start.php";
        StringRequest strrq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){

                        Log.d(Tag, response);
                        final ArrayList<loco> mgmtlist = new JsonConverter<loco>().toArrayList(response,loco.class);
                        locoadapter adapter = new locoadapter( mgmtlist,location.this);
                        rvloco.setAdapter(adapter);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Log.d(TAG, error.toString());
                    if (error instanceof TimeoutError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(location.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        });
                        alert.setCancelable(false);
                        alert.show();
                    } else if (error instanceof NoConnectionError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(location.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        });
                        alert.setCancelable(false);
                        alert.show();
                    } else if (error instanceof NetworkError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(location.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        });
                        alert.setCancelable(false);
                        alert.show();
                    } else if (error instanceof AuthFailureError) {
                        pd.dismiss();
                        Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        pd.dismiss();
                        Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        pd.dismiss();
                        Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ClientError) {
                        pd.dismiss();
                        Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mycommand.add(strrq);
        pd.show();
        mycommand.execute();
        mycommand.remove(strrq);
    }
 private void delivery(){
     String url="http://unimart.mabnets.com/android/delivery.php";
     StringRequest strrq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
             pd.dismiss();
             Log.d(Tag,response);
             if(!response.isEmpty()){

                 Log.d(Tag, response);
                 try {
                  deliverycacher.writeCache(response);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }


             }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             if(error!=null) {
                 Log.d(TAG, error.toString());
                 if (error instanceof TimeoutError) {
                     pd.dismiss();
                     AlertDialog.Builder alert=new AlertDialog.Builder(location.this);
                     alert.setMessage("please check your internet connectivity");
                     alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             System.exit(0);
                         }
                     });
                     alert.setCancelable(false);
                     alert.show();
                 } else if (error instanceof NoConnectionError) {
                     pd.dismiss();
                     AlertDialog.Builder alert=new AlertDialog.Builder(location.this);
                     alert.setMessage("please check your internet connectivity");
                     alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             System.exit(0);
                         }
                     });
                     alert.setCancelable(false);
                     alert.show();
                 } else if (error instanceof NetworkError) {
                     pd.dismiss();
                     AlertDialog.Builder alert=new AlertDialog.Builder(location.this);
                     alert.setMessage("please check your internet connectivity");
                     alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             System.exit(0);
                         }
                     });
                     alert.setCancelable(false);
                     alert.show();
                 } else if (error instanceof AuthFailureError) {
                     pd.dismiss();
                     Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof ParseError) {
                     pd.dismiss();
                     Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof ServerError) {
                     pd.dismiss();
                     Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof ClientError) {
                     pd.dismiss();
                     Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                 } else {
                     pd.dismiss();
                     Toast.makeText(location.this, "error time out ", Toast.LENGTH_SHORT).show();
                 }
             }
         }
     });
     mycommand.add(strrq);
     pd.show();
     mycommand.execute();
     mycommand.remove(strrq);
    }
}
