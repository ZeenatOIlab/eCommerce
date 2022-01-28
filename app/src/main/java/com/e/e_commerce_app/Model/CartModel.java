package com.e.e_commerce_app.Model;

public class CartModel {
    String CartId;
    int cId;
    String CartItemName,CartItemImg;
    String CartItemPrice,CartItemTotalPrice,CartQuantity,CartItemShipngChrg,CartItemDiscnt,CartItemDiscntType,CartItemSubTotal;
    private boolean isSelected = false;
    String CartGrandTotal;


    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean checked) {
        isSelected = checked;
    }

    public String getCartId() {
        return CartId;
    }

    public void setCartId(String cartId) {
        CartId = cartId;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cid) {
        cId = cid;
    }

    public String getCartItemImg() {
        return CartItemImg;
    }

    public void setCartItemImg(String cartItemImg) {
        CartItemImg = cartItemImg;
    }

    public String getCartItemName() {
        return CartItemName;
    }

    public void setCartItemName(String cartItemName) {
        CartItemName = cartItemName;
    }

    public String getCartItemPrice() {
        return CartItemPrice;
    }

    public void setCartItemPrice(String  cartItemPrice) {
        CartItemPrice = cartItemPrice;
    }


    public String  getCartQuantity() {
        return CartQuantity;
    }

    public String getCartItemTotalPrice() {
        return CartItemTotalPrice;
    }

    public void setCartItemTotalPrice(String cartItemTotalPrice) {
        CartItemTotalPrice = cartItemTotalPrice;
    }

    public void setCartQuantity(String  cartQuantity) {
        CartQuantity = cartQuantity;
    }

    public String getCartItemShipngChrg() {
        return CartItemShipngChrg;
    }

    public void setCartItemShipngChrg(String cartItemShipngChrg) {
        CartItemShipngChrg = cartItemShipngChrg;
    }

    public String getCartItemSubTotal() {
        return CartItemSubTotal;
    }

    public void setCartItemSubTotal(String cartItemSubTotal) {
        CartItemSubTotal = cartItemSubTotal;
    }

    public String getCartItemDiscnt() {
        return CartItemDiscnt;
    }

    public void setCartItemDiscnt(String cartItemDiscnt) {
        CartItemDiscnt = cartItemDiscnt;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getCartGrandTotal() {
        return CartGrandTotal;
    }

    public void setCartGrandTotal(String cartGrandTotal) {
        CartGrandTotal = cartGrandTotal;
    }

    public String getCartItemDiscntType() {
        return CartItemDiscntType;
    }

    public void setCartItemDiscntType(String cartItemDiscntType) {
        CartItemDiscntType = cartItemDiscntType;
    }
}
