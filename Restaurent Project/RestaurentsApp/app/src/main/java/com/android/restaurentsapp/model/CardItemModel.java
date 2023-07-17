package com.android.restaurentsapp.model;

public class CardItemModel
{
    String foodName;
    String foodType;
    String price;
    String Quantity;
    String image;

    public CardItemModel()
    {}

    public CardItemModel(String foodName, String foodType, String price, String Quantity, String image) {
        this.foodName = foodName;
        this.foodType = foodType;
        this.price = price;
        this.Quantity = Quantity;
        this.image = image;
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

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
