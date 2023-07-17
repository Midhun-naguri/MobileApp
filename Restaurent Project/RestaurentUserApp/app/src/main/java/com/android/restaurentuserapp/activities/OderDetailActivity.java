package com.android.restaurentuserapp.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import com.android.restaurentuserapp.R;
import com.android.restaurentuserapp.adapter.OrderCartAdapter;
import com.android.restaurentuserapp.databinding.ActivityMainBinding;
import com.android.restaurentuserapp.databinding.ActivityOderDetailBinding;
import com.android.restaurentuserapp.model.CardItemModel;
import com.android.restaurentuserapp.model.CardItemModel;
import com.android.restaurentuserapp.model.OrderModel;
import com.android.restaurentuserapp.utils.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;

public class OderDetailActivity extends BaseActivity implements View.OnClickListener
{
    ActivityOderDetailBinding binding;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    OrderCartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    void initView() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();

        binding.imgBack.setOnClickListener(this);
        binding.txtSubmit.setOnClickListener(this);
        adapter = new OrderCartAdapter(OderDetailActivity.this, MainActivity.mCardList, storage);
        binding.rvList.setLayoutManager(new LinearLayoutManager(OderDetailActivity.this));
        binding.rvList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack: {
                finish();
                break;
            }
            case R.id.txtSubmit: {
                if(MainActivity.mCardList.isEmpty())
                {
                    showToast(this , "Please add item.");
                }
                else
                {
                    submitOrderMethod();
                }
                break;
            }
            default:
                System.out.printf("");
        }
    }

    private void submitOrderMethod()
    {
        OrderModel orderModel = new OrderModel("",""+MainActivity.tableNumber, "new", MainActivity.mCardList);
        mDatabase.child("OrderRecords").child(""+MainActivity.tableNumber).setValue(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(OderDetailActivity.this , "Success");
                        MainActivity.mCardList.clear();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(OderDetailActivity.this , "Fail");
                    }
                });
    }
}