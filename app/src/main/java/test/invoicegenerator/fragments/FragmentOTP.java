package test.invoicegenerator.fragments;

/**
 * Created by User on 1/4/2019.
 */

import android.content.Intent;
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

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.goodiebag.pinview.Pinview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.model.SharedPref;
import test.invoicegenerator.view.activities.MainActivity;

public class FragmentOTP extends BaseFragment{
    //Snackbar snackbar;
    //ConstraintLayout main_layout;
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    @BindView(R.id.confirmationView)
    LottieAnimationView confirmationView;

    Pinview pin;
   Button verify_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        verify_btn=(Button)view.findViewById(R.id.verify_btn);
        pin=(Pinview)view.findViewById(R.id.pinview);



        verify_btn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!(pin.getValue().toString().length()<4))
                {
                    DataSendToServerForVerification();
                }
            }
        });

        return view;
    }


    void DataSendToServerForVerification()
    {



        showProgressBar();
        initVolleyCallbackForVerification();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        Map<String, String> data = new HashMap<String, String>();
        data.put("code",pin.getValue());


        mVolleyService.putDataVolleyWithHeader("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.Confirm_User,data );


    }

    void initVolleyCallbackForVerification(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Boolean status = jsonObject.getBoolean("status");

                    if(status)
                    {
                        JSONObject data = jsonObject.getJSONObject("data");

                        String login_id = data.getString("id");

                        SharedPref.init(getActivity());
                        SharedPref.write(SharedPref.LoginID, login_id);



                        confirmationView.setVisibility(View.VISIBLE);
                        confirmationView.playAnimation();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {



                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            }
                        }, 1000);

                    } else {


                        String error = jsonObject.getString("Error");

                        Toasty.error(getActivity(),error, Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void notifyError(String requestType,VolleyError error) {
                hideProgressBar();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }

}

