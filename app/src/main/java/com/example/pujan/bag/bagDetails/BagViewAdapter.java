package com.example.pujan.bag.bagDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.NDSpinner;
import com.example.pujan.bag.R;
import com.example.pujan.bag.database.DbHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/3/2017.
 */
public class BagViewAdapter extends RecyclerView.Adapter<BagViewAdapter.TestHolder> {

    private ArrayList<BagEntity> listData;
    private LayoutInflater inflater;
    private int bag_id[];
    private int bag_qty[];
    private int id_bag = -1;
    private String bag;
    private Context context;
    String ip = "";

    String[] array_spinner;


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

        bag_id = new int[listData.size()];
        bag_qty = new int[listData.size()];
        for (int i = 0; i < listData.size(); i++) {
            bag_id[i] = 0;
            bag_qty[i] = 0;
        }

        DbHelper db = new DbHelper(context);
        try {
            ip = db.getIP();
        } catch (Exception e) {
        } finally {
            db.close();
        }

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
        holder.bagQtyTxt.setVisibility(View.INVISIBLE);
        holder.bagDown.setVisibility(View.INVISIBLE);
        holder.bagUp.setVisibility(View.INVISIBLE);
        holder.bagQtyTxt.setText((Integer.toString(bag_qty[position])));

        holder.colorOption.setVisibility(View.INVISIBLE);
        holder.optionsMenu.setVisibility(View.VISIBLE);
        holder.bagCheck.setVisibility(View.INVISIBLE);


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

            holder.bagCheck.setVisibility(View.VISIBLE);
            holder.optionsMenu.setVisibility(View.INVISIBLE);


            if (bag_id[position] == 0) {
                holder.bagCheck.setChecked(false);
                holder.bagUp.setVisibility(View.INVISIBLE);
                holder.bagDown.setVisibility(View.INVISIBLE);
                holder.bagQtyTxt.setVisibility(View.INVISIBLE);
                holder.colorOption.setVisibility(View.INVISIBLE);


            } else {
                holder.bagCheck.setChecked(true);
                holder.bagUp.setVisibility(View.VISIBLE);
                holder.bagDown.setVisibility(View.VISIBLE);
                holder.bagQtyTxt.setVisibility(View.VISIBLE);
                holder.colorOption.setVisibility(View.VISIBLE);
            }
        }

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
        private Button bagUp, bagDown;
        private TextView bagQtyTxt;
        private View container;
        private CheckBox bagCheck;
        private ImageView photoBox;
        private NDSpinner colorOption;
        private TextView optionsMenu;


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
            bagUp = (Button) itemView.findViewById(R.id.bag_btn_up);
            bagDown = (Button) itemView.findViewById(R.id.bag_btn_down);
            bagQtyTxt = (TextView) itemView.findViewById(R.id.bag_qty_txt);
            bagCheck = (CheckBox) itemView.findViewById(R.id.bag_checkbox);

            optionsMenu = (TextView) itemView.findViewById(R.id.optionsMenu);
            colorOption = (NDSpinner) itemView.findViewById(R.id.colorOption);
            context = itemView.getContext();


            bagUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int qty = Integer.parseInt(bagQtyTxt.getText().toString());
                    qty++;

                    bag_qty[getAdapterPosition()] = qty;
                    bagQtyTxt.setText(Integer.toString(qty));


                }
            });

            bagDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int qty = Integer.parseInt(bagQtyTxt.getText().toString());
                    qty--;
                    if (qty < 0)
                        qty = 0;
                    bag_qty[getAdapterPosition()] = qty;
                    bagQtyTxt.setText(Integer.toString(qty));

                }

            });


            bagCheck.setOnClickListener(this);
            container.setOnClickListener(this);

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

            if (bag.equals("customer")) {

                array_spinner = context.getResources().getStringArray(R.array.comboColor);
                ArrayAdapter adapter = new ArrayAdapter(context,
                        android.R.layout.simple_selectable_list_item, array_spinner);
                colorOption.setAdapter(adapter);
                colorOption.setSelection(Adapter.NO_SELECTION,false);


                colorOption.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        array_spinner[0]="VIOLET";
                        ArrayAdapter adapter = new ArrayAdapter(context,
                                android.R.layout.simple_selectable_list_item, array_spinner);
                        colorOption.setAdapter(adapter);
                        colorOption.setSelection(Adapter.NO_SELECTION,false);
                        return false;
                    }
                });


                colorOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(context, colorOption.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {


                    }
                });
            }


        }

        @Override
        public void onClick(View v) {
            itemClickCallback.onItemClick(getAdapterPosition());

            if (v.getId() == R.id.bag_checkbox) {
                if (bagCheck.isChecked()) {
                    bag_id[getAdapterPosition()] = listData.get(getAdapterPosition()).getId();
                    bagUp.setVisibility(View.VISIBLE);
                    bagDown.setVisibility(View.VISIBLE);
                    bagQtyTxt.setVisibility(View.VISIBLE);
                    colorOption.setVisibility(View.VISIBLE);
                } else {
                    bag_id[getAdapterPosition()] = 0;
                    array_spinner[0]="SELECT COLOR";
                    ArrayAdapter adapter = new ArrayAdapter(context,
                            android.R.layout.simple_selectable_list_item, array_spinner);
                    colorOption.setAdapter(adapter);
                    colorOption.setSelection(Adapter.NO_SELECTION,false);
                    bagUp.setVisibility(View.INVISIBLE);
                    bagDown.setVisibility(View.INVISIBLE);
                    bagQtyTxt.setVisibility(View.INVISIBLE);
                    colorOption.setVisibility(View.INVISIBLE);
                }


                bagQtyTxt.setText("0");

            }

        }


    }

    public RecValue getRecValues() {
        RecValue recValue = new RecValue();
        recValue.setBag_id(bag_id);
        recValue.setQuantity(bag_qty);
        return recValue;
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


}
