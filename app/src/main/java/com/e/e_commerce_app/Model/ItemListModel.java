package com.e.e_commerce_app.Model;

public class ItemListModel {
    String listimg,relatedImg,heartImg;
    int ID;
    int pro_Id,pro_Price,related_img_Id,pro_Stock;
    String pro_Name,pro_Ava,pro_Color,pro_Size  ;

    public String getHeartImg() {
        return heartImg;
    }

    public void setHeartImg(String heartImg) {
        this.heartImg = heartImg;
    }

    public int getID() {
        return ID;
    }

    public void setID(int CAT_ID) {
        this.ID = CAT_ID;
    }

    public String getListimg() {
        return listimg;
    }

    public void setListimg(String listimg) {
        this.listimg = listimg;
    }

    public int getPro_Price() {
        return pro_Price;
    }

    public void setPro_Price(int pro_Price) {
        this.pro_Price = pro_Price;
    }

    public String getPro_Name() {
        return pro_Name;
    }

    public void setPro_Name(String pro_Name) {
        this.pro_Name = pro_Name;
    }

    public String getPro_Ava() {
        return pro_Ava;
    }

    public void setPro_Ava(String pro_Ava) {
        this.pro_Ava = pro_Ava;
    }

    public int getPro_Id() {
        return pro_Id;
    }

    public void setPro_Id(int pro_Id) {
        this.pro_Id = pro_Id;
    }

    public int getRelated_img_Id() {
        return related_img_Id;
    }

    public void setRelated_img_Id(int related_img_Id) {
        this.related_img_Id = related_img_Id;
    }

    public String getPro_Color() {
        return pro_Color;
    }

    public void setPro_Color(String pro_Color) {
        this.pro_Color = pro_Color;
    }

    public String getPro_Size() {
        return pro_Size;
    }

    public void setPro_Size(String pro_Size) {
        this.pro_Size = pro_Size;
    }

    public String getRelatedImg() {
        return relatedImg;
    }

    public void setRelatedImg(String relatedImg) {
        this.relatedImg = relatedImg;
    }


    public int getPro_Stock() {
        return pro_Stock;
    }

    public void setPro_Stock(int pro_Stock) {
        this.pro_Stock = pro_Stock;
    }
}
