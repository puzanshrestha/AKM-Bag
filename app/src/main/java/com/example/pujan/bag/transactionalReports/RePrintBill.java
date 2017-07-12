package com.example.pujan.bag.transactionalReports;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.printPackage.DeviceListActivity;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class RePrintBill extends Activity implements VolleyFunctions.AsyncResponse {
    Button btnSearch;
    Button btnSendDraw,pendingBillBtn;

    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;  //��ȡ�豸��Ϣ


    ArrayList<RePrintEntity> getData;

    /**
     * Called when the activity is first created.
     */
    String customer_id = "";
    String customer_name = "";
    String discount,source;
    String order_id="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        order_id= getIntent().getStringExtra("id");
        VolleyFunctions t = new VolleyFunctions(this);
        t.reprintBill(order_id);
        t.trigAsyncResponse(this);




        mService = new BluetoothService(this, mHandler);
        //�����������˳�����
        if (mService.isAvailable() == false) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }

    }


    String alignMiddle(String input) {
        int length = (32 - input.length()) / 2;

        String temp = "";
        for (int i = 0; i < length; i++) {
            temp += " ";
        }
        return temp + input;

    }


    @Override
    public void onStart() {
        super.onStart();

        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        try {
            btnSendDraw = (Button) this.findViewById(R.id.btn_test);
            btnSendDraw.setOnClickListener(new ClickEvent());
            btnSearch = (Button) this.findViewById(R.id.btnSearch);
            btnSearch.setOnClickListener(new ClickEvent());
            btnSendDraw.setEnabled(false);
            pendingBillBtn = (Button)findViewById(R.id.pendingBillBtn);
            pendingBillBtn.setVisibility(View.GONE);

        } catch (Exception ex) {
            Log.e("������Ϣ", ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }

    @Override
    public void onComplete(String output) {

        convertJson(output);


    }

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnSearch) {
                Intent serverIntent = new Intent(RePrintBill.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                print();



           } else if (v == btnSendDraw) {
                print();

                }

            }
        }



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            btnSendDraw.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d("��������", "��������.....");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d("��������", "�ȴ�����.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    btnSendDraw.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    con_dev = mService.getDevByMac(address);

                    mService.connect(con_dev);
                }
                break;
        }
    }

    //��ӡͼ��
    @SuppressLint("SdCardPath")
    private void printImage() {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(384);
        pg.initPaint();
        pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        sendData = pg.printDraw();
        mService.write(sendData);
    }

    String getSpace(int colLen, int fieldLen) {
        String space = "";

        for (int k = 1; k <= (colLen - fieldLen); k++) {
            space += " ";
        }
        return space;
    }




    void convertJson(String response) {

        getData= new ArrayList<>();

        try {
            JSONObject bagQuantityColor = new JSONObject(response);
            JSONArray bag = bagQuantityColor.getJSONArray("result");

            JSONObject jObject2 = null;
            int qty=0;

            RePrintEntity printentity = new RePrintEntity();
            for (int i = 0; i < bag.length(); i++) {
                JSONObject jObject = bag.getJSONObject(i);

                if (i + 1 < bag.length()) {
                    jObject2 = bag.getJSONObject(i + 1);

                }
                else
                    jObject2=jObject;
                if ((jObject2.getString("bag_name")).equals(jObject.getString("bag_name"))) {

                    //bcqEntity.setQuantityColor(cqe);
                    qty+=Integer.parseInt(jObject.getString("quantity"));

                } else {

                    qty+=Integer.parseInt(jObject.getString("quantity"));
                    printentity.setCustomer_name(jObject.getString("customer_name"));
                    discount=(jObject.getString("discount"));
                    printentity.setProduct(jObject.getString("bag_name"));
                    printentity.setPrice(Integer.parseInt(jObject.getString("bag_price")));
                    printentity.setColor(jObject.getString("bag_color"));
                    printentity.setQuantity(qty);
                    printentity.setDate(jObject.getString("date"));


                    getData.add(printentity);
                    printentity = new RePrintEntity();

                    qty=0;
                }




            }
            JSONObject jObject = bag.getJSONObject(bag.length()-1);
            printentity.setCustomer_name(jObject.getString("customer_name"));
            discount=(jObject.getString("discount"));
            printentity.setProduct(jObject.getString("bag_name"));
            printentity.setPrice(Integer.parseInt(jObject.getString("bag_price")));
            printentity.setColor(jObject.getString("bag_color"));
            printentity.setQuantity(qty);
            printentity.setDate(jObject.getString("date"));


            getData.add(printentity);



        } catch (JSONException e) {
            e.printStackTrace();
        }













    }


    void print()
    {

        String msg = "";
        String lang = getString(R.string.strLang);


        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;
        if ((lang.compareTo("en")) == 0) {


            //cmd[2] |= 0x10;   //Bigger Text
            cmd[2] &= 0xEF;
            mService.write(cmd);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            String sDate = sdf.format(date);

            msg += alignMiddle("PBRS Enterprises") + "\n";

            msg += alignMiddle("New Road, Kathmandu") + "\n";

            msg += alignMiddle("Phone:+977-1-5589330") + "\n\n";
            msg += "               " + "Date : " + sDate + "\n\n";


            cmd[2] &= 0xEF;
            mService.write(cmd);


            msg += "Cus Name: " + getData.get(0).getCustomer_name() + "\n\n";
            msg += "SN| Products| Rate| Qty| T.Price\n";
            msg += "--------------------------------\n";
            for (int i = 0; i < getData.size(); i++) {

                int subQty = 0;

                int Tprice;
                Tprice = getData.get(i).getQuantity() * getData.get(i).getPrice();

                String SNspace = new String();
                String PROspace = new String();
                String RATspace = new String();
                String QTYspace = new String();
                String TOTspace = new String();
                //Right Align Serial NO.
                int SnColLenght = 2;
                int SnLength = String.valueOf(i + 1).length();
                for (int k = 1; k <= (SnColLenght - SnLength); k++) {
                    SNspace += " ";
                }
                //Right Align Products
                int ProColLength = 10;
                int prolength = getData.get(i).getProduct().length();
                for (int k = 1; k <= (ProColLength - prolength); k++) {
                    PROspace += " ";
                }
                //Right Align Rate
                int RatColLength = 6;
                int RatLength = String.valueOf(getData.get(i).getPrice()).length();
                for (int k = 1; k <= (RatColLength - RatLength); k++) {
                    RATspace += " ";
                }
                //Right Align Qty
                int QtyColLength = 5;
                int QtyLength = String.valueOf(getData.get(i).getQuantity()).length();
                for (int k = 1; k <= (QtyColLength - QtyLength); k++) {
                    QTYspace += " ";
                }
                //Right Align Total
                int TotColLength = 9;
                int TotLength = String.valueOf(Tprice).length();
                for (int k = 1; k <= (TotColLength - TotLength); k++) {
                    TOTspace += " ";
                }
                //Stores above value in msg to print
                msg += SNspace + (i + 1) + PROspace + getData.get(i).getProduct() + RATspace + getData.get(i).getPrice() + QTYspace + getData.get(i).getQuantity()
                        + TOTspace + Tprice + "\n";


            }
            msg += "--------------------------------\n";

            double Total = 0;
            for (int i = 0; i < getData.size(); i++) {
                {

                    Total += getData.get(i).getPrice() * getData.get(i).getQuantity();
                }

            }

            DecimalFormat df = new DecimalFormat("#");

            String totalText = "Total: Rs ";
            msg += getSpace(26, totalText.length()) + totalText;
            String totalValue = df.format(Total);
            msg += getSpace(6, String.valueOf(totalValue).length()) + totalValue + "\n";

            Double discountValue = Double.valueOf(discount);// / 100 * Total;
            String discountText = "Discount Amount: Rs ";
            msg += getSpace(26, discountText.length()) + discountText;
            String discountValueText = String.valueOf(df.format(discountValue));
            msg += getSpace(6, discountValueText.length()) + discountValueText + "\n";


            String gTotalText = "G.Total: Rs ";
            String gTotalValue = String.valueOf(df.format(Total - discountValue));
            msg += getSpace(26, gTotalText.length()) + gTotalText;
            msg += getSpace(6, gTotalValue.length()) + gTotalValue + "\n";


            msg += getSpace(32, 0) + "\n";
            msg += "\n" + alignMiddle("**Thank You**");

            mService.sendMessage(msg, "GBK");
            mService.sendMessage(" \n", "GBK");
            System.out.println(msg);




        }
    }
}
