package com.android.restaurentuserapp.activities;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.android.restaurentuserapp.R;
import com.android.restaurentuserapp.adapter.OrderDetailAdapter;
import com.android.restaurentuserapp.databinding.ActivityCheckOrderStausBinding;
import com.android.restaurentuserapp.model.CardItemModel;
import com.android.restaurentuserapp.model.OrderModel;
import com.android.restaurentuserapp.utils.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CheckOrderStatusActivity extends BaseActivity implements View.OnClickListener
{
    ActivityCheckOrderStausBinding binding;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    OrderDetailAdapter adapter;
    ArrayList<CardItemModel> mCardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckOrderStausBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    void initView() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();

        binding.imgBack.setOnClickListener(this);
        adapter = new OrderDetailAdapter(CheckOrderStatusActivity.this, mCardList, storage);
        binding.rvList.setLayoutManager(new LinearLayoutManager(CheckOrderStatusActivity.this));
        binding.rvList.setAdapter(adapter);

        getOrderApi();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack: {
                finish();
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
                    if(noteSnapshot.getKey().equals(MainActivity.tableNumber))
                    {
                        OrderModel mObj = noteSnapshot.getValue(OrderModel.class);
                        mCardList.addAll(mObj.getItemList());
                        binding.txtStatus.setText("Status : "+mObj.getOrderStatus());
                    }
                }
                if(mCardList.isEmpty())
                {
                    binding.txtStatus.setVisibility(View.GONE);
                    binding.tvNotFound.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    binding.txtStatus.setVisibility(View.VISIBLE);
                    binding.tvNotFound.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast(CheckOrderStatusActivity.this , databaseError.getMessage());
            }
        });
    }

}