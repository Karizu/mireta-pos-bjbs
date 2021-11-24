package com.boardinglabs.mireta.standalone.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GDeals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhimas on 3/27/18.
 */

public class RecyDealsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ActionDealsAdapter mListener;
    private List<GDeals> deals;

    public RecyDealsAdapter(ActionDealsAdapter mListener) {
        this.mListener = mListener;
        deals = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deals_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final GDeals deal = deals.get(position);
        if (deal.image != null) {
            Glide.with(holder.itemView.getContext()).load(deal.image.base_url + "/" + deal.image.path)
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            ((ViewHolder) holder).loading.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(((ViewHolder) holder).imagePromo);
        }
        ((ViewHolder) holder).titlePromo.setText(deal.name);
        ((ViewHolder) holder).totalDeal.setText(deal.points);
        ((ViewHolder) holder).descPromo.setText(Html.fromHtml(deal.description));
        ((ViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClickDeals(deal);
                }
            }
        });
    }

    public void setDeals(List<GDeals> listDeal) {
        deals = listDeal;
        notifyDataSetChanged();
    }
    /**
     * Returns the total number of transactionItems in the data set held by the adapter.
     *
     * @return The total number of transactionItems in this adapter.
     */
    @Override
    public int getItemCount() {
        return deals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titlePromo;
        private TextView descPromo;
        private ImageView imagePromo;
        private LinearLayout container;
        private ProgressBar loading;
        private TextView totalDeal;

        public ViewHolder(View itemView) {
            super(itemView);
            imagePromo = (ImageView) itemView.findViewById(R.id.image_deals);
            descPromo = (TextView) itemView.findViewById(R.id.info_deals);
            titlePromo = (TextView) itemView.findViewById(R.id.title_deals);
            totalDeal = (TextView) itemView.findViewById(R.id.total_deal);
            container = (LinearLayout) itemView.findViewById(R.id.container_deals);
            loading = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public interface ActionDealsAdapter{
        void onClickDeals(GDeals deal);
    }
}
