package com.example.pujan.bag.customerDetails;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pujan.bag.FunctionsThread;
import com.example.pujan.bag.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pujan on 1/4/2017.
 */
public class CustomerViewAdapter extends RecyclerView.Adapter<CustomerViewAdapter.TestHolder> {

    ArrayList<CustomerEntity> listData;
    LayoutInflater layoutInflater;
    String method;
    int a = 0;
    private int cust_id=-1;
    Context context;
    private static final int CAMERA_REQUEST = 1888;



    private ItemClickCallback itemClickCallback;
    public void setFilter(ArrayList<CustomerEntity> list)
    {
        listData=new ArrayList<>();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    public interface ItemClickCallback {
        void onItemClick(int p);


    }

    void onItemClickCallback(ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;

    }

    public CustomerViewAdapter(ArrayList<CustomerEntity> listData, String method, Context c) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(c);
        this.method = method;
        this.context = c;


    }

    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.activity_customer_list, parent, false);

        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(TestHolder holder, int position) {

        CustomerEntity item = listData.get(position);
        holder.id.setText(Integer.toString(item.getId()));
        holder.name.setText(item.getName());
        holder.address.setText(item.getAddress());
        holder.phone.setText(item.getPhone());
        holder.edit.setVisibility(View.INVISIBLE);
        holder.delete.setVisibility(View.INVISIBLE);
        if (cust_id!= position) {
            holder.edit.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);
            holder.checkBox.setChecked(false);
        } else {
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class TestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView id, name, address, phone;
        private CheckBox checkBox;
        private View container;
        private Button edit, delete,snap;
        private  ImageView imageview;



        public TestHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.lbl_cust_id);
            name = (TextView) itemView.findViewById(R.id.lbl_cust_name);
            address = (TextView) itemView.findViewById(R.id.lbl_cust_address);
            phone = (TextView) itemView.findViewById(R.id.lbl_cust_phone);
            container = itemView.findViewById(R.id.cont_cust_item);
            edit = (Button) itemView.findViewById(R.id.cust_btn_edit);
            delete = (Button) itemView.findViewById(R.id.cust_btn_delete);
            checkBox = (CheckBox) itemView.findViewById(R.id.cust_checkbox);
            snap=(Button)itemView.findViewById(R.id.snap);
            //imageview=(ImageView)itemView.findViewById(R.id.imageView);
           // phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.call, 0, 0, 0);



            container.setOnClickListener(this);

            checkBox.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
//            snap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST,  -1);
//                }
//
//                private void startActivityForResult(Intent data, int requestCode, int resultCode) {
//                    if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//                        Bitmap photo = (Bitmap) data.getExtras().get("data");
//                        imageview.setImageBitmap(photo);
//                    }
//                }
//            });
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    CustomerEntity customerEntity= listData.get(position);
                    String phone = customerEntity.getPhone();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    context.startActivity(intent);
                }
            });

            if (method.equals("customerview")){
                checkBox.setVisibility(View.VISIBLE);

                checkBox.setOnClickListener(this);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(context,AddCustomerActivity.class);
                        i.putExtra("source","update");
                        int position=getAdapterPosition();
                        CustomerEntity customerEntity= listData.get(position);
                        i.putExtra("cust_id", Integer.toString(customerEntity.getId()));
                        i.putExtra("cust_name",customerEntity.getName());
                        i.putExtra("cust_address",customerEntity.getAddress());
                        i.putExtra("cust_phone",customerEntity.getPhone());
                        context.startActivity(i);

                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setMessage("Are you sure,You wanted to Delete");
                                    alertDialogBuilder.setPositiveButton("yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    String a="";
                                                    int position=getAdapterPosition();
                                                    CustomerEntity customerEntity= listData.get(position);
                                                    String cid= Integer.toString(customerEntity.getId());
                                                    final String check;
                                                    try {
                                                        check = new FunctionsThread(context).execute("AddCustomer", a, a, a, "delete", cid).get();

                                                        if (check.equals("Delete")) {
                                                            Toast.makeText(context, "Successfully Deleted Customer", Toast.LENGTH_SHORT).show();
                                                            Intent i = new Intent(context, CustomerListActivity.class);
                                                            i.putExtra("method", "customerview");
                                                            context.startActivity(i);
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(context,"Failed Deleting the Customer",Toast.LENGTH_SHORT).show();
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
                });
            }
            else if (method.equals("orderView")){
                container.setOnClickListener(this);


            }


        }



        @Override
        public void onClick(View v) {
                itemClickCallback.onItemClick(getAdapterPosition());

            if (v.getId()==R.id.cust_checkbox){

                if (checkBox.isChecked()){
                    cust_id=getAdapterPosition();
                    a=getAdapterPosition();
                    System.out.println(getAdapterPosition());
                    System.out.println(listData.get(getAdapterPosition()).getId());
                    edit.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();

                }
                else{
                    cust_id=-1;
                    edit.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                }

            }




        }
    }


}
