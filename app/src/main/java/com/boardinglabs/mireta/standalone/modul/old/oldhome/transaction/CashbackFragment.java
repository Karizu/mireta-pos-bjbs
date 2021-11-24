package com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.ListActionLoadMore;
import com.boardinglabs.mireta.standalone.component.adapter.RecyCashbackAdapter;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GCashbackAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransactionTopup;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;

import java.util.List;

/**
 * Created by Dhimas on 12/27/17.
 */

public class CashbackFragment extends Fragment implements RecyCashbackAdapter.OnListClicked, CommonInterface, TransactionView, ListActionLoadMore {
    private RecyclerView listView;
    private RecyCashbackAdapter mAdapter;
    private TransactionPresenter mPresenter;
    private RelativeLayout empty;
    private ImageView emptyImg;
    private TextView emptyText;

    public CashbackFragment newInstance() {
        CashbackFragment fragment = new CashbackFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_fragment_layout, container, false);
        initComponent(view);
        mPresenter = new TransactionPresenterImpl(this, this);
        mPresenter.getCashback();
        return view;
    }

    private void initComponent(View view) {
        empty = (RelativeLayout) view.findViewById(R.id.empty);
        emptyImg = (ImageView) view.findViewById(R.id.img_empty);
        emptyText = (TextView) view.findViewById(R.id.text_empty);
        listView = (RecyclerView) view.findViewById(R.id.transaction_list);
        mAdapter = new RecyCashbackAdapter(this);
        mAdapter.setClickListener(this);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(mAdapter);
    }

    @Override
    public void listClick(int position) {

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgresLoading() {
        mAdapter.removeLoadingList();
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(getActivity(), msg, R.drawable.ic_error_login);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            PreferenceManager.logOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }


    @Override
    public void onSuccessGetTransaction(List<GTransaction> response) {

    }

    @Override
    public void onSuccessGetTopup(List<GTransactionTopup> response) {

    }


    @Override
    public void onSuccessGetCashback(List<GCashbackAgent> response) {
        mAdapter.addAll(response);
        if (response.size() < 1) {
            listView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            emptyText.setText("Saat ini belum ada transaksi cashback");
            emptyImg.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.empty_cashback));
        }
    }

    @Override
    public void hideProgressList() {

    }

    @Override
    public void onLoadMoreList() {
        mPresenter.loadNextCashback();
    }
}
