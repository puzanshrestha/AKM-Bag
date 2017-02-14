package com.example.pujan.bag.bagDetails;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;
import com.example.pujan.bag.bagStock.ColorQuantityEntity;
import com.example.pujan.bag.database.DbHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/3/2017.
 */
public class BagViewAdapter extends RecyclerView.Adapter<BagViewAdapter.TestHolder> {

    private ArrayList<BagEntity> listData;
    private LayoutInflater inflater;

    private String bag;
    private Context context;
    String ip = "";

    String[] array_spinner;

    ArrayList<BagColorQuantity> colorQuantities = new ArrayList<>();


    private ItemClickCallback itemClickCallback;

    public void setFilter(ArrayList<BagEntity> list) {
        listData = new ArrayList<>();
        listData.addAll(list);
        notifyDataSetChanged();

    }

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    void onItemClickCallback(BagListActivity itemClickCallback) {
        this.itemClickCallback = itemClickCallback;

    }

    public BagViewAdapter(ArrayList<BagEntity> listData, String viewsource, Context c) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(c);
        this.bag = viewsource;
        this.context = c;


        DbHelper db = new DbHelper(context);
        try {
            ip = db.getIP();
        } catch (Exception e) {
        } finally {
            db.close();
        }
/*
        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();
        map.put("NO1",3);
        map.put("skf",5);
        BagColorQuantity bcq = new BagColorQuantity();
        bcq.setBag_id(1);
        bcq.setQuantityColor(map);
        colorQuantities.add(bcq);


*/
    }


    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_bag_list, parent, false);

        return new TestHolder(view);

    }

    @Override
    public void onBindViewHolder(final TestHolder holder, int position) {
        BagEntity item = listData.get(position);
        holder.id.setText((Integer.toString(item.getId())));
        holder.name.setText(item.getName());
        holder.category.setText(item.getCategory());
        holder.price.setText(Integer.toString(item.getPrice()));
        holder.company.setText(item.getCompany());
        holder.quantity.setText(Integer.toString(item.getQuantity()));


        holder.optionsMenu.setVisibility(View.VISIBLE);


        try {
            Picasso
                    .with(context)
                    .load("http://" + ip + "/bagWebServices/uploads/" + item.getPhoto())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(300, 300)
                    .placeholder(R.drawable.bag)
                    .into(holder.photoBox);

        } catch (Exception e) {
            System.out.println("No file found");
        }


        if (bag.equals("customer")) {

            holder.optionsMenu.setVisibility(View.GONE);

        } else
            holder.bagDialog.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class TestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView id;
        private TextView name;
        private TextView price;
        private TextView category;
        private TextView quantity;
        private TextView company;


        private View container;

        private ImageView photoBox;
        private TextView optionsMenu;
        private Button bagDialog;


        public TestHolder(View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.lbl_bag_id);
            name = (TextView) itemView.findViewById(R.id.lbl_bag_name);
            price = (TextView) itemView.findViewById(R.id.lbl_bag_price);
            category = (TextView) itemView.findViewById(R.id.lbl_bag_category);
            company = (TextView) itemView.findViewById(R.id.lbl_bag_company);
            quantity = (TextView) itemView.findViewById(R.id.lbl_bag_quantity);
            container = itemView.findViewById(R.id.cont_root_item);
            photoBox = (ImageView) itemView.findViewById(R.id.photo_box);


            bagDialog = (Button) itemView.findViewById(R.id.dialogWindow);

            optionsMenu = (TextView) itemView.findViewById(R.id.optionsMenu);

            context = itemView.getContext();


            container.setOnClickListener(this);
            bagDialog.setOnClickListener(this);

            if (bag.equals("customer")) {
                optionsMenu.setVisibility(View.INVISIBLE);
            } else {
                bagDialog.setVisibility(View.INVISIBLE);
            }

            optionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    PopupMenu popup = new PopupMenu(context, v);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.bag_option_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.editMenu:
                                    edit(getAdapterPosition());
                                    break;
                                case R.id.deleteMenu:
                                    delete(getAdapterPosition());
                                    break;

                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();


                }
            });


        }

        @Override
        public void onClick(View v) {
            itemClickCallback.onItemClick(getAdapterPosition());

            if (v.getId() == R.id.dialogWindow) {
                final Dialog mydialog = new Dialog(context);
                mydialog.setTitle("Select Your Order");
                mydialog.setContentView(R.layout.add_bag_dialog);


                String array_spinner[];
                final Spinner colorCombo;
                TableLayout tableLayout;
                ArrayList<ColorQuantityEntity> colorValues = new ArrayList<>();
                final int bid;
                final EditText quantity;
                Button updateStockBtn;
                TextView nameEditText;
                TextView typeEditText;
                TextView priceEditText;
                TextView companyEditText;
                TextView quantityEditText;
                ImageView bagPhoto;
                BagEntity bagEntity;
                Button addDoneBtn;
                Button addColorBtn;


                colorCombo = (Spinner) mydialog.findViewById(R.id.colorCombo);
                quantity = (EditText) mydialog.findViewById(R.id.cqty);
                updateStockBtn = (Button) mydialog.findViewById(R.id.updateStockBtn);

                nameEditText = (TextView) mydialog.findViewById(R.id.nameEditText);
                typeEditText = (TextView) mydialog.findViewById(R.id.typeEditText);
                priceEditText = (TextView) mydialog.findViewById(R.id.priceEditText);
                companyEditText = (TextView) mydialog.findViewById(R.id.companyEditText);
                quantityEditText = (TextView) mydialog.findViewById(R.id.quantityEditText);
                bagPhoto = (ImageView) mydialog.findViewById(R.id.bag_photo);
                tableLayout = (TableLayout) mydialog.findViewById(R.id.tableLayout);

                addDoneBtn = (Button) mydialog.findViewById(R.id.addDoneBtn);
                addColorBtn = (Button) mydialog.findViewById(R.id.addColorBtn);

                bid = listData.get(getAdapterPosition()).getId();


                try {
                    String response = new FunctionsThread(context).execute("ViewStockInformation", String.valueOf(bid)).get();
                    System.out.println(response);
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
                TableRow tr1 = new TableRow(context);
                TableRow.LayoutParams tb1c = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                tb1c.gravity = Gravity.CENTER;
                tr1.setLayoutParams(tb1c);

                final TextView color = new TextView(context);
                color.setPadding(6, 6, 6, 6);
                color.setText("Colors ");
                color.setTextSize(bigText);
                color.setSingleLine();
                TableRow.LayoutParams lnc = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lnc.gravity = Gravity.CENTER;
                color.setLayoutParams(lnc);
                tr1.addView(color);


                TextView qty = new TextView(context);
                qty.setPadding(6, 6, 6, 6);
                qty.setText(" Quantity");
                qty.setTextSize(bigText);
                qty.setSingleLine();
                TableRow.LayoutParams lnq = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lnq.gravity = Gravity.CENTER;
                qty.setLayoutParams(lnq);
                tr1.addView(qty);

                tableLayout.addView(tr1);
                newHorizontalLine(tableLayout);

                int total = 0;
                for (int i = 0; i < colorValues.size(); i++) {
                    TableRow tr1s = new TableRow(context);
                    TableRow.LayoutParams tb1cs = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    tb1cs.gravity = Gravity.CENTER;
                    tr1s.setLayoutParams(tb1c);

                    TextView colors = new TextView(context);
                    colors.setPadding(6, 6, 6, 6);
                    colors.setText(colorValues.get(i).getColor());
                    colors.setTextSize(bigText);
                    colors.setSingleLine();
                    TableRow.LayoutParams lncs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    lncs.gravity = Gravity.CENTER;
                    colors.setLayoutParams(lncs);
                    tr1s.addView(colors);


                    total += colorValues.get(i).getCquantity();

                    TextView qtys = new TextView(context);
                    qtys.setPadding(6, 6, 6, 6);
                    qtys.setText(Integer.toString(colorValues.get(i).getCquantity()));
                    qtys.setTextSize(bigText);
                    qtys.setSingleLine();
                    TableRow.LayoutParams lnqs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    lnqs.gravity = Gravity.CENTER;
                    qtys.setLayoutParams(lnqs);
                    tr1s.addView(qtys);

                    tableLayout.addView(tr1s);
                }

                newHorizontalLine(tableLayout);

                TableRow tr1s = new TableRow(context);
                TableRow.LayoutParams tb1cs = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                tb1cs.gravity = Gravity.CENTER;
                tr1s.setLayoutParams(tb1c);

                TextView colors = new TextView(context);
                colors.setPadding(6, 6, 6, 6);
                colors.setText("Total: ");
                colors.setTextSize(bigText);
                colors.setSingleLine();
                TableRow.LayoutParams lncs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lncs.gravity = Gravity.RIGHT;
                colors.setLayoutParams(lncs);
                tr1s.addView(colors);


                TextView qtys = new TextView(context);
                qtys.setPadding(6, 6, 6, 6);
                qtys.setText(Integer.toString(total));
                qtys.setTextSize(bigText);
                qtys.setSingleLine();
                TableRow.LayoutParams lnqs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lnqs.gravity = Gravity.CENTER;
                qtys.setLayoutParams(lnqs);
                tr1s.addView(qtys);

                populateColor(colorCombo);
                tableLayout.addView(tr1s);


                //Table for Stock


                //This is for Current Order


                final TableLayout tableLayout1 = (TableLayout) mydialog.findViewById(R.id.tableLayout2);


                TableRow tr12 = new TableRow(context);
                TableRow.LayoutParams tb1c2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                tb1c2.gravity = Gravity.CENTER;
                tr12.setLayoutParams(tb1c);

                TextView color2 = new TextView(context);
                color2.setPadding(6, 6, 6, 6);
                color2.setText("Colors ");
                color2.setTextSize(bigText);
                color2.setSingleLine();
                TableRow.LayoutParams lnc2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lnc2.gravity = Gravity.CENTER;
                color2.setLayoutParams(lnc2);
                tr12.addView(color2);


                TextView qty2 = new TextView(context);
                qty2.setPadding(6, 6, 6, 6);
                qty2.setText(" Quantity");
                qty2.setTextSize(bigText);
                qty2.setSingleLine();
                TableRow.LayoutParams lnq2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lnq2.gravity = Gravity.CENTER;
                qty2.setLayoutParams(lnq2);
                tr12.addView(qty2);

                tableLayout1.addView(tr12);
                newHorizontalLine(tableLayout1);


                final BagColorQuantity bcq = new BagColorQuantity();
                bcq.setBag_id(bid);

                LinkedHashMap<String, Integer> colorMap = new LinkedHashMap<String, Integer>();


                for (int j = 0; j < colorQuantities.size(); j++) {
                    if (bid == colorQuantities.get(j).getBag_id()) {
                        colorMap = colorQuantities.get(j).getQuantityColor();
                    }
                }


                final LinkedHashMap<String, Integer> finalColorMap = colorMap;


                populateOrder(tableLayout1, finalColorMap);


                mydialog.show();

                addDoneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bcq.setQuantityColor(finalColorMap);
                        for (int i = 0; i < colorQuantities.size(); i++) {
                            if (bid == colorQuantities.get(i).getBag_id()) {
                                colorQuantities.remove(i);


                            }
                        }
                        colorQuantities.add(bcq);
                        mydialog.dismiss();
                    }
                });


                addColorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finalColorMap.put(colorCombo.getSelectedItem().toString(), Integer.valueOf(quantity.getText().toString()));
                        populateOrder(tableLayout1, finalColorMap);


                    }
                });


            }


        }
    }

    public ArrayList<BagColorQuantity> getRecValues() {
        return colorQuantities;
    }


    public void edit(int position) {
        Intent i;
        if (bag.equals("bag"))//EDIT
        {
            i = new Intent(context, AddBagActivity.class);

            BagEntity item = listData.get(position);
            String bagid = Integer.toString(item.getId());
            String category = item.getCategory();
            String name = item.getName();
            String company = item.getCompany();
            String price = Integer.toString(item.getPrice());
            String quantity = Integer.toString(item.getQuantity());
            i.putExtra("bagid", bagid);
            i.putExtra("name", name);
            i.putExtra("category", category);
            i.putExtra("price", price);
            i.putExtra("company", company);
            i.putExtra("quantity", quantity);
            i.putExtra("source", "source");
            i.putExtra("photo", item.getPhoto());
            context.startActivity(i);
        }


    }

    public void delete(int position) {
        if (bag.equals("bag"))//DELETE
        {
            final String a = " ";

            BagEntity item = listData.get(position);
            final String bagid = Integer.toString(item.getId());

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Are you sure,You wanted to Delete?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            final String check;
                            try {
                                System.out.println("------------" + bagid + "  " + a);
                                check = new FunctionsThread(context).execute("AddBag", a, a, a, a, "delete", bagid, a, a).get();
                                System.out.println(check);
                                if (check.equals("delete")) {
                                    Intent i = new Intent(context, BagListActivity.class);
                                    i.putExtra("source", "bag");
                                    context.startActivity(i);
                                    Toast.makeText(context, "deleted successfully :)", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "cannot delete :(", Toast.LENGTH_LONG).show();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }


                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override

                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }
    }


    public void populateColor(Spinner colorCombo) {
        array_spinner = context.getResources().getStringArray(R.array.comboColor);
        ArrayAdapter adapter = new ArrayAdapter(context,
                android.R.layout.simple_selectable_list_item, array_spinner);
        colorCombo.setAdapter(adapter);
    }


    private void newHorizontalLine(TableLayout tableLayout) {
        TableRow trgap = new TableRow(context);
        TableRow.LayoutParams tbgap = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tbgap.gravity = Gravity.CENTER;
        trgap.setLayoutParams(tbgap);

        View linebreak = new View(context);
        linebreak.setBackgroundResource(R.color.colorPrimaryDark);
        TableRow.LayoutParams lnbr = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1);
        lnbr.gravity = Gravity.CENTER;
        linebreak.setLayoutParams(lnbr);
        trgap.addView(linebreak);


        View linebreak2 = new View(context);
        linebreak2.setBackgroundResource(R.color.colorPrimaryDark);
        TableRow.LayoutParams lnbr2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1);
        lnbr2.gravity = Gravity.CENTER;
        linebreak2.setLayoutParams(lnbr2);
        trgap.addView(linebreak2);


        tableLayout.addView(trgap);

    }


    public void populateOrder(TableLayout tableLayout1, LinkedHashMap<String, Integer> finalColorMap) {

        tableLayout1.removeAllViews();
        int bigText = 20;
        int total = 0;
        for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {
            TableRow tr1s2 = new TableRow(context);
            TableRow.LayoutParams tb1cs2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            tb1cs2.gravity = Gravity.CENTER;
            tr1s2.setLayoutParams(tb1cs2);

            TextView colors2 = new TextView(context);
            colors2.setPadding(6, 6, 6, 6);
            colors2.setText(entry.getKey().toString());
            colors2.setTextSize(bigText);
            colors2.setSingleLine();
            TableRow.LayoutParams lncs2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            lncs2.gravity = Gravity.CENTER;
            colors2.setLayoutParams(lncs2);
            tr1s2.addView(colors2);


            total += entry.getValue();

            TextView qtys2 = new TextView(context);
            qtys2.setPadding(6, 6, 6, 6);
            qtys2.setText(entry.getValue().toString());
            qtys2.setTextSize(bigText);
            qtys2.setSingleLine();
            TableRow.LayoutParams lnqs2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            lnqs2.gravity = Gravity.CENTER;
            qtys2.setLayoutParams(lnqs2);
            tr1s2.addView(qtys2);

            tableLayout1.addView(tr1s2);
        }

        newHorizontalLine(tableLayout1);

        TableRow tr1s2 = new TableRow(context);
        TableRow.LayoutParams tb1cs2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tb1cs2.gravity = Gravity.CENTER;
        tr1s2.setLayoutParams(tb1cs2);

        TextView colors2 = new TextView(context);
        colors2.setPadding(6, 6, 6, 6);
        colors2.setText("Total: ");
        colors2.setTextSize(bigText);
        colors2.setSingleLine();
        TableRow.LayoutParams lncs2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lncs2.gravity = Gravity.RIGHT;
        colors2.setLayoutParams(lncs2);
        tr1s2.addView(colors2);


        TextView qtys2 = new TextView(context);
        qtys2.setPadding(6, 6, 6, 6);
        qtys2.setText(String.valueOf(total));
        qtys2.setTextSize(bigText);
        qtys2.setSingleLine();
        TableRow.LayoutParams lnqs2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lnqs2.gravity = Gravity.CENTER;
        qtys2.setLayoutParams(lnqs2);
        tr1s2.addView(qtys2);


        tableLayout1.addView(tr1s2);


    }


}
