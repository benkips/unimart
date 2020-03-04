package com.mabnets.www.unimart;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.util.ArrayList;

import static com.mabnets.www.unimart.index.cartitems;
import static com.mabnets.www.unimart.index.externalurl;


/**
 * A simple {@link Fragment} subclass.
 */
public class product extends Fragment {
    private String  id ;
    private String product;
    private String category;
    private String description;
    private Integer now;
    private String was;
    private String photo;
    private String measure;
    final String Tag=this.getClass().getName();

    private TextView tvproduct;
    private TextView tvcategory;
    private TextView tvdescription;
    private TextView tvnow;
    private TextView tvwas;
    private ImageView  ivpphoto;
    private Button btnbuy;



    private FileCacher<String>cartcacher;

    public product() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pproduct= inflater.inflate(R.layout.fragment_product, container, false);
        cartcacher=new FileCacher<>(getContext(),"cart.txt");
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            product= bundle.getString("product");
            category = bundle.getString("category");
            description = bundle.getString("description");
               now =Integer.parseInt( bundle.getString("now"));
               was= bundle.getString("was");
            photo= bundle.getString("photo");
            measure=bundle.getString("measure");
        }
        tvproduct=(TextView)pproduct.findViewById(R.id.pproduct);
        tvcategory=(TextView)pproduct.findViewById(R.id.pcategory);
        tvdescription=(TextView)pproduct.findViewById(R.id.pdescription);
        tvnow=(TextView)pproduct.findViewById(R.id.pprice);
        tvwas=(TextView)pproduct.findViewById(R.id.pwas);
        ivpphoto=(ImageView)pproduct.findViewById(R.id.pimage);
        btnbuy=(Button)pproduct.findViewById(R.id.pbuy);


       product= WordUtils.capitalize(product);
        category= WordUtils.capitalize(category);
        description= WordUtils.capitalize(description);

        ImageLoader.getInstance().displayImage("http://"+externalurl+"/products/"+photo,ivpphoto);
        tvproduct.setText(product);
        tvcategory.setText(category);
        tvdescription.setText(description);
        tvnow.setText("Now "+now+"ksh"+"@ "+measure);
        tvwas.setText("Was "+was+"ksh");

        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String dataa=id+","+photo+","+product;
                Log.d(Tag, dataa);*/
                cartitems.add(new cartz(id,product,photo,now));
                Log.d(Tag, String.valueOf(cartitems));
            /*    try {
                    cartcacher.appendOrWriteCache(dataa);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(cartcacher.hasCache()){
                    try {
                        String t=cartcacher.readCache();
                        *//*cartitems.add(t);*//*
                        Log.d(Tag, String.valueOf(cartitems));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }*/
                int ww=0;
                ww=index.cart_count;
                int crt=cartitems.size();
                ww=crt;
                ((index)getActivity()).setupbadge(crt);
                Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();


            }
        });

        return  pproduct;
    }

}
