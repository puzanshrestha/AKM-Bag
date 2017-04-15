package com.example.pujan.bag.orderDetailsFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagColorQuantity;

public class OrderFragment extends Fragment implements CurrentOrderListFragment.CurrentAddOrder, BagListFragment.ItemSelect {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_activity, null);
        BagListFragment frag = new BagListFragment();
        frag.onItemClickCallback(OrderFragment.this);
        getFragmentManager().beginTransaction().replace(R.id.topFrame, frag).commit();
        return view;
    }

    @Override
    public void onClickAdd(BagColorQuantity bcq) {

    }

    @Override
    public void onItemClick(int p) {

        StockListFragment frag2 = new StockListFragment(p);
        getFragmentManager().beginTransaction().replace(R.id.bottomLeft, frag2).commit();

        CurrentOrderListFragment currentOrder = new CurrentOrderListFragment();
        getFragmentManager().beginTransaction().replace(R.id.bottomRight, currentOrder).commit();

        currentOrder.OnItemClickCallBack(OrderFragment.this);

    }
}
