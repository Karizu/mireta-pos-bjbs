package com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boardinglabs.mireta.standalone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhimas on 12/25/17.
 */

public class AllTransactionFragment extends Fragment {
    private SectionsPagerAdapter pagerAdapter;

    public AllTransactionFragment newInstance() {
        AllTransactionFragment fragment = new AllTransactionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_transaction_fragment_layout, container, false);
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        pagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.all_trans_viewPager);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.all_trans_tab);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        pagerAdapter.addTabFragment(new TopupTransactionFragment().newInstance());
        pagerAdapter.addTabFragment(new TransactionFragment().newInstance(false));
        pagerAdapter.addTabFragment(new TransactionFragment().newInstance(true));
//        pagerAdapter.addTabFragment(new CashbackFragment().newInstance());
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragment;

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.mFragment = new ArrayList<>();
        }

        private void addTabFragment(Fragment fragment) {
            mFragment.add(fragment);
            notifyDataSetChanged();
        }

        private void removeTabFragment() {
            mFragment.clear();
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return this.mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment != null ? mFragment.size() : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TopUp";
                case 1:
                    return "Service";
                case 2:
                    return "PPOB";
                default:
                    return "";
            }
        }
    }
}
