package com.e.e_commerce_app.Model;

import com.e.e_commerce_app.Activitys.MyOrdersActivity;

import java.util.ArrayList;
import java.util.Date;

public class MyOrderModel {
    ArrayList<Product_Model> product_list=new ArrayList<>();
    String Orderid,Orderstatus,Ordertoal;
    String orderDate;

    public ArrayList<Product_Model> getProduct_list() {
        return product_list;
    }

    public void setProduct_list( ArrayList<Product_Model> product_list) {
        this.product_list = product_list;
    }

    public String getOrderid() {
        return Orderid;
    }

    public void setOrderid(String orderid) {
        Orderid = orderid;
    }

    public String getOrderstatus() {
        return Orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        Orderstatus = orderstatus;
    }

    public String getOrdertoal() {
        return Ordertoal;
    }

    public void setOrdertoal(String ordertoal) {
        Ordertoal = ordertoal;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
