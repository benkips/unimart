package com.mabnets.www.unimart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import maes.tech.intentanim.CustomIntent;

public class locoadapter extends RecyclerView.Adapter<locoadapter.locoholder> {
    public ArrayList locolist;
    public Context context;

    public locoadapter(ArrayList locolist, Context context) {
        this.locolist = locolist;
        this.context = context;
    }

    @NonNull
    @Override
    public locoholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.locoinf, parent, false);
        return new locoholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull locoholder holder, int position) {
        final loco loco=(loco)locolist.get(position);
        holder.tvsite.setText(loco.site+" university");
        ImageLoader.getInstance().displayImage("http://unimart.mabnets.com/sitelogo/"+loco.logo,holder.ivsite);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("url",loco.url);
                Intent intent=new Intent(context,index.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
                CustomIntent.customType(context,"right-to-left");
            }
        });

    }

    @Override
    public int getItemCount() {
        if(locolist!=null){
            return  locolist.size();
        }
        return 0;
    }

    public static  class locoholder extends RecyclerView.ViewHolder {
        private TextView tvsite;
        private ImageView ivsite;
        private LinearLayout cv;
        public locoholder(@NonNull View itemView) {
            super(itemView);
            tvsite=(TextView)itemView.findViewById(R.id.site);
            ivsite=(ImageView)itemView.findViewById(R.id.siteimg);
            cv=(LinearLayout)itemView.findViewById(R.id.cvsite);
        }
    }
}
