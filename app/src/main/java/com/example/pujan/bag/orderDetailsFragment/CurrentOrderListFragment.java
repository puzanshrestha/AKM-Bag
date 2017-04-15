package com.example.pujan.bag.orderDetailsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagColorQuantity;

/**
 * Created by puzan on 08-Apr-17.
 */
public class CurrentOrderListFragment extends Fragment {

    private CurrentAddOrder currentAddOrder;

    public interface CurrentAddOrder{
        void onClickAdd(BagColorQuantity bcq);

    }

    public void OnItemClickCallBack(OrderFragment activity){
        this.currentAddOrder=activity;
    }

    Button addBtn,doneBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_orders,null);


        addBtn = (Button)view.findViewById(R.id.addColorBtn);
        return view;
    }
}
