package com.boardinglabs.mireta.standalone.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.gson.GContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhimas on 2/18/18.
 */

public class RecyContactCustomerInviteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GContact> contacts;
    private InviteListListener mListener;

    public RecyContactCustomerInviteAdapter(InviteListListener listener) {
        contacts = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_customer_invite, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GContact contact = contacts.get(position);

        ((ViewHolder) holder).name.setText(contact.getName());
        ((ViewHolder) holder).phone.setText(contact.getContact());
        ((ViewHolder) holder).checkIcon.setVisibility(contact.isSelected()?View.VISIBLE:View.GONE);
        ((ViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.onClickInvite(position,!contact.isSelected());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setDataContact(List<GContact> contactList) {
        contacts = contactList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView phone;
        private ImageView checkIcon;
        private LinearLayout root;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            checkIcon = (ImageView) itemView.findViewById(R.id.checkIcon);
            root = (LinearLayout) itemView.findViewById(R.id.root);
        }
    }

    public interface InviteListListener{
        void onClickInvite(int position, boolean isChecked);
    }
}
