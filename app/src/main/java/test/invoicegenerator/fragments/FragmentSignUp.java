package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.SharedPref;


public class FragmentSignUp extends BaseFragment implements View.OnClickListener{
    private static final int REQUEST_READ_CONTACTS = 0;

    Snackbar snackbar;
    ConstraintLayout main_layout;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    LottieAnimationView progressView,confirmationView;
    public Unbinder unbinder;





    Progressbar cdd;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.company_name)
    EditText company_name;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.login_text)
    TextView login_text;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_up,container,false);
        unbinder= ButterKnife.bind(this,view);
        cdd=new Progressbar(getActivity());
        progressView = view.findViewById(R.id.progressView);
        confirmationView =  view.findViewById(R.id.confirmationView);

        Button mEmailSignInButton =  view.findViewById(R.id.sign_up_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();

            }
        });

        login_text.setOnClickListener(FragmentSignUp.this);
        return view;
    }

    private void attemptSignUp() {

        // Reset errors.
        email.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String Email = email.getText().toString();
        String Password = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(Password) && !isPasswordValid(Password)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        // Check for a valid email address.
        else if(TextUtils.isEmpty(name.getText().toString()))
        {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }
        else if(!Util.isFullname(name.getText().toString()))
        {
            name.setError(getString(R.string.error_invalid_name));
            focusView = name;
            cancel = true;
        }
        else if (TextUtils.isEmpty(company_name.getText().toString())) {
            company_name.setError(getString(R.string.error_field_required));
            focusView = company_name;
            cancel = true;
        }
        else if (TextUtils.isEmpty(Email)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!Util.isEmailValid(Email)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }

        else if (!isPasswordValid(Password)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }
        else if(TextUtils.isEmpty(Password))
        {
            password.setError(getString(R.string.error_field_required));
            focusView = password;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            DataSendToServerForSignUp();
        }
    }

    /**
     * Uses snackbar to show error message.
     */
    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar
                .LENGTH_LONG);
        snackbar.show();


    }

    void DataSendToServerForSignUp()
    {

        progressView.setVisibility(View.VISIBLE);


        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        //Map<String, String> data = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        JSONObject data=new JSONObject();


        try {
            data.put("password",password.getText().toString());
            data.put("email",email.getText().toString());
            data.put("name",name.getText().toString());
            data.put("company_attributes[name]",company_name.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mVolleyService.postDataVolleyForHeaders("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.SignUp,data );
    }

    void initVolleyCallbackForSignUp(){
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


                        SharedPref.init(getActivity().getApplicationContext());
                        String login_id = inner_data.getString("id");
                        SharedPref.write(SharedPref.LoginID, login_id);

                        String access_token=header_obj.getString("access-token");
                        String client=header_obj.getString("client");
                        String uid=header_obj.getString("uid");
                        //Headers values
                        SharedPref.write(Constants.ACCESS_TOKEN,access_token);
                        SharedPref.write(Constants.CLIENT,client);
                        SharedPref.write(Constants.UID,uid);

                        progressView.setVisibility(View.GONE);
                        confirmationView.setVisibility(View.VISIBLE);
                        confirmationView.playAnimation();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                loadFragment(new FragmentOTP());

                            }
                        }, 1000);//delay in milliseconds


                    }else {

                        progressView.setVisibility(View.GONE);
                        String error = jsonObject.getString("Error");

                        Toasty.error(getActivity(),error, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                {
                    progressView.setVisibility(View.INVISIBLE);
                    if(error.networkResponse != null && error.networkResponse.data != null){
                        VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                        if(error.networkResponse.statusCode==422) {
                            String error_response = new String(error.networkResponse.data);
                            try {
                                JSONObject response_obj = new JSONObject(error_response);
                                String status = response_obj.getString("status");
                                if (status.equals("false")) {
                                    JSONObject error_obj = response_obj.getJSONObject("error");
                                    String message = error_obj.getString("full_messages");
                                    if(message.contains("already been taken"))
                                    Toasty.error(getActivity(), "Email has already been taken", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        else
                            Toasty.error(getActivity(), Util.getMessage(error), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 8;
    }


    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.login_text:
                loadFragment(new FragmentLogin());
                break;
        }
    }

    private void loadFragment(Fragment dashboardFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(dashboardFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
