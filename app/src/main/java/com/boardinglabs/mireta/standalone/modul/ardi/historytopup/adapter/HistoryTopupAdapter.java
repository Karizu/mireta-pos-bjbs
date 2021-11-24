package com.boardinglabs.mireta.standalone.modul.ardi.historytopup.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.fontview.RobotoBoldTextView;
import com.boardinglabs.mireta.standalone.component.network.entities.Ardi.HistoryTopup.HistoryTopup;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HistoryTopupAdapter extends RecyclerView.Adapter<HistoryTopupAdapter.ViewHolder> {
    private List<HistoryTopup> transactionModels;
    private Context context;
    private Dialog dialog;

    public HistoryTopupAdapter(List<HistoryTopup> transactionModels, Context context){
        this.transactionModels = transactionModels;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryTopupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_transaction_topup, parent, false);

        return new HistoryTopupAdapter.ViewHolder(v);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull HistoryTopupAdapter.ViewHolder holder, int position){
        final HistoryTopup transactionModel = transactionModels.get(position);
        final int id = transactionModel.getId();
        final String member_id = transactionModel.getMemberId();
        final int amount = transactionModel.getAmount();
        final String order_date = transactionModel.getDate();
        final String memberName = transactionModel.getMember().getFullname();

        Date d = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            d = sdf.parse(order_date);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }

        sdf.applyPattern("dd-MM-yyyy HH:mm");

        holder.member_id.setText(member_id);
        holder.tvOrderNo.setText(memberName);
//        holder.tvDiscount.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mDiscount));
        holder.tvOrderDate.setText(sdf.format(d));
        holder.tvAmount.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(amount));

        Date finalD = d;
        holder.layoutTransaction.setOnClickListener(view -> {
//            Snackbar snackbar = Snackbar.make(view.getRootView().findViewById(android.R.id.content), "On Progress", Snackbar.LENGTH_LONG);
//            snackbar.show();
//            Intent intent = new Intent(context, DetailTransactionActivity.class);
//            intent.putExtra("order_no", id);
//            intent.putExtra("total", harga);
//            intent.putExtra("order_date", sdf.format(finalD));
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
        });

        holder.layoutDelete.setOnClickListener(v -> {
//            Snackbar snackbar = Snackbar.make(v.getRootView().findViewById(android.R.id.content), "On Progress", Snackbar.LENGTH_LONG);
//            snackbar.show();
            transactionModels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, transactionModels.size());
        });
    }

    @Override
    public int getItemCount(){
        return transactionModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatusOrder;
        RobotoBoldTextView tvOrderNo;
        RobotoBoldTextView tvAmount;
        RobotoBoldTextView member_id;
        TextView tvOrderDate;
        LinearLayout layoutTransaction;
        FrameLayout layoutDelete;

        ViewHolder(View v){
            super(v);

            tvStatusOrder = v.findViewById(R.id.status_transaction);
            tvOrderNo = v.findViewById(R.id.transaction_id);
            member_id = v.findViewById(R.id.member_id);
            tvAmount = v.findViewById(R.id.amount_transaction);
            tvOrderDate = v.findViewById(R.id.time_transaction);
            layoutTransaction = v.findViewById(R.id.container_transaction);
            layoutDelete = v.findViewById(R.id.user_feed_row_bottom);
        }
    }

    private void showDialog(int layout, Context context) {
        dialog = new Dialog(Objects.requireNonNull(context));
        //set content
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void updateData(List<HistoryTopup> newUser){
        transactionModels = new ArrayList<>();
        transactionModels.addAll(newUser);
        notifyDataSetChanged();
    }
}
