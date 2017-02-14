package com.example.pujan.bag.printPackage;


import android.content.Intent;

import com.example.pujan.bag.R;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PrintDemo extends Activity {
	Button btnSearch;
	Button btnSendDraw;
	private static final int REQUEST_ENABLE_BT = 2;
	BluetoothService mService = null;
	BluetoothDevice con_dev = null;
	private static final int REQUEST_CONNECT_DEVICE = 1;  //��ȡ�豸��Ϣ

	ArrayList<PrintEntity> getData;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getData = (ArrayList<PrintEntity>) getIntent().getSerializableExtra("PrintValue");
		mService = new BluetoothService(this, mHandler);
		//�����������˳�����
		if( mService.isAvailable() == false ){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
		}

	}


	String alignMiddle(String input)
	{
		int length = (32- input.length())/2;

		String temp="";
		for(int i=0;i<length;i++)
		{
			temp+=" ";
		}
		return temp+input;

	}


    @Override
    public void onStart() {
    	super.onStart();

		if( mService.isBTopen() == false)
		{
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		try {
			btnSendDraw = (Button) this.findViewById(R.id.btn_test);
			btnSendDraw.setOnClickListener(new ClickEvent());
			btnSearch = (Button) this.findViewById(R.id.btnSearch);
			btnSearch.setOnClickListener(new ClickEvent());
			btnSendDraw.setEnabled(false);
		} catch (Exception ex) {
            Log.e("������Ϣ",ex.getMessage());
		}
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mService != null) 
			mService.stop();
		mService = null; 
	}
	
	class ClickEvent implements View.OnClickListener {
		public void onClick(View v) {
			if (v == btnSearch) {			
				Intent serverIntent = new Intent(PrintDemo.this,DeviceListActivity.class);
				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
			} else if (v == btnSendDraw) {
                String msg = "";
                String lang = getString(R.string.strLang);

				
            	byte[] cmd = new byte[3];
        	    cmd[0] = 0x1b;
        	    cmd[1] = 0x21;
            	if((lang.compareTo("en")) == 0){	
            		//cmd[2] |= 0x10;   //Bigger Text
					cmd[2] &= 0xEF;
            		mService.write(cmd);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					Date date = new Date();
					String sDate= sdf.format(date);

					msg+= alignMiddle("PBRS Enterprises")+"\n";

					msg+=alignMiddle("New Road, Kathmandu")+"\n";

					msg+= alignMiddle("Phone:+977-1-5589330")+"\n\n";
					msg+="               "+"Date : "+sDate+"\n\n";


            		cmd[2] &= 0xEF;
            		mService.write(cmd);

					/*

					msg+="Cus Name: "+getData.get(0).getCustomer_name()+"\n\n";
					msg+="SN| Products| Rate| Qty| T.Price\n";
					msg+="--------------------------------\n";
					for(int i=0;i<getData.size();i++)
					{
						int Tprice;
						Tprice=getData.get(i).getQuantity()*getData.get(i).getPrice();
						String SNspace= new String();
						String PROspace = new String();
						String RATspace = new String();
						String QTYspace = new String();
						String TOTspace = new String();
						         //Right Align Serial NO.
						int SnColLenght=2;
						int SnLength=String.valueOf(getData.get(i).getId()).length();
						for(int k=1;k<=(SnColLenght-SnLength);k++)
						{
							SNspace+=" ";
						}
								//Right Align Products
						int ProColLength=10;
						int prolength=getData.get(i).getProducts().length();
						for(int k=1;k<=(ProColLength-prolength);k++)
						{
							PROspace+=" ";
						}
								//Right Align Rate
						int RatColLength=6;
						int RatLength=String.valueOf(getData.get(i).getPrice()).length();
						for(int k=1;k<=(RatColLength-RatLength);k++)
						{
							RATspace+=" ";
						}
								//Right Align Qty
						int QtyColLength=5;
						int QtyLength=String.valueOf(getData.get(i).getQuantity()).length();
						for(int k=1;k<=(QtyColLength-QtyLength);k++)
						{
							QTYspace+=" ";
						}
								//Right Align Total
						int TotColLength=9;
						int TotLength=String.valueOf(Tprice).length();
						for(int k=1;k<=(TotColLength-TotLength);k++)
						{
							TOTspace+=" ";
						}
								//Stores above value in msg to print
						msg += SNspace+getData.get(i).getId()+PROspace+getData.get(i).getProducts()+RATspace+getData.get(i).getPrice()+QTYspace+getData.get(i).getQuantity()
								+TOTspace+Tprice+"\n";
					}
					msg+="--------------------------------\n";

					double Total=0;
					for(int i=0;i<getData.size();i++)
					{
						Total+=getData.get(i).getPrice()*getData.get(i).getQuantity();
					}
					int totlength = String.valueOf(Total).length();
					for(int j=1;j<=(32-totlength-7);j++)
					{
						msg+=" ";
					}
					msg+="Total"+": "+Total+"\n";

					double Vat=0;
					Vat=0.13*Total;
					int vatlength=String.valueOf(Vat).length();
					int diff=totlength-vatlength;
					for(int j=1;j<=(32-vatlength-9-diff);j++)
					{
						msg+=" ";
					}

					String vatspace=new String();
					for(int j=1;j<=diff;j++)
					{

						vatspace+=" ";
					}
					msg+="13% Vat"+": "+vatspace+Vat+"\n";

					double Gtotal=0;
					Gtotal=Total+Vat;
					int Gtotlength=String.valueOf(Gtotal).length();
					for(int j=1;j<=(32-Gtotlength-9);j++)
					{
						msg+=" ";
					}
					msg+="G.Total"+": "+Gtotal+"\n\n";
					msg+= alignMiddle("**Thank You**");

					mService.sendMessage(msg,"GBK");
					mService.sendMessage(" \n","GBK");



*/
            	}else if((lang.compareTo("ch")) == 0){
            		cmd[2] |= 0x10;
            		mService.write(cmd);           //��������ģʽ
        		    mService.sendMessage("��ϲ����\n", "GBK"); 
            		cmd[2] &= 0xEF;
            		mService.write(cmd);           //ȡ�����ߡ�����ģʽ
            		msg = "  ���Ѿ��ɹ��������������ǵ�������ӡ����\n\n"
            		+ "  ����˾��һ��רҵ�����з�����������������Ʊ�ݴ�ӡ��������ɨ���豸��һ��ĸ߿Ƽ���ҵ.\n\n";
            	    
            		mService.sendMessage(msg,"GBK");	
            	}
			}
		}
	}
	

    private final  Handler mHandler = new Handler() {
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
                	Log.d("��������","��������.....");
                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
                	Log.d("��������","�ȴ�����.....");
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
        case  REQUEST_CONNECT_DEVICE:
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
}
