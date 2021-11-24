package com.boardinglabs.mireta.standalone.component.adapter;

import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GCostumer;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransferRequestLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RecyChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<GTransferRequestLog> feedList;
    public GCostumer toCustomer;
    private ListActionListener itemActionListener;

    public RecyChatAdapter(GCostumer customer) {
        feedList = new ArrayList<>();
        toCustomer = customer;
    }

    public void setDataList(List<GTransferRequestLog> feeds) {
        feedList = feeds;
        notifyDataSetChanged();
    }
    public void addDataList(List<GTransferRequestLog> feeds) {
        if (feedList == null){
            feedList = new ArrayList<>();
        }
        feedList.addAll(feeds);
        notifyDataSetChanged();
    }

    public GTransferRequestLog getItemAtPosition(int position){
        return feedList.get(position);
    }

    private GTransferRequestLog getItem(int pPosition) {
        return feedList.get(pPosition);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyChatAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reqtrans, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GTransferRequestLog log = feedList.get(position);
        RecyChatAdapter.ViewHolder vHolder = (RecyChatAdapter.ViewHolder) holder;
        vHolder.bind(log, position);

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void setListener(ListActionListener listClicked) {
        this.itemActionListener = listClicked;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout normal_frame_left;
        private LinearLayout waiting_frame_left;
        private TextView activity_tv_left;
        private ImageView checklist_iv_left;
        private TextView amount_tv_left;
        private TextView status_action_tv_left;
        private ImageView arrow_detail_iv_left;
        private Button action_button_left;
        private Button action_button_left_accept;
        private Button action_button_left_reject;
        private LinearLayout message_body_left;


        private FrameLayout normal_frame_right;
        private LinearLayout waiting_frame_right;
        private TextView activity_tv_right;
        private ImageView checklist_iv_right;
        private TextView amount_tv_right;
        private TextView status_action_tv_right;
        private ImageView arrow_detail_iv_right;
        private Button action_button_right;
        private Button action_button_right_accept;
        private Button action_button_right_reject;
        private LinearLayout message_body_right;

        public ViewHolder(View itemView) {
            super(itemView);
            waiting_frame_left = (LinearLayout) itemView.findViewById(R.id.waiting_frame_left);
            normal_frame_left = (FrameLayout) itemView.findViewById(R.id.normal_frame_left);
            activity_tv_left = (TextView) itemView.findViewById(R.id.activity_tv_left);
            checklist_iv_left = (ImageView) itemView.findViewById(R.id.checklist_iv_left);
            amount_tv_left = (TextView)itemView.findViewById(R.id.amount_tv_left);
            status_action_tv_left = (TextView)itemView.findViewById(R.id.status_action_tv_left);
            arrow_detail_iv_left = (ImageView) itemView.findViewById(R.id.arrow_detail_iv_left);
            action_button_left = (Button) itemView.findViewById(R.id.action_button_left);
            action_button_left_accept = (Button) itemView.findViewById(R.id.action_button_left_accept);
            action_button_left_reject = (Button) itemView.findViewById(R.id.action_button_left_reject);
            message_body_left = (LinearLayout) itemView.findViewById(R.id.message_body_left);

            waiting_frame_right = (LinearLayout) itemView.findViewById(R.id.waiting_frame_right);
            normal_frame_right = (FrameLayout) itemView.findViewById(R.id.normal_frame_right);
            activity_tv_right = (TextView) itemView.findViewById(R.id.activity_tv_right);
            checklist_iv_right = (ImageView) itemView.findViewById(R.id.checklist_iv_right);
            amount_tv_right = (TextView)itemView.findViewById(R.id.amount_tv_right);
            status_action_tv_right = (TextView)itemView.findViewById(R.id.status_action_tv_right);
            arrow_detail_iv_right = (ImageView) itemView.findViewById(R.id.arrow_detail_iv_right);
            action_button_right = (Button) itemView.findViewById(R.id.action_button_right);
            action_button_right_accept = (Button) itemView.findViewById(R.id.action_button_right_accept);
            action_button_right_reject = (Button) itemView.findViewById(R.id.action_button_right_reject);
            message_body_right = (LinearLayout) itemView.findViewById(R.id.message_body_right);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind(GTransferRequestLog log, final int position) {
            if (toCustomer.id.equals(log.to_customer_id)) {
                waiting_frame_left.setVisibility(View.GONE);
                normal_frame_left.setVisibility(View.GONE);
                waiting_frame_right.setVisibility(View.GONE);
                normal_frame_right.setVisibility(View.VISIBLE);
                message_body_left.setVisibility(View.GONE);
                message_body_right.setVisibility(View.VISIBLE);

                activity_tv_right.setText(log.activity.toUpperCase());
                amount_tv_right.setText("Rp " + NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(Double.parseDouble(log.amount)).replace("$","").replace(".00","").replace(",","."));
                if (log.status.equals("5")){
                    if (log.activity.equalsIgnoreCase("transfer")){
                        checklist_iv_right.setVisibility(View.GONE);
                        arrow_detail_iv_right.setVisibility(View.VISIBLE);
                        status_action_tv_right.setText("DETIL");

                        action_button_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                itemActionListener.itemClicked(position);
                            }
                        });
                    }
                    else{
                        checklist_iv_right.setVisibility(View.GONE);
                        arrow_detail_iv_right.setVisibility(View.GONE);
                        if (log.accepted_status.equals("1")){
                            status_action_tv_right.setText("DITERIMA");
                        } else if (log.accepted_status.equals("2")) {
                            status_action_tv_right.setText("DITOLAK");
                        }
                    }

                }
                else{
                    checklist_iv_right.setVisibility(View.GONE);
                    arrow_detail_iv_right.setVisibility(View.GONE);
                    if (log.accepted_status.equals("0")){
                        status_action_tv_right.setText("TUNGGU");
                    } else if (log.accepted_status.equals("1")){
                        status_action_tv_right.setText("DITERIMA");
                    } else if (log.accepted_status.equals("2")){
                        status_action_tv_right.setText("DITOLAK");
                    }
                    status_action_tv_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });

                }
            }
            else{
                waiting_frame_left.setVisibility(View.GONE);
                normal_frame_left.setVisibility(View.VISIBLE);
                waiting_frame_right.setVisibility(View.GONE);
                normal_frame_right.setVisibility(View.GONE);
                message_body_left.setVisibility(View.VISIBLE);
                message_body_right.setVisibility(View.GONE);

                activity_tv_left.setText(log.activity.toUpperCase());
                amount_tv_left.setText("Rp " + NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(Double.parseDouble(log.amount)).replace("$","").replace(".00",""));
                if (log.status.equals("5")){
                    if (log.activity.equalsIgnoreCase("transfer")){
                        waiting_frame_left.setVisibility(View.GONE);
                        normal_frame_left.setVisibility(View.VISIBLE);
                        checklist_iv_left.setVisibility(View.GONE);
                        arrow_detail_iv_left.setVisibility(View.VISIBLE);
                        status_action_tv_left.setText("DETIL");

                        action_button_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                itemActionListener.itemClicked(position);
                            }
                        });
                    }
                    else{
                        checklist_iv_left.setVisibility(View.GONE);
                        arrow_detail_iv_left.setVisibility(View.GONE);
                        if (log.accepted_status.equals("1")){
                            waiting_frame_left.setVisibility(View.GONE);
                            normal_frame_left.setVisibility(View.VISIBLE);
                            status_action_tv_left.setText("DITERIMA");
                        } else if (log.accepted_status.equals("2")) {
                            waiting_frame_left.setVisibility(View.GONE);
                            normal_frame_left.setVisibility(View.VISIBLE);
                            status_action_tv_left.setText("DITOLAK");
                        }
                    }
                }
                else{
                    if (log.accepted_status.equals("0")){
                        waiting_frame_left.setVisibility(View.VISIBLE);
                        normal_frame_left.setVisibility(View.GONE);

                        action_button_left_accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                itemActionListener.itemClicked(position);
                            }
                        });
                        action_button_left_reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                itemActionListener.itemDeleted(position);
                            }
                        });
                    } else{
                        checklist_iv_left.setVisibility(View.GONE);
                        arrow_detail_iv_left.setVisibility(View.GONE);
                        if (log.accepted_status.equals("1")){
                            waiting_frame_left.setVisibility(View.GONE);
                            normal_frame_left.setVisibility(View.VISIBLE);
                            status_action_tv_left.setText("DITERIMA");
                        } else if (log.accepted_status.equals("2")){
                            waiting_frame_left.setVisibility(View.GONE);
                            normal_frame_left.setVisibility(View.VISIBLE);
                            status_action_tv_left.setText("DITOLAK");
                        }
                    }
                }

            }
        }
    }

}