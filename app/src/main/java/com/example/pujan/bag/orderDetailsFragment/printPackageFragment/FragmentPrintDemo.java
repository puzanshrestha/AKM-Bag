package com.example.pujan.bag.orderDetailsFragment.printPackageFragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.pujan.bag.ActionListActivity;
import com.example.pujan.bag.R;
import com.example.pujan.bag.VolleyFunctions;
import com.example.pujan.bag.database.DbHelper;
import com.example.pujan.bag.printPackage.PrintEntity;
import com.google.gson.Gson;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import static android.view.View.GONE;


public class FragmentPrintDemo extends DialogFragment implements VolleyFunctions.AsyncResponse {
    Button btnSearch;
    Button btnSendDraw;
    Button btnPendingBill;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;  //��ȡ�豸��Ϣ


    ArrayList<PrintEntity> getData;

    /**
     * Called when the activity is first created.
     */
    String customer_id = "";
    String customer_name = "";
    String address = "";
    String shop_number = "";
    String discount, source;
    int total = 0;

    public FragmentPrintDemo(ArrayList<PrintEntity> getData, String customer_id, String customer_name, String address, int total, int discount, String source) {
        this.getData = getData;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.discount = String.valueOf(discount);
        this.source = source;
        this.address = address;
        this.total = total;


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
        DbHelper dbh = new DbHelper(getContext());
        this.shop_number = dbh.getShop();
        View view = inflater.inflate(R.layout.main, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        mService = new BluetoothService(getContext(), mHandler);
        //�����������˳�����
        if (mService.isAvailable() == false) {
            Toast.makeText(getContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        try {
            btnSendDraw = (Button) view.findViewById(R.id.btn_test);
            btnSendDraw.setOnClickListener(new ClickEvent());
            btnSearch = (Button) view.findViewById(R.id.btnSearch);
            btnSearch.setOnClickListener(new ClickEvent());
            btnSendDraw.setEnabled(false);


            btnPendingBill = (Button) view.findViewById(R.id.pendingBillBtn);
            btnPendingBill.setOnClickListener(new ClickEvent());

            if (customer_id.equals("0")) {
                btnPendingBill.setVisibility(GONE);
            }
        } catch (Exception ex) {
            Log.e("������Ϣ", ex.getMessage());
        }


        return view;
    }

    public String getreceipt() {
        DbHelper dbh = new DbHelper(getContext());
        String Receipt_No = dbh.getReceipt().split(",")[0];

        return Receipt_No;
    }

    public String getdatereceipt() {
        DbHelper dbh = new DbHelper(getContext());
        String dateReceipt = dbh.getReceipt().split(",")[1];
        return dateReceipt;
    }

    public void setReceiptAll(String receipt_no, String dateToday, String Receipt_prev_no) {
        DbHelper dbh = new DbHelper(getContext());
        dbh.setReceipt(receipt_no, dateToday, Receipt_prev_no);
    }

    public String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String sDate = sdf.format(date);
        return sDate;
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
    public void onComplete(String output) {

        if (output.equals("addedPendingBill")) {
     /*
            Toast.makeText(getContext(), "Added To Pending Bill List", Toast.LENGTH_SHORT).show();
            String msg = "";
            String lang = getString(R.string.strLang);


            byte[] cmd = new byte[3];
            cmd[0] = 0x1b;
            cmd[1] = 0x21;
            if ((lang.compareTo("en")) == 0) {


                //cmd[2] |= 0x10;   //Bigger Text
                cmd[2] &= 0xEF;
                mService.write(cmd);

              *//*  msg += alignMiddle("PBRS Enterprises") + "\n";

                msg += alignMiddle("New Road, Kathmandu") + "\n";

                msg += alignMiddle("Phone:+977-1-5589330") + "\n\n";
                *//*

                msg += "               " + "Date : " + date() + "\n\n";
                msg +="Receipt No : " + getreceipt() + "\n";



                cmd[2] &= 0xEF;
                mService.write(cmd);


                msg += "Cus Name: " + customer_name + "\n\n";
                msg += "SN| Products| Rate| Qty| T.Price\n";
                msg += "--------------------------------\n";
                for (int i = 0; i < getData.size(); i++) {

                    LinkedHashMap<String, Integer> finalColorMap = getData.get(i).getColorQuantity();

                    int subQty = 0;
                    for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {
                        subQty += entry.getValue();
                    }
                    int Tprice;
                    Tprice = subQty * getData.get(i).getPrice();
                    String SNspace  = new String();
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
                    int QtyLength = String.valueOf(subQty).length();
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
                    msg += SNspace + (i + 1) + PROspace + getData.get(i).getProduct() + RATspace + getData.get(i).getPrice() + QTYspace + subQty
                            + TOTspace + Tprice + "\n";


                }
                msg += "--------------------------------\n";

                double Total = 0;
                for (int i = 0; i < getData.size(); i++) {
                    LinkedHashMap<String, Integer> finalColorMap = getData.get(i).getColorQuantity();

                    for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {

                        Total += getData.get(i).getPrice() * entry.getValue();
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


            } else if ((lang.compareTo("ch")) == 0) {
                cmd[2] |= 0x10;
                mService.write(cmd);           //��������ģʽ
                mService.sendMessage("��ϲ����\n", "GBK");
                cmd[2] &= 0xEF;
                mService.write(cmd);           //ȡ�����ߡ�����ģʽ
                msg = "  ���Ѿ��ɹ��������������ǵ�������ӡ����\n\n"
                        + "  ����˾��һ��רҵ�����з�����������������Ʊ�ݴ�ӡ��������ɨ���豸��һ��ĸ߿Ƽ���ҵ.\n\n";

                mService.sendMessage(msg, "GBK");

                System.out.println(msg);

            }*/
            Toast.makeText(getContext(), "Successfully added to Pending Bill List", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getContext(), ActionListActivity.class);
            startActivity(i);
        } else if (output.equals("updated")) {
            Toast.makeText(getContext(), "New Record has been inserted", Toast.LENGTH_SHORT).show();
            String msg = "";
            String lang = getString(R.string.strLang);


            byte[] cmd = new byte[3];
            cmd[0] = 0x1b;
            cmd[1] = 0x21;
            if ((lang.compareTo("en")) == 0) {


                //cmd[2] |= 0x10;   //Bigger Text
                cmd[2] &= 0xEF;
                mService.write(cmd);

              /*  msg += alignMiddle("PBRS Enterprises") + "\n";

                msg += alignMiddle("New Road, Kathmandu") + "\n";

                msg += alignMiddle("Phone:+977-1-5589330") + "\n\n";

                */
                msg += "               " + "Date : " + date() + "\n\n";
                msg += "Receipt No : " + getreceipt() + "\n";
                cmd[2] &= 0xEF;
                mService.write(cmd);


                msg += "Cus Name: " + customer_name + "\n\n";
                msg += "SN| Products| Rate| Qty| T.Price\n";
                msg += "--------------------------------\n";
                for (int i = 0; i < getData.size(); i++) {

                    LinkedHashMap<String, Integer> finalColorMap = getData.get(i).getColorQuantity();

                    int subQty = 0;
                    for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {
                        subQty += entry.getValue();
                    }
                    int Tprice;
                    Tprice = subQty * getData.get(i).getPrice();
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
                    int QtyLength = String.valueOf(subQty).length();
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
                    msg += SNspace + (i + 1) + PROspace + getData.get(i).getProduct() + RATspace + getData.get(i).getPrice() + QTYspace + subQty
                            + TOTspace + Tprice + "\n";


                }
                msg += "--------------------------------\n";

                double Total = 0;
                for (int i = 0; i < getData.size(); i++) {
                    LinkedHashMap<String, Integer> finalColorMap = getData.get(i).getColorQuantity();

                    for (LinkedHashMap.Entry<String, Integer> entry : finalColorMap.entrySet()) {

                        Total += getData.get(i).getPrice() * entry.getValue();
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


            } else if ((lang.compareTo("ch")) == 0) {
                cmd[2] |= 0x10;
                mService.write(cmd);           //��������ģʽ
                mService.sendMessage("��ϲ����\n", "GBK");
                cmd[2] &= 0xEF;
                mService.write(cmd);           //ȡ�����ߡ�����ģʽ
                msg = "  ���Ѿ��ɹ��������������ǵ�������ӡ����\n\n"
                        + "  ����˾��һ��רҵ�����з�����������������Ʊ�ݴ�ӡ��������ɨ���豸��һ��ĸ߿Ƽ���ҵ.\n\n";

                mService.sendMessage(msg, "GBK");

            }
            Intent i = new Intent(getContext(), ActionListActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(getContext(), "Failed to insert record", Toast.LENGTH_SHORT).show();
        }
    }

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {

            String receipt;

            if (v == btnSearch) {
                Intent serverIntent = new Intent(getContext(), FragmentDeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

            } else if (v == btnSendDraw) {

                if (getdatereceipt().equals(date())) {
                    setReceiptAll(String.valueOf(Integer.parseInt(getreceipt()) + 1), date(), getreceipt());
                } else {
                    setReceiptAll("1", date(), getreceipt());
                }
                receipt = getreceipt();
                Gson test = new Gson();
                String jsonData = test.toJson(getData);
                VolleyFunctions t = new VolleyFunctions(getContext());
                t.addOrder(jsonData, customer_id, customer_name, discount, source, shop_number, receipt);
                t.trigAsyncResponse(FragmentPrintDemo.this);


            } else if (v == btnPendingBill) {

                Gson test = new Gson();
                String jsonData = test.toJson(getData);
                VolleyFunctions t = new VolleyFunctions(getContext());
                t.addPendingBill(jsonData, customer_id, customer_name, String.valueOf(total), address, shop_number);
                t.trigAsyncResponse(FragmentPrintDemo.this);
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
                            Toast.makeText(getContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            btnSendDraw.setEnabled(true);
                            btnPendingBill.setEnabled(true);
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
                    Toast.makeText(getContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    btnSendDraw.setEnabled(false);
                    btnPendingBill.setEnabled(true);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(getContext(), "Unable to connect device",
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
                    Toast.makeText(getContext(), "Bluetooth open successful", Toast.LENGTH_LONG).show();
                } else {
                    getActivity().finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(FragmentDeviceListActivity.EXTRA_DEVICE_ADDRESS);
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
}
