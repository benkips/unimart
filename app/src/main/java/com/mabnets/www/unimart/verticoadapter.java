package com.mabnets.www.unimart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.mabnets.www.unimart.index.externalurl;

public class verticoadapter extends RecyclerView.Adapter<verticoadapter.verticolHolder> {
    private Context context;
    private ArrayList<productdata> vertyitems;

    public verticoadapter( Context context, ArrayList<productdata> vertyitem) {
        this.context=context;
        this.vertyitems = vertyitem;
    }

    @NonNull
    @Override
    public verticolHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verticox_single, parent, false);
        return new verticolHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull verticolHolder holder, int position) {
     final productdata productdata=(productdata)vertyitems.get(position);
        ImageLoader.getInstance().displayImage("http://"+externalurl+"/products/"+productdata.photo,holder.cvphoto);
        holder.now.setText("Now:"+productdata.cprice+"ksh");
        holder.was.setText("Was:"+productdata.iprice+"ksh");
        holder.product.setText(productdata.product+" @"+productdata.measure);
        if(productdata.offer.equals("0")) {
            holder.offer.setText("");
        }else{
            holder.offer.setText("Offer ksh."+productdata.offer);
        }
        holder.cvphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("id",productdata.id);
                bundle.putString("product",productdata.product);
                bundle.putString("category",productdata.category);
                bundle.putString("description",productdata.description);
                bundle.putString("was",productdata.iprice);
                bundle.putString("now",productdata.cprice);
                bundle.putString("photo",productdata.photo);
                bundle.putString("measure",productdata.measure);
                Fragment fragment=new product();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        if(vertyitems!=null){
            return vertyitems.size();
        }
        return 0;
    }

    public static class verticolHolder extends RecyclerView.ViewHolder {
        private ImageView cvphoto;
        private TextView now;
        private TextView was;
        private TextView offer;
        private TextView product;
        public verticolHolder(@NonNull View itemView) {
            super(itemView);
            cvphoto=(ImageView)itemView.findViewById(R.id.ivproduct);
            now=(TextView)itemView.findViewById(R.id.now);
            was=(TextView)itemView.findViewById(R.id.was);
            offer=(TextView)itemView.findViewById(R.id.offer);
            product=(TextView)itemView.findViewById(R.id.product);


        }
    }
}
