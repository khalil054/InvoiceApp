package test.invoicegenerator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

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
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.SharedPref;
import test.invoicegenerator.view.activities.MainActivity;

public class CompanyDetails extends Fragment {
    @BindView(R.id.company_name)
    EditText company_name;

    @BindView(R.id.company_email)
    EditText company_email;

    @BindView(R.id.company_phone)
    EditText company_phone;

    @BindView(R.id.company_address)
    EditText company_address;

    @BindView(R.id.city)
    EditText city;

    @BindView(R.id.state)
    EditText state;


    @BindView(R.id.country)
    EditText country;

    @BindView(R.id.email_sign_in_button)
    Button email_sign_in_button;

    @BindView(R.id.zip_code)
    EditText zip_code;

    Progressbar cdd;
    private VolleyService mVolleyService;
    IResult mResultCallback = null;

    public static CompanyDetails newInstance() {
        CompanyDetails fragment = new CompanyDetails();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_company_details, container, false);
      ///  View rootView = inflater.inflate(R.layout.frag, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;

    }
    private void init()
    {
        cdd=new Progressbar(getActivity());
        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSendDataToServer();
            }
        });
    }

    private void validateAndSendDataToServer() {
        if(company_name.getText().toString().equals(""))
        {
            company_name.setError(getString(R.string.error_field_required));
            company_name.requestFocus();
        }
        else if(!Util.isFullname(company_name.getText().toString()))
        {
            company_name.setError(getString(R.string.error_invalid_name));
            company_name.requestFocus();
        }
        else if(company_email.getText().toString().equals(""))
        {
            company_email.setError(getString(R.string.error_field_required));
            company_email.requestFocus();
        }
        else if(!Util.isEmailValid(company_email.getText().toString()))
        {
            company_email.setError(getString(R.string.error_invalid_email));
            company_email.requestFocus();
        }
        else if(company_phone.getText().toString().equals(""))
        {
            company_phone.setError(getString(R.string.error_field_required));
            company_phone.requestFocus();
        }
        else if(company_address.getText().toString().equals(""))
        {
            company_address.setError(getString(R.string.error_field_required));
            company_address.requestFocus();
        }
        else if(city.getText().toString().equals(""))
        {
            city.setError(getString(R.string.error_field_required));
            city.requestFocus();
        }
        else if(country.getText().toString().equals(""))
        {
            country.setError(getString(R.string.error_field_required));
            country.requestFocus();
        }
        else if(state.getText().toString().equals(""))
        {
            state.setError(getString(R.string.error_field_required));
            state.requestFocus();
        }
        else if(zip_code.getText().toString().equals(""))
        {
            zip_code.setError(getString(R.string.error_field_required));
            zip_code.requestFocus();
        }
        else if(!Util.isZipCodeValid(zip_code.getText().toString()))
        {
            zip_code.setError(getString(R.string.error_invalid_zip_code));
            zip_code.requestFocus();
        }

    }

    void DataSendToServerForAddCompany()
    {
        cdd.ShowProgress();


        initVolleyCallbackForSignIn();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        /*Map<String, String> data = new HashMap<String, String>();

        data.put("email",email_txt.getText().toString());
        data.put("password",password_txt.getText().toString());
*/
        JSONObject data=new JSONObject();

        try {
            data.put("email",company_name.getText().toString());

            data.put("password",company_email.getText().toString());
            data.put("password",company_address.getText().toString());
            data.put("password",company_phone.getText().toString());
            data.put("password",city.getText().toString());
            data.put("password",country.getText().toString());
            data.put("password",state.getText().toString());
            data.put("password",zip_code.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //mVolleyService.postDataVolleyForHeaders("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.SignIn,data );


    }

    void initVolleyCallbackForSignIn(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data=jsonObject.getJSONObject("data");
                    String status = data.getString("status");


                    if(status.equals("true"))
                    {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        JSONObject inner_data = jsonObject1.getJSONObject("data");
                        JSONObject header_obj = jsonObject.getJSONObject("headers");

                        String access_token=header_obj.getString("access-token");
                        String client=header_obj.getString("client");
                        String uid=header_obj.getString("uid");

                        String login_id = inner_data.getString("id");


                        SharedPref.init(getActivity());
                        SharedPref.write(SharedPref.LoginID, login_id);
                        SharedPref.write(Constants.ACCESS_TOKEN,access_token);
                        SharedPref.write(Constants.CLIENT,client);
                        SharedPref.write(Constants.UID,uid);


                        cdd.HideProgress();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {


                                //  loadFragment(new FragmentOTP());
                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                startActivity(intent);
                                // finish();

                            }
                        }, 1000);//1000
                    }else {

                        cdd.HideProgress();
                        String error = jsonObject.getString("Error");

                        Toasty.error(getActivity(),error, Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
//                Log.d(TAG, "Volley requester " + requestType);
//                Log.d(TAG, "Volley JSON post" + "That didn't work!");
                cdd.HideProgress();
                error.printStackTrace();

                if(error.networkResponse != null && error.networkResponse.data != null){
                    VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj=new JSONObject(error_response);
                        String status=response_obj.getString("status");
                        if(status.equals("false"))
                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            Toasty.error(getActivity(),message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //  return error;
                // snackbar = Snackbar.make(main_layout, error.toString(), Snackbar.LENGTH_LONG);
                //snackbar.show();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }

}
