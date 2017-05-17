package com.example.pujan.bag.orderDetailsFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.bagStock.ColorQuantityEntity;
import com.example.pujan.bag.orderDetailsFragment.customerSelectFragment.SelectCustomerFragment;
import com.example.pujan.bag.orderDetailsFragment.printPackageFragment.FragmentPrintDemo;
import com.example.pujan.bag.printPackage.PrintEntity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by puzan on 01-Apr-17.
 */
public class OrderActivity extends AppCompatActivity implements BagListFragment.BagAdapterPuller, FunctionsThread.AsyncResponse {


    private TabLayout tabLayout;
    private static ViewPager viewPager;
    private Button orderDoneBtn;
    private Button bottomSheetDoneBtn;

    private EditText totalEditText;


    private ArrayList<PrintEntity> bagColorQuantities = new ArrayList<>();
    private static int scrollPosition = -1;

    private String source = "";
    Button selectCustomerBtn;
    EditText customerInfo;

    String customer_name="";
    String customer_id="";
    String customer_address="";

    int total=0;
    int discount=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_tabview);


        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Order Menu \n" + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        selectCustomerBtn=(Button)findViewById(R.id.selectCustomerBtn);
        customerInfo =(EditText) findViewById(R.id.customerInfoBox);
        customerInfo.setInputType(InputType.TYPE_NULL);

        selectCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();

                SelectCustomerFragment selectCustomerFragment = new SelectCustomerFragment();
                selectCustomerFragment.show(fm,"Test");

            }
        });


        Bundle bundle;
        bundle=getIntent().getExtras();
        source = bundle.getString("source");
        String pendingData = bundle.getString("pendingData");

        if (source.equals("Pending")) {
            convertPendingData(pendingData);
            customer_id=getIntent().getStringExtra("customer_id");
            customer_name=getIntent().getStringExtra("customer_name");
            setCustomerInfo(bundle);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        orderDoneBtn = (Button) findViewById(R.id.orderDoneBtn);
        bottomSheetDoneBtn = (Button) findViewById(R.id.bottomSheetDone);
        //Bottom Sheet Initialization

        totalEditText= (EditText)findViewById(R.id.totalAmount);
        totalEditText.setInputType(InputType.TYPE_NULL);



        //
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        orderDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                } else {

                    findTotal();
                    totalEditText.setText(String.valueOf(total));
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    orderDoneBtn.setVisibility(View.GONE);


                }
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (behavior.getState() == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                }
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    orderDoneBtn.setVisibility(View.GONE);
                else
                    orderDoneBtn.setVisibility(View.VISIBLE);




            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {


            }
        });


        bottomSheetDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                FragmentManager fm = getSupportFragmentManager();
                FragmentPrintDemo printDemo = new FragmentPrintDemo(bagColorQuantities, customer_id, customer_name, customer_address,total,discount, "bag");
                printDemo.show(fm, "TEst");
                orderDoneBtn.setVisibility(View.VISIBLE);
            }
        });

    }

    private void findTotal() {

        total=0;
        for (int i = 0; i < bagColorQuantities.size(); i++) {
            LinkedHashMap<String, Integer> finalColorMap = bagColorQuantities.get(i).getColorQuantity();

            for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {

                total += bagColorQuantities.get(i).getPrice() * entry.getValue();
            }

        }
    }

    private void convertPendingData(String pendingData) {


        try {
            JSONObject bagQuantityColor = new JSONObject(pendingData);
            JSONArray bag = bagQuantityColor.getJSONArray("result");


            LinkedHashMap<String, Integer> cqe = new LinkedHashMap<>();

            PrintEntity bcqEntity = new PrintEntity();
            JSONObject jObject2 = null;
            for (int i = 0; i < bag.length(); i++) {
                JSONObject jObject = bag.getJSONObject(i);
                bcqEntity.setBag_id(Integer.valueOf(jObject.getString("bag_id")));
                bcqEntity.setPrice(Integer.valueOf(jObject.getString("bag_price")));
                bcqEntity.setProduct(jObject.getString("bag_name"));

                if (i + 1 < bag.length()) {
                    jObject2 = bag.getJSONObject(i + 1);

                } else
                    jObject2 = jObject;
                if ((jObject2.getString("bag_id")).equals(jObject.getString("bag_id"))) {


                    cqe.put(jObject.getString("color"), Integer.valueOf(jObject.getString("quantityColor")));

                } else {

                    cqe.put(jObject.getString("color"), Integer.valueOf(jObject.getString("quantityColor")));
                    bcqEntity.setColorQuantity(cqe);
                    bagColorQuantities.add(bcqEntity);
                    bcqEntity = new PrintEntity();
                    cqe = new LinkedHashMap<>();

                }


            }


            bcqEntity.setColorQuantity(cqe);
            if (cqe.size() > 0)
                bagColorQuantities.add(bcqEntity);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("list", bagColorQuantities.toString());

    }

    BagListFragment bagListFragment;
    CurrentOrderListFragment currentOrderListFragment;

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (!source.equals("Pending"))
            bagListFragment = new BagListFragment();
        else
            bagListFragment = new BagListFragment(bagColorQuantities);

        bagListFragment.onItemClickCallback(this);

        adapter.addFragment(bagListFragment, "Order");

        currentOrderListFragment = new CurrentOrderListFragment();
        adapter.addFragment(currentOrderListFragment, "Current Order");


        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    orderDoneBtn.setText("DONE");

                    if (scrollPosition != -1) {
                        bagListFragment.scrollToPosition(scrollPosition);
                        scrollPosition = -1;
                    }
                }
                if (position == 1) {

                    orderDoneBtn.setText("ORDER NOW");
                    currentOrderListFragment.receiveData(bagColorQuantities);


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void getBagAdapter(BagViewFragmentAdapter bfAdapter) {
        bagColorQuantities = bfAdapter.getColorQuantity();

    }

    public static void changePage(int p) {

        scrollPosition = p;
        viewPager.setCurrentItem(0);


    }


    @Override
    public void onComplete(String output) {

        viewPager.setCurrentItem(1);
    }


    public void hideActionBar(boolean val) {
        if (val == true)
            getSupportActionBar().hide();
        else
            getSupportActionBar().show();

    }


    public void setCustomerInfo(Bundle bundle) {

        customer_id=String.valueOf(bundle.getInt("customer_id"));
        customer_name=bundle.getString("customer_name");
        customer_address=bundle.getString("customer_address");

        if(!source.equals("Pending"))
        customerInfo.setText(customer_name);
        else
            customerInfo.setText(customer_name+", "+customer_address);


    }
}
