package com.e.e_commerce_app.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Model.ProfileAdressModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProfileAdressAdapter extends RecyclerView.Adapter<ProfileAdressAdapter.MyViewHolder> {
    private SessionManager sessionManager;
    ArrayList<ProfileAdressModel> proAdrsList=new ArrayList<>();
    Context context;

    public ProfileAdressAdapter(ArrayList<ProfileAdressModel> proAdrsList, Context context,AdressProfileListener adressProfileListener) {
        this.proAdrsList = proAdrsList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adressview = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_adress_item, parent, false);
        return new MyViewHolder(adressview);    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.proAdrs.setText(proAdrsList.get(position).getProAdress());
        holder.proCity.setText(proAdrsList.get(position).getProCity());
        holder.proState.setText(proAdrsList.get(position).getProState());
        holder.proPin.setText(proAdrsList.get(position).getProPincode());
        holder.proCntry.setText(proAdrsList.get(position).getProCountry());
        holder.adrsId=(proAdrsList.get(position).getAdrsId());


        holder.deleteAdrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete Adress");
                builder.setMessage("Are you sure want to delete this adress?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAdreess(holder,position);
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
        return proAdrsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        String adrsId;

        TextView deleteAdrs,proAdrs,proCity,proState,proPin,proCntry;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteAdrs=itemView.findViewById(R.id.pro_adrsDelete);
            proAdrs=itemView.findViewById(R.id.tv_proadrs);
            proCity=itemView.findViewById(R.id.tv_pro_city);
            proState=itemView.findViewById(R.id.tv_prostate);
            proPin=itemView.findViewById(R.id.tv_propin);
            proCntry=itemView.findViewById(R.id.tv_procntrty);

        }
    }
    public void deleteAdreess(final MyViewHolder myViewHolder,final int POSITION) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Log.e("deleteAdrs", "" + APIs.DELETE_ADRESS);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.DELETE_ADRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" deleteAdrs", "=" + response);

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                Object toRemove = proAdrsList.get(POSITION);
                                proAdrsList.remove(toRemove);
                                notifyDataSetChanged();
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

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
                params.put("address_id",""+myViewHolder.adrsId);
                params.put("user_id",""+sessionManager.getString("userid"));
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
    public interface AdressProfileListener {
        void adresslistner(String product);
    }


}
