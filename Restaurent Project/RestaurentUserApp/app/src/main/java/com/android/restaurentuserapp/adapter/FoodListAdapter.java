package com.android.restaurentuserapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.restaurentuserapp.R;
import com.android.restaurentuserapp.databinding.ItemFoodBinding;
import com.android.restaurentuserapp.model.AddFoodModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.MyViewHolder>
{
    Context cContext;
    ArrayList<AddFoodModel> dataList = new ArrayList<AddFoodModel>() ;
    FirebaseStorage storage;
    StorageReference storageReference;
    ClickInterface clickInterface;

    public FoodListAdapter(Context cContext, ArrayList<AddFoodModel> dataList, FirebaseStorage storage, ClickInterface clickInterface)
    {
        this.cContext = cContext;
        this.dataList = dataList;
        this.storage = storage;
        this.clickInterface = clickInterface;
    }

    public interface ClickInterface
    {
        void clickAddButton(AddFoodModel foodItem);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(cContext).inflate(R.layout.item_food, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.txtName.setText(dataList.get(position).getFoodName());
        holder.binding.txtType.setText(dataList.get(position).getFoodType());
        holder.binding.txtPrice.setText(cContext.getResources().getString(R.string.pound)+dataList.get(position).getPrice());
        storageReference = storage.getReference().child(dataList.get(position).getImage());
        downloadImage(holder.binding);
        holder.binding.txtAddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickInterface.clickAddButton(dataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemFoodBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = ItemFoodBinding.bind(itemView);
        }
    }

    void downloadImage(ItemFoodBinding binding)
    {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(cContext)
                        .load(uri)
                        .error(R.drawable.splash_icon)
                        .into(binding.imgFood);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                binding.imgFood.setImageResource(R.drawable.splash_icon);
            }
        });
    }
}
