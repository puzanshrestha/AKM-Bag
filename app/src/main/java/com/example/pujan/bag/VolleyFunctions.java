package com.example.pujan.bag;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pujan.bag.bagDetails.AddBagActivity;
import com.example.pujan.bag.bagDetails.BagDetailsActivity;
import com.example.pujan.bag.bagDetails.BagListActivity;
import com.example.pujan.bag.bagDetails.vendorSelectFragment.VendorSelectFragment;
import com.example.pujan.bag.bagStock.StockDetailsActivity;
import com.example.pujan.bag.bagStock.StockListActivity;
import com.example.pujan.bag.customerDetails.AddCustomerActivity;
import com.example.pujan.bag.customerDetails.CustomerDetailsActivity;
import com.example.pujan.bag.customerDetails.CustomerListActivity;
import com.example.pujan.bag.database.DbHelper;
import com.example.pujan.bag.orderDetailsFragment.BagListFragment;
import com.example.pujan.bag.orderDetailsFragment.OrderActivity;
import com.example.pujan.bag.orderDetailsFragment.customerSelectFragment.SelectCustomerFragment;
import com.example.pujan.bag.orderDetailsFragment.printPackageFragment.FragmentPrintDemo;
import com.example.pujan.bag.pendingBill.pendingBillList;
import com.example.pujan.bag.pendingBill.PendingBillListFragment;
import com.example.pujan.bag.printPackage.PrintDemo;
import com.example.pujan.bag.transactionalReports.BagReports;
import com.example.pujan.bag.transactionalReports.RePrintBill;
import com.example.pujan.bag.vendorDetails.AddVendorActivity;
import com.example.pujan.bag.vendorDetails.VendorDetailsActivity;
import com.example.pujan.bag.vendorDetails.VendorListActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by puzan on 06-Jun-17.
 */

public class VolleyFunctions {
    Context c;
    String ip;

    String responseRet;

    public VolleyFunctions(Context c) {
        this.c = c;
        DbHelper db = new DbHelper(c);

        try {
            ip = "http://" + db.getIP() + "/BagWebServices/";
        } catch (Exception e) {
        } finally {
            db.close();
        }

    }

    private AsyncResponse callback = null;

    public interface AsyncResponse {
        void onComplete(String output);
    }

