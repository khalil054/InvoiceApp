package test.invoicegenerator.fragments;

/**
 * Created by User on 1/4/2019.
 */

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

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.model.SharedPref;

public class FragmentOTP extends BaseFragment{
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
                DataSendToServerForVerification();
            }
        });

        return view;
    }


    void DataSendToServerForVerification()
    {

        cdd.ShowProgress();


        initVolleyCallbackForVerification();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        Map<String, String> data = new HashMap<String, String>();

        data.put("code",/*pinView.getText().toString()*/code_text.getText().toString());

        SharedPref.init(getActivity());
        String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
        String client=SharedPref.read(Constants.CLIENT,"");
        String uid=SharedPref.read(Constants.UID,"");
        HashMap<String, String>  headers = new HashMap<String, String>();
        headers.put("access-token", access_token);
        headers.put("client",client );
        headers.put("uid",uid);

        mVolleyService.postDataVolley("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.Confirm_User,data,headers );


    }

    void initVolleyCallbackForVerification(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data=jsonObject.getJSONObject("data");
                    String status = jsonObject.getString("status");


                    if(status.equals("true"))
                    {

                        cdd.HideProgress();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                loadFragment(new FragmentLogin());
                                // finish();

                            }
                        }, 1000);//1000
                    }else {

                        cdd.HideProgress();
                        String error = jsonObject.getString("Error");
                        String err="";
                        if(error.contains("AuthFail"))
                        err=getString(R.string.verification_code);
                        Toasty.error(getActivity(),err, Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void notifyError(String requestType,VolleyError error) {
//                Log.d(TAG, "Volley requester " + requestType);
//                Log.d(TAG, "Volley JSON post" + "That didn't work!");
               // cdd.HideProgress();
                error.printStackTrace();
                String err="";
              //  if(error.toString().contains("AuthFail"))
                    err=getString(R.string.verification_code);

                Toasty.error(getActivity(),err, Toast.LENGTH_SHORT).show();


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

