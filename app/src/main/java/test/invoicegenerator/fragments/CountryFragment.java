package test.invoicegenerator.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import test.invoicegenerator.adapters.SelectionCountryAdapter;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.Country_Model;

public class CountryFragment extends BaseFragment  {

    SearchView searchView;
    ListView listView;
    SelectionCountryAdapter countryAdapter;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    ArrayList<Country_Model>CountryModels = new ArrayList<Country_Model>();
    int selectedposition=0;

    public static CountryFragment newInstance() {
        CountryFragment fragment = new CountryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        ButterKnife.bind(this, view);


            intthis(view);
        return view;
    }

    private void intthis(View view) {
        listView=view.findViewById(R.id.country_list);
        searchView=view.findViewById(R.id.searchView);
        searchView.setVisibility(View.GONE);
       /* searchView.setQueryHint("Search Country");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null,
                null);
        TextView textView =  searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        EditText editText =  searchView.findViewById(id);
        editText.setHintTextColor(Color.GRAY);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                countryAdapter.getFilter().filter(newText);

                return false;
            }
        });*/


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    CountryModels.get(selectedposition).setIschecked(false);

                GlobalData.StrCountryID=CountryModels.get(i).getStrCountryID();
                CountryModels.get(i).setIschecked(true);
                selectedposition=i;
                countryAdapter.notifyDataSetChanged();

              //  Toast.makeText(getActivity(), CountryModels.get(i).getStrCountryName(), Toast.LENGTH_SHORT).show();
            }
        });

        GetCountyList();
    }

    public void GetCountyList()
    {

        showProgressBar();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL+ NetworkURLs.Country);

    }

    void initVolleyCallbackForClientList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray clients = data.getJSONArray("countries");

                        for (int i = 0; i < clients.length(); i++) {
                            Country_Model countryModel = new Country_Model(clients.getJSONObject(i));
                            CountryModels.add(countryModel);
                            //clientModel.setSelect(false);
                        }

                        countryAdapter = new SelectionCountryAdapter(getActivity(),CountryModels);
                        listView.setAdapter(countryAdapter);
                    }
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
