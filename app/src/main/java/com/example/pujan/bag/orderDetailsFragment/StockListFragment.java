package com.example.pujan.bag.orderDetailsFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.bagStock.ColorQuantityEntity;
import com.example.pujan.bag.orderDetailsFragment.BagListFragment.ItemSelect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by puzan on 08-Apr-17.
 */
public class StockListFragment extends Fragment implements ItemSelect{


    Context context;



    View view;
    int bid=0;


    public StockListFragment(int bid){
        this.bid=bid;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_stock_list, null);

        context = getContext();


        final TableLayout tableLayout;
        final ArrayList<ColorQuantityEntity> colorValues = new ArrayList<>();



        tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);


        new android.os.Handler().postDelayed(
                new Runnable() {

                    public void run() {

                        try {
                            String response = new FunctionsThread(context).execute("ViewStockInformation", String.valueOf(bid)).get();
                            JSONObject stockJson = new JSONObject(response);
                            JSONArray stockJsonArray = stockJson.getJSONArray("result");
                            for (int i = 0; i < stockJsonArray.length(); i++) {
                                JSONObject jObject = stockJsonArray.getJSONObject(i);
                                ColorQuantityEntity cqe = new ColorQuantityEntity();
                                cqe.setColor(jObject.getString("color"));
                                cqe.setCquantity(Integer.valueOf(jObject.getString("quantityColor")));
                                colorValues.add(cqe);
                            }

                            JSONArray bagInfoJsonArray = stockJson.getJSONArray("bagInfo");
                            JSONObject jObject = bagInfoJsonArray.getJSONObject(0);
                            BagEntity bagEntitynew = new BagEntity();
                            bagEntitynew.setId(Integer.valueOf(jObject.getString("bag_id")));
                            bagEntitynew.setName(jObject.getString("bag_name"));
                            bagEntitynew.setCategory(jObject.getString("bag_category"));
                            bagEntitynew.setPrice(Integer.valueOf(jObject.getString("bag_price")));
                            bagEntitynew.setCompany(jObject.getString("bag_company"));
                            bagEntitynew.setQuantity(Integer.valueOf(jObject.getString("bag_quantity")));
                            bagEntitynew.setPhoto(jObject.getString("bag_photo"));


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int bigText = 14;


                        tableLayout.removeAllViews();
                        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
                        tableLayout.setBackgroundColor(Color.WHITE);


                        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
                        tableRowParams.setMargins(3, 3, 3, 3);
                        tableRowParams.weight = 1;

                        TableRow tableRow1 = new TableRow(context);
                        tableRow1.setBackgroundColor(Color.parseColor("#0087e2"));


                        TextView color = new TextView(context);
                        color.setBackgroundColor(Color.parseColor("#ff6600"));
                        color.setTextColor(Color.parseColor("#FFFFFF"));
                        color.setPadding(16, 6, 16, 6);
                        color.setGravity(Gravity.CENTER);
                        color.setText("Color");
                        tableRow1.addView(color, tableRowParams);

                        TextView quantityV = new TextView(context);
                        quantityV.setBackgroundColor(Color.parseColor("#ff6600"));
                        quantityV.setTextColor(Color.parseColor("#FFFFFF"));
                        quantityV.setPadding(16, 6, 16, 6);
                        quantityV.setGravity(Gravity.CENTER);
                        quantityV.setText("QTY");
                        tableRow1.addView(quantityV, tableRowParams);

                        tableLayout.addView(tableRow1, tableLayoutParams);


                        int total = 0;
                        for (int i = 0; i < colorValues.size(); i++) {
                            TableRow tableRow = new TableRow(context);
                            tableRow.setBackgroundColor(Color.WHITE);

                            TextView colors = new TextView(context);
                            colors.setBackgroundColor(Color.parseColor("#a9a7a5"));
                            colors.setTextColor(Color.parseColor("#FFFFFF"));
                            colors.setPadding(6, 6, 6, 6);
                            colors.setGravity(Gravity.CENTER);
                            colors.setText(colorValues.get(i).getColor());
                            tableRow.addView(colors, tableRowParams);

                            TextView quantitys = new TextView(context);
                            quantitys.setBackgroundColor(Color.parseColor("#a9a7a5"));
                            quantitys.setTextColor(Color.parseColor("#FFFFFF"));
                            quantitys.setPadding(6, 6, 6, 6);
                            quantitys.setGravity(Gravity.CENTER);
                            quantitys.setText(String.valueOf(colorValues.get(i).getCquantity()));
                            tableRow.addView(quantitys, tableRowParams);

                            total += colorValues.get(i).getCquantity();

                            tableLayout.addView(tableRow, tableLayoutParams);
                        }

                        TableRow tableRowlast = new TableRow(context);
                        tableRowlast.setBackgroundColor(Color.parseColor("#FFFFFF"));

                        TextView totals = new TextView(context);
                        totals.setBackgroundColor(Color.parseColor("#00d857"));
                        totals.setTextColor(Color.parseColor("#FFFFFF"));
                        totals.setPadding(6, 6, 6, 6);
                        totals.setGravity(Gravity.RIGHT);
                        totals.setText("Total: " + total);
                        TableRow.LayoutParams params = new TableRow.LayoutParams();
                        params.span = 2; //amount of columns you will span
                        totals.setLayoutParams(params);
                        tableRowlast.addView(totals, tableRowParams);

                        tableLayout.addView(tableRowlast, tableLayoutParams);



                    }
                }, 0);



        return view;

    }

    @Override
    public void onItemClick(int p) {

    }
}
