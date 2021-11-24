package com.boardinglabs.mireta.standalone.component.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransactionTopup;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 12/25/17.
 */

public class RecyTopupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<GTransactionTopup> topupList;
    private OnListClicked onListClicked;
    private ListActionLoadMore mListener;

    private enum ITEM_TYPE {
        ITEM_TYPE_TRANSACTION,
        ITEM_TYPE_LOADMORE
    }

    public RecyTopupAdapter(ListActionLoadMore mListener) {
        this.mListener = mListener;
        topupList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_TRANSACTION.ordinal()) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_layout, parent, false));
        }
        return new ViewHolderLoadMore(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loadmore, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final GTransactionTopup transaction = topupList.get(position);
//            if (PreferenceManager.getStatusAkupay()) {
//                Glide.with(holder.itemView.getContext()).load(R.drawable.akupay_fix).dontAnimate().into(((ViewHolder) holder).iconProduct);
//            } else {
                Glide.with(holder.itemView.getContext()).load(R.drawable.pampasy_icon).dontAnimate().into(((ViewHolder) holder).iconProduct);
//            }

            if (transaction.status.equalsIgnoreCase(Constant.TOPUP_STATUS_SUCCESS)) {
                ((ViewHolder) holder).statusText.setText("BERHASIL");
                ((ViewHolder) holder).statusText.setTextColor(Color.parseColor("#2CC013"));
            } else if (transaction.status.equalsIgnoreCase(Constant.TOPUP_STATUS_REJECT)) {
                ((ViewHolder) holder).statusText.setText("GAGAL");
                ((ViewHolder) holder).statusText.setTextColor(Color.parseColor("#A31627"));
            } else {
                ((ViewHolder) holder).statusText.setText("MENUNGGU");
                ((ViewHolder) holder).statusText.setTextColor(Color.parseColor("#E0D000"));
            }

            String[] dateTime = MethodUtil.formatDateAndTime(transaction.created_at);
            ((ViewHolder) holder).dateText.setText(dateTime[0]);
            ((ViewHolder) holder).timeText.setText(dateTime[1]);
            if (TextUtils.isEmpty(transaction.sub_customer_id)) {
                ((ViewHolder) holder).topupText.setText("Isi Saldo");
                ((ViewHolder) holder).infoText.setText(transaction.customer.mobile);
            } else {
                long temp = Long.parseLong(transaction.balance_before) - Long.parseLong(transaction.balance_after);
                if (temp > 0) {
                    ((ViewHolder) holder).topupText.setText("Kirim Saldo");
                    if (transaction.sub_customer != null) {
                        ((ViewHolder) holder).infoText.setText("Ke " + transaction.sub_customer.name + " " + transaction.sub_customer.mobile);
                    }

                } else {
                    ((ViewHolder) holder).topupText.setText("Terima Saldo");
                    ((ViewHolder) holder).infoText.setText(transaction.customer.mobile);
                    if (transaction.sub_customer != null) {
                        ((ViewHolder) holder).infoText.setText("Dari " + transaction.sub_customer.name + " " + transaction.sub_customer.mobile);
                    }
                }
            }

            String uniqueAmount = !TextUtils.isEmpty(transaction.unique_amount) ? transaction.unique_amount : "0";
            String adminVa = "0";
            if (transaction.method_payment.equalsIgnoreCase("4")) {
                adminVa = transaction.bank != null && !TextUtils.isEmpty(transaction.bank.va_fee) ? transaction.bank.va_fee : "0";
            }
            String amountCharge = "0";
            if (TextUtils.isEmpty(transaction.amount_charged)) {
                amountCharge = transaction.amount;
            } else {
                amountCharge = transaction.amount_charged;
            }

            int totalAmount = Integer.parseInt(amountCharge) + Integer.parseInt(uniqueAmount) + Integer.parseInt(adminVa);
            ((ViewHolder) holder).paketText.setText("Rp " + MethodUtil.toCurrencyFormat(totalAmount + ""));


            RxView.clicks(((ViewHolder) holder).container).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (onListClicked != null) {
                        onListClicked.listClick(transaction);
                    }
                }
            });
        } else if (holder instanceof ViewHolderLoadMore) {
            if (mListener != null) {
                mListener.onLoadMoreList();
            }
        }
    }

    @Override
    public int getItemCount() {
        return topupList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return topupList.get(position) != null ? ITEM_TYPE.ITEM_TYPE_TRANSACTION.ordinal() : ITEM_TYPE.ITEM_TYPE_LOADMORE.ordinal();
    }

    public void addData(List<GTransactionTopup> topups) {
        topupList.addAll(topups);
        notifyDataSetChanged();
    }

    public void removeLoadingList() {
        topupList.remove(topupList.size() - 1);
        notifyDataSetChanged();
    }

    public void setListenerOnClick(OnListClicked listenerOnClick) {
        onListClicked = listenerOnClick;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconProduct;
        private TextView topupText;
        private TextView paketText;
        private TextView infoText;
        private TextView statusText;
        private TextView dateText;
        private TextView timeText;
        private LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            iconProduct = (ImageView) itemView.findViewById(R.id.icon_transaction);
            topupText = (TextView) itemView.findViewById(R.id.title_transaction);
            paketText = (TextView) itemView.findViewById(R.id.amount_transaction);
            infoText = (TextView) itemView.findViewById(R.id.info_transaction);
            statusText = (TextView) itemView.findViewById(R.id.status_transaction);
            dateText = (TextView) itemView.findViewById(R.id.date_transaction);
            timeText = (TextView) itemView.findViewById(R.id.time_transaction);
            container = (LinearLayout) itemView.findViewById(R.id.container_transaction);
        }
    }

    class ViewHolderLoadMore extends RecyclerView.ViewHolder {
        ViewHolderLoadMore(View view) {
            super(view);
        }
    }

    public interface OnListClicked{
        void listClick(GTransactionTopup response);
    }

}
