package com.boardinglabs.mireta.standalone.modul.old.feed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.boardinglabs.mireta.standalone.modul.old.oldhome.PaymentFragment;

public class FeedPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    private PaymentFragment paymentFragment;
    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public FeedPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FeedUserFragment.newInstance();
            case 1:
                return FeedChatFragment.newInstance();
            case 2:
                FeedTrxFragment feedtrx = new FeedTrxFragment();
                return feedtrx.newInstance(true);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    public void setPaymentFragment(PaymentFragment paymentFragment) {
        this.paymentFragment = paymentFragment;
    }
}
