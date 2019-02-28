package test.invoicegenerator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.SharedPreferenceHelper;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.SharedPref;
import test.invoicegenerator.view.activities.MainActivity;

/**
 * Created by User on 1/4/2019.
 */

public class FragmentLogin extends BaseFragment{

    Progressbar cdd;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    Snackbar snackbar;
    ConstraintLayout main_layout;
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    @BindView(R.id.confirmationView)
    LottieAnimationView confirmationView;

    @BindView(R.id.rememberme_chkbox)
    CheckBox rememberCheckBox;

    @BindView(R.id.email)
    EditText email_txt;

    @BindView(R.id.password)
    EditText password_txt;

    @BindView(R.id.forgot_password_btn)
    Button forgot_password_text;
    public Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        unbinder= ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view) {
        cdd=new Progressbar(getActivity());

        password_txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    // attemptLogin();
                    return true;
                }
                return false;
            }
        });
        //setRememberedCredential();
        Button mEmailSignInButton = view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
               // attemptLogin();
            }
        });



    }

    private void attemptLogin() {


        // Reset errors.
        email_txt.setError(null);
        password_txt.setError(null);

        // Store values at the time of the login attempt.
        String email = email_txt.getText().toString();
        String password = password_txt.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.

        if (!isPasswordValid(password)) {
            password_txt.setError(getString(R.string.error_invalid_password));
            focusView = password_txt;
            cancel = true;
        }
        if(TextUtils.isEmpty(password))
        {
            password_txt.setError(getString(R.string.error_field_required));
            focusView = password_txt;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            email_txt.setError(getString(R.string.error_field_required));
            focusView = email_txt;
            cancel = true;
        } else if (!isEmailValid(email)) {
            email_txt.setError(getString(R.string.error_invalid_email));
            focusView = email_txt;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            DataSendToServerForSignIn();
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    void DataSendToServerForSignIn()
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
            data.put("email",email_txt.getText().toString());

            data.put("password",password_txt.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mVolleyService.postDataVolleyForHeaders("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.SignIn,data );


    }

    void initVolleyCallbackForSignIn(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                rememberCredential();
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
                        String active_user = inner_data.getString("active");
                      //  if(active_user.equals("true")) {
                            SharedPref.init(getActivity());
                            SharedPref.write(SharedPref.LoginID, login_id);
                            SharedPref.write(Constants.ACCESS_TOKEN, access_token);
                            SharedPref.write(Constants.CLIENT, client);
                            SharedPref.write(Constants.UID, uid);


                            cdd.HideProgress();
                            confirmationView.setVisibility(View.VISIBLE);
                            confirmationView.playAnimation();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {


                                    //  loadFragment(new FragmentOTP());
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    // finish();

                                }
                            }, 1000);//1000
                        //checking verfication
                     /*   }
                        else if(active_user.equals("false"))
                        {
                            cdd.HideProgress();
                            Toasty.error(getActivity(),"Please verifiy your account first",Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {


                                    //  loadFragment(new FragmentOTP());
                                    loadFragment(new FragmentOTP());
                                    // finish();

                                }
                            }, 1000);
                        }*/
                        //close verification
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
                        Toasty.error(getActivity(), Util.getMessage(error), Toast.LENGTH_SHORT).show();
                    }

                }
                else
                    Toasty.error(getActivity(), Util.getMessage(error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }
    private void rememberCredential()
    {
        SharedPreferenceHelper sharedPreferenceHelper=new SharedPreferenceHelper(getActivity());

        if(rememberCheckBox.isChecked())
        {
            sharedPreferenceHelper.setValue("rememberme_chkbox","true");
            sharedPreferenceHelper.setValue("email",email_txt.getText().toString());
            sharedPreferenceHelper.setValue("password",password_txt.getText().toString());

        }else {
            sharedPreferenceHelper.setValue("rememberme_chkbox","false");
            sharedPreferenceHelper.setValue("email","");
            sharedPreferenceHelper.setValue("password","");
        }
    }
    @OnClick(R.id.sign_up_text)
    public void openSignUpPage()
    {
        loadFragment(new FragmentSignUp());

    }
    @OnClick(R.id.forgot_password_btn)
    public void openForgotPasswordPage()
    {
        loadFragment(new FragmentForgotPassword());

    }

    private void loadFragment(Fragment dashboardFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(dashboardFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        setRememberedCredential();
    }

    private void setRememberedCredential() {
        SharedPreferenceHelper sharedPreferenceHelper=new SharedPreferenceHelper(getActivity());
        String is_remmeber=sharedPreferenceHelper.getValue("rememberme_chkbox");
        if(is_remmeber.equals("true"))
        {
            email_txt.setText(sharedPreferenceHelper.getValue("email"));
            password_txt.setText(sharedPreferenceHelper.getValue("password"));
            rememberCheckBox.setChecked(true);
        }
        else
        {
            email_txt.setText("");
            password_txt.setText("");
            rememberCheckBox.setChecked(false);
        }
    }

}
