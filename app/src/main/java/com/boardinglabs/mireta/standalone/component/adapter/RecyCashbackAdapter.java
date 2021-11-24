package com.boardinglabs.mireta.standalone.component.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GCashbackAgent;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 12/26/17.
 */

public class RecyCashbackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<GCashbackAgent> listCashback;
    private OnListClicked onListClicked;
    private ListActionLoadMore mListener;

    private enum ITEM_TYPE {
        ITEM_TYPE_TRANSACTION,
        ITEM_TYPE_LOADMORE
    }

    public RecyCashbackAdapter(ListActionLoadMore mListener) {
        listCashback = new ArrayList<>();
        this.mListener = mListener;
    }

    public void setClickListener(OnListClicked onListClicked) {
        this.onListClicked = onListClicked;
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
            GCashbackAgent response = listCashback.get(position);
            ((ViewHolder)holder).amountCashbackText.setText("Rp " + MethodUtil.toCurrencyFormat(response.amount));
            ((ViewHolder)holder).statusText.setText("BERHASIL");
            ((ViewHolder)holder).statusText.setTextColor(Color.parseColor("#2CC013"));

            switch (response.status) {
                case Constant.TYPE_TRANSACTION:
                    ((ViewHolder)holder).iconProduct.setImageDrawable(holder.itemView.getContext().getDrawable(R.drawable.komisi_agen));
                    ((ViewHolder)holder).typeCashbackText.setText("Cashback Transaksi");
                    ((ViewHolder)holder).infoCashbackText.setText("Transaksi");
                    break;
                case Constant.TYPE_TOPUP:
                    ((ViewHolder)holder).iconProduct.setImageDrawable(holder.itemView.getContext().getDrawable(R.drawable.komisi_downline));
                    ((ViewHolder)holder).typeCashbackText.setText("Cashback Topup");
                    ((ViewHolder)holder).infoCashbackText.setText("Isi Ulang");
                    break;
                case Constant.TYPE_REFERRAL:
                    ((ViewHolder)holder).iconProduct.setImageDrawable(holder.itemView.getContext().getDrawable(R.drawable.cashback_agen));
                    ((ViewHolder)holder).typeCashbackText.setText("Cashback Rekrut");
                    ((ViewHolder)holder).infoCashbackText.setText("Premium");
                    break;
                default:
                    break;
            }
            if (response.transaction != null) {
                String[] dateTime = MethodUtil.formatDateAndTime(response.transaction.created_at);
                ((ViewHolder)holder).dateText.setText(dateTime[0]);
                ((ViewHolder)holder).timeText.setText(dateTime[1]);
            }

            RxView.clicks(((ViewHolder)holder).container).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (onListClicked != null) {
                        onListClicked.listClick(position);
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
        return listCashback.size();
    }

    @Override
    public int getItemViewType(int position) {
        return listCashback.get(position) != null ? ITEM_TYPE.ITEM_TYPE_TRANSACTION.ordinal() : ITEM_TYPE.ITEM_TYPE_LOADMORE.ordinal();
    }

    public void addAll(List<GCashbackAgent> cashbacks) {
        listCashback.addAll(cashbacks);
        notifyDataSetChanged();
    }

    public void removeLoadingList() {
        listCashback.remove(listCashback.size() - 1);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iconProduct;
        private TextView typeCashbackText;
        private TextView amountCashbackText;
        private TextView infoCashbackText;
        private TextView statusText;
        private TextView dateText;
        private TextView timeText;
        private LinearLayout container;

        private ViewHolder(View view) {
            super(view);
            iconProduct = (ImageView) itemView.findViewById(R.id.icon_transaction);
            typeCashbackText = (TextView) itemView.findViewById(R.id.title_transaction);
            amountCashbackText = (TextView) itemView.findViewById(R.id.amount_transaction);
            infoCashbackText = (TextView) itemView.findViewById(R.id.info_transaction);
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
        void listClick(int position);
    }
}
