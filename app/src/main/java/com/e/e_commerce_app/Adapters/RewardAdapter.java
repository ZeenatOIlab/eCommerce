package com.e.e_commerce_app.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.e_commerce_app.Activitys.RewardActivity;
import com.e.e_commerce_app.Model.RewardModel;
import com.e.e_commerce_app.R;
import com.e.e_commerce_app.Utility.APIs;
import com.e.e_commerce_app.Utility.RequestHandler;
import com.e.e_commerce_app.Utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.myinnos.androidscratchcard.ScratchCard;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyViewHolder> {
    SessionManager sessionManager;
    Context context;
    ArrayList<RewardModel> rewardModels =new ArrayList<>();
    ScratchCard mScratchCard;
    ImageView ScrachImg;
    TextView ScrachText;
    Dialog scretckdialog;


    public RewardAdapter(Context context,ArrayList<RewardModel> rewardModels) {
        this.context=context;
        this.rewardModels = rewardModels;
        sessionManager=new SessionManager(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.reward_item, parent, false);
        return new MyViewHolder(itemview);    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.scrachImg.setImageResource(Integer.parseInt(""+rewardModels.get(position).getScrachImg()));
        holder.scrachText.setText(rewardModels.get(position).getScrachText());
        Picasso.with(context).load("" + APIs.IMG_URL + rewardModels.get(position).getRewardImg()).error(R.drawable.no_image).into(holder.rewardImg);
        holder.rewardText.setText(rewardModels.get(position).getRewardText());
        holder.rewardId=rewardModels.get(position).getReward_Id();
        holder.rewardStatus=rewardModels.get(position).getRewardStatus();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkretchPopup(holder,position);
            }
        });
        if (rewardModels.get(position).getRewardStatus().equals(""+1))
        {
            holder.scrachImg.setVisibility(View.GONE);
            holder.scrachText.setVisibility(View.GONE);
            holder.itemView.setClickable(false);
        }
        else if (rewardModels.get(position).getRewardStatus().equals(""+0))

        {
            holder.scrachImg.setVisibility(View.VISIBLE);
            holder.scrachText.setVisibility(View.VISIBLE);
            holder.itemView.setClickable(true);
        }
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return rewardModels.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView rewardImg,scrachImg;
        TextView rewardText,scrachText;
        String rewardId,rewardStatus;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rewardImg = itemView.findViewById(R.id.rewardImg);
            rewardText = itemView.findViewById(R.id.rewardText);
            scrachImg = itemView.findViewById(R.id.scratchImg);
            scrachText = itemView.findViewById(R.id.scrchText);
        }

        @Override
        public void onClick(View view) {

        }
    }
    void SkretchPopup(final MyViewHolder holder, final int pos) {
          scretckdialog = new Dialog(context);//, R.style.FullHeightDialog);

        scretckdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        scretckdialog.setCancelable(true);

        scretckdialog.setContentView(R.layout.skrech_view);
        Window window = scretckdialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        mScratchCard = scretckdialog.findViewById(R.id.scratchCard);
        ScrachImg = scretckdialog.findViewById(R.id.scrachbackImg);
        ScrachText = scretckdialog.findViewById(R.id.scrachbackText);
        Picasso.with(context).load("" + APIs.IMG_URL + rewardModels.get(pos).getRewardImg()).error(R.drawable.no_image).into(ScrachImg);
        ScrachText.setText(rewardModels.get(pos).getRewardText());
        mScratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent > 0.3) {
                  addReward(holder,pos);
                }
            }
        });

        scretckdialog.show();
    }
    public void addReward(final MyViewHolder holder, final int pos) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("mywl_url", "" + APIs.Update_Reward );
        final StringRequest request = new StringRequest(Request.Method.POST,
                APIs.Update_Reward ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(" addrward", "=" + response);
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {
                                mScratchCard.setVisibility(View.GONE);
                               holder.scrachImg.setVisibility(View.GONE);
                                holder.scrachText.setVisibility(View.GONE);
                                holder.itemView.setClickable(false);
                                scretckdialog.dismiss();
                                rewardModels.get(pos).setRewardStatus("1");

                            } else {

                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("resp updaterewaed", "=1");
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
                params.put("id", "" +rewardModels.get(pos).getReward_Id().toString());
                params.put("status", "1");
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
