package com.boardinglabs.mireta.standalone.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GCard;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 2/15/18.
 */

public class RecyCreditcardAdapter extends RecyclerSwipeAdapter<RecyCreditcardAdapter.SimpleViewHolder> {
    private List<GCard> mCard;
    private ActionAdapter mListener;

    public RecyCreditcardAdapter(ActionAdapter listener) {
        mCard = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_creditcard_layout, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final GCard mdl = mCard.get(position);
        final String digit = mdl.cardhash.substring(mdl.cardhash.length() - 4);
        viewHolder.cardName.setText(mdl.card_name);
        viewHolder.cardNumber.setText("**** **** **** " + digit);
        viewHolder.cardType.setText("Card type: " + mdl.card_type);
        viewHolder.cardExpDate.setText("Exp Date: " + mdl.expiry_month + "/" + mdl.expiry_year);
        if (mdl.card_type.equalsIgnoreCase("mastercard")) {
            viewHolder.iconCreditcard.setImageDrawable((viewHolder).itemView.getContext().getDrawable(R.drawable.icn_master));
        } else if (mdl.card_type.equalsIgnoreCase("visa")) {
            viewHolder.iconCreditcard.setImageDrawable((viewHolder).itemView.getContext().getDrawable(R.drawable.icn_visa));
        } else {
            viewHolder.iconCreditcard.setImageDrawable((viewHolder).itemView.getContext().getDrawable(R.drawable.icn_jcb));
        }

        RxView.clicks(viewHolder.detailCard).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                mView.showDetailCard(position, digit);
                if (mListener != null) {
                    mListener.onShowDetail(position, digit);
                }
            }
        });

        RxView.clicks(viewHolder.wrapperListCreditCard).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mListener != null) {
                    mListener.onShowDetail(position, digit);
                }
            }
        });

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        if (mdl.is_default != null && mdl.is_default.equalsIgnoreCase("1")) {
            viewHolder.imageArrow.setRotation(0);
            viewHolder.imageArrow.setImageDrawable(viewHolder.itemView.getContext().getDrawable(R.drawable.default_cc));
        } else {
            viewHolder.imageArrow.setRotation(180);
            viewHolder.imageArrow.setImageDrawable(viewHolder.itemView.getContext().getDrawable(R.drawable.back_button));
        }

        viewHolder.deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDeleteCard(position, viewHolder);
                }
//                mView.onDeleteCard(position, viewHolder);
            }
        });

        mItemManger.bindView(viewHolder.itemView, position);
    }

    /**
     * Returns the total number of transactionItems in the data set held by the adapter.
     *
     * @return The total number of transactionItems in this adapter.
     */
    @Override
    public int getItemCount() {
        return mCard.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public void setCardList(List<GCard> cardList) {
        this.mCard = cardList;
        notifyDataSetChanged();
    }

    public void onDeleteCreditCard(int position, SimpleViewHolder viewHolder){
        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
        mCard.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCard.size());
        mItemManger.closeAllItems();
    }

    public GCard getCard(int position) {
        if (mCard == null || mCard.size() == 0) {
            return null;
        }
        return mCard.get(position);
    }

    public interface ActionAdapter{
        void onDeleteCard(int position, SimpleViewHolder viewHolder);
        void onShowDetail(int position, String digit);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        Button buttonDelete;

        private TextView cardName;
        private TextView cardNumber;
        private TextView cardType;
        private TextView cardExpDate;
        private LinearLayout wrapperListCreditCard;
        private LinearLayout deleteCreditcard;
        private ImageView iconCreditcard;
        private ImageView imageArrow;
        private ImageView trash;
        private LinearLayout detailCard;
        private LinearLayout deleteCard;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            buttonDelete = (Button) itemView.findViewById(R.id.delete);
            iconCreditcard = (ImageView) itemView.findViewById(R.id.icon_creditcard);
            cardName = (TextView) itemView.findViewById(R.id.rowCreditCard_cardName);
            cardNumber = (TextView) itemView.findViewById(R.id.rowCreditCard_cardNumber);
            cardType = (TextView) itemView.findViewById(R.id.rowCreditCard_cardType);
            cardExpDate = (TextView) itemView.findViewById(R.id.rowCreditCard_cardExpDate);
            wrapperListCreditCard = (LinearLayout) itemView.findViewById(R.id.rowCreditCard_wrapperCard);
            detailCard = (LinearLayout) itemView.findViewById(R.id.detail_card);
//            deleteCreditcard = (LinearLayout) itemView.findViewById(R.id.deleted_creditcard);
            deleteCard = (LinearLayout) itemView.findViewById(R.id.trash);
            imageArrow = (ImageView) itemView.findViewById(R.id.image_arrow);
        }
    }
}
