package com.example.pujan.bag.orderDetailsFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pujan.bag.R;
import com.example.pujan.bag.pendingBill.PendingBillListFragment;


public class OrderFragment extends Fragment implements PendingBillListFragment.ChangeFragmentListener {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_activity, null);
        BagListFragment frag = new BagListFragment();
        getFragmentManager().beginTransaction().replace(R.id.topFrame, frag).commit();

        return view;


    }


    @Override
    public void changeFromPendingBillList() {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.topFrame,new BagListFragment());
        fragmentTransaction.commit();


    }
}
