package test.invoicegenerator.Activities;

import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.taxcodeAdapter;
import test.invoicegenerator.fragments.AddTaxCode;
import test.invoicegenerator.fragments.UpdateTaxCode;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.TaxCodeModel;

public class SelectTextCodeFromList extends BaseActivity {

    ListView listView;
    RelativeLayout main_layout;
    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    taxcodeAdapter taxAdapter;
   /* SearchView searchView;*/
    ArrayList<TaxCodeModel> taxModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_text_code_from_list);


        init();

    }
    private void init() {

        GetClientList();
        main_layout=findViewById(R.id.main_layout);
        listView = findViewById(R.id.clientList);


       /* searchView.setQueryHint("Search Tax Code");
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
        });*/





        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalData.taxCodeModel =  taxModels.get(position);

                GlobalData.Text_Code_ID=taxModels.get(position).getId();
                finish();
              //  loadFragment(new UpdateTaxCode(),null);
            }
        });

    }


    public void GetClientList() {
        if (taxModels.size() > 0) {
            taxModels.clear();
        }
        showProgressDialog();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, SelectTextCodeFromList.this);
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL + NetworkURLs.GetTaxCodesList);

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
                        JSONArray company_taxes = data.getJSONArray("tax_codes");

                        for (int i = 0; i < company_taxes.length(); i++) {
                            TaxCodeModel taxModel = new TaxCodeModel(company_taxes.getJSONObject(i));
                            taxModels.add(taxModel);
                        }
                        GlobalData.taxCodeModels = taxModels;
                        taxAdapter = new taxcodeAdapter(SelectTextCodeFromList.this, taxModels);
                        listView.setAdapter(taxAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dismissProgress();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                dismissProgress();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }
}
