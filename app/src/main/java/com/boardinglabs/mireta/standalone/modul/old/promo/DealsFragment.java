package com.boardinglabs.mireta.standalone.modul.old.promo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyDealsAdapter;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GDeals;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;

import java.util.List;

/**
 * Created by Dhimas on 3/27/18.
 */

public class DealsFragment extends Fragment implements RecyDealsAdapter.ActionDealsAdapter, DealPresenterImpl.SuccessResponseDeals, CommonInterface {
    private RecyclerView dealsList;
    private RecyDealsAdapter mAdapter;
    private DealPresenter mPresenter;
    private CustomProgressBar progressBar = new CustomProgressBar();

    public DealsFragment newInstance() {
        DealsFragment fragment = new DealsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.promo_fragment_layout, container, false);
        initComponent(view);
        mPresenter = new DealPresenterImpl(this, this);
        mPresenter.getDeals();
        return view;
    }

    private void initComponent(View view) {
        mAdapter = new RecyDealsAdapter(this);
        dealsList = (RecyclerView) view.findViewById(R.id.promo_list);
        dealsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //dealsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        dealsList.setAdapter(mAdapter);
    }

    @Override
    public void onClickDeals(GDeals deal) {

    }

    @Override
    public void onSuccessDeals(List<GDeals> deals) {
        mAdapter.setDeals(deals);
    }

    @Override
    public void showProgressLoading() {
        progressBar.show(getContext(), "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        progressBar.getDialog().dismiss();
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
}
