
package com.e.e_commerce_app.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Activitys.Item_Description_Activity;
import com.e.e_commerce_app.Activitys.MyCartActivity;
import com.e.e_commerce_app.ApplicationClass;
import com.e.e_commerce_app.Model.CartModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {
    Context context;
    private SessionManager sessionManager;
    private SharedPreferences sharedPreferences;
    onClickInterface onClickInterface;
    SharedPreferences.Editor editor;
    public static ArrayList<CartModel> cartItemList = new ArrayList<>();
    Inflater inflater;
    int num = 0;
    int qyn = 0;
    TextView subtotal;
    RelativeLayout cartamnt;
    LinearLayout carterror;
    String inc;

    public MyCartAdapter(Context context, ArrayList<CartModel> cartItemList,onClickInterface onClickInterface) {
        this.context = context;
        this.cartItemList = cartItemList;
        sessionManager = new SessionManager(context);
        subtotal = (TextView) ((MyCartActivity) context).findViewById(R.id.cartsubtotal);
        cartamnt = (RelativeLayout) ((MyCartActivity) context).findViewById(R.id.cartanmmt);
        carterror = (LinearLayout) ((MyCartActivity) context).findViewById(R.id.carterror);
        sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.onClickInterface = onClickInterface;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final CartModel cartModel = cartItemList.get(position);
        Picasso.with(context).load("" + APIs.CartImgUrl + cartModel.getCartItemImg()).error(R.drawable.no_image).into(holder.cartItemImage);
        holder.cartItemName.setText(cartModel.getCartItemName());
        holder.cartId = "" + cartModel.getCartId();
        holder.cartItemPrice.setText("₹" + cartModel.getCartItemPrice() );
        holder.cartQuantity.setText("" + cartModel.getCartQuantity());
        holder.cartTotal.setText("₹" + cartModel.getCartItemTotalPrice());
        holder.cartShipChrg.setText("₹" + cartModel.getCartItemShipngChrg());
        holder.cartDiscount.setText("" + cartModel.getCartItemDiscnt());
        holder.cartSubTotal.setText("₹"+cartModel.getCartItemSubTotal());
        holder.Quantity = String.valueOf(Integer.parseInt("" + cartModel.getCartQuantity()));
        holder.minteger = Integer.parseInt("" + cartModel.getCartQuantity());
        holder.price = Integer.parseInt("" + cartModel.getCartItemPrice());
        holder.cartItemDiscount = "" + cartModel.getCartItemDiscnt();
        holder.cartItemDiscountType = "" + cartModel.getCartItemDiscntType();
        holder.CartItemShipChg = "" + cartModel.getCartItemShipngChrg();
        holder.grandtotal = "" + cartModel.getCartGrandTotal();
        holder.selectCheckBox.setChecked(cartModel.getSelected());
        holder.selectCheckBox.setTag(position);
        holder.cartItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Item_Description_Activity.class);
                intent.putExtra("Id", "" + cartModel.getcId());
                context.startActivity(intent);
            }
        });
        holder.cartItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Item_Description_Activity.class);
                intent.putExtra("Id", "" + cartModel.getcId());
                context.startActivity(intent);
            }
        });
        holder.cartMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.cartQuantity.getText().equals("1")) {
                    num = 1;
                    updateQuantity(holder, position);
                }
            }
        });
        holder.cartPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = 2;
                updateQuantity(holder, position);
            }
        });
        holder.deletecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure want to delete this item?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteitem(holder, position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
            }
        });

        holder.selectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                Integer pos = (Integer) holder.selectCheckBox.getTag();
                if (cartItemList.get(pos).getSelected()) {
                    cartItemList.get(pos).setSelected(false);
                    ApplicationClass.cartModelslist.clear();
                } else {
                    cartItemList.get(pos).setSelected(true);
                    ApplicationClass.cartModelslist.add(cartModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cartItemImage;
        TextView cartItemName, cartItemPrice, cartTotal, cartQuantity, deletecart,cartDiscount,cartShipChrg,cartSubTotal;
        Button cartMinus, cartPlus;
        int price, minteger;
        String cartId;
        String Quantity, CartItemShipChg, cartItemDiscount, cartItemDiscountType;
        CheckBox selectCheckBox;
        String grandtotal;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            cartItemImage = itemView.findViewById(R.id.cartitem_p_img);
            cartItemName = itemView.findViewById(R.id.cartitem_p_name);
            cartItemPrice = itemView.findViewById(R.id.cartitem_p_untilprice);
            cartTotal = itemView.findViewById(R.id.cartitem_p_totalprice);
            cartMinus = itemView.findViewById(R.id.cartminusbtn);
            cartQuantity = itemView.findViewById(R.id.cartqun);
            cartShipChrg = itemView.findViewById(R.id.cartitem_p_ship_chrg);
            cartDiscount = itemView.findViewById(R.id.cartitem_p_discount);
            cartSubTotal = itemView.findViewById(R.id.cartitem_p_subtotal);
            cartPlus = itemView.findViewById(R.id.cartplsbtn);
            deletecart = itemView.findViewById(R.id.deleteicon);
            selectCheckBox = itemView.findViewById(R.id.select_checkbox);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface DataTransferInterface {
        public void setValues(ArrayList<CartModel> al);
    }

    public void updateQuantity(final MyViewHolder holder, final int position) {

        if (num == 1) {
            qyn = -1;
        }
        if (num == 2) {
            qyn = 1;
        }

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("update_qyn", "" + APIs.UpdateQuantitty);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.UpdateQuantitty,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(" updateqyn", "=" + jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                if (num == 1) {
                                    if (holder.minteger == 1) {
                                        holder.cartQuantity.setText("1");
                                    } else {
                                        holder.minteger = (holder.minteger - 1);
                                        holder.cartQuantity.setText("" + holder.minteger);
                                        cartItemList.get(position).setCartQuantity(""+holder.minteger);

                                        if(jsonObject.getString("prod_price").equals("") || jsonObject.getString("prod_price").equals("null"))
                                        {
                                            holder.cartTotal.setText("N/A");
                                        }
                                        else {
                                            holder.cartTotal.setText("₹"+jsonObject.getString("prod_price"));
                                        }
                                        if(jsonObject.getString("discount").equals("") || jsonObject.getString("discount").equals("null"))
                                        {
                                            holder.cartDiscount.setText("0");
                                        }
                                        else {
                                            holder.cartDiscount.setText(""+jsonObject.getString("discount"));
                                        }
                                        if(jsonObject.getString("single_prod_cal_price").equals("") || jsonObject.getString("single_prod_cal_price").equals("null"))
                                        {
                                            holder.cartSubTotal.setText("0"+" ₹");
                                        }
                                        else {
                                            holder.cartSubTotal.setText("₹"+jsonObject.getString("single_prod_cal_price")+" ₹");
                                        }
                                        if(jsonObject.getString("total_cart_grand_total").equals("") || jsonObject.getString("total_cart_grand_total").equals("null"))
                                        {
                                            subtotal.setText("0"+" ₹");
                                        }
                                        else {
                                            subtotal.setText("₹"+jsonObject.getString("total_cart_grand_total"));
                                        }
                                    }
                                } else if (num == 2) {
                                    holder.minteger = holder.minteger + 1;
                                    holder.cartQuantity.setText("" + holder.minteger);
                                    cartItemList.get(position).setCartQuantity(""+holder.minteger);

                                    if(jsonObject.getString("prod_price").equals("") || jsonObject.getString("prod_price").equals("null"))
                                    {
                                        holder.cartTotal.setText("0"+" ₹");
                                    }
                                    else {
                                        holder.cartTotal.setText("₹"+jsonObject.getString("prod_price"));
                                    }
                                    if(jsonObject.getString("discount").equals("") || jsonObject.getString("discount").equals("null"))
                                    {
                                        holder.cartDiscount.setText("0");
                                    }
                                    else {
                                        holder.cartDiscount.setText(""+jsonObject.getString("discount"));
                                    }

                                    if(jsonObject.getString("total_cart_grand_total").equals("") || jsonObject.getString("total_cart_grand_total").equals("null"))
                                    {
                                        subtotal.setText("0"+" ₹");
                                    }
                                    else {
                                        subtotal.setText("₹"+jsonObject.getString("total_cart_grand_total"));
                                    }
                                    if(jsonObject.getString("single_prod_cal_price").equals("") || jsonObject.getString("single_prod_cal_price").equals("null"))
                                    {
                                        holder.cartSubTotal.setText("0"+"₹");
                                    }
                                    else {
                                        holder.cartSubTotal.setText("₹"+jsonObject.getString("single_prod_cal_price"));
                                    }
                                }
                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my updtqyn", "=1");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                    }
                }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", "" + sessionManager.getString("userid"));
                params.put("cart_id", "" + holder.cartId);
                params.put("qty", "" + qyn);
                Log.e("param uq", params.toString());
                return params;
            }

        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(40),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void deleteitem(final MyViewHolder myViewHolder, final int POSITION) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("addwl_url", "" + APIs.DELETE_ITEM);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.DELETE_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" add_wishlist", "=" + response);
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                inc = jsonObject.getString("item_count");
                                Object toRemove = cartItemList.get(POSITION);
                                cartItemList.remove(toRemove);
                                notifyDataSetChanged();
                                if (getItemCount() == 0) {
                                    cartamnt.setVisibility(View.GONE);
                                    carterror.setVisibility(View.VISIBLE);
                                }
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                editor.putString("carttotal", "" + inc);
                                editor.commit();
                                editor.apply();

                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my deleteitem", "=1");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                    }
                }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", "" + sessionManager.getString("userid"));
                params.put("cart_id", "" + myViewHolder.cartId);
                Log.e("params", "=" + params.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(40),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public interface onClickInterface {
        void setClick(int abc);
    }
}
