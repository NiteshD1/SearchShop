package com.example.searchshop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;

import android.widget.Toast;

import com.example.searchshop.Models.ShopModel;
import com.example.searchshop.R;
import com.example.searchshop.Response.ShopResponse;
import com.example.searchshop.adapters.ShopAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    // firestore object

    static FirebaseFirestore db;


    private final String TAG = "MainActivity";


    private ShopAdapter adapter;

    // recyclerView

    RecyclerView recyclerView;

    List<ShopModel> shopList;
    private ShopResponse shopResponse;



    private ProgressBar progressBarRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set text on action bar
        getSupportActionBar().setTitle("Search Product");



        // set db

        db=FirebaseFirestore.getInstance();


        // upload data on firestore
        //addShopsOnFirestore();

        // recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        //round progress for loading firebase content
        progressBarRound = findViewById(R.id.progressBar);

        shopList = new ArrayList<>();

        getAllDataFromFirestore();



//        myButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openGoogleMap(address);
//            }
//        });

        
    }

    private void getAllDataFromFirestore() {
        dataFromFirebaseFirestore();
    }


    // menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                progressBarRound.setVisibility(View.VISIBLE);
                resetDataFromFirebaseFirestore();

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void searchData(String query) {
        progressBarRound.setVisibility(View.VISIBLE);

        db.collection("ShopList").whereArrayContains("productsList",query.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        shopList.clear();
                        progressBarRound.setVisibility(View.GONE);
                      //  Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();


                        // call reset list method and copy from previous firestore code

                        for(DocumentSnapshot documentSnapshot: task.getResult()) {
                            String shopName = documentSnapshot.getString("shopName");

                            int discount = documentSnapshot.getDouble("discount").intValue();
                            int cashback =  documentSnapshot.getDouble("cashback").intValue();
                            String address = documentSnapshot.getString("address");

                            ShopModel shopModel = new ShopModel(shopName, discount,cashback,address,null);
                            shopList.add(shopModel);
                            Log.i("Enter","paper added");

                        }

                        if(shopList.size() < 1){
                            Toast.makeText(MainActivity.this,"No Shop Available for this product", Toast.LENGTH_LONG).show();

                        }

                        adapter.resetList(shopList);

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBarRound.setVisibility(View.GONE);

                        Toast.makeText(MainActivity.this, "Error ;-.-;", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_search){

        }

        return super.onOptionsItemSelected(item);
    }



    private void printShopList() {


        for(int i=0;i<shopList.size();i++){

            Log.d(TAG, "printShopList: Name "+ shopList.get(i).getShopName() +" Address " + shopList.get(i).getAddress() );
        }
    }

    private void dataFromFirebaseFirestore() {

        //if(test_headList.size()>0)
        //shopList.clear();

        // set progress bar to show progress



        //Dialog.setMessage("Doing something...");
        //Dialog.setIndeterminate(true);





        // get data from db

        db.collection("ShopList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(DocumentSnapshot documentSnapshot: task.getResult()) {
                            String shopName = documentSnapshot.getString("shopName");

                            int discount = documentSnapshot.getDouble("discount").intValue();
                            int cashback =  documentSnapshot.getDouble("cashback").intValue();
                            String address = documentSnapshot.getString("address");



                            ShopModel shopModel = new ShopModel(shopName, discount,cashback,address,null);
                            shopList.add(shopModel);
                            Log.i("Enter","paper added");

                        }
                        // if no item present for particular exam

                        if(shopList.size() < 1){
                            Toast.makeText(MainActivity.this,"Coming Soon...", Toast.LENGTH_LONG).show();

                        }

                        adapter = new ShopAdapter(MainActivity.this,shopList); //creating recycler view adapter

                        // setting adapter to recycler view

                        recyclerView.setAdapter(adapter);

                        progressBarRound.setVisibility(View.GONE);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error ;-.-;", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void resetDataFromFirebaseFirestore() {

        //if(test_headList.size()>0)
        //shopList.clear();

        // set progress bar to show progress



        //Dialog.setMessage("Doing something...");
        //Dialog.setIndeterminate(true);




        shopList.clear();
        // get data from db

        db.collection("ShopList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(DocumentSnapshot documentSnapshot: task.getResult()) {
                            String shopName = documentSnapshot.getString("shopName");

                            int discount = documentSnapshot.getDouble("discount").intValue();
                            int cashback =  documentSnapshot.getDouble("cashback").intValue();
                            String address = documentSnapshot.getString("address");



                            ShopModel shopModel = new ShopModel(shopName, discount,cashback,address,null);
                            shopList.add(shopModel);
                            Log.i("Enter","paper added");

                        }
                        // if no item present for particular exam

                        if(shopList.size() < 1){
                            Toast.makeText(MainActivity.this,"No Shop Available for this product", Toast.LENGTH_LONG).show();

                        }



                        // setting adapter to recycler view

                        adapter.resetList(shopList);

                        progressBarRound.setVisibility(View.GONE);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error ;-.-;", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void addShopsOnFirestore(){


            uploadDataOnFirestore("Kanti Sweets",8,7,"Door No. 910, Plot NO. 157, St Patrick's Complex Near-St. Joseph's College, Brigade Rd, Bengaluru, Karnataka 560025",new ArrayList<String>(){{add("chocolate");add("sweets");}});

    }

    private void uploadDataOnFirestore(String shopName,int discount,int cashback, String address,List<String> productList) {




            Map<String, Object> myFieldMap = new HashMap<>();   // map to store field info of firestore


            myFieldMap.put("shopName", shopName);
            myFieldMap.put("discount", discount);
            myFieldMap.put("cashback", cashback);
            myFieldMap.put("address", address);
            myFieldMap.put("productsList", productList);



            String id = db.collection("ShopList").document().getId();




            db.collection("ShopList").document(id).set(myFieldMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Information Updated on Firestore", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });



    }



    private List<ShopModel> fillList() {
        shopResponse = new ShopResponse();

        shopResponse.getShopList().add(new ShopModel("Banchharam Sweets JP Nagar",4,5,"Delmia Office, 1st Main Rd, Dollar Layout, 3rd Phase, J. P. Nagar, Bengaluru, Karnataka 560078",null));
        shopResponse.getShopList().add(new ShopModel("Kanti Sweets",8,5,"Door No. 910, Plot NO. 157, St Patrick's Complex Near-St. Joseph's College, Brigade Rd, Bengaluru, Karnataka 560025",null));
        shopResponse.getShopList().add(new ShopModel("Mobile mane",10,20,"1111, 180K, 19th Main Rd, 1st Block, Rajajinagar, Bengaluru, Karnataka 560010",null));

        return shopResponse.getShopList();

        //printShopList();
    }

}