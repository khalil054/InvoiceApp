package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.chaos.view.PinView;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.SharedPref;

/**
 * Created by User on 1/10/2019.
 */

public class FragmentOTPForgotPassword extends BaseFragment{
    //Snackbar snackbar;
    //ConstraintLayout main_layout;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    Progressbar cdd;

    Button verify_btn;
    PinView pinView;
    EditText code_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        verify_btn=(Button)view.findViewById(R.id.verify_btn);
        pinView = view.findViewById(R.id.PinView);
        code_text=(EditText)view.findViewById(R.id.code_text);


        cdd=new Progressbar(getActivity());

        pinView.setItemCount(4);
        pinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        pinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        pinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        pinView.setAnimationEnable(true);// start animation when adding text
        pinView.setCursorVisible(false);
        pinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));
        pinView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pinView.setHideLineWhenFilled(false);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!code_text.getText().toString().equals(""))
                sendDataForCodeVerification();
                else
                {
                    code_text.setError(getString(R.string.error_field_required));
                    code_text.requestFocus();
                }
            }
        });

        return view;
    }


    void sendDataForCodeVerification()
    {

        cdd.ShowProgress();


        initVolleyCallbackForCodeVerification();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        JSONObject data=new JSONObject();

        try {
            data.put("reset_password_token",code_text.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mVolleyService.postDataVolleyForHeaders("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.VerifiyCode,data);


    }

    private void initVolleyCallbackForCodeVerification() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data=jsonObject.getJSONObject("data");
                    String status=data.getString("status");


                    if(status.equals("true"))
                    {
                        JSONObject inner_data = data.getJSONObject("data");
                        JSONObject header_obj = jsonObject.getJSONObject("headers");

                        String access_token=header_obj.getString("access-token");
                        String client=header_obj.getString("client");
                        String uid=header_obj.getString("uid");

                        SharedPref.init(getActivity());
                        SharedPref.write(Constants.ACCESS_TOKEN_FORGOT_PASSWORD,access_token);
                        SharedPref.write(Constants.CLIENT_FORGOT_PASSWORD,client);
                        SharedPref.write(Constants.UID_FORGOT_PASSWORD,uid);

                      //  Toasty.success(getActivity(),"Verification code sent on email", Toast.LENGTH_SHORT).show();


                        cdd.HideProgress();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                loadFragment(new FragmentSetPassword());
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
                else
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



