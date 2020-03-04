package com.mabnets.www.unimart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class Horizontoladapter extends RecyclerView.Adapter<Horizontoladapter.Horiviewholder> {
    private Context context;
    private ArrayList<categorydata> horitems;

    public Horizontoladapter(Context context, ArrayList<categorydata> horitem) {
        this.context = context;
        this.horitems = horitem;
    }

    @NonNull
    @Override
    public Horiviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontox_single, parent, false);
        return new Horiviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Horiviewholder holder, int position) {
        final categorydata categorydata=(categorydata)horitems.get(position);
        holder.categorytxt.setText(categorydata.category);
        if(categorydata.category.equals("fruits")){
            holder.cateiv.setImageResource(R.drawable.fruiticon);
        }else if(categorydata.category.equals("cereals")){
            holder.cateiv.setImageResource(R.drawable.cearalicon);
        }else if(categorydata.category.equals("gas")){
            holder.cateiv.setImageResource(R.drawable.gas);
        }else if(categorydata.category.equals("flour")){
            holder.cateiv.setImageResource(R.drawable.flour);
        }else if(categorydata.category.equals("grocery")) {
            holder.cateiv.setImageResource(R.drawable.grocery);
        }else if(categorydata.category.equals("Cometics")) {
            holder.cateiv.setImageResource(R.drawable.cosmetics);
        }
        else {
            holder.cateiv.setImageResource(R.drawable.shopping);
        }
        holder.cateiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("category",categorydata.category);
                Fragment fragmentc=new category();
                fragmentc.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.framelayout,fragmentc).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
       if(horitems!=null){
           return horitems.size();
       }
       return 0;
    }

    public static class Horiviewholder extends  RecyclerView.ViewHolder {
        private ImageView cateiv;
        private TextView  categorytxt;

        public Horiviewholder(@NonNull View itemView) {
            super(itemView);
            cateiv=(ImageView) itemView.findViewById(R.id.ivcategory);
            categorytxt=(TextView)itemView.findViewById(R.id.category);
        }
    }
}
