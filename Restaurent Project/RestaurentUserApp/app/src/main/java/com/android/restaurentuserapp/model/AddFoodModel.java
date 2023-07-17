package com.android.restaurentuserapp.model;

public class AddFoodModel
{
    String key;
    String foodName;
    String foodType;
    String price;
    String image;

    public AddFoodModel()
    {}

    public AddFoodModel(String key, String foodName, String foodType, String price, String image) {
        this.key = key;
        this.foodName = foodName;
        this.foodType = foodType;
        this.price = price;
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
