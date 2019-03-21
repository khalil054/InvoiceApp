package test.invoicegenerator.fragments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.SharedPref;
import test.invoicegenerator.view.activities.MainActivity;


public class FragmentAddClient extends BaseFragment{

    @BindView(R.id.add_client_button)
    Button Btn_AddClient;
    @BindView(R.id.client_name)
    EditText Et_Client_Name;
    @BindView(R.id.client_email)
    EditText Et_Client_Email;
    @BindView(R.id.client_phone)
    EditText Et_Client_Phone;
    @BindView(R.id.client_city)
    public EditText Et_Client_City;
    @BindView(R.id.client_state)
    public EditText Et_Client_State;
    @BindView(R.id.client_country)
    public EditText Et_Client_Country;
    @BindView(R.id.client_zip)
    public EditText Et_Client_ZipCode;
    @BindView(R.id.main_layout)
    ConstraintLayout main_layout;
    @BindView(R.id.confirmationView)
    LottieAnimationView confirmationView;

    public PlacesAutocompleteTextView Et_Client_Address;
    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    private OnItemSelectedListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_client,container,false);
        Et_Client_Address = view.findViewById(R.id.places_autocomplete);

        unbinder= ButterKnife.bind(this,view);


        ((MainActivity)getActivity()).LoadAddressFields(view);

        Btn_AddClient.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String StrName=Et_Client_Name.getText().toString();
                String StrEmail=Et_Client_Email.getText().toString();
                String StrPhone=Et_Client_Phone.getText().toString();
                String StrAddress=Et_Client_Address.getText().toString();
                String StrCity=Et_Client_City.getText().toString();
                String StrState=Et_Client_State.getText().toString();
                String StrCountry=Et_Client_Country.getText().toString();
                String StrPostalCode=Et_Client_ZipCode.getText().toString();

                validateAndSaveData(StrName,StrEmail,StrPhone,StrAddress,StrCity,StrState,StrCountry,StrPostalCode);

            }
        });

        return view;
    }




    private void validateAndSaveData(String StrName,String StrEmail,String StrPhone,String StrAddress,String StrCity,
                                     String StrState,String StrCountry,String StrPostalCode) {
        if(StrEmail.equals(""))
        {
            Et_Client_Email.setError(getString(R.string.error_field_required));
            Et_Client_Email.requestFocus();
        }
        if(StrName.equals(""))
        {
            Et_Client_Name.setError(getString(R.string.error_field_required));
            Et_Client_Name.requestFocus();
        }
        else if(!Util.isFullname(StrName))
        {
            Et_Client_Name.setError(getString(R.string.error_invalid_name));
            Et_Client_Name.requestFocus();
        }

        else if(!Util.isEmailValid(StrEmail))
        {
            Et_Client_Email.setError(getString(R.string.error_invalid_email));
            Et_Client_Email.requestFocus();
        }
        else if(StrAddress.equals(""))
        {
            Et_Client_Address.setError(getString(R.string.error_field_required));
            Et_Client_Address.requestFocus();
        }
        else if(StrCity.equals(""))
        {
            Et_Client_City.setError(getString(R.string.error_field_required));
            Et_Client_City.requestFocus();
        }
        else if(StrState.equals(""))
        {
            Et_Client_State.setError(getString(R.string.error_field_required));
            Et_Client_State.requestFocus();
        }
        else if(StrCountry.equals("")) {
            Et_Client_Country.setError(getString(R.string.error_field_required));
            Et_Client_Country.requestFocus();
        }
        else if(!Util.isZipCodeValid(StrPostalCode))
        {
            Et_Client_ZipCode.setError(getString(R.string.error_invalid_zip_code));
            Et_Client_ZipCode.requestFocus();
        }
        else
        {

            DataSendToServerForAddClient();

        }


    }

    void DataSendToServerForAddClient()
    {
        showProgressBar();

        initVolleyCallbackForAddClient();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        JSONObject Data=new JSONObject();
        JSONObject jsonObject=new JSONObject();
        JSONObject jsonObject1=new JSONObject();
        JSONArray jsonArray=new JSONArray();

        try {
            jsonObject.put("name",Et_Client_Name.getText().toString());
            jsonObject.put("email",Et_Client_Email.getText().toString());
            jsonObject.put("phone",Et_Client_Phone.getText().toString());

            jsonObject1.put("name",Et_Client_Address.getText().toString());
            jsonObject1.put("line_1",Et_Client_Address.getText().toString());
            jsonObject1.put("line_2","");
            jsonObject1.put("city",Et_Client_City.getText().toString());
            jsonObject1.put("state",Et_Client_State.getText().toString());
            jsonObject1.put("zip_code",Et_Client_ZipCode.getText().toString());
            jsonObject1.put("default",true);
            jsonObject1.put("country_name",Et_Client_Country.getText().toString());


            jsonArray.put(jsonObject1);

            jsonObject.put("addresses_attributes",jsonArray);


            Data.put("client",jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mVolleyService.postDataVolleyForHeadersWithJson("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.AddClient,Data );
    }

    void initVolleyCallbackForAddClient(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    Boolean status = jsonObject1.getBoolean("status");


                    if(status)
                    {

                        confirmationView.setVisibility(View.VISIBLE);
                        confirmationView.playAnimation();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                loadFragment(new FragmentAllClients(),null);

                            }
                        }, 1000);

                        snackbar = Snackbar.make(main_layout,"Client Added Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    } else {


                        String error = jsonObject.getString("Error");
                        Toasty.error(getActivity(),error, Toast.LENGTH_SHORT).show();
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

    public boolean isValidString(String Str){

        return !Str.isEmpty() && !Str.equalsIgnoreCase("null");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SavedCoupansLocationFragment.OnItemSelectedListener");
        }
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onAddClientFragCallBack(int position);
    }

}
