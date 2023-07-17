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
import com.android.restaurentuserapp.databinding.ItemCartBinding;
import com.android.restaurentuserapp.model.CardItemModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder>
{
    Context cContext;
    ArrayList<CardItemModel> dataList = new ArrayList<CardItemModel>() ;
    FirebaseStorage storage;
    StorageReference storageReference;

    public OrderDetailAdapter(Context cContext, ArrayList<CardItemModel> dataList, FirebaseStorage storage)
    {
        this.cContext = cContext;
        this.dataList = dataList;
        this.storage = storage;
    }

    @Override
    public OrderDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(cContext).inflate(R.layout.item_cart, parent, false);
        return new OrderDetailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderDetailAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.txtName.setText(dataList.get(position).getFoodName());
        if(Integer.parseInt(dataList.get(position).getQuantity()) > 1)
            holder.binding.txtQuantity.setText(dataList.get(position).getQuantity()+" Items");
        else
            holder.binding.txtQuantity.setText(dataList.get(position).getQuantity()+" Item");
        holder.binding.txtPrice.setText(cContext.getResources().getString(R.string.pound)+Integer.parseInt(dataList.get(position).getPrice())*Integer.parseInt(dataList.get(position).getQuantity()));
        storageReference = storage.getReference().child(dataList.get(position).getImage());
        downloadImage(holder.binding);
        holder.binding.txtDelete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }

    void downloadImage(ItemCartBinding binding)
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

    void deleteItem(int position)
    {
        dataList.remove(position);
        notifyDataSetChanged();
    }
}

