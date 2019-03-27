package test.invoicegenerator.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
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
import test.invoicegenerator.adapters.ReportAdapter;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.JsonInvoiceModel;

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
    private ArrayList<JsonInvoiceModel> Invoicelist=new ArrayList<>();
    private ArrayList<String> items;
    int DeletePosition = 0;
    int OpenPosition = 0;
    SearchView searchView;
    ReportAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        searchView = (SearchView) view.findViewById(R.id.searchView);
     //   report_list = view.findViewById(R.id.invoiceList);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {

        db=new DBHelper(getActivity());
        items=new ArrayList<>();
        GetInvoiceList();

        searchView.setQueryHint("Search Invoice");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null,
                null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        EditText editText = (EditText) searchView.findViewById(id);
        editText.setHintTextColor(Color.GRAY);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;
            }
        });

        add_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("new", "true");
                args.putString("clicked", "false");
                loadFragment(new FragmentEditReport(),args);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(255, 0,
                        0)));
                // set item width
                deleteItem.setWidth(90);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPosition = position;
                GlobalData.invoiceModel =  Invoicelist.get(position);
                loadFragment(new FragmentUpdateClient(),null);
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        DeletePosition = position;
                      //  Toast.makeText(getActivity(), String.valueOf(DeletePosition), Toast.LENGTH_SHORT).show();
                       DeleteInvoice(Invoicelist.get(position).getId());
                        break;
                    case 1:
                        OpenPosition = position;
                        GlobalData.invoiceModel =  Invoicelist.get(position);
                        /*Bundle args = new Bundle();
                        args.putString("new", "false");
                        args.putString("clicked", "true");
                        args.putSerializable("invoice",list.get(position));
                        loadFragment(new FragmentReportDetail(),args);*/
                       // loadFragment(new FragmentUpdateClient(),null);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


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
                            Invoicelist.add(invoiceModel);
                        }
                        adapter= new ReportAdapter(getActivity(), Invoicelist);
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

    ///////////////////////////Delete Report/////////////////////


    public void DeleteInvoice(String id)
    {

        showProgressBar();
        initVolleyCallbackForDeleteClient();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.DeleteDataVolley(NetworkURLs.BaseURL+ NetworkURLs.DeleteInvoice + id + ".json" );

    }

    void initVolleyCallbackForDeleteClient() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean st=jsonObject.getBoolean("status");
                    if (st) {

                        Invoicelist.remove(DeletePosition);
                        adapter= new ReportAdapter(getActivity(), Invoicelist);
                        listView.setAdapter(adapter);

                        snackbar = Snackbar.make(main_layout,"Invoice Deleted Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                   /* if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        Invoicelist.remove(DeletePosition);
                        adapter= new ReportAdapter(getActivity(), Invoicelist);
                        listView.setAdapter(adapter);

                        snackbar = Snackbar.make(main_layout,"Invoice Deleted Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressBar();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }


}
