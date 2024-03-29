package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.goodiebag.pinview.Pinview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;

public class FragmentOTPForgotPassword extends BaseFragment {
    Snackbar snackbar;
    ConstraintLayout main_layout;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    LinearLayout LayoutHeader;

    LottieAnimationView confirmationView;

    Pinview pin;
    Button verify_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        verify_btn = view.findViewById(R.id.verify_btn);
        pin = view.findViewById(R.id.pinview);
        confirmationView = view.findViewById(R.id.confirmationView);
        LayoutHeader = view.findViewById(R.id.linearLayout);

        verify_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (pin.getPinLength() == 4) {
                    DataSendToServerForVerification();


                } else {
                    Toast.makeText(getActivity(), "else" + String.valueOf(pin.getPinLength()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    void DataSendToServerForVerification() {
        showProgressBar();

        initVolleyCallbackForVerification();
        mVolleyService = new VolleyService(mResultCallback, getActivity());

        Map<String, String> data = new HashMap<>();
        data.put("reset_password_token", pin.getValue());
        String Str = NetworkURLs.BaseURL + NetworkURLs.VerifiyCode;
        mVolleyService.postDataVolleyWithoutHeaders("POSTCALL", Str, data);

    }

    void initVolleyCallbackForVerification() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    hideProgressBar();

                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");

                    if (status) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        String login_id = data.getString("message");
                        Toasty.success(getActivity(), login_id, Toast.LENGTH_SHORT).show();

                        confirmationView.setVisibility(View.VISIBLE);
                        confirmationView.playAnimation();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {


                                loadFragment(new FragmentSetPassword(), null);

                            }
                        }, 1000);

                    } else {


                        String error = jsonObject.getString("Error");

                        Toasty.error(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
                if (error.networkResponse != null && error.networkResponse.data != null) {

                    String error_response = new String(error.networkResponse.data);


                    try {
                        JSONObject response_obj = new JSONObject(error_response);

                        {
                            JSONObject error_obj = response_obj.getJSONObject("error");
                            String message = error_obj.getString("message");
                            showErrorMessage(LayoutHeader, "Error" + message);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showErrorMessage(LayoutHeader, "Error  not responding");

                }
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }

}

