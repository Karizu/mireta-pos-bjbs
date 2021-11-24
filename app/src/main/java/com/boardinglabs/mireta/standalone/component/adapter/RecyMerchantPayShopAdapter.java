package com.boardinglabs.mireta.standalone.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GMerchant;
import com.boardinglabs.mireta.standalone.component.ui.SquareImageView;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 2/6/18.
 */

public class RecyMerchantPayShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Action listAction;
    private List<GMerchant> merchants;
    private ListActionLoadMore mListener;

    public enum ITEM_TYPE {
        ITEM_TYPE_CONTENT,
        ITEM_TYPE_LOADMORE
    }

    public RecyMerchantPayShopAdapter(ListActionLoadMore listener) {
        this.mListener = listener;
        this.merchants = new ArrayList<>();
    }

    public interface Action {
        void openMerchant(String merchantCode, String merchantId, String merchantName);
    }

    public void setOnListActionClicked(Action callback) {
        this.listAction = callback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal()) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_merchant_grid, parent, false));
//            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_merchant_list_layout, parent, false));
        }
        return new ViewHolderLoadMore(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loadmore, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final GMerchant mdl = merchants.get(position);

            String countryCode = "";
            String city = "";

//            if (mdl.country != null) {
//                countryCode = mdl.country.code;
//            }

            if (mdl.city != null) {
                city = mdl.city.name;
            }

            ((ViewHolder) holder).merchantName.setText(mdl.name);
//            ((ViewHolder) holder).merchantCity.setText(city);
            if (mdl.avatars != null) {
                String img = mdl.avatars.base_url + "/" + mdl.avatars.path;
                Glide.with(((ViewHolder) holder).itemView.getContext()).load(img)
                        .placeholder(R.drawable.default_image_merchant).fitCenter().dontAnimate()
                        .into(((ViewHolder) holder).merchantPicture);
            }

            RxView.clicks(((ViewHolder) holder).btnDetailMerchant).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    listAction.openMerchant(mdl.code, mdl.id, mdl.name);
                }
            });
//            if (!TextUtils.isEmpty(countryCode)) ((ViewHolder) holder).merchantCountry.setText(countryCode);
//            if (!TextUtils.isEmpty(city)) ((ViewHolder) holder).merchantLocation.setText(city);
//            ((ViewHolder) holder).merchantName.setText(mdl.name);
//
//            if (mdl.avatars != null && mdl.avatars.id != null) {
//                String img = mdl.avatars.base_url + "/" + mdl.avatars.path;
//                Glide.with(((ViewHolder) holder).itemView.getContext()).load(img)
//                        .placeholder(R.drawable.default_image_merchant).fitCenter().dontAnimate()
//                        .into(((ViewHolder) holder).merchantPicture);
//            }
//
//            RxView.clicks(((ViewHolder) holder).merchantPicture).subscribe(new Action1<Void>() {
//                @Override
//                public void call(Void aVoid) {
//                    listAction.openMerchant(mdl.code, mdl.id, mdl.name);
//                    if (mListener != null) mAnalytic.setActionAnalytic(mdl.name, mdl.id);
//                }
//            });
//
//            RxView.clicks(((ViewHolder) holder).btnDetailMerchant).subscribe(new Action1<Void>() {
//                @Override
//                public void call(Void aVoid) {
//                    if (mListener != null) mAnalytic.setActionAnalytic(mdl.name, mdl.id);
//                    listAction.openMerchant(mdl.code, mdl.id, mdl.name);
//                }
//            });
//
//            holder.itemView.post(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//
//            if (mdl.is_premium) {
//                ((ViewHolder) holder).imgPremium.setVisibility(View.VISIBLE);
//            } else {
//                ((ViewHolder) holder).imgPremium.setVisibility(View.GONE);
//            }
//
//            if (mdl.is_instant) {
//                ((ViewHolder) holder).imgInstant.setVisibility(View.VISIBLE);
//            } else {
//                ((ViewHolder) holder).imgInstant.setVisibility(View.GONE);
//            }
        } else if (holder instanceof ViewHolderLoadMore) {
            if (mListener != null) mListener.onLoadMoreList();
        }
    }

    @Override
    public int getItemCount() {
        return merchants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SquareImageView merchantPicture;
        private TextView merchantName;
        private TextView merchantLocation;
        private TextView merchantCountry;
        private RelativeLayout btnDetailMerchant;
        private ImageView imgPremium;
        private ImageView imgInstant;

        private TextView merchantCity;
        private CircularImageView merchantImage;
        private LinearLayout containerMerchantList;

        ViewHolder(View view) {
            super(view);
            merchantPicture = (SquareImageView) view.findViewById(R.id.rowMerchant_image);
            merchantName = (TextView) view.findViewById(R.id.rowMerchant_merchantName);
            merchantLocation = (TextView) view.findViewById(R.id.rowMerchant_merchantLocation);
            merchantCountry = (TextView) view.findViewById(R.id.rowMerchant_merchantCountry);
            btnDetailMerchant = (RelativeLayout) view.findViewById(R.id.rowMerchant_wrapper);
            imgPremium = (ImageView) view.findViewById(R.id.badge_premium);
            imgInstant = (ImageView) view.findViewById(R.id.badge_instant);
            merchantImage = (CircularImageView) view.findViewById(R.id.merchant_image);
            merchantCity = (TextView) view.findViewById(R.id.merchant_city);
            containerMerchantList = (LinearLayout) view.findViewById(R.id.container_merchant_list);
        }
    }

    class ViewHolderLoadMore extends RecyclerView.ViewHolder {
        ViewHolderLoadMore(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return merchants.get(position) != null ?
                ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal() : ITEM_TYPE.ITEM_TYPE_LOADMORE.ordinal();
    }

    public void removeLoadingList() {
        if (merchants != null && merchants.size() > 0) {
            merchants.remove(merchants.size() - 1);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (merchants != null) {
            merchants.clear();
            notifyDataSetChanged();
        }
    }

    public void addAll(List<GMerchant> merchantCollection) {
        if (!merchantCollection.isEmpty()) {
            merchants.addAll(merchantCollection);
            notifyDataSetChanged();
        }
    }

    public void add(GMerchant merchant) {
        merchants.add(merchant);
        notifyDataSetChanged();
    }

}
