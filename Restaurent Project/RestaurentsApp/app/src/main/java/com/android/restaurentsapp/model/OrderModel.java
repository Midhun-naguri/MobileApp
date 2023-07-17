package com.android.restaurentsapp.model;

import java.util.ArrayList;

public class OrderModel
{
    String orderNumber;
    String tableNumber;
    String orderStatus;
    ArrayList<CardItemModel> itemList;

    public OrderModel()
    {}

    public OrderModel(String orderNumber, String tableNumber, String orderStatus, ArrayList<CardItemModel> itemList) {
        this.orderNumber = orderNumber;
        this.tableNumber = tableNumber;
        this.orderStatus = orderStatus;
        this.itemList = itemList;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public ArrayList<CardItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<CardItemModel> itemList) {
        this.itemList = itemList;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
