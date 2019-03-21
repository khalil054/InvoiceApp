package test.invoicegenerator.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.ClientAdapter;
import test.invoicegenerator.adapters.ReportAdapter;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.ClientModel;
import test.invoicegenerator.model.InvoiceModel;
import test.invoicegenerator.model.JsonInvoiceModel;
import test.invoicegenerator.model.SharedPref;

public class FragmentReport extends BaseFragment{
   /* @BindView(R.id.invoiceList)
    ListView report_list;*/
    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    @BindView(R.id.add_invoice)
    FloatingActionButton add_invoice;
    DBHelper db;
    @BindView(R.id.invoiceList)
    SwipeMenuListView listView;
    private ArrayList<JsonInvoiceModel> list=new ArrayList<>();
    private ArrayList<String> items;
    int DeletePosition = 0;
    int OpenPosition = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

     //   report_list = view.findViewById(R.id.invoiceList);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
        db=new DBHelper(getActivity());
        items=new ArrayList<>();
        GetInvoiceList();

      /*  ReportAdapter adapter = new ReportAdapter(getActivity(), list);
        listView.setAdapter(adapter);
*/




        /*getInvoiceList();

        report_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putString("new", "false");
                args.putString("clicked", "true");
                args.putSerializable("invoice",list.get(position));
                loadFragment(new FragmentReportDetail(),args);
            }
        });*/
    }

   /* private void getInvoiceList() {
        {
            Cursor rs = db.getInvoiceData();
            rs.moveToFirst();

            items.clear();
            list.clear();
            while (!rs.isAfterLast()) {
                InvoiceModel value_item=new InvoiceModel();
                String id = rs.getString(rs.getColumnIndex("id"));
                String client_key=rs.getString(rs.getColumnIndex("client_key"));
                String subtotal = rs.getString(rs.getColumnIndex("subtotal"));
                String discount = rs.getString(rs.getColumnIndex("discount"));
                String dis_type = rs.getString(rs.getColumnIndex("dis_type"));
                String tax_rate = rs.getString(rs.getColumnIndex("tax_rate"));
                String tax_type = rs.getString(rs.getColumnIndex("tax_type"));
                String total_value = rs.getString(rs.getColumnIndex("total_value"));
                String comment = rs.getString(rs.getColumnIndex("comment"));
                String invoice_name = rs.getString(rs.getColumnIndex("invoice_name"));
                String due_date = rs.getString(rs.getColumnIndex("due_date"));
                String invoice_date = rs.getString(rs.getColumnIndex("invoice_date"));
                String attachment = rs.getString(rs.getColumnIndex("photo"));
                String signatue = rs.getString(rs.getColumnIndex("signature"));
                String signature_date = rs.getString(rs.getColumnIndex("signature_date"));

                value_item.setId(id);
                value_item.setClient_key(client_key);
                // value_item.setItem_key(item_key);
                value_item.setSubtotal(subtotal);
                value_item.setDiscount(discount);
                value_item.setDis_type(dis_type);
                value_item.setTax_rate(tax_rate);
                value_item.setTax_type(tax_type);
                value_item.setTotal_value(total_value);
                value_item.setComment(comment);
                value_item.setInvoice_name(invoice_name);
                value_item.setDue_date(due_date);
                value_item.setPhoto(attachment);
                value_item.setSignture_date(signature_date);
                value_item.setSignature(signatue);
                value_item.setInvoice_date(invoice_date);
                list.add(value_item);
                rs.moveToNext();
            }


        }
        for(int i=0;i<list.size();i++)
        {
            items.add(list.get(i).getInvoice_name()+"");
        }
    }*/

    public void GetInvoiceList()
    {

        showProgressBar();
        initVolleyCallbackForInvoiceList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        String Str=NetworkURLs.BaseURL+ NetworkURLs.GetInvoiceList;
        mVolleyService.getDataVolley("GETCALL", Str);

    }

    void initVolleyCallbackForInvoiceList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray InvoiceArray = data.getJSONArray("invoices");

                        for (int i = 0; i < InvoiceArray.length(); i++) {
                            JsonInvoiceModel invoiceModel = new JsonInvoiceModel(InvoiceArray.getJSONObject(i));
                            list.add(invoiceModel);
                        }
                        ReportAdapter adapter = new ReportAdapter(getActivity(), list);
                        listView.setAdapter(adapter);

                    }else {
                        Toast.makeText(getActivity(), "Status found false", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressBar();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
               hideProgressBar();
                if(error.networkResponse != null && error.networkResponse.data != null) {

                    String error_response = new String(error.networkResponse.data);
                    // dialogHelper.showErroDialog(error_response);
                    Toast.makeText(getActivity(), String.valueOf("Error" + error_response), Toast.LENGTH_SHORT).show();

                    SharedPref.init(getActivity());
                    String access_token = SharedPref.read(Constants.ACCESS_TOKEN, "");
                    String client = SharedPref.read(Constants.CLIENT, "");
                    String uid = SharedPref.read(Constants.UID, "");

                    Toast.makeText(getActivity(), String.valueOf("Error" + error_response + "\n" + access_token + "\n" + client + "\n" + uid), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage(String.valueOf("Error" + error_response + "\n" + access_token + "\n" + client + "\n" + uid));
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.create().show();


                    try {
                        JSONObject response_obj = new JSONObject(error_response);

                        {
                            JSONObject error_obj = response_obj.getJSONObject("error");
                            String message = error_obj.getString("message");

                            Toast.makeText(getActivity(), String.valueOf("Error" + message), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), String.valueOf("Error not responding" ), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }

        };
    }

}
