package com.e.e_commerce_app.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.e.e_commerce_app.Activitys.OrderProcessActivity;
import com.e.e_commerce_app.Activitys.WishList_Activity;
import com.e.e_commerce_app.Model.WishListModel;
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

public class WishListAdapter  extends RecyclerView.Adapter<WishListAdapter.MyViewHolder>{
    ArrayList<WishListModel> wishList=new ArrayList<>();
    Context context;
    SessionManager sessionManager;
    LinearLayout wishlistErrorLayout,wishlistlayout;
    TextView wishlistmsg;

    public WishListAdapter(Context context,ArrayList<WishListModel> wishList) {
        this.context=context;
        this.wishList = wishList;
        sessionManager=new SessionManager(context);
        wishlistErrorLayout=(LinearLayout)((WishList_Activity)context).findViewById(R.id.wishlisterror);
        wishlistlayout=(LinearLayout)((WishList_Activity)context).findViewById(R.id.wishlistLayout);
        wishlistmsg=(TextView) ((WishList_Activity)context).findViewById(R.id.wishlisterrormsg);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View wishlistview = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, parent, false);
        return new MyViewHolder(wishlistview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Picasso.with(context).load(APIs.Image_url+wishList.get(position).getWishListProImage()).error(R.drawable.no_image).into(holder.WlProImg);
        holder.wishlist_id=wishList.get(position).getWishlist_id();
        holder.WlProName.setText(wishList.get(position).getWishListProName());
        holder.WLproPrice.setText("₹"+wishList.get(position).getWishListProPrice());
        holder.WLproDate.setText(""+wishList.get(position).getWishListProDate());
        holder.proId=""+wishList.get(position).getProID();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Item_Description_Activity.class);
                intent.putExtra("Id",""+wishList.get(position).getProID());
                context.startActivity(intent);

            }
        });

        holder.delete_wishlist_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete Adress");
                builder.setMessage("Are you sure want to delete this adress?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Delete_Wishlist_item(holder,position);
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
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        String wishlist_id,proId,size,Quantitynmbr;
        ImageView WlProImg;
        TextView WlProName,WLproPrice,WLproDate;
        ImageView delete_wishlist_Btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            WlProImg=itemView.findViewById(R.id.wishlist_p_image);
            WlProName=itemView.findViewById(R.id.wishlist_p_name);
            WLproPrice=itemView.findViewById(R.id.wishlist_p_price);
            delete_wishlist_Btn=itemView.findViewById(R.id.delete_btn_wl);
            WLproDate=itemView.findViewById(R.id.wishlist_p_date);
        }
    }
    public void addCart(final MyViewHolder holder) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Log.e("cart_url", "" + APIs.ADD_CART);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.ADD_CART ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" add_cart", "=" + response);

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {

                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my cart", "=1");
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
                params.put("products_id", ""+holder.proId);
                params.put("size", "" + holder.size);
                params.put("quantity", ""+holder.Quantitynmbr);
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

    public void Delete_Wishlist_item(final MyViewHolder myViewHolder,final int POSITION) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Log.e("addwlurl", "" + APIs.DELETE_WISHLIST_ITEM);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.DELETE_WISHLIST_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" add_wisjlist", "=" + response);

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                Object toRemove = wishList.get(POSITION);
                                wishList.remove(toRemove);
                                notifyDataSetChanged();
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                if(getItemCount()==0)
                                {
                                    wishlistErrorLayout.setVisibility(View.VISIBLE);
                                    wishlistlayout.setVisibility(View.GONE);
                                    wishlistmsg.setText("empty wishlist");

                                }

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
                params.put("wishlist_id",""+myViewHolder.wishlist_id);
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

}
