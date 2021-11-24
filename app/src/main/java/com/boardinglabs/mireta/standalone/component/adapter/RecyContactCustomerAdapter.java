package com.boardinglabs.mireta.standalone.component.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.gson.GCostumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyContactCustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<GCostumer> customers;
    private List<GCostumer> filterCustomers;
    private ListActionListener mListener;
    private LinkedHashMap<String, Integer> mMapIndex;
    private ArrayList<String> mSectionList;
    private String[] mSections;
    private ContactFilter filter;

    public GCostumer getItemAtPosition(int position){
        List<GCostumer> displayList = (filterCustomers == null ? customers : filterCustomers);
        return displayList.get(position);
    }

    private GCostumer getItem(int pPosition) {
        List<GCostumer> displayList = (filterCustomers == null ? customers : filterCustomers);
        return displayList.get(pPosition);
    }

    public RecyContactCustomerAdapter(ListActionListener listener, List<GCostumer> pcustomers) {
        customers = pcustomers;
        mListener = listener;
        fillSections();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_customer, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        List<GCostumer> displayList = (filterCustomers == null ? customers : filterCustomers);
        final GCostumer customer = displayList.get(position);

        ((ViewHolder) holder).name.setText(Html.fromHtml(customer.name));
        GCostumer lcustomer = getItem(position);
        String section = getSection(customer);
        ((ViewHolder) holder).bind(customer, section, mMapIndex.get(section) == position);

        ((ViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.itemClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        List<GCostumer> displayList = (filterCustomers == null ? customers : filterCustomers);
        return displayList.size();
    }

    public void setListener(ListActionListener listClicked) {
        this.mListener = listClicked;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private CircleImageView avatar;
        private LinearLayout container;
        private TextView sectionTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.contact_avatar_iv);
            name = (TextView) itemView.findViewById(R.id.contact_name_tv);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            sectionTitle = (TextView) itemView.findViewById(R.id.section_title);
        }

        public void bind(GCostumer pItem, String pSection, boolean bShowSection) {
            name.setText(pItem.name);
            if (pItem != null && pItem.avatar_base64 != null && !pItem.avatar_base64.equals("")){
                byte[] decodedString = Base64.decode(pItem.avatar_base64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                avatar.setImageBitmap(decodedByte);
            }
            sectionTitle.setText(pSection);
            sectionTitle.setVisibility(bShowSection ? View.VISIBLE : View.GONE);
        }
    }


    private void fillSections() {
        List<GCostumer> displayList = (filterCustomers == null ? customers : filterCustomers);

        mMapIndex = new LinkedHashMap<String, Integer>();

        for (int x = 0; x < displayList.size(); x++) {
            String fruit = displayList.get(x).name;
            if (fruit.length() > 1) {
                String ch = fruit.substring(0, 1);
                ch = ch.toUpperCase();
                if (!mMapIndex.containsKey(ch)) {
                    mMapIndex.put(ch, x);
                }
            }
        }
        Set<String> sectionLetters = mMapIndex.keySet();
        // create a list from the set to sort
        mSectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(mSectionList);

        mSections = new String[mSectionList.size()];
        mSectionList.toArray(mSections);

    }
    private String getSection(GCostumer pCustomer) {

        return pCustomer.name.substring(0, 1).toUpperCase();
    }


    public void setDataCustomer(List<GCostumer> customerList) {
        customers = customerList;
        filterCustomers = null;
        fillSections();
        notifyDataSetChanged();
    }

    public void setFilterDataCustomer(List<GCostumer> customerList) {
        filterCustomers = customerList;
        fillSections();
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
            filter = new ContactFilter();
        return filter;
    }

    public class ContactFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            constraint = constraint.toString().toLowerCase();

            FilterResults newFilterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {


                ArrayList<GCostumer> auxData = new ArrayList<>();

                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).name.toString().toLowerCase().contains(constraint))
                        auxData.add(customers.get(i));
                }

                newFilterResults.count = auxData.size();
                newFilterResults.values = auxData;
            } else {

                newFilterResults.count = customers.size();
                newFilterResults.values = customers;
            }

            return newFilterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<GCostumer> resultData = (ArrayList<GCostumer>) results.values;

            setFilterDataCustomer(resultData);
        }

    }


}