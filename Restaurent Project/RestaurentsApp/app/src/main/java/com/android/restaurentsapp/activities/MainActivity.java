package com.android.restaurentsapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.restaurentsapp.R;
import com.android.restaurentsapp.adapter.OrderListAdapter;
import com.android.restaurentsapp.databinding.ActivityMainBinding;
import com.android.restaurentsapp.model.CardItemModel;
import com.android.restaurentsapp.model.OrderModel;
import com.android.restaurentsapp.utils.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends BaseActivity  implements View.OnClickListener, OrderListAdapter.ClickInterface
{
    ActivityMainBinding binding;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    OrderListAdapter adapter;
    ArrayList<OrderModel> mCardList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding .inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    void initView()
    {
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();

        binding.imgBack.setOnClickListener(this);
        binding.tvAdd.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        adapter = new OrderListAdapter(MainActivity.this, mCardList, this, storage);
        binding.rvList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.rvList.setAdapter(adapter);

        getOrderApi();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgBack : {
                finish();
                break;
            }
            case R.id.tvAdd : {
                startActivity(new Intent(MainActivity.this, AddFoodActivity.class));
                break;
            }
            default:
                System.out.printf("");
        }
    }

    void getOrderApi()
    {
        mDatabase.child("OrderRecords").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCardList.clear();
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    OrderModel mObj = noteSnapshot.getValue(OrderModel.class);
                    mCardList.add(mObj);
                }
                if(mCardList.isEmpty())
                {
                    binding.tvNotFound.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    binding.tvNotFound.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast(MainActivity.this , databaseError.getMessage());
            }
        });
    }

    @Override
    public void clickViewButton(OrderModel orderModel) {
        startActivity(new Intent(MainActivity.this, ViewOrderActivity.class).putExtra("tableNumber", orderModel.getTableNumber()).putExtra("status" , orderModel.getOrderStatus()));
    }
}
