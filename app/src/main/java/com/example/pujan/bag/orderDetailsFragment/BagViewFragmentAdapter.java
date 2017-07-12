package com.example.pujan.bag.orderDetailsFragment;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.bagDetails.BagColorQuantity;
import com.example.pujan.bag.bagDetails.BagEntity;
import com.example.pujan.bag.database.DbHelper;
import com.example.pujan.bag.printPackage.PrintEntity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Pujan on 1/3/2017.
 */
public class BagViewFragmentAdapter extends RecyclerView.Adapter<BagViewFragmentAdapter.TestHolder> implements VolleyFunctions.AsyncResponse {


    boolean show = false;
    int selected_position = 0;
    private ArrayList<BagEntity> listData;
    private LayoutInflater inflater;

    private String bag;
    private Context context;
    String ip = "";

    private RecyclerView recyclerView;
    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;


    ArrayList<PrintEntity> colorQuantities = new ArrayList<>();
    ArrayList<BagColorQuantity> stockList = new ArrayList<>();


    private ItemClickCallback itemClickCallback;


    public void setFilter(ArrayList<BagEntity> list) {
        listData = new ArrayList<>();
        listData.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public void onComplete(String output) {

        notifyDataSetChanged();
    }

    public interface ItemClickCallback {
        void onItemClick(ArrayList<PrintEntity> p);
    }





    public BagViewFragmentAdapter(ArrayList<BagEntity> listData, ArrayList<BagColorQuantity> stockList,Context c, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.listData = listData;
        this.inflater = LayoutInflater.from(c);
        this.context = c;
        this.stockList=stockList;


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
        View view = inflater.inflate(R.layout.fragment_activity_bag_list, parent, false);

        return new TestHolder(view);

    }


    @Override
    public void onBindViewHolder(final TestHolder holder, int position) {
        BagEntity item = listData.get(position);
        holder.id.setText((Integer.toString(item.getId())));
        holder.id.setVisibility(View.GONE);

        holder.name.setText(Html.fromHtml("<b><u>Product: " + item.getName() + "</u></b>"));
        holder.category.setText(Html.fromHtml("Category: " + item.getCategory()));
        holder.price.setText(Html.fromHtml("Price: " + Integer.toString(item.getPrice())));
        holder.company.setText(Html.fromHtml("Company: " + item.getCompany()));
        holder.quantity.setText(Html.fromHtml("Stock: " + Integer.toString(item.getQuantity())));


        holder.redStockQty.setText("0");
        holder.blackStockQty.setText("0");
        holder.brownStockQty.setText("0");
        holder.othersStockQty.setText("0");


        holder.bind();
        int stockTotal =0;
        for(int i=0;i<stockList.size();i++)
        {
            if(listData.get(position).getId()==stockList.get(i).getBag_id()) {

                for (LinkedHashMap.Entry<String, Integer> entry : stockList.get(i).getQuantityColor().entrySet()) {
                    stockTotal+=entry.getValue();
                    switch (entry.getKey()) {
                        case "RED":
                            holder.redStockQty.setText(entry.getValue().toString());
                            break;
                        case "BLACK":
                            holder.blackStockQty.setText(entry.getValue().toString());
                            break;
                        case "BROWN":
                            holder.brownStockQty.setText(entry.getValue().toString());
                            break;
                        case "OTHERS":
                            holder.othersStockQty.setText(entry.getValue().toString());
                            break;

                        default:
                            break;
                    }

                }

                break;
            }

        }

        holder.quantity.setText(Html.fromHtml("Stock: " + Integer.toString(stockTotal)));



        int hold = -1;
        for (int i = 0; i < colorQuantities.size(); i++) {
            if (listData.get(position).getId() == colorQuantities.get(i).getBag_id()) {

                hold = i;
                break;
            }

        }

        if (hold != -1) {
            holder.redEditTxt.setText("");
            holder.blackEditTxt.setText("");
            holder.othersEditText.setText("");
            holder.brownEditTxt.setText("");
            for (LinkedHashMap.Entry<String, Integer> entry : colorQuantities.get(hold).getColorQuantity().entrySet()) {
                switch (entry.getKey()) {
                    case "RED":
                        holder.redEditTxt.setText(entry.getValue().toString());
                        break;
                    case "BLACK":
                        holder.blackEditTxt.setText(entry.getValue().toString());
                        break;
                    case "BROWN":
                        holder.brownEditTxt.setText(entry.getValue().toString());
                        break;
                    case "OTHERS":
                        holder.othersEditText.setText(entry.getValue().toString());
                        break;

                    default:
                    break;
                }
            }



        } else {
            holder.redEditTxt.setText("");
            holder.blackEditTxt.setText("");
            holder.brownEditTxt.setText("");
            holder.othersEditText.setText("");
        }

        try {
            Picasso
                    .with(context)
                    .load("http://" + ip + "/bagWebServices/uploads/" + item.getPhoto())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(300, 300)
                    .placeholder(R.drawable.vector_drawable_bag)
                    .into(holder.photoBox);

        } catch (Exception e) {
            System.out.println("No file found");
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
        private View container;
        private LinearLayout orderContainer;
        private ImageView photoBox;
        private Button addOrder, clearAllOrder;
        private EditText redEditTxt, blackEditTxt, brownEditTxt, othersEditText;
        private TextView redStockQty,blackStockQty,brownStockQty,othersStockQty;

        public ExpandableLayout expandableLayout;

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
            context = itemView.getContext();


            addOrder = (Button) itemView.findViewById(R.id.addOrder);
            clearAllOrder = (Button) itemView.findViewById(R.id.clearOrder);

            orderContainer = (LinearLayout) itemView.findViewById(R.id.orderContainer);

            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandableLayout);
            expandableLayout.setInterpolator(new OvershootInterpolator());



            container.setOnClickListener(this);

            redEditTxt = (EditText) itemView.findViewById(R.id.redQty);
            blackEditTxt = (EditText) itemView.findViewById(R.id.blackQty);
            brownEditTxt = (EditText) itemView.findViewById(R.id.brownQty);
            othersEditText = (EditText) itemView.findViewById(R.id.othersQty);

            redStockQty = (TextView) itemView.findViewById(R.id.redStockQty);
            blackStockQty = (TextView) itemView.findViewById(R.id.blackStockQty);
            brownStockQty = (TextView) itemView.findViewById(R.id.brownStockQty);
            othersStockQty = (TextView) itemView.findViewById(R.id.othersStockQty);



            addOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    for (int i = 0; i < colorQuantities.size(); i++) {
                        if (colorQuantities.get(i).getBag_id() == listData.get(getAdapterPosition()).getId()) {
                            colorQuantities.remove(i);
                        }
                    }
                    PrintEntity bcq = new PrintEntity();
                    LinkedHashMap<String, Integer> colorQty = new LinkedHashMap<>();

                    if (!redEditTxt.getText().toString().replaceAll("\\s+", "").equals("")&!redEditTxt.getText().toString().replaceAll("\\s+", "").equals("0"))
                        colorQty.put("RED", Integer.parseInt(redEditTxt.getText().toString()));
                    if (!blackEditTxt.getText().toString().replaceAll("\\s+", "").equals("")&!blackEditTxt.getText().toString().replaceAll("\\s+", "").equals("0"))
                        colorQty.put("BLACK", Integer.parseInt(blackEditTxt.getText().toString()));
                    if (!brownEditTxt.getText().toString().replaceAll("\\s+", "").equals("")&!brownEditTxt.getText().toString().replaceAll("\\s+", "").equals("0"))
                        colorQty.put("BROWN", Integer.parseInt(brownEditTxt.getText().toString()));
                    if (!othersEditText.getText().toString().replaceAll("\\s+", "").equals("")&!othersEditText.getText().toString().replaceAll("\\s+", "").equals("0"))
                        colorQty.put("OTHERS", Integer.parseInt(othersEditText.getText().toString()));


                    if (!(redEditTxt.getText().toString().replaceAll("\\s+", "").equals("") & blackEditTxt.getText().toString().replaceAll("\\s+", "").equals("") & brownEditTxt.getText().toString().replaceAll("\\s+", "").equals("") & othersEditText.getText().toString().replaceAll("\\s+", "").equals(""))) {
                        //if((Integer.parseInt(redStockQty.getText().toString())>Integer.parseInt(redEditTxt.getText().toString()))&(Integer.parseInt(blackStockQty.getText().toString())>Integer.parseInt(blackEditTxt.getText().toString()))&(Integer.parseInt(brownStockQty.getText().toString())>Integer.parseInt(brownEditTxt.getText().toString()))&(Integer.parseInt(othersStockQty.getText().toString())>Integer.parseInt(othersEditText.getText().toString())))
                        if(isStockAvailable())
                        {
                            bcq.setBag_id(listData.get(getAdapterPosition()).getId());
                            bcq.setProduct(listData.get(getAdapterPosition()).getName());
                            bcq.setPrice(listData.get(getAdapterPosition()).getPrice());
                            bcq.setColorQuantity(colorQty);

                            colorQuantities.add(bcq);
                        }
                        else
                        {
                            Toast.makeText(context,"Stock Not Available",Toast.LENGTH_LONG).show();
                        }


                    }


                }


            });


        }

        private boolean isStockAvailable() {

            boolean result=true;
            if(!redEditTxt.getText().toString().equals(""))
            {
                if(Integer.parseInt(redStockQty.getText().toString())<Integer.parseInt(redEditTxt.getText().toString()))
                {
                    result=false;
                }
            }
            if(!blackEditTxt.getText().toString().equals(""))
            {
                if(Integer.parseInt(blackStockQty.getText().toString())<Integer.parseInt(blackEditTxt.getText().toString()))
                {
                    result=false;
                }
            }
            if(!brownEditTxt.getText().toString().equals(""))
            {
                if(Integer.parseInt(brownStockQty.getText().toString())<Integer.parseInt(brownEditTxt.getText().toString()))
                {
                    result=false;
                }
            }
            if(!othersEditText.getText().toString().equals(""))
            {
                if(Integer.parseInt(othersStockQty.getText().toString())<Integer.parseInt(othersEditText.getText().toString()))
                {
                    result=false;
                }
            }

            return result;

        }


        @Override
        public void onClick(View v) {


            final TestHolder holder = (TestHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (holder != null) {

                holder.expandableLayout.collapse();

            }

            if (selectedItem == getAdapterPosition()) {
                selectedItem = UNSELECTED;
            } else {
                expandableLayout.expand();
                selectedItem = getAdapterPosition();

                if (getAdapterPosition() > listData.size() - 3)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollBy(0, 500);
                        }
                    }, 200);

            }


        }


        public void bind() {

            expandableLayout.collapse();
        }
    }



    public void expandLayout(int scrollPosition) {


        try {

            TestHolder holderIn;
            holderIn = (TestHolder) recyclerView.findViewHolderForLayoutPosition(scrollPosition);
            holderIn.expandableLayout.expand();
            selectedItem = scrollPosition;
            recyclerView.smoothScrollToPosition(scrollPosition);
            if (scrollPosition > listData.size() - 3)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollBy(0, 500);
                    }
                }, 200);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public ArrayList<PrintEntity> getColorQuantity() {
        return colorQuantities;
    }


    public void setPendingData(ArrayList<PrintEntity> colorQuantities)
    {
        this.colorQuantities.clear();
        this.colorQuantities.addAll(colorQuantities);
        notifyDataSetChanged();
    }

}