    public void trigAsyncResponse(BagListActivity activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(BagListFragment activity) {
        this.callback = activity;
    }
    public void trigAsyncResponse(VendorSelectFragment activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(CustomerListActivity activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(VendorListActivity activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(AddBagActivity activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(AddCustomerActivity activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(AddVendorActivity activity) {
        this.callback = activity;
    }
    public void trigAsyncResponse(VendorDetailsActivity activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(BagReports activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(StockListActivity activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(CustomerDetailsActivity activity) {
        this.callback = activity;
    }
    public void trigAsyncResponse(SelectCustomerFragment activity) {
        this.callback = activity;
    }


    public void trigAsyncResponse(FragmentPrintDemo activity) {
        this.callback = activity;
    }
    public void trigAsyncResponse(OrderActivity activity) {
        this.callback = activity;
    }
    public void trigAsyncResponse(PendingBillListFragment activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(StockDetailsActivity activity) {
        this.callback = activity;
    }



    public void trigAsyncResponse(MainActivity activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(PrintDemo activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(pendingBillList activity) {
        this.callback = activity;
    }

    public void trigAsyncResponse(RePrintBill activity) {
        this.callback = activity;
    }


    public void trigAsyncResponse(BagDetailsActivity activity) {
        this.callback = activity;
    }


    public void login(final String username, final String password, final String shop_number) {
        System.out.println("This is inside classsss" + ip);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();

                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        callback.onComplete("Network Error");
                        System.out.println("ERROR HAS BEEN OCCURED");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("username", username);
                params.put("password", password);
                params.put("shop_number", shop_number);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }


    public void addBag(final String name, final String type, final String price, final String company, final String source, final String bid, final String ext, final String vendor_id) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "addBag.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        System.out.println(response);
                        System.out.println("THIS IS RESPONSE======================== " + response);
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("bag_name", name);
                params.put("bag_category", type);
                params.put("bag_price", price);
                params.put("bag_company", company);
                params.put("source", source);
                params.put("bid", bid);
                params.put("ext", ext);
                params.put("vendor_id", vendor_id);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void addCustomer(final String customerName, final String customerAddress, final String customerPhone, final String customerSource, final String cid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "addCustomer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        System.out.println(response);
                        System.out.println("THIS IS RESPONSE======================== " + response);
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("customer_name", customerName);
                params.put("customer_address", customerAddress);
                params.put("customer_phone", customerPhone);
                params.put("customer_source", customerSource);
                params.put("customer_id", cid);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void addVendor(final String vendorName, final String vendorAddress, final String source, final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "addVendor.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("vendor_name", vendorName);
                params.put("vendor_address", vendorAddress);
                params.put("source", source);
                params.put("id", id);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }


    public void viewBag(final String offset, final String difference) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "viewBagWithStock.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        callback.onComplete("ERROR");
                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("offset", offset);
                params.put("difference", difference);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }


    public void viewCustomer() {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "viewCustomer.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server

                            try {
                                responseRet = response.trim().toString();
                                callback.onComplete(responseRet);
                            }catch (Exception e)
                            {
                                System.out.println("error");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            try {

                                callback.onComplete("ERROR");
                                responseRet = error.toString();
                                System.out.println("ERROR HAS BEEN OCCURERD");

                            }
                            catch (Exception e)
                            {
                                System.out.println("ERror");
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request


                    //returning parameter
                    return params;
                }
            };

            //Adding the string request to the queue
            RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
            requestQueue.add(stringRequest);




    }

    public void viewVendor() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "viewVendor.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        callback.onComplete("ERROR");
                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void addOrderTemp(final String customer_id, final String bag_id_code, final String source) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "addOrderTemp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("customer_id", customer_id);
                params.put("source", source);
                params.put("bag_id", bag_id_code);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }


    public void addOrder(final String orderJson, final String customer_id, final String customer_name, final String discount, final String source, final String shop_number,final String receipt) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "addOrder.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("AddOrderJson", orderJson);
                params.put("customer_id", customer_id);
                params.put("customer_name", customer_name);
                params.put("source", source);
                params.put("discount", discount);
                params.put("shop_number", shop_number);
                params.put("receipt_no",receipt);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void cancelPendingBill(final String pId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "cancelPendingBill.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("pId", pId);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void reprintBill(final String orderId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "rePrintBill.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("order_id", orderId);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void addPendingBill(final String orderJson, final String customer_id, final String customer_name, final String total, final String address, final String shop_number) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "addPendingBill.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("AddOrderJson", orderJson);
                params.put("customer_id", customer_id);
                params.put("customer_name", customer_name);
                params.put("address", address);
                params.put("total", total);
                params.put("shop_number", shop_number);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void queryPendingBill(final String pId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "queryPendingBill.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("pId", pId);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void queryPendingBillList(final String shop_number) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "queryPendingBillList.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        callback.onComplete("ERROR");
                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                    params.put("shop_number",shop_number);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void viewRecords(final String dateTo, final String dateFrom, final String shop_number) {

        System.out.println(shop_number+"is the shop number");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "viewRecords.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("dateTo", dateTo);
                params.put("dateFrom", dateFrom);
                params.put("shop_number",shop_number);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void viewStockInformation(final String bag_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "viewStockInformation.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        callback.onComplete("ERROR");
                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("bag_id", bag_id);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void updateStockInformation(final String bag_id, final String color, final String quantity) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "updateStockInformation.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("bag_id", bag_id);
                params.put("color", color);
                params.put("quantity", quantity);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void editStockInformation(final String bag_id, final String stockEditJson) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "editStockInformation.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("bag_id", bag_id);
                params.put("stockEditJson", stockEditJson);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }

    public void queryQuantity(final String quantity) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip + "queryTable.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        responseRet = response.trim().toString();
                        callback.onComplete(responseRet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        responseRet = error.toString();
                        System.out.println("ERROR HAS BEEN OCCURERD");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put("quantity", quantity);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }
}
