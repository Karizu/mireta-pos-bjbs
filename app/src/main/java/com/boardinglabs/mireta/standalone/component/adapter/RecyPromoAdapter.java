package com.boardinglabs.mireta.standalone.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GPromo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhimas on 2/18/18.
 */

public class RecyPromoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GPromo> promos;
    private ActionPromoAdapter mListener;

    public RecyPromoAdapter(ActionPromoAdapter listener) {
        promos = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_promo_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final GPromo promo = promos.get(position);

        ((ViewHolder) holder).titlePromo.setText(Html.fromHtml(promo.title));
        ((ViewHolder) holder).infoPromo.setText(Html.fromHtml(promo.content));
        if (promo.image != null) {
            Glide.with(((ViewHolder) holder).itemView.getContext())
                    .load(promo.image.base_url + "/" + promo.image.path)
                    .centerCrop()
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
        } else {
            ((ViewHolder) holder).loading.setVisibility(View.GONE);
            ((ViewHolder) holder).imagePromo.setVisibility(View.GONE);
            ((ViewHolder) holder).containerImage.setVisibility(View.GONE);
        }

        ((ViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClickPromo(promo.id + "");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return promos.size();
    }

    public void setDataPromo(List<GPromo> promoList) {
        promos = promoList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titlePromo;
        private TextView infoPromo;
        private ImageView imagePromo;
        private LinearLayout container;
        private ProgressBar loading;
        private RelativeLayout containerImage;

        public ViewHolder(View itemView) {
            super(itemView);
            imagePromo = (ImageView) itemView.findViewById(R.id.image_promo);
            titlePromo = (TextView) itemView.findViewById(R.id.title_promo);
            infoPromo = (TextView) itemView.findViewById(R.id.info_promo);
            container = (LinearLayout) itemView.findViewById(R.id.container_promo);
            loading = (ProgressBar) itemView.findViewById(R.id.progressBar);
            containerImage = (RelativeLayout) itemView.findViewById(R.id.container_image_promo);
        }
    }

    public interface ActionPromoAdapter{
        void onClickPromo(String promoId);
    }
}
