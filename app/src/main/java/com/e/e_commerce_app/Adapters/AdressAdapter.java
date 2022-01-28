package com.e.e_commerce_app.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Activitys.EditProfileActivity;
import com.e.e_commerce_app.Model.AdressModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AdressAdapter extends RecyclerView.Adapter<AdressAdapter.MyViewHolder> {
    private SessionManager sessionManager;
    Context context;
    ArrayList<AdressModel> adrslist = new ArrayList<AdressModel>();
    ArrayList<String> statelist;
    ArrayList<String> citylist;
    ArrayAdapter<String> country_adapter, state_adapter, city_adapter;
    String cntryid, stateid;
    Dialog custom_countrydialog_dialog,custom_state_dialog,custom_city_dialog;
    LinearLayout addadrs_layout;
    EditText country_editText, state_editText, city_editText;
    ListView country_listview, state_listview, city_listview;


    public AdressAdapter(Context context, ArrayList<AdressModel> adrslist) {
        this.adrslist = adrslist;
        this.context = context;
        sessionManager = new SessionManager(context);
        addadrs_layout = (LinearLayout) ((EditProfileActivity) context).findViewById(R.id.add_adrsLayout);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView et_country, et_city, et_state;
        EditText et_Adress, et_pincode;
        TextView adrsText;
        Button adrsDeleteBtn;
        String adrsId;
        String country, state, city;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            adrsText = itemView.findViewById(R.id.multiadrstext);
            et_Adress = itemView.findViewById(R.id.useradres);
            et_city = itemView.findViewById(R.id.usercity);
            et_state = itemView.findViewById(R.id.userstate);
            et_country = itemView.findViewById(R.id.usercountry);
            et_pincode = itemView.findViewById(R.id.userpincode);
            adrsDeleteBtn = itemView.findViewById(R.id.btndeleteadrs);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adress_item, parent, false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.adrsText.setText("Adress " + (position + 1));
        holder.et_Adress.setText(adrslist.get(position).getAdrs());
        holder.country = (adrslist.get(position).getCountry());
        holder.state = (adrslist.get(position).getState());
        holder.city = (adrslist.get(position).getCity());
        holder.adrsId = adrslist.get(position).getAdd_id();

        holder.et_country.setText(adrslist.get(position).getCountry());
        holder.et_state.setText(adrslist.get(position).getState());
        holder.et_city.setText(adrslist.get(position).getCity());

        cntryid = adrslist.get(position).getCountry();
        stateid = adrslist.get(position).getState();

        holder.et_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountrySearchableDialog(holder, adrslist.get(position).getCountrylist());
            }
        });

        holder.et_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateSearchableDialog(position, holder, adrslist.get(position).getStatelist());
            }
        });
        holder.et_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CitySearchableDialog(position, holder, adrslist.get(position).getCitylist());

            }
        });

        holder.et_pincode.setText("" + adrslist.get(position).getPincode());


        holder.adrsDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Address");
                builder.setMessage("Are you sure want to delete this adress?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAdreess(holder, position);
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
        return adrslist.size();
    }

    public void deleteAdreess(final MyViewHolder myViewHolder, final int POSITION) {
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
                                Object toRemove = adrslist.get(POSITION);
                                adrslist.remove(toRemove);
                                notifyDataSetChanged();
                                if (getItemCount() < 3) {
                                    addadrs_layout.setVisibility(View.VISIBLE);
                                }
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
                params.put("address_id", "" + myViewHolder.adrsId);
                params.put("user_id", "" + sessionManager.getString("userid"));
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

    public void getState(final int pos) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("stateurl", "" + APIs.GET_STATE_LIST);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_STATE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" state", "=" + response);
                        statelist = new ArrayList<>();
                        progressDialog.dismiss();
                        String states;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("state");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    states = jsonObject1.getString("city_state");
                                    statelist.add(states);
                                    Log.e("stateL", "=" + statelist.toString());
                                }
                                state_adapter.addAll(statelist);
                                state_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, adrslist.get(pos).getStatelist());
                                state_listview.setAdapter(state_adapter);
                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my state", "=1");
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
                params.put("country_id", "" + cntryid);
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

    public void getCity(final int pos) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("cityurl", "" + APIs.GET_CITY_LIST);
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.GET_CITY_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" state", "=" + response);
                        citylist = new ArrayList<>();
                        String citys;
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("city");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    citys = jsonObject1.getString("city_name");
                                    citylist.add(citys);
                                }
                                city_adapter.addAll(citylist);
                                city_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, adrslist.get(pos).getCitylist());
                                city_listview.setAdapter(city_adapter);

                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp my city", "=1");
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
                params.put("state_id", "" + stateid);
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

    public void CountrySearchableDialog(final MyViewHolder holder, ArrayList<String> mylist) {
        custom_countrydialog_dialog = new Dialog(context);
        custom_countrydialog_dialog.setContentView(R.layout.dialog_searchable_spinner);
        custom_countrydialog_dialog.getWindow().setLayout(1000, 1900);
        custom_countrydialog_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        custom_countrydialog_dialog.show();
        country_editText = custom_countrydialog_dialog.findViewById(R.id.dialog_edittext);
        country_listview = custom_countrydialog_dialog.findViewById(R.id.dialog_listview);
        TextView dialog_tittle=custom_countrydialog_dialog.findViewById(R.id.dialog_tittle);
        dialog_tittle.setText("choose country");

        ImageView imageView=custom_countrydialog_dialog.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_countrydialog_dialog.dismiss();
            }
        });
        country_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, mylist);
        country_listview.setAdapter(country_adapter);
        country_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                country_adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        country_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long b) {
                holder.et_country.setText(country_adapter.getItem(position));
                cntryid = country_adapter.getItem(position);
                custom_countrydialog_dialog.dismiss();
            }
        });

    }

    public void StateSearchableDialog(int pos, final MyViewHolder holder, ArrayList<String> mylist) {
        custom_state_dialog = new Dialog(context);
        custom_state_dialog.setContentView(R.layout.dialog_searchable_spinner);
        custom_state_dialog.getWindow().setLayout(1000, 1900);
        custom_state_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        custom_state_dialog.show();
        getState(pos);
        state_editText = custom_state_dialog.findViewById(R.id.dialog_edittext);
        state_listview = custom_state_dialog.findViewById(R.id.dialog_listview);
        TextView dialog_tittle=custom_state_dialog.findViewById(R.id.dialog_tittle);
        dialog_tittle.setText("choose state");

        ImageView imageView=custom_state_dialog.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_state_dialog.dismiss();
            }
        });
        state_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, mylist);
        state_listview.setAdapter(state_adapter);

        state_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                state_adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        state_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long b) {
                holder.et_state.setText(state_adapter.getItem(position));
                stateid = state_adapter.getItem(position);
                custom_state_dialog.dismiss();
            }
        });
    }
    public void CitySearchableDialog(int pos, final MyViewHolder holder, ArrayList<String> mylist) {
        custom_city_dialog = new Dialog(context);
        custom_city_dialog.setContentView(R.layout.dialog_searchable_spinner);
        custom_city_dialog.getWindow().setLayout(1000, 1900);
        custom_city_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        custom_city_dialog.show();
        getCity(pos);
        city_editText = custom_city_dialog.findViewById(R.id.dialog_edittext);
        city_listview = custom_city_dialog.findViewById(R.id.dialog_listview);
        TextView dialog_tittle=custom_city_dialog.findViewById(R.id.dialog_tittle);
        dialog_tittle.setText("choose city");
        ImageView imageView=custom_city_dialog.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_city_dialog.dismiss();
            }
        });

        city_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, mylist);
        city_listview.setAdapter(city_adapter);

        city_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                city_adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        city_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long b) {
                holder.et_city.setText(city_adapter.getItem(position));
                custom_city_dialog.dismiss();
            }
        });

    }
}
