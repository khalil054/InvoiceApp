package test.invoicegenerator.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.taxAdapter;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.TaxModel;

public class TaxesList extends BaseFragment {

    ListView listView;
    FloatingActionButton floating_AddClient;
    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    taxAdapter taxAdapter;
    SearchView searchView;
    int DeletePosition = 0;
    ArrayList<TaxModel> taxModels = new ArrayList<>();

    public static TaxesList newInstance() {
        TaxesList fragment = new TaxesList();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_taxes_list, container, false);
        searchView = view.findViewById(R.id.searchView); // inititate a search view
        listView = view.findViewById(R.id.clientList);
        floating_AddClient = view.findViewById(R.id.floating_add_new_client);
        init();
        unbinder = ButterKnife.bind(this, view);
        GetClientList();
        return view;
    }

    private void init() {

        searchView.setQueryHint("Search Tax");
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

                taxAdapter.getFilter().filter(newText);

                return false;
            }
        });

        BottomNavigationView navigation = getActivity().findViewById(R.id.navigation);
        navigation.setVisibility(View.GONE);

        floating_AddClient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(new AddTax(), null);
            }
        });


        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalData.taxModel = taxModels.get(position);
                loadFragment(new UpdateTax(), null);
            }
        });

    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onAllClientFragCallBack(int position);
    }


    public void GetClientList() {
        if (taxModels.size() > 0) {
            taxModels.clear();
        }
        showProgressBar();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        String StrAPi = NetworkURLs.BaseURL + NetworkURLs.AddTax;
        mVolleyService.getDataVolley("GETCALL", StrAPi);

    }

    void initVolleyCallbackForClientList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company_taxes = data.getJSONArray("company_taxes");

                        for (int i = 0; i < company_taxes.length(); i++) {
                            TaxModel taxModel = new TaxModel(company_taxes.getJSONObject(i));
                            taxModels.add(taxModel);
                        }

                        GlobalData.taxModels = taxModels;
                        taxAdapter = new taxAdapter(getActivity(), taxModels);
                        listView.setAdapter(taxAdapter);
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
                    try {
                        JSONObject response_obj = new JSONObject(error_response);

                        {

                            String message = response_obj.getString("errors");
                            Toasty.error(getActivity(), message, Toast.LENGTH_SHORT).show();
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


