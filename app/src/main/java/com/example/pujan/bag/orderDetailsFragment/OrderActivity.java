package com.example.pujan.bag.orderDetailsFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.orderDetailsFragment.customerSelectFragment.SelectCustomerFragment;
import com.example.pujan.bag.orderDetailsFragment.printPackageFragment.FragmentPrintDemo;
import com.example.pujan.bag.pendingBill.PendingBillList;
import com.example.pujan.bag.printPackage.PrintEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static android.view.View.GONE;

/**
 * Created by puzan on 01-Apr-17.
 */
public class OrderActivity extends AppCompatActivity implements BagListFragment.BagAdapterPuller, SelectCustomerFragment.ItemClickCallback,VolleyFunctions.AsyncResponse {


    private TabLayout tabLayout;
    private static ViewPager viewPager;
    private Button orderDoneBtn;


    private ArrayList<PrintEntity> bagColorQuantities = new ArrayList<>();
    private static int scrollPosition = -1;

    private String source = "";
    Button selectCustomerBtn;
    EditText customerInfo;

    String customer_name = "";
    String customer_id = "0";
    String customer_address = "";

    int total = 0;
    int discount = 0;


    LinearLayout discountPercentSection, discountAmountSection;
    private Button bottomSheetDoneBtn;
    private EditText totalEditText, discountAmountEditText, discountPercentEditText, grandTotalEditText;
    private RadioButton radioDiscountNone, radioDiscountPercent, radioDiscountAmount;
    private RadioGroup radioDiscountMode;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, PendingBillList.class);
                startActivity(i);

        }
        return true;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_tabview);


        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Order Menu \n" + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        selectCustomerBtn = (Button) findViewById(R.id.selectCustomerBtn);
        customerInfo = (EditText) findViewById(R.id.customerInfoBox);
        customerInfo.setInputType(InputType.TYPE_NULL);

        selectCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FragmentManager fm = getSupportFragmentManager();

                    SelectCustomerFragment selectCustomerFragment = new SelectCustomerFragment();
                    selectCustomerFragment.show(fm, "Test");
                    selectCustomerFragment.onCustomerSelected(OrderActivity.this);

                }
                catch (Exception e)
                {
                    System.out.println("Already running");
                }

            }
        });


        Bundle bundle;
        bundle = getIntent().getExtras();
        source = bundle.getString("source");


        if (source.equals("Pending")) {
            String pId= bundle.getString("pId");
            VolleyFunctions thread = new VolleyFunctions(this);
            thread.queryPendingBill(pId);
            thread.trigAsyncResponse(OrderActivity.this);

            setCustomerInfo(bundle);
            selectCustomerBtn.setEnabled(false);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        orderDoneBtn = (Button) findViewById(R.id.orderDoneBtn);


        //Bottom Sheet Initialization
        bottomSheetDoneBtn = (Button) findViewById(R.id.bottomSheetDone);
        discountPercentSection = (LinearLayout) findViewById(R.id.discountPercentSection);
        discountAmountSection = (LinearLayout) findViewById(R.id.discountSection);


        radioDiscountMode = (RadioGroup) findViewById(R.id.radioDiscountGroup);
        radioDiscountNone = (RadioButton) findViewById(R.id.radioDiscountNone);
        radioDiscountPercent = (RadioButton) findViewById(R.id.radioDiscountPercent);
        radioDiscountAmount = (RadioButton) findViewById(R.id.radioDiscountAmount);

        discountPercentEditText = (EditText) findViewById(R.id.discountPercentEditTxt);
        discountAmountEditText = (EditText) findViewById(R.id.discountAmountEditTxt);
        totalEditText = (EditText) findViewById(R.id.totalAmountEditTxt);
        grandTotalEditText = (EditText) findViewById(R.id.grandTotalEditTxt);


        totalEditText.setInputType(InputType.TYPE_NULL);
        grandTotalEditText.setInputType(InputType.TYPE_NULL);


        discountPercentSection.setVisibility(View.GONE);
        discountAmountSection.setVisibility(View.GONE);
        radioDiscountMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.radioDiscountPercent) {
                    discountPercentSection.setVisibility(View.VISIBLE);
                    discountAmountSection.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioDiscountAmount) {
                    discountPercentSection.setVisibility(GONE);
                    discountAmountSection.setVisibility(View.VISIBLE);
                } else {
                    discountPercentSection.setVisibility(GONE);
                    discountAmountSection.setVisibility(GONE);
                }



            }
        });
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
                    if(!customerInfo.getText().toString().equals("Select Customer")) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        orderDoneBtn.setVisibility(GONE);
                    }
                    else
                        Toast.makeText(getBaseContext(),"Please Select Customer First",Toast.LENGTH_SHORT).show();

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
                    orderDoneBtn.setVisibility(GONE);
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

                if(radioDiscountNone.isChecked())
                {
                    discount=0;
                }
                else if(radioDiscountPercent.isChecked())
                {
                    if(!discountPercentEditText.getText().toString().equals(""))
                    discount=total*Integer.parseInt(discountPercentEditText.getText().toString())/100;
    }
                else if(radioDiscountAmount.isChecked())
                {
                    if(!discountAmountEditText.getText().toString().equals(""))
                    discount=Integer.parseInt(discountAmountEditText.getText().toString());
     }
                else
                {
                    Toast.makeText(getBaseContext(),"Unkown Item Selected",Toast.LENGTH_SHORT);
                }

                grandTotalEditText.setText(String.valueOf(total-discount));
                if (!customer_id.equals("") | !customer_name.equals("")) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentPrintDemo printDemo = new FragmentPrintDemo(bagColorQuantities, customer_id, customer_name, customer_address, total, discount, source);
                    printDemo.show(fm, "PrintFragment");
                    orderDoneBtn.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getBaseContext(), "Please Select Customer", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void findTotal() {

        total = 0;
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

                }
                else {

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

    public void hideActionBar(boolean val) {
        if (val == true)
            getSupportActionBar().hide();
        else
            getSupportActionBar().show();

    }


    public void setCustomerInfo(Bundle bundle) {

        customer_id = String.valueOf(bundle.getInt("customer_id"));
        customer_name = bundle.getString("customer_name");
        customer_address = bundle.getString("customer_address");

        if (!source.equals("Pending"))
            customerInfo.setText(customer_name);
        else
            customerInfo.setText(customer_name + ", " + customer_address);


    }

    @Override
    public void customerSelected(String customerName) {
        customer_name = customerName;
        customer_id = "0";
        customerInfo.setText(customer_name);
    }

    @Override
    public void onComplete(String output) {
        convertPendingData(output);
    }
}
