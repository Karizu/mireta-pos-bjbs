package com.boardinglabs.mireta.standalone.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GServices;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 10/2/17.
 */

public class RecyPurchaseQuotaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Action mListener;
    private List<GServices> listServices;
    private boolean isSimple;

    public RecyPurchaseQuotaAdapter(boolean isSimple) {
        listServices = new ArrayList<>();
        this.isSimple = isSimple;
    }

    public interface Action {
        void onListClick(int position);
    }

    public void setListener(Action action) {
        mListener = action;
    }

    private enum ITEM_TYPE {
        ITEM_TYPE_DETAIL,
        ITEM_TYPE_SIMPLE
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_DETAIL.ordinal()) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_purchase_quota_layout, parent, false));
        } else {
            return new ViewHolderSimple(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_purchase_quota_layout, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GServices services = listServices.get(position);

        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).desc.setText(services.description);
            ((ViewHolder) holder).price.setText("Rp " + MethodUtil.toCurrencyFormat(services.default_price));
            ((ViewHolder) holder).info.setText(services.name);
            RxView.clicks(((ViewHolder) holder).container).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (mListener != null) {
                        mListener.onListClick(position);
                    }
                }
            });
        } else if (holder instanceof ViewHolderSimple) {
            ((ViewHolderSimple) holder).info.setText(services.name);
            ((ViewHolderSimple) holder).desc.setVisibility(View.GONE);
            RxView.clicks(((ViewHolderSimple) holder).container).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (mListener != null) {
                        mListener.onListClick(position);
                    }
                }
            });
        }



    }

    public void setData(List<GServices> listServices) {
        this.listServices = listServices;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSimple) {
            return ITEM_TYPE.ITEM_TYPE_SIMPLE.ordinal();
        }
        return ITEM_TYPE.ITEM_TYPE_DETAIL.ordinal();
    }

    @Override
    public int getItemCount() {
        return listServices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView info;
        private TextView price;
        private TextView desc;
        private LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            info = (TextView) itemView.findViewById(R.id.info);
            price = (TextView) itemView.findViewById(R.id.price);
            desc = (TextView) itemView.findViewById(R.id.description);
            container = (LinearLayout) itemView.findViewById(R.id.container);
        }
    }

    class ViewHolderSimple extends RecyclerView.ViewHolder {
        private TextView info;
        private LinearLayout container;
        private TextView desc;
        public ViewHolderSimple(View itemView) {
            super(itemView);
            info = (TextView) itemView.findViewById(R.id.info);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            desc = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
