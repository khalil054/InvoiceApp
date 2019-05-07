package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.Activities.MainActivity;

public class UpdateAddress extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.update_button)
    Button Btn_Update;
    @BindView(R.id.client_city)
    EditText client_city;
    @BindView(R.id.client_state)
    EditText client_state;
    @BindView(R.id.client_country)
    EditText client_country;
    @BindView(R.id.client_zip)
    EditText client_zip;
    @BindView(R.id.main_layout)
    ConstraintLayout main_layout;

    public PlacesAutocompleteTextView Et_Client_Address;

    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_address, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressbar = new Progressbar(getActivity());
        Et_Client_Address = view.findViewById(R.id.places_autocomplete);
        ((MainActivity)getActivity()).LoadAddressFields(view);

        init();


        return view;
    }

    private void init() {

        Btn_Update.setText("Update");
        loadData();
    }

    public void loadData()
    {
        Et_Client_Address.setText(GlobalData.addressModel.getName());
        client_city.setText(GlobalData.addressModel.getCity());
        client_state.setText(GlobalData.addressModel.getState());
        client_country.setText(GlobalData.addressModel.getCountry_name());
        client_zip.setText(GlobalData.addressModel.getZip_code());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Btn_Update.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.update_button:

                String Address = Et_Client_Address.getText().toString();
                String city = client_city.getText().toString();
                String state = client_state.getText().toString();
                String country = client_country.getText().toString();
                String zip = client_zip.getText().toString();

                validateAndSaveData(Address,city,state,country,zip);

                break;
        }
    }

    private void validateAndSaveData(String Address, String city, String state, String country, String zip) {
        if(Address.equals(""))
        {
            Et_Client_Address.setError(getString(R.string.error_field_required));
            Et_Client_Address.requestFocus();
        }
        if(city.equals(""))
        {
            client_city.setError(getString(R.string.error_field_required));
            client_city.requestFocus();
        }
        else if(state.equals(""))
        {
            client_state.setError(getString(R.string.error_invalid_name));
            client_state.requestFocus();
        }

        else if(country.equals(""))
        {
            client_country.setError(getString(R.string.error_invalid_email));
            client_country.requestFocus();
        }
        else if(zip.equals(""))
        {
            client_zip.setError(getString(R.string.error_invalid_email));
            client_zip.requestFocus();
        }
        else
        {
            DataSendToServerForUpdate();
        }


    }

    void DataSendToServerForUpdate()
    {
        progressbar.ShowProgress();

        initVolleyCallbackForUpdate();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        JSONObject Data=new JSONObject();
        JSONObject jsonObject1=new JSONObject();


        try {


            jsonObject1.put("name",Et_Client_Address.getText().toString());
            jsonObject1.put("line_1",Et_Client_Address.getText().toString());
            jsonObject1.put("line_2","");
            jsonObject1.put("city",client_city.getText().toString());
            jsonObject1.put("state",client_state.getText().toString());
            jsonObject1.put("zip_code",client_zip.getText().toString());
            jsonObject1.put("default",true);
            jsonObject1.put("country_name",client_country.getText().toString());

            Data.put("address",jsonObject1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mVolleyService.putDataVolleyForHeadersWithJson("PUTCALL", NetworkURLs.BaseURL + NetworkURLs.UpdateClient + GlobalData.clientId + NetworkURLs.UpdateAddress + GlobalData.addressModel.getId()+".json",Data );
    }

    void initVolleyCallbackForUpdate(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    Boolean status = jsonObject1.getBoolean("status");


                    if(status)
                    {

                        progressbar.HideProgress();
                        progressbar.ShowConfirmation();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                progressbar.HideConfirmation();

                            }
                        }, 1000);

                        snackbar = Snackbar.make(main_layout,"Address Update Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    } else {


                        String error = jsonObject.getString("Error");
                        Toasty.error(getActivity(),error, Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    progressbar.HideProgress();
                }



            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                progressbar.HideProgress();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }


        };
    }








}

