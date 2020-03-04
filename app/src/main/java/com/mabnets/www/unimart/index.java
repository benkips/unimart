package com.mabnets.www.unimart;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import maes.tech.intentanim.CustomIntent;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static TextView carttext;
    public static int cart_count=0;
    public static ArrayList cartitems=new ArrayList();
    ImageLoaderConfiguration config;
    private FileCacher<String> stringcacher;
    private FileCacher<String> urlcacher;
    private final int CAMERA_REQUEST = 13323;
    File cacheDir;
    DisplayImageOptions options;
    private TextView navname;
    private  TextView navphone;
    private Mycommand mycommand;
    final String Tag=this.getClass().getName();
    public static  String externalurl="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mycommand=new Mycommand(index.this);
        stringcacher=new FileCacher<>(getApplicationContext(),"cart.txt");
        urlcacher=new FileCacher<>(getApplicationContext(),"url.txt");
        permission();

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String urlg;
            urlg=bundle.getString("url");
            savingpreff(urlg);

        }
        if(urlcacher.hasCache()) {
            try {
                String k=urlcacher.readCache();
                if (!k.equals("")) {
                    Log.d(Tag, k);
                    seturl(k);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();
        cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .defaultDisplayImageOptions(options) // default
                .writeDebugLogs()
                .build();

   /*     ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(index.this)
                .build();*/
        ImageLoader.getInstance().init(config);


        cart_count=cartitems.size();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragmentmain=new main();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();

        View NavHeader=navigationView.getHeaderView(0);
        navphone=(TextView)NavHeader.findViewById(R.id.navphone);
        navname=(TextView)NavHeader.findViewById(R.id.navname);

        if(stringcacher.hasCache()){
            try {
                String t=stringcacher.readCache();
                /*cartcacher.clearCache();*/
                Log.d(Tag, t);
                ArrayList<details> locodetails = new JsonConverter<details>().toArrayList(t, details.class);
                ArrayList title = new ArrayList<String>();
                for (details value : locodetails) {
                    title.add(value.getUsername());
                    title.add(value.getPhone());
                    title.add(value.getAdress());
                }
                navphone.setText(String.valueOf(title.get(1)));
                navname.setText(String.valueOf(title.get(0)));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        versioning();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        /*if (getSupportFragmentManager().getBackStackEntryCount() == 0){
            finish();
        }
        else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        final MenuItem menuItem=menu.findItem(R.id.cart_action);

        View actionView= MenuItemCompat.getActionView(menuItem);
        carttext=(TextView)actionView.findViewById(R.id.cart_badge);

        setupbadge(cart_count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);

            }
        });

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }else */if(id==R.id.cart_action){
            if (cartitems.size()!=0) {
                Fragment fragmentmain=new cashout();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();
            }else{
                Toast.makeText(this, "You have to add product to cart", Toast.LENGTH_SHORT).show();
            }
                return true;

        }else if(id==R.id.search){
            Fragment fragmentmain=new Search();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.emailus) {
            // Handle the camera action
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","unimart.care@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else if (id == R.id.textus) {
            Uri uri = Uri.parse("smsto:0719864925");
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", "coverse with us");
            startActivity(intent);

        } else if (id == R.id.call) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0719864925"));
            startActivity(intent);
        }  else if (id == R.id.developers) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://dev.mabnets.com"));
            String title = "Complete Action Using";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);

        } else if (id == R.id.clocation) {
            try {
                urlcacher.clearCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(index.this, location.class));
            index.this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static void setupbadge(int cart){
        /*carttext.setText(String.valueOf(cart));*/
           if(cart==0){
                if(carttext.getVisibility()!=View.GONE){
                    carttext.setVisibility(View.GONE);
                }
           }else{
                carttext.setText(String.valueOf(Math.min(cart,99)));
                carttext.setText(String.valueOf(cart));
               if(carttext.getVisibility()!=View.VISIBLE){
                   carttext.setVisibility(View.VISIBLE);
               }
       }
    }
    private void versioning(){
        String url="http://"+externalurl+"/android/updatecheck.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(Tag,response);
                if(!response.isEmpty()){
                    ArrayList<updates> update = new JsonConverter<updates>().toArrayList(response, updates.class);
                    ArrayList versions = new ArrayList<String>();
                    for (updates value :update) {
                        versions.add(value.id);
                        versions.add(value.version);
                        versions.add(value.status);
                    }
                    String version=String.valueOf(versions.get(1));
                    String status=String.valueOf(versions.get(2));
                    int v=2;
                    if(v!=Integer.parseInt(version)) {
                        if (status.equals("1")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(index.this);
                            alert.setMessage("This version of the app is outdated click ok to download");
                            alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://unimart.mabnets.com/android/update.php"));
                                    String title = "Complete Action Using";
                                    Intent chooser = Intent.createChooser(intent, title);
                                    startActivity(chooser);
                                }
                            });

                            alert.show();
                        } else if (status.equals("2")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(index.this);
                            alert.setMessage("This version of the app is outdated click ok to download");
                            alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://unimart.mabnets.com/android/update.php"));
                                    String title = "Complete Action Using";
                                    Intent chooser = Intent.createChooser(intent, title);
                                    startActivity(chooser);
                                    System.exit(0);
                                }
                            });
                            alert.setCancelable(false);
                            alert.show();


                        } else {
                            Log.d(Tag, response);
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Log.d(TAG, error.toString());
                    if (error instanceof TimeoutError) {
                        Toast.makeText(index.this, "timeout error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(index.this, "No connction", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(index.this, "network error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof AuthFailureError) {
                        
                        Toast.makeText(index.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        
                        Toast.makeText(index.this, "error while parsing", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        
                        Toast.makeText(index.this, "error  in server", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ClientError) {
                        
                        Toast.makeText(index.this, "error with Client", Toast.LENGTH_SHORT).show();
                    } else {
                        
                        Toast.makeText(index.this, "error while loading", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mycommand.add(request);
        mycommand.execute();
        mycommand.remove(request);

    }
    private void savingpreff(String urlz){
        try {
            urlcacher.writeCache(urlz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(Tag, "saved");
    }
    private void seturl(String x){
        externalurl=x;
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(index.this,"right-to-left");
    }
    private void permission() {
        if (ContextCompat.checkSelfPermission(index.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(index.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
            }
        }
    }
}
