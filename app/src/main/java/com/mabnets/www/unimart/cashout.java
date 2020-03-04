package com.mabnets.www.unimart;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;

import java.io.IOException;
import java.util.ArrayList;

import static com.mabnets.www.unimart.index.cartitems;


/**
 * A simple {@link Fragment} subclass.
 */
public class cashout extends Fragment {
    private RecyclerView rvcashout;
    private TextView tvtotal;
    private TextView tvdelivery;
    private TextView tvdue;
    final String Tag=this.getClass().getName();
    private Button btnorder;
    private String boughtproducts="";
    public static  int totalz = 0;
    private FileCacher<String> deliverycacher;
    private int L;



    public cashout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View vcashout=inflater.inflate(R.layout.fragment_cashout, container, false);
      rvcashout=(RecyclerView)vcashout.findViewById(R.id.rvcashout);
       tvtotal=(TextView)vcashout.findViewById(R.id.tvtotal);
       tvdelivery=(TextView)vcashout.findViewById(R.id.tvdeliveryfee);
       tvdue=(TextView)vcashout.findViewById(R.id.tvdue);
       btnorder=(Button)vcashout.findViewById(R.id.btnorder);
        deliverycacher=new FileCacher<>(getContext(),"delivery.txt");





      rvcashout.setHasFixedSize(true);
       rvcashout.setLayoutManager(new LinearLayoutManager(getContext()));
       cashoutadapter cashoutadapter=new cashoutadapter(getContext(), index.cartitems,cashout.this);
       rvcashout.setAdapter(cashoutadapter);


        if(deliverycacher.hasCache()) {
            try {
               String k=deliverycacher.readCache();
                if (!k.equals("")) {
                    Log.d(Tag, k);
                    ArrayList<delivery> locodetails = new JsonConverter<delivery>().toArrayList(k, delivery.class);
                    ArrayList title = new ArrayList<String>();
                    for (delivery value : locodetails) {
                        title.add(value.fee);

                    }
                    L= Integer.parseInt(String.valueOf(title.get(0)));
                    tvdelivery.setText("Delivery fee=Ksh."+title.get(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        sendgson();
        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(totalz <200){
                Toast.makeText(getContext(), "Please order products above 200 ksh", Toast.LENGTH_SHORT).show();
            }else {
                Bundle bundle = new Bundle();
                bundle.putString("productbought", boughtproducts);
                Fragment fragment = new ordernow();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).addToBackStack(null).commit();
            }
            }
        });




      return vcashout;
    }

   public void sendgson() {
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(index.cartitems).getAsJsonArray();
        boughtproducts=String.valueOf(myCustomArray);
        Log.d(Tag, String.valueOf(myCustomArray));
        if(String.valueOf(myCustomArray)!=null) {
            ArrayList<productdata> notidata = new JsonConverter<productdata>().toArrayList(String.valueOf(myCustomArray), productdata.class);
                int o=0;
            for (productdata value : notidata) {
                o+=value.price;
            }

            Log.d(Tag, String.valueOf(o));
            totalz=o;
            if(totalz==0){
            if(  btnorder.getVisibility()!=View.GONE){
                btnorder.setVisibility(View.GONE);
            }
            }else{
                if(btnorder.getVisibility()!=View.VISIBLE){
                    btnorder.setVisibility(View.VISIBLE);
                }
            }

            tvtotal.setText("Total cost=Ksh."+String.valueOf(totalz));
            int ttpize=0;
            ttpize=totalz+L;

            tvdue.setText("Total due=Ksh."+String.valueOf(ttpize));
        }
    }
    public void retrivebal(int bal){
        tvtotal.setText(String.valueOf(bal));
    }
}
