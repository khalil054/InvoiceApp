package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Util;


public class FragmentForgotPassword extends BaseFragment {

    @BindView(R.id.email)
    EditText email_txt;
    TextView TvBack;
    @BindView(R.id.get_password_btn)
    Button password_btn;

    IResult mResultCallback = null;

    VolleyService mVolleyService;

    Progressbar cdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        unbinder = ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    private void init(View view) {

        cdd = new Progressbar(getActivity());
        TvBack = view.findViewById(R.id.login_text);

        TvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new FragmentLogin());
            }
        });
    }

    @OnClick(R.id.get_password_btn)
    public void onClickGetPassword() {
        if (email_txt.getText().toString().equals("")) {
            email_txt.setError(getString(R.string.error_field_required));
            return;
        }
        if (!isEmailValid(email_txt.getText().toString())) {
            email_txt.setError(getString(R.string.error_invalid_email));
            return;
        } else {
            sendDataForgotPasswordData();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    void sendDataForgotPasswordData() {

        showProgressBar();
        initVolleyCallbackForgotPassword();
        mVolleyService = new VolleyService(mResultCallback, getActivity());

        Map<String, String> data = new HashMap<String, String>();
        data.put("email", email_txt.getText().toString());

        mVolleyService.postDataVolley("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.ForgotPassword, data);

    }

    private void initVolleyCallbackForgotPassword() {

        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data_obj = jsonObject.getJSONObject("data");
                    String status = jsonObject.getString("status");


                    if (status.equals("true")) {

                        Toasty.success(getActivity(), "Verifivation code sent on email", Toast.LENGTH_SHORT).show();


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                loadFragment(new FragmentOTPForgotPassword());

                                // finish();

                            }
                        }, 1000);//1000
                    } else {

                        String error = jsonObject.getString("Error");

                        Toasty.error(getActivity(), error, Toast.LENGTH_SHORT).show();
                        //snackbar = Snackbar.make(main_layout, error, Snackbar.LENGTH_LONG);
                        // snackbar.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideProgressBar();

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

                hideProgressBar();
                error.printStackTrace();

                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String error_response = new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj = new JSONObject(error_response);

                        {
                            //  JSONObject error_obj=response_obj.getJSONObject("error");
                            String message = response_obj.getString("errors");
                            Toasty.error(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else
                    Toasty.error(getActivity(), Util.getMessage(error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }

        };

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
