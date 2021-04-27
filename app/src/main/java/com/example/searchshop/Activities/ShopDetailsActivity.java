package com.example.searchshop.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.searchshop.Models.ShopModel;
import com.example.searchshop.R;

public class ShopDetailsActivity extends AppCompatActivity {

    private String TAG = "ShopDetailsActivity";
    private TextView textViewShopName;
    private TextView textViewShopAdress;
    private TextView textViewDiscount;
    private TextView textViewCashback;
    private ImageButton gMapButton;
    private ShopModel shopModel = null;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        textViewShopName = findViewById(R.id.textViewShopName);
        textViewShopAdress = findViewById(R.id.textViewShopAdress);
        textViewDiscount = findViewById(R.id.textViewDiscount);
        textViewCashback = findViewById(R.id.textViewCashback);
        gMapButton = findViewById(R.id.buttonGmap);

        shopModel = (ShopModel) getIntent().getSerializableExtra("Editing");

        // set text on action bar
        getSupportActionBar().setTitle("Shop Details");
        
        if(shopModel != null){
            Log.d(TAG, "onCreate: " + shopModel.getAddress());
            textViewShopName.setText(shopModel.getShopName());
            textViewShopAdress.setText(shopModel.getAddress());
            textViewDiscount.setText(String.valueOf(shopModel.getDiscount()) + " %");
            textViewCashback.setText(String.valueOf(shopModel.getCashback()) + " %");
        }


        gMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMap(shopModel.getAddress());
            }
        });
    }

    private void openGoogleMap(String address) {
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}