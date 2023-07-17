package com.android.restaurentuserapp.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.android.restaurentuserapp.R;
import com.android.restaurentuserapp.adapter.FoodListAdapter;
import com.android.restaurentuserapp.databinding.ActivityMainBinding;
import com.android.restaurentuserapp.model.AddFoodModel;
import com.android.restaurentuserapp.model.CardItemModel;
import com.android.restaurentuserapp.utils.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, FoodListAdapter.ClickInterface
{
    ActivityMainBinding binding;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    ArrayList<AddFoodModel> mList = new ArrayList<>();
    FoodListAdapter adapter;
    int itemQuantity = 1;
    public static ArrayList<CardItemModel> mCardList = new ArrayList<>();
    public static String tableNumber = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    void initView()
    {
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();

        binding.imgBack.setOnClickListener(this);
        binding.txtViewOrder.setOnClickListener(this);
        binding.txtViewCard.setOnClickListener(this);
        adapter =new FoodListAdapter(MainActivity.this ,  mList, storage, this);
        binding.rvList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.rvList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgBack : {
                finish();
                break;
            }
            case R.id.txtViewOrder : {
                startNewActivity(MainActivity.this , new CheckOrderStatusActivity());
                break;
            }
            case R.id.txtViewCard : {
                startNewActivity(MainActivity.this , new OderDetailActivity());
                break;
            }
            default:
                System.out.printf("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList.clear();
        geDataMethod();
        if(mCardList.isEmpty()) {
            binding.clCardItem.setVisibility(View.GONE);
        }
    }


    private void geDataMethod()
    {
        mDatabase.child("FoodList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    AddFoodModel mObj = noteSnapshot.getValue(AddFoodModel.class);
                    mObj.setKey(noteSnapshot.getKey());
                    mList.add(mObj);
                }
                if(mList.isEmpty())
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
    public void clickAddButton(AddFoodModel foodItem) {
        itemQuantity = 1;
        addItemDialog(foodItem);
    }

    private void addItemDialog(AddFoodModel foodItem)
    {
        Dialog dialog =new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_item);
        Window window = dialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        TextView txtName = dialog.findViewById(R.id.txtName);
        TextView txtQuantity = dialog.findViewById(R.id.txtQuantity);
        TextView txtPlus = dialog.findViewById(R.id.txtPlus);
        TextView txtMinus = dialog.findViewById(R.id.txtMinus);
        TextView txtAddItem = dialog.findViewById(R.id.txtAddItem);

        txtName.setText(foodItem.getFoodName());
//        txtPrice.setText(getResources().getString(R.string.Rs)+foodItem.getPrice());

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemQuantity+=1;
                txtQuantity.setText(""+itemQuantity);
            }
        });

        txtMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemQuantity>1)
                {
                    itemQuantity-=1;
                    txtQuantity.setText(""+itemQuantity);
                }
            }
        });
        txtAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCardList.add(new CardItemModel(foodItem.getFoodName(),foodItem.getFoodType(),foodItem.getPrice(),""+itemQuantity,foodItem.getImage()));
                dialog.dismiss();
                cardProcess();
            }
        });
        dialog.show();
    }

    void cardProcess()
    {
        if(!mCardList.isEmpty())
        {
            binding.clCardItem.setVisibility(View.VISIBLE);
            if(mCardList.size() > 1)
            {
                binding.txtTotalItem.setText(""+mCardList.size()+" Items Added");
            }
            else
            {
                binding.txtTotalItem.setText(""+mCardList.size()+" Item Added");
            }
        }
    }
}
