package com.boardinglabs.mireta.standalone.component.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GCostumer;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransferRequestLogGroup;
import com.boardinglabs.mireta.standalone.component.util.DateHelper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RecyFeedChatPreviewAdapter extends BaseSwipeAdapter {
    public List<GTransferRequestLogGroup> feedList;
    private ListActionListener itemActionListener;
    public GAgent agent;

    public RecyFeedChatPreviewAdapter() {
        feedList = new ArrayList<>();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.chat_feed_row_swipe_layout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed_chat_perview, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemClicked(position);
            }
        });

        v.findViewById(R.id.row_feed_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemClicked(position);
            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView customerName = (TextView)convertView.findViewById(R.id.customer_name_tv);
        TextView dateTV = (TextView)convertView.findViewById(R.id.date_tv);
        TextView lastActivity = (TextView)convertView.findViewById(R.id.last_activity_tv);
        CircleImageView avatar_img = (CircleImageView) convertView.findViewById(R.id.avatar_img);

        GTransferRequestLogGroup logGroup = feedList.get(position);
        GCostumer customer = null;
        if (logGroup.date != null){
            dateTV.setText(DateHelper.formatDateOrTime(logGroup.date));
        }
        if (logGroup.activity_by.equalsIgnoreCase(agent.id)){
            customer = logGroup.activity_to_customer;
            if (logGroup.activity.equals("request")){
                lastActivity.setText("Anda telah Request Transfer ke "+ logGroup.activity_to_customer.name);
            }
            else{
                lastActivity.setText("Anda telah Transfer ke "+ logGroup.activity_to_customer.name);
            }

        }
        else {
            customer = logGroup.activity_by_customer;
            if (logGroup.activity.equals("request")){
                lastActivity.setText(logGroup.activity_by_customer.name + " telah Request Transfer ke anda");
            }
            else{
                lastActivity.setText(logGroup.activity_by_customer.name + " telah Transfer ke anda");
            }


        }

        customerName.setText(customer.name);
        if (customer != null && customer.avatar_base64 != null && !customer.avatar_base64.equals("")){
            byte[] decodedString = Base64.decode(customer.avatar_base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatar_img.setImageBitmap(decodedByte);
        }
        else{
            avatar_img.setImageResource(R.drawable.ic_avatar);
        }
    }

    public void setListener(ListActionListener listClicked) {
        this.itemActionListener = listClicked;
    }

    public void setDataList(List<GTransferRequestLogGroup> feeds) {
        feedList = feeds;
        notifyDataSetChanged();
    }
    public void addDataList(List<GTransferRequestLogGroup> feeds) {
        if (feedList == null){
            feedList = new ArrayList<>();
        }
        feedList.addAll(feeds);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return feedList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}