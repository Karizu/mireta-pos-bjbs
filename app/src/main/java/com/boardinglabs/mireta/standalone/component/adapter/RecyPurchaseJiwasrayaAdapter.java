package com.boardinglabs.mireta.standalone.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GJiwasrayaBillDetail;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Action1;

/**
 * Created by Randy on 10/08/18.
 */

public class RecyPurchaseJiwasrayaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Action mListener;
    private GJiwasrayaBillDetail[] listServices;
    private boolean isSimple;
    private String transaction;

    public RecyPurchaseJiwasrayaAdapter(boolean isSimple) {
//        listServices = new GJiwasrayaBillDetail[2];
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
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_purchase_jiwasraya_layout, parent, false));
        } else {
            return new ViewHolderSimple(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_purchase_jiwasraya_layout, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GJiwasrayaBillDetail services = listServices[position];
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).info.setText(services.billName.replace("TOT:",""));
            ((ViewHolder) holder).price.setText("Rp " + MethodUtil.toCurrencyFormat(services.billAmount));
            ((ViewHolder) holder).desc.setText(services.billShortName.replace("TOT.",""));
            RxView.clicks(((ViewHolder) holder).container).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (mListener != null) {
                        mListener.onListClick(position);
                    }
                }
            });
        } else if (holder instanceof ViewHolderSimple) {
            ((ViewHolderSimple) holder).info.setText(services.billName.replace("TOT:",""));
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

    public GJiwasrayaBillDetail[] getDataService(){
        return this.listServices;
    }

    public void setData(GTransaction transaction) throws JSONException {
//        this.listServices = listServices;
//        this.transaction=transaction;
        JSONObject objJiwasraya = new JSONObject(transaction.data);
        JSONObject objBIllDetails = new JSONObject(objJiwasraya.get("billDetails").toString());
        String BillDetail = objBIllDetails.get("BillDetail").toString();
        if(!BillDetail.contains("[")){
            this.listServices = new GJiwasrayaBillDetail[1];
            JSONObject js=new JSONObject(objBIllDetails.get("BillDetail").toString());
            this.listServices[0]=new GJiwasrayaBillDetail();
            this.listServices[0].billCode = js.get("billCode").toString();
            this.listServices[0].billName = js.get("billName").toString();
            this.listServices[0].billShortName = js.get("billShortName").toString();
            this.listServices[0].billAmount = js.get("billAmount").toString();
            this.listServices[0].reference1 = js.get("reference1").toString();
            this.listServices[0].reference2 = js.get("reference2").toString();
            this.listServices[0].reference3 = js.get("reference3").toString();

//            this.listServices[1]=new GJiwasrayaBillDetail();
//            this.listServices[1].billCode = "";
//            this.listServices[1].billName = "";
//            this.listServices[1].billShortName = "";
//            this.listServices[1].billAmount = "";
//            this.listServices[1].reference1 = "";
//            this.listServices[1].reference2 = "";
//            this.listServices[1].reference3 = "";
        }else{
            this.listServices = new GJiwasrayaBillDetail[2];
            JSONArray objBillDetail = new JSONArray(objBIllDetails.get("BillDetail").toString());
            for (int i=0;i<objBillDetail.length();i++){
                JSONObject jsonObject = objBillDetail.getJSONObject(i);
//            String billCode = jsonObject.get("billCode").toString();
                this.listServices[i]=new GJiwasrayaBillDetail();
                this.listServices[i].billCode = jsonObject.get("billCode").toString();
                this.listServices[i].billName = jsonObject.get("billName").toString();
                this.listServices[i].billShortName = jsonObject.get("billShortName").toString();
                this.listServices[i].billAmount = jsonObject.get("billAmount").toString();
                this.listServices[i].reference1 = jsonObject.get("reference1").toString();
                this.listServices[i].reference2 = jsonObject.get("reference2").toString();
                this.listServices[i].reference3 = jsonObject.get("reference3").toString();
            }
        }
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
        return listServices.length;
//        return 0;
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
            desc = (TextView) itemView.findViewById(R.id.add_info);
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
            desc = (TextView) itemView.findViewById(R.id.add_info);
        }
    }
}
