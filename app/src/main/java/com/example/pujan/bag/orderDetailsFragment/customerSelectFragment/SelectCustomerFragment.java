package com.example.pujan.bag.orderDetailsFragment.customerSelectFragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.customerDetails.CustomerEntity;
import com.example.pujan.bag.orderDetailsFragment.OrderActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectCustomerFragment extends DialogFragment implements VolleyFunctions.AsyncResponse, SelectCustomerFragmentAdapter.ItemClickCallback {

    RecyclerView recView;
    SelectCustomerFragmentAdapter selectCustomerFragmentAdapter;
    ArrayList<CustomerEntity> customerData;

    ProgressBar progressBar;

    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void customerSelected(String customerName);


    }

    public void onCustomerSelected(ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;

    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.colorPrimary);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)

    {
        View view = inflater.inflate(R.layout.activity_select_customer_fragment, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        VolleyFunctions t = new VolleyFunctions(getContext());
        t.viewCustomer();
        t.trigAsyncResponse(SelectCustomerFragment.this);


        recView = (RecyclerView) view.findViewById(R.id.recView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Enter Customer Name:");

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                TableRow.LayoutParams param = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMargins(6, 6, 6, 6);
                layout.setLayoutParams(param);
                layout.setPadding(10, 10, 10, 10);
                layout.setBackgroundColor(Color.LTGRAY);


                TextView txt = new TextView(getContext());
                txt.setTextColor(Color.parseColor("#000000"));
                txt.setText("Customer Name");


                final EditText eTxt = new EditText(getContext());
                eTxt.setTextColor(Color.parseColor("#000000"));
                layout.addView(txt);
                layout.addView(eTxt);

                dialog.setView(layout);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!eTxt.getText().toString().equals(""))
                            itemClickCallback.customerSelected(eTxt.getText().toString());
                        dismiss();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });


        return view;

    }


    public void onComplete(String output) {

        progressBar.setVisibility(View.GONE);
        if (output.equals("ERROR")) {
            System.out.println("error has been occured");
            //Toast.makeText(getActivity().getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
        } else {


            customerData = new ArrayList<>();
            if (output.equals("Error")) {
                Toast.makeText(getContext(), "Error in Connection", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    JSONObject customerJson = new JSONObject(output);
                    JSONArray customerJsonArray = customerJson.getJSONArray("result");


                    for (int i = 0; i < customerJsonArray.length(); i++) {
                        JSONObject jObject = customerJsonArray.getJSONObject(i);
                        CustomerEntity customerEntity = new CustomerEntity();
                        customerEntity.setId(((jObject.getInt("customer_id"))));
                        customerEntity.setName(jObject.getString("customer_name"));
                        customerEntity.setAddress(jObject.getString("customer_address"));
                        customerEntity.setPhone(jObject.getString("customer_phone"));

                        customerData.add(customerEntity);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


                selectCustomerFragmentAdapter = new SelectCustomerFragmentAdapter(customerData, (OrderActivity) getActivity(), getContext());
                selectCustomerFragmentAdapter.onItemClickCallback(this);
                recView.setAdapter(selectCustomerFragmentAdapter);

//            customerViewAdapter.onItemClickCallback(this);

            }
        }

    }


    @Override
    public void onItemClick(int p) {
        this.dismiss();
    }
}
