package test.invoicegenerator.fragments;

/**
 * Created by User on 1/24/2019.
 */

import android.content.ContentValues;
import android.content.Context;
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
import test.invoicegenerator.databaseutilities.Client;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.ClientModel;


public class FragmentUpdateClient extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.add_client_button)
    Button Btn_AddClient;
    @BindView(R.id.client_name)
    EditText Et_Client_Name;
    @BindView(R.id.client_email)
    EditText Et_Client_Email;
    @BindView(R.id.client_phone)
    EditText Et_Client_Phone;
    @BindView(R.id.client_address)
    EditText Et_Client_Address;
    @BindView(R.id.confirmationView)
    LottieAnimationView confirmationView;
    @BindView(R.id.main_layout)
    ConstraintLayout main_layout;

    private OnItemSelectedListener listener;
    private Client client;

    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_client,container,false);
        unbinder= ButterKnife.bind(this,view);
        init();

        DataSendToServerForUpdate();

        return view;
    }

    private void init() {
        ClientModel client = GlobalData.clientModel;
        Et_Client_Name.setText(client.getName());
        Et_Client_Email.setText(client.getEmail());
        Et_Client_Phone.setText(client.getPhone());
        Et_Client_Address.setText(client.getAddress());


        Btn_AddClient.setText("Update");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Btn_AddClient.setOnClickListener(this);
        Et_Client_Name.setOnClickListener(this);
        Et_Client_Email.setOnClickListener(this);
        Et_Client_Phone.setOnClickListener(this);
        Et_Client_Address.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.add_client_button:

                String StrName=Et_Client_Name.getText().toString();
                String StrEmail=Et_Client_Email.getText().toString();
                String StrPhone=Et_Client_Phone.getText().toString();
                String StrAddress=Et_Client_Address.getText().toString();

                validateAndSaveData(StrName,StrEmail,StrPhone,StrAddress);
                //  loadFragment(new FragmentLogin());


                break;
        }
    }

    private void validateAndSaveData(String StrName,String StrEmail,String StrPhone,String StrAddress) {
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
        else
        {

        }


    }

    void DataSendToServerForUpdate()
    {
        showProgressBar();

        initVolleyCallbackForUpdate();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        Map<String, String> data = new HashMap<String, String>();

        data.put("client[name]",Et_Client_Name.getText().toString());
        data.put("client[email]",Et_Client_Email.getText().toString());
        data.put("client[phone]",Et_Client_Phone.getText().toString());
        data.put("client[address]",Et_Client_Address.getText().toString());

        mVolleyService.putDataVolleyForHeaders("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.UpdateClient);
    }

    void initVolleyCallbackForUpdate(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Boolean status = jsonObject.getBoolean("status");


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

