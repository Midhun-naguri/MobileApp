package com.android.restaurentsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.restaurentsapp.R;
import com.android.restaurentsapp.databinding.ItemCartBinding;
import com.android.restaurentsapp.databinding.ItemOrderListBinding;
import com.android.restaurentsapp.model.OrderModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OrderListAdapter  extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder>
{
    Context cContext;
    ArrayList<OrderModel> dataList = new ArrayList<OrderModel>() ;
    FirebaseStorage storage;
    StorageReference storageReference;
    ClickInterface clickInterface;

    public OrderListAdapter(Context cContext, ArrayList<OrderModel> dataList, ClickInterface clickInterface, FirebaseStorage storage)
    {
        this.cContext = cContext;
        this.dataList = dataList;
        this.clickInterface = clickInterface;
        this.storage = storage;
    }

    public interface ClickInterface
    {
        void clickViewButton(OrderModel foodItem);
    }

    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(cContext).inflate(R.layout.item_order_list, parent, false);
        return new OrderListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.txtTableNumber.setText("Table : "+dataList.get(position).getTableNumber());
        holder.binding.txtStatus.setText("Status : "+dataList.get(position).getOrderStatus());
        if(dataList.get(position).getItemList().size() > 1)
            holder.binding.txtQuantity.setText(dataList.get(position).getItemList().size()+" Items");
        else
            holder.binding.txtQuantity.setText(dataList.get(position).getItemList().size()+" Item");

        holder.binding.txtViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickInterface.clickViewButton(dataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderListBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = ItemOrderListBinding.bind(itemView);
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
}
