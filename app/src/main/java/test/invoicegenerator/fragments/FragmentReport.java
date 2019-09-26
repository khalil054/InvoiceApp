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
import android.widget.ImageView;
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
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.GetSingleInvoiceDetailModel;
import test.invoicegenerator.model.JsonInvoiceModel;

public class FragmentReport extends BaseFragment {

    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    @BindView(R.id.add_invoice)
    FloatingActionButton add_invoice;
    @BindView(R.id.invoiceList)
    SwipeMenuListView listView;
    private ArrayList<JsonInvoiceModel> Invoicelist = new ArrayList<>();
    int DeletePosition = 0;
    int OpenPosition = 0;
    SearchView searchView;
    ReportAdapter adapter;
    public static boolean CanUpdateInvoice = false;
    @BindView(R.id.img_dummy)
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        searchView = view.findViewById(R.id.searchView);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        //items=new ArrayList<>();
        GetInvoiceList();
        searchView.setQueryHint("Search Invoice");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null,
                null);
        TextView textView = searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        EditText editText = searchView.findViewById(id);
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
                if (FragmentEditReport.item_values.size() > 0) {
                    FragmentEditReport.item_values.clear();
                }
                loadFragment(new FragmentEditReport(), args);
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
                CanUpdateInvoice = true;
                OpenPosition = position;
                GlobalData.invoiceModel = Invoicelist.get(position);
                FragmentReportDetail.InvoiceId_ToBeFetch = GlobalData.invoiceModel.getId();
                GetSingleInvoiceDetail();


            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        DeletePosition = position;
                        DeleteInvoice(Invoicelist.get(position).getId());
                        break;
                    case 1:
                        OpenPosition = position;
                        GlobalData.invoiceModel = Invoicelist.get(position);
                        break;
                }
                return false;
            }
        });

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

    }

    public void GetInvoiceList() {
        if (Invoicelist.size() > 0) {
            Invoicelist.clear();
        }
        showProgressBar();
        initVolleyCallbackForInvoiceList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        String Str = NetworkURLs.BaseURL + NetworkURLs.GetInvoiceList;
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
                        adapter = new ReportAdapter(getActivity(), Invoicelist);
                        listView.setAdapter(adapter);
                    } else {
                        //Toast.makeText(getActivity(), "Status found false", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressBar();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
                if (error.networkResponse != null && error.networkResponse.data != null) {

                    String error_response = new String(error.networkResponse.data);
                    //  Toast.makeText(getActivity(), String.valueOf("Error" + error_response), Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject response_obj = new JSONObject(error_response);
                        {
                            JSONObject error_obj = response_obj.getJSONObject("error");
                            String message = error_obj.getString("message");
                            //   Toast.makeText(getActivity(), String.valueOf("Error" + message), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //  Toast.makeText(getActivity(), String.valueOf("Error"+error.getMessage()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {
            }

        };
    }

    ///////////////////////////Delete Report/////////////////////


    public void DeleteInvoice(String id) {
        showProgressBar();
        initVolleyCallbackForDeleteClient();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.DeleteDataVolley(NetworkURLs.BaseURL + NetworkURLs.DeleteInvoice + id + ".json");

    }

    void initVolleyCallbackForDeleteClient() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean st = jsonObject.getBoolean("status");
                    if (st) {
                        Invoicelist.remove(DeletePosition);
                        adapter = new ReportAdapter(getActivity(), Invoicelist);
                        listView.setAdapter(adapter);
                        snackbar = Snackbar.make(main_layout, "Invoice Deleted Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressBar();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String error_response = new String(error.networkResponse.data);
                    // dialogHelper.showErroDialog(error_response);
                    //Toast.makeText(getActivity(), String.valueOf("Error" + error_response), Toast.LENGTH_SHORT).show();


                    try {
                        JSONObject response_obj = new JSONObject(error_response);
                        {
                            JSONObject error_obj = response_obj.getJSONObject("error");
                            String message = error_obj.getString("message");

                            //   Toast.makeText(getActivity(), String.valueOf("Error" + message), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Toast.makeText(getActivity(), String.valueOf("Error not responding" ), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }
////////////////////////////////////////////Get Single Invoice Detail////////////////////////
    ///////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////

    public void GetSingleInvoiceDetail() {

        showProgressBar();
        initVolleyCallbackForSingleDetail();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        String Str = NetworkURLs.BaseURL + NetworkURLs.GetSingleInvoice + FragmentReportDetail.InvoiceId_ToBeFetch + ".json";
        mVolleyService.getDataVolley("GETCALL", Str);
    }

    void initVolleyCallbackForSingleDetail() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                hideProgressBar();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        JSONObject mydata = jsonObject.getJSONObject("data");
                        JSONObject InvoiceModel = mydata.getJSONObject("invoice");
                        GlobalData.singleInvoiceDetailModel = new GetSingleInvoiceDetailModel(InvoiceModel);

                        loadFragment(new FragmentReportDetail(), null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String error_response = new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj = new JSONObject(error_response);
                        {
                            JSONObject error_obj = response_obj.getJSONObject("error");
                            String message = error_obj.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }

        };
    }
}
