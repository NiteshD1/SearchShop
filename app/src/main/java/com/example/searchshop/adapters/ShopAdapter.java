package com.example.searchshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.searchshop.Activities.MainActivity;
import com.example.searchshop.Activities.ShopDetailsActivity;
import com.example.searchshop.Models.ShopModel;
import com.example.searchshop.R;
import com.example.searchshop.Response.ShopResponse;

import java.util.List;



public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    public static final String TAG ="ShopAdapter";
    private ShopResponse shopResponse;

    private Context context;
    Context mContext;


    private List<ShopModel> shopList;

    public ShopAdapter(Context mContext,List<ShopModel> shopList) {
        this.mContext = mContext;
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {


        ShopModel currentShop = shopList.get(position);

        holder.textViewShopName.setText(currentShop.getShopName());
        holder.textViewShopAddress.setText(currentShop.getAddress());
        holder.textViewCashback.setText(String.valueOf(currentShop.getCashback()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myactivity = new Intent(context.getApplicationContext(), ShopDetailsActivity.class);
//                myactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                context.getApplicationContext().startActivity(myactivity);
               // Toast.makeText(v.getContext(),"item position :" + position + " clicked",Toast.LENGTH_LONG ).show();
                Intent intent = new Intent(mContext, ShopDetailsActivity.class);
                intent.putExtra("Editing", currentShop); // sending our object. In Kotlin is the same

                mContext.startActivity(intent);

            //openGoogleMap(shopList.get(getAdapterPosition()).getAddress());
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public void resetList(List<ShopModel> shopList){
        this.shopList = shopList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewShopName;
        private TextView textViewShopAddress;
        private TextView textViewCashback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewShopName = itemView.findViewById(R.id.textViewShopName);
            textViewShopAddress = itemView.findViewById(R.id.textViewShopAdress);
            textViewCashback = itemView.findViewById(R.id.textViewCashback);
            context = itemView.getContext();
            //itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            Toast.makeText(v.getContext(),"item position :" + getAdapterPosition() + " clicked",Toast.LENGTH_LONG ).show();
//            Log.d(TAG, "onClick: item " + getAdapterPosition());
//
//            Intent intent = new Intent(context, ShopDetailsActivity.class);
//            intent.putExtra("Editing", shopList.get(getAdapterPosition())); // sending our object. In Kotlin is the same
//            context.startActivity(intent);
//
//            //openGoogleMap(shopList.get(getAdapterPosition()).getAddress());
//        }
    }




}
