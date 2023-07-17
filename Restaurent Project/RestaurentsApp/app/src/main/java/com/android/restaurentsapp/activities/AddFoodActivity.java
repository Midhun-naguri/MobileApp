package com.android.restaurentsapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.android.restaurentsapp.R;
import com.android.restaurentsapp.databinding.ActivityAddFoodBinding;
import com.android.restaurentsapp.model.AddFoodModel;
import com.android.restaurentsapp.utils.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddFoodActivity extends BaseActivity implements View.OnClickListener
{
    ActivityAddFoodBinding binding;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri selectedImageUri;
    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    void initView()
    {
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();

        binding.imgBack.setOnClickListener(this);
        binding.addImage.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgBack : {
                finish();
                break;
            }
            case R.id.addImage : {
                imageChooser();
                break;
            }
            case R.id.btnSubmit : {
                if(checkValidation())
                {
                    addFoodMethod();
                }
                break;
            }
            default:
                System.out.printf("");
        }
    }

    private void imageChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK)
                {

                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null)
                    {
                        selectedImageUri = data.getData();
                        setImage();
                        uploadImage();
                    }
                }
            });

    void setImage()
    {
        Bitmap selectedImageBitmap = null;
        try {
            selectedImageBitmap
                    = MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(),
                    selectedImageUri);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        binding.ivFood.setImageBitmap(selectedImageBitmap);
    }

    // UploadImage method
    private void uploadImage()
    {
        if (selectedImageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            storageReference = storage.getReference().child("images/" + UUID.randomUUID().toString());
            Log.d("FoodApp", storageReference.toString());
            Log.d("FoodApp", selectedImageUri.toString());

            storageReference.putFile(selectedImageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    imagePath = taskSnapshot.getMetadata().getPath();
                                    progressDialog.dismiss();
                                    setImage();
                                    showToast(AddFoodActivity.this, "Image Uploaded!!");
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            binding.ivFood.setImageBitmap(null);
                            showToast(AddFoodActivity.this, "Failed " + e.getLocalizedMessage());
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

    private Boolean checkValidation()
    {
        if(imagePath.trim().isEmpty())
        {
            showToast(this, "Please select food image.");
            return false;
        }
        else
            if(binding.etFoodName.getText().toString().trim().isEmpty())
        {
            showToast(this, "Please enter food name.");
            return false;
        }
        else if(binding.etFoodType.getText().toString().trim().isEmpty())
        {
            showToast(this, "Please enter food type.");
            return false;
        }else if(binding.etPrice.getText().toString().trim().isEmpty())
        {
            showToast(this, "Please enter price.");
            return false;
        }
        return true;
    }

    private void addFoodMethod()
    {
        AddFoodModel model = new AddFoodModel("",binding.etFoodName.getText().toString().trim(), binding.etFoodType.getText().toString().trim(), binding.etPrice.getText().toString().trim(), imagePath);
        mDatabase.child("FoodList").child(binding.etFoodType.getText().toString().trim()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(AddFoodActivity.this , "Success");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(AddFoodActivity.this , "Fail");
                    }
                });
    }
}