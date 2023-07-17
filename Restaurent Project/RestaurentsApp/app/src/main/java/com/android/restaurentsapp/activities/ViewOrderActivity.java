package com.android.restaurentsapp.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import com.android.restaurentsapp.R;
import com.android.restaurentsapp.adapter.OrderDetailAdapter;
import com.android.restaurentsapp.databinding.ActivityViewOrderBinding;
import com.android.restaurentsapp.model.AddFoodModel;
import com.android.restaurentsapp.model.CardItemModel;
import com.android.restaurentsapp.model.OrderModel;
import com.android.restaurentsapp.utils.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;

public class ViewOrderActivity extends BaseActivity implements View.OnClickListener
{
    ActivityViewOrderBinding binding;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    OrderDetailAdapter adapter;
    ArrayList<CardItemModel> mCardList = new ArrayList<>();
    String tableNumber = "0";
    String newStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    void initView() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();

        binding.imgBack.setOnClickListener(this);
        binding.btnStatus.setOnClickListener(this);
        adapter = new OrderDetailAdapter(ViewOrderActivity.this, mCardList, storage);
        binding.rvList.setLayoutManager(new LinearLayoutManager(ViewOrderActivity.this));
        binding.rvList.setAdapter(adapter);

        tableNumber = getIntent().getStringExtra("tableNumber");
        checkStatus(getIntent().getStringExtra("status"));

        getOrderApi();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack: {
                finish();
                break;
            }
            case R.id.btnStatus: {
                updateStatusMethod();
                break;
            }
            default:
                System.out.printf("");
        }
    }

    void checkStatus(String status)
    {
        binding.txtStatus.setText("Status : "+status);
        if(status.equals("new"))
        {
            newStatus = "cooking";
            binding.btnStatus.setText("Start Cooking");
        }
        else if(status.equals("cooking"))
        {
            newStatus = "delivered";
            binding.btnStatus.setText("Delivered");
        }
        else{
            binding.btnStatus.setVisibility(View.GONE);
        }
    }

    void getOrderApi()
    {
        mDatabase.child("OrderRecords").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCardList.clear();
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    if(noteSnapshot.getKey().equals(tableNumber))
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
                showToast(ViewOrderActivity.this , databaseError.getMessage());
            }
        });
    }

    private void updateStatusMethod()
    {
        mDatabase.child("OrderRecords").child(tableNumber).child("orderStatus").setValue(newStatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        checkStatus(newStatus);
                        showToast(ViewOrderActivity.this , "Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(ViewOrderActivity.this , "Fail");
                    }
                });
    }

}
